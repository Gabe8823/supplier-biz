# supplier-biz · 供应链采购管理系统

基于 Spring Boot 3 的供应链采购业务后端，覆盖 **认证授权 → 供应商/物料主数据 → 采购订单 → 库存与预警 → 异步通知** 的完整业务链路，并按企业研发流程（需求卡片 → 技术方案 → 分支开发 → 规约扫描 → CR → Conventional Commits）迭代。

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.2-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue)
![Redis](https://img.shields.io/badge/Redis-Redisson%203.24.3-red)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Spring%20AMQP-ff69b4)

## 技术栈

| 分类 | 选型 | 版本 / 说明 |
| :--- | :--- | :--- |
| 基础框架 | Spring Boot | 3.0.2（JDK 17） |
| ORM | MyBatis-Plus | 3.5.7 |
| 数据库 | MySQL | 8.x（utf8mb4 / InnoDB） |
| 缓存 / 分布式锁 | Redis + Redisson | 3.24.3 |
| 消息队列 | RabbitMQ | spring-boot-starter-amqp |
| 认证 | JWT（jjwt） | 0.11.5，HS256 |
| 接口文档 | Knife4j + springdoc-openapi | 4.4.0 |
| 工具库 | Hutool | 5.8.46 |
| 密码加密 | BCrypt | 随机盐内嵌密文 |
| 规约门禁 | maven-pmd-plugin + P3C（阿里黄山版） | 3.21.0 / 2.1.1 |
| 构建 | Maven | 3.9+ |

## 已实现功能

### 1. 认证与授权
- BCrypt 存储密码（随机盐内嵌，防彩虹表）；登录失败统一返回 4003，不区分「用户不存在 / 密码错误」，防用户名枚举
- JWT payload 仅放 userId / username；`JwtInterceptor` 拦截 `/api/**`，白名单放行登录与接口文档，异常统一返回 401

### 2. 供应商与物料主数据
- 供应商分页查询、详情、新增、更新、逻辑删除、启停状态
- 更新使用 `LambdaUpdateWrapper` 显式 `set`，支持把字段置空；入参/出参 DTO 与 VO 隔离
- 物料分页查询、名称/编码模糊搜索、详情查询（走 Redis 缓存）

### 3. 采购订单（核心）
- 主表 + 明细表一对多级联创建，`@Transactional(rollbackFor = Exception.class)` 保证原子性
- 状态机：`PENDING(1) → APPROVED(2) / REJECTED(3) / CLOSED(4)`，审核、驳回、作废均以卫语句校验当前状态
- 作废使用独立的 `cancelled_at / cancelled_by` 留痕，不污染审核字段语义
- 关键细节：
  - 先插主表拿到自增 id 回填，再 `saveBatch` 批量插入明细
  - `listByIds` 一次 IN 查询 + `Map<id, 物料>` 内存索引，避免循环查库的 N+1
  - 金额用 `BigDecimal`（每行 `setScale(2, HALF_UP)` 后累加），比较用 `compareTo`
  - 订单详情 VO 化，剔除 `deleted` / 审计字段等内部字段
- 列表支持供应商名、状态、日期区间组合筛选；日期采用裸列比较，避免 `DATE()` 导致索引失效

### 4. 库存与预警
- 库存分页联表 `inventory / materials / warehouse`，返回物料名与仓库名
- 入库（有记录累加 / 无记录新建）、出库扣减，联合唯一键 `(material_id, warehouse_id)`
- 预警查询：`LEFT JOIN` 输出可读名称，按 `available_quantity` 升序返回低于阈值的库存

### 5. 库存并发控制（项目亮点）
- **问题复现**：100 线程并发扣减、初始库存 10，朴素「先查后改」出现丢失更新（66 次请求成功，但库存只降到 0）
- **修复方案**：Redisson 分布式锁为主（`lock:inventory:{materialId}:{warehouseId}` 细粒度 + watchdog 续期），数据库乐观锁 `@Version` 兜底
- **验证结果**：100 并发下 10 成功 / 90 快速失败，库存归零且不为负
- **边界处理**：锁内自旋重试与事务可见性冲突，`stockIn` / `stockOut` 不开启外层事务，避免自旋读到旧值

### 6. 缓存设计
- 物料详情 Cache Aside：查缓存 → miss 查库 → 回填 JSON，二次查询零 SQL
- 空值缓存 2 分钟防穿透；正常数据基础 TTL 30 分钟并叠加 ±5 分钟随机偏移防雪崩

### 7. RabbitMQ 异步通知
- `order.direct` 交换机 + durable 队列 `order.notify.queue`（routingKey `order.notified`）
- 订单审核 / 驳回 / 作废落库后发送供应商通知，消息体为 Jackson JSON
- 生产者开启 `publisher-confirm-type: correlated` 与 `publisher-returns`，通过 confirm / returns 回调感知未确认与路由失败
- 消费端 `@RabbitListener` 异步处理，遵循「先落库、后发消息」，避免数据库回滚产生假通知

### 8. 工程基础设施
- 统一响应体 `Result<T>` + 全局异常处理：Service 抛业务异常，Controller 不写 try-catch
- 全表审计字段：`status` / `deleted`（逻辑删除）/ `created_at` / `updated_at`
- 阿里《Java 开发手册》黄山版：禁魔法值、禁 `Executors` 创建线程池、卫语句、分层约束
- P3C 规约门禁：`mvn pmd:pmd` 生成 `target/pmd.html`，提交前 Blocker / Critical 清零

## 接口概览

| 模块 | 方法与路径 | 说明 |
| :--- | :--- | :--- |
| 认证 | `POST /api/auth/login` | 登录并返回 JWT |
| 供应商 | `GET /api/suppliers/page` | 分页查询 |
| 供应商 | `GET /api/suppliers/{id}` | 详情 |
| 供应商 | `POST /api/suppliers` | 新增 |
| 供应商 | `PUT /api/suppliers` | 更新 |
| 供应商 | `DELETE /api/suppliers/{id}` | 逻辑删除 |
| 供应商 | `PUT /api/suppliers/{id}/status` | 启停 |
| 物料 | `GET /api/materials` | 分页 + 模糊搜索 |
| 物料 | `GET /api/materials/{id}` | 详情（缓存） |
| 采购订单 | `POST /api/purchase-orders` | 创建（主表 + 明细） |
| 采购订单 | `GET /api/purchase-orders/{id}` | 详情 |
| 采购订单 | `GET /api/purchase-orders/page` | 分页 + 组合筛选 |
| 采购订单 | `PUT /api/purchase-orders/{id}/approve` | 审核通过 |
| 采购订单 | `PUT /api/purchase-orders/{id}/reject` | 驳回 |
| 采购订单 | `PUT /api/purchase-orders/{id}/cancel` | 作废 |
| 库存 | `GET /api/inventory/page` | 分页联表查询 |
| 库存 | `POST /api/inventory/stock-in` | 入库 |
| 库存 | `POST /api/inventory/stock-out` | 出库 |
| 库存 | `GET /api/inventory/warning` | 低于阈值的预警列表 |

## 快速开始

### 环境要求
- JDK 17、Maven 3.9+
- MySQL 8.x、Redis、RabbitMQ（通知功能需要）

### 1. 准备数据库

```sql
CREATE DATABASE supplier_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

初始化 `sys_user` 表并写入一条 BCrypt 密文的 admin 用户（建表脚本与初始化数据待补充）。

### 2. 配置

仓库内不含任何密码或密钥，以下配置从环境变量读取，本地开发也可写在 `application-local.yaml` 中覆盖：

| 变量 | 说明 | 是否必填 |
| :--- | :--- | :--- |
| `DB_PASSWORD` | MySQL root 密码 | 必填 |
| `JWT_SECRET` | JWT 签名密钥（≥32 字节） | 必填 |
| `MQ_PASSWORD` | RabbitMQ 密码 | 必填 |
| `MQ_HOST` | RabbitMQ 地址 | 可选，默认 `localhost` |

本地开发：在 `src/main/resources/application-local.yaml` 填写真实值即可，该文件已被 `.gitignore` 忽略，启动时自动加载：

```yaml
spring:
  rabbitmq:
    host: 127.0.0.1
    password: your-mq-password
  datasource:
    password: your-db-password

jwt:
  secret: at-least-32-bytes-random-secret
```

生产环境直接用环境变量注入，不要把密钥写进配置文件。

### 3. 启动

```bash
mvn spring-boot:run
```

启动成功后控制台可见 `Tomcat started on port 8080`。

### 4. 接口文档

打开 Knife4j：<http://localhost:8080/doc.html>

调用需认证的接口前，在「文档管理 → 全局参数设置」添加 header：

- 参数名：`Authorization`
- 参数值：`Bearer <登录接口返回的 token>`（Bearer 后带一个空格）

### 5. 规约扫描

```bash
mvn pmd:pmd        # 报告：target/pmd.html、target/pmd.xml
```

## 目录结构

```
src/main/java/com/sup/supplierbiz
├── common/            # 统一响应、业务异常、全局异常处理、JWT 拦截器、枚举
├── config/            # WebMvc、MyBatis-Plus、JWT、RabbitMQ、OpenAPI
├── controller/        # 接口层：收参、调 Service、包 Result
├── domain/
│   ├── dto/           # 入参契约
│   ├── po/            # 持久化对象
│   └── vo/            # 出参视图对象
├── mapper/            # MyBatis-Plus Mapper + XML
├── mq/                # RabbitMQ 生产者与消费者
├── service/           # 业务层（impl 下放实现，事务边界在此）
└── util/              # JWT 等工具类
```

## 安全与隐私

- 仓库不包含简历、证件照、个人联系方式等个人信息；`.gitignore` 已屏蔽 `resume/`、`个人简历*`、`*.pdf`、`*.docx`、`photo*.jpg` 等文件
- 仓库内配置文件不含任何密码或密钥；数据库密码、JWT 密钥、RabbitMQ 密码从环境变量或本地 `application-local.yaml` 读取
- 本地的真实配置建议放入 `application-local.yaml`（已被忽略），不要提交 `.env` 或密钥文件
- 提交前建议自查：`git status` 与 `git diff --cached`，确认没有个人材料或凭据进入暂存区

## 开发流程与提交规范

- 功能开发在 `feature/*` 分支进行，验收通过后合入主干
- Conventional Commits：`feat(模块): 描述` / `fix(模块): 描述` / `docs: 描述` / `chore: 描述`
- 提交前执行 P3C 扫描，Blocker / Critical 清零
- 开发记录见 [`docs/dev-log`](docs/dev-log)

## Roadmap

- [ ] 登录图形验证码（Redis 存储 + TTL + 一次性校验）
- [ ] 数据库建表与初始化脚本入库
- [ ] 按业务域拆分微服务：Nacos + OpenFeign + Gateway + Sentinel
- [ ] Nginx 反向代理与容器化部署
