# supplier-biz 供应链采购管理系统（后端）

基于 Spring Boot 3 的供应链采购业务后端，覆盖 **认证授权 → 主数据管理 → 采购订单主细表事务 → 库存并发控制** 的完整业务链路。本项目为个人作品集项目，按企业实际研发流程（需求卡片 → 技术方案 → 分支开发 → 规约扫描 → CR → Conventional Commits）迭代。

## 技术栈

| 分类 | 选型 | 版本 |
| :--- | :--- | :--- |
| 基础框架 | Spring Boot | 3.0.2（JDK 17） |
| ORM | MyBatis-Plus | 3.5.7 |
| 数据库 | MySQL | 8.x（utf8mb4 / InnoDB） |
| 缓存 / 分布式锁 | Redis + Redisson | Day 4 接入 |
| 认证 | JWT（jjwt） | 0.11.5，HS256 |
| 接口文档 | Knife4j + springdoc-openapi | 4.4.0 |
| 工具库 | Hutool | 5.8.x |
| 密码加密 | BCrypt（随机盐内嵌密文） | — |
| 规约门禁 | maven-pmd-plugin + P3C（阿里黄山版） | 3.21.0 / 2.1.1 |
| 构建 | Maven | 3.9+ |

## 已实现功能

### 1. 认证与授权
- BCrypt 密码存储（随机盐内嵌，防彩虹表），登录校验失败统一返回 4003，**不区分"用户不存在/密码错误"以防用户名枚举**
- JWT 签发与校验：payload 仅放 userId/username（Base64 非加密，不放敏感信息）
- `JwtInterceptor` 统一拦截 `/api/**`，白名单放行登录接口与接口文档；token 异常一律返回 401 并 `return false`

### 2. 物料管理
- 分页查询 + 名称/编码模糊搜索，入参/出参使用自定义 `PageQuery` / `PageDTO`，不让 MyBatis-Plus 的 `Page` 泄漏到接口契约

### 3. 采购订单（核心）
- 主表 + 明细表（一对多）级联创建，`@Transactional(rollbackFor = Exception.class)` 保证原子性
- 关键设计：
  - 先插主表拿到自增 id 回填，再批量插入明细（`saveBatch`）
  - `listByIds` 一次 IN 查询 + `Map<id, 物料>` 内存索引，**杜绝循环查库的 N+1 问题**
  - 金额用 `BigDecimal` 计算（除法显式 scale + `HALF_UP`），等值比较用 `compareTo`
  - 物料不存在 / 明细为空 → 抛业务异常整体回滚（含 InnoDB 自增 id 跳号的验证记录）
- 订单详情接口（VO 化进行中，避免 deleted/createdBy 等内部字段外泄）

### 4. 工程基础设施
- 统一响应体 `Result<T>` + 全局异常处理器（Service 抛业务异常，Controller 不写 try-catch）
- 全表审计五件套：`status` / `deleted`（逻辑删除）/ `created_at` / `updated_at` / 表注释
- 阿里《Java 开发手册》黄山版落地：禁魔法值、禁 Executors 创建线程池、卫语句、分层约束
- **P3C 规约扫描门禁**：`mvn pmd:pmd` 输出 `target/pmd.html`，提交前 Blocker/Critical 清零

## 进行中（Roadmap）

- [ ] 库存模块：分页查询（联表物料/仓库）、入库累加、出库扣减
- [ ] 超卖复现实验：100 线程并发扣减（`ThreadPoolExecutor` 显式参数 + `CountDownLatch` 发令枪）
- [ ] 并发修复：乐观锁（`@Version`）与 Redisson 分布式锁（细粒度 key + 看门狗）对比
- [ ] Redis 缓存：物料详情 Cache Aside（穿透 / 击穿 / 雪崩应对）

## 快速开始

### 环境要求
- JDK 17、Maven 3.9+、MySQL 8.x
- 本地 Redis（库存与缓存模块需要）

### 1. 准备数据库

创建数据库（表结构初始化脚本待补充至仓库）：

```sql
CREATE DATABASE supplier_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

需初始化 `sys_user` 表并写入一条 BCrypt 密文的 admin 用户（123456）。

### 2. 配置（密钥不入库）

敏感配置通过环境变量注入，均提供本地开发默认值：

| 环境变量 | 说明 | 不设置时的默认值 |
| :--- | :--- | :--- |
| `DB_PASSWORD` | 本地 MySQL 的 root 密码 | `root` |
| `JWT_SECRET` | JWT 签名密钥（≥32 字节，生产必改） | 仅用于本地开发的非生产密钥 |

### 3. 启动

```bash
mvn spring-boot:run
```

启动成功后控制台可见 `Tomcat started on port 8080`。

### 4. 接口文档

打开 Knife4j：<http://localhost:8080/doc.html>

调用需认证的接口前，先在「文档管理 → 全局参数设置」中添加 header：

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
├── config/            # WebMvc、MyBatis-Plus 分页插件、JWT 配置
├── controller/        # 接口层：只做收参、调 Service、包 Result
├── domain/
│   ├── dto/           # 入参/出参契约（PageQuery、PageDTO、CreateDTO）
│   └── po/            # 持久化对象，与表一一对应
├── mapper/            # MyBatis-Plus Mapper + XML
├── service/           # 业务层（impl 下放实现，事务边界在此）
└── util/              # JWT 等工具类
```

## 提交规范

遵循 Conventional Commits：`feat(模块): 描述` / `fix(模块): 描述` / `docs: 描述` / `chore: 描述`，功能开发在 `feature/*` 分支进行，验收通过后合入主干。
