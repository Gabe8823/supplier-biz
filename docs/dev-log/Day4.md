# Day4 开发日志（2026-10-09）

> 任务清单：`供应链项目-Day4任务细化清单.md`
> 规范基线：《供应链项目-开发规范（阿里Java手册落地版）》

## 一、今日完成

| 任务 | 交付物 | 状态 |
| :--- | :--- | :--- |
| D4-1 setScale 修复 | PurchaseOrderServiceImpl 金额统一 setScale(2, HALF_UP) | ✅ commit 25ae81d |
| D4-1 dev-log | Day3.md / Day4.md 落盘 | ✅ |
| D4-2 订单详情 VO 化 | 之前合入（commit 90fa500） | ✅ |
| D4-3 库存三接口 | 之前合入（commit af4b2a7） | ✅ |
| D4-4 超卖复现实验 | 实验数据见下文 | ✅ |
| D4-5 Redisson + 乐观锁修复超卖 | commit c88fc17 | ✅ |
| D4-5 并发用例验证 | 成功 10 / 失败 90 / 库存=0 不为负 | ✅ |
| D4-5 P3C 扫描 | 无新增违规（仅遗留 JWT 类命名 2 处豁免） | ✅ |
| D4-6 Redis 缓存 | 顺延 | ⏸ |

## 二、超卖复现实验（D4-4/D4-5）

### 实验设计

- 初始库存：10 件
- 并发线程：100（每线程扣 1 件）
- 期望：成功 10 / 失败 90 / 最终 available=0 不为负
- 工具：`CountDownLatch` 发令枪 + `ThreadPoolExecutor` 显式参数（阿里手册第三节【强制】禁用 Executors）

### 三组对照实验数据

| 方案 | 成功 | 失败 | 最终 available | 是否超卖 |
| :--- | :---: | :---: | :---: | :---: |
| A：无锁（朴素"先查后改"） | 66 | 34 | 0 | ❌ 丢失更新（多线程读到同一旧值，update 互相覆盖） |
| B：纯乐观锁（@Version + OptimisticLockerInnerInterceptor） | 10 | 90 | 0 | ✅（CAS 失败自旋重试） |
| C：Redisson 分布式锁 + 乐观锁兜底 | 10 | 90 | 0 | ✅ |

> 方案 A 的 66/34 是经典的"丢失更新"——多个线程并发读到同一 available=10，分别写回 9，最终 available 卡在 9 而不是 0。严重时出现 available 变负数。

### 最终方案选择：C（Redisson + 乐观锁）

| 维度 | 乐观锁 | Redisson 分布式锁 |
| :--- | :--- | :--- |
| 实现 | @Version + MP 乐观锁插件 | getLock + try/finally unlock |
| 冲突处理 | 失败自旋重试 | 排队等待 |
| 适用 | 冲突概率低、读多写少 | 热点行、高冲突 |
| 本项目 | 作为对照方案 | **主方案**（库存是热点行） |

锁粒度：`lock:inventory:{materialId}:{warehouseId}`，避免锁全表互不相干的库存行。

## 三、规约偏离说明（模拟企业豁免流程）

| # | 偏离条款 | 偏离内容 | 理由 | 影响 |
| :--- | :--- | :--- | :--- | :--- |
| 1 | 阿里手册第二节【推荐】事务方法 | stockOut/stockIn 移除了 @Transactional | 锁内自旋重试与事务边界冲突：事务会让 update 的可见性推迟，自旋读到的还是旧值，导致死循环 | 锁内读-改-写本身在单线程临界区已原子，事务边界无必要 |
| 2 | JWT 类命名（ClassNamingShouldBeCamelRule） | JWTProperties / JWTUtils 未改为 JwtProperties / JwtUtils | 沿用 Day3 命名，避免广泛改动 | P3C 仅 2 处豁免，可后续单独整改 |

## 四、踩坑记录

### 坑 1：Redisson 启动报 `ERR AUTH <password> called without any password configured`

- **问题**：Spring Boot 起不来，报 `Unable to connect to Redis server: localhost/127.0.0.1:6379`
- **根因**：application.yaml 写 `password: ${REDIS_PASSWORD:}`，Spring 把空字符串当密码传给 Redisson，Redisson 发 AUTH 命令被 Redis 拒绝（本地无密码）
- **解决**：删除 password 行，需要密码时通过环境变量 REDIS_PASSWORD 单独注入
- **教训**：占位符空默认值 `${VAR:}` 解析为空字符串而非 null，对接无密码服务时仍会触发 AUTH

### 坑 2：测试断言 BigDecimal.equals 受 scale 影响

- **问题**：`assertEquals(BigDecimal.ZERO, finalInventory.getAvailableQuantity())` 失败，`expected: <0> but was: <0.0000>`
- **根因**：`BigDecimal.equals` 比较 scale，0 与 0.0000 scale 不同
- **解决**：用 `compareTo() == 0` 替代 equals

### 坑 3：@BeforeEach 用 saveOrUpdate 重置库存失败

- **问题**：100 线程全部抛 STOCK_NOT_ENOUGH，库存没重置成功
- **根因**：saveOrUpdate 走 updateById 触发乐观锁拦截器，entity.version=0 与表里 version=11 不匹配，Updates=0
- **解决**：@BeforeEach 改用 `LambdaUpdateWrapper.set(...)` 直接 UPDATE，绕开乐观锁拦截器
- **教训**：拦截器对 updateById(entity) 生效，对 update(Wrapper) 不生效，重置逻辑要选对路径

## 五、面试点速查

1. **超卖根因**：读-改-写非原子，多线程并发读到同一旧值，update 互相覆盖
2. **乐观锁 vs 分布式锁**：乐观锁适用冲突低/读多写少；分布式锁适用热点行高冲突；本项目库存是热点行→选 Redisson
3. **Redisson 看门狗**：`lock()` 默认 30s，每 10s 自动续期；unlock 必须 finally 防锁泄漏
4. **锁粒度**：`lock:inventory:{materialId}:{warehouseId}`，避免锁全表互不相干的行
5. **为什么不加 @Transactional**：锁内自旋重试 + 事务边界会让 update 可见性推迟，自旋读到旧值死循环
6. **BigDecimal.equals 陷阱**：scale 不同判 false，比较值用 compareTo
7. **MP 乐观锁拦截器生效范围**：仅 updateById(entity) 触发；update(Wrapper) 不触发，重置数据走 Wrapper 路径

## 六、Git 提交记录

```
25ae81d fix(order): 金额统一 setScale 舍入              （D4-1）
c88fc17 feat(inventory): Redisson 分布式锁+乐观锁修复超卖  （D4-5）
6650ec3 merge: feature/inventory-lock-fix-v2          （合入 master）
```

## 七、明日计划

- D4-6 物料详情缓存（Cache Aside）—顺延
- D4-7 算法 + Redis 八股
- 待补：Day3 dev-log 中"@Transactional 移除"的回滚边界测试用例补充
