# Day3 开发日志（2026-10-09）

> 规范基线：《供应链项目-开发规范（阿里Java手册落地版）》

## 一、今日完成

- JWT 鉴权拦截器 + RBAC 权限拦截器落地
- 白名单路径配置
- 采购订单 createOrder 主细表事务
- 订单查询接口

## 二、踩坑记录（问题→根因→解决）

### 坑 1：卫语句三 bug（JWT 拦截器）

JwtInterceptor.preHandle 中三个卫语句曾连续踩坑，最终改为"每个失败条件单独判断并提前退出"：

- **bug 1**：`Authorization` 头为 null 时直接 `header.startsWith("Bearer ")` → NPE
  - 根因：未判空就调用字符串方法
  - 解决：`if (header == null || !header.startsWith(BEARER_PREFIX))` 合并判空
- **bug 2**：未校验 Bearer 前缀就 `substring(7)` 取 token → 非 Bearer 头取错子串
  - 根因：假设所有 Authorization 头都是 Bearer 格式
  - 解决：startsWith 校验通过后再 `substring(TOKEN_START_INDEX)`
- **bug 3**：token 解析异常只 `return false`，前端拿不到 401 响应体
  - 根因：`return false` 只中断链路，不会自动写响应
  - 解决：catch 块中调用 `write401(response)` 写出 Result JSON 再 return false

### 坑 2：拦截器 return false 不返回响应体

- 问题：HandlerInterceptor.preHandle 不通过时 `return false`，前端拿不到响应体
- 根因：`return false` 只中断链路，不会自动写响应；需要手动 `response.getWriter().write(...)` 写出 Result JSON
- 解决：在 return false 之前用 response 写错误 JSON + 设状态码（抽取 `write401` 私有方法复用）

### 坑 3：白名单斜杠坑

- 问题：白名单路径匹配不一致，本该放行的请求被拦
- 根因：最初用 `request.getRequestURI().equals("/api/auth/login")` 手动匹配，`/api/auth/login/`（带尾斜杠）匹配失败；且 Knife4j 文档路径（`/doc.html`、`/swagger-ui/**`、`/v3/api-docs/**`）未加入白名单导致文档 401
- 解决：改用 Spring MVC 原生 `excludePathPatterns(...)` 由框架做 Ant 风格路径匹配，白名单一次性列全：`/api/auth/login`、`/doc.html`、`/swagger-ui/**`、`/v3/api-docs/**`、`/swagger-resources/**`、`/webjars/**`

### 坑 4：@PathVariable 坑

- 问题：`@GetMapping("/{id}")` + `@PathVariable Long id` 在某些环境下报 "Name for argument of type [java.lang.Long] not specified"
- 根因：Spring Boot 3 编译默认不保留方法参数名（需 `-parameters`），`@PathVariable` 不显式指定 `name` 时框架无法解析
- 解决：pom.xml 中 maven-compiler-plugin 配置 `<parameters>true</parameters>`，或显式写 `@PathVariable("id") Long id`；本项目通过 compiler 插件参数解决，代码中保持简洁写法

## 三、面试点

1. Spring MVC 拦截器 vs Servlet Filter 的执行顺序与差异
2. 拦截器 preHandle 返回 false 后续链路行为；如何返回业务错误 JSON
3. @PathVariable 与 @RequestParam 的适用场景
4. 白名单路径匹配的几种实现方式（String equals / PathMatcher / AntPathMatcher）

## 四、明日计划

- Day4 任务清单见 `供应链项目-Day4任务细化清单.md`
- 重点：库存并发实验 + Redisson 锁修复
