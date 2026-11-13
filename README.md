**# 资产管理系统后端使用说明

## 一、系统简介

本系统是一套基于 **Spring Boot 3.2.5** 开发的企业资产管理系统后端，覆盖资产从入库、申请、审批、分配到归还的全生命周期管理，并内置完善的权限体系与操作日志审计。

**核心功能模块：**

| 模块 | 接口前缀 | 功能说明 |
|------|----------|----------|
| 认证登录 | `/api/auth` | 用户登录、获取当前用户信息、修改密码 |
| 资产管理 | `/api/asset` | 资产的新增、修改、删除、分页查询 |
| 资产分类 | `/api/category` | 资产分类树的维护 |
| 资产申请 | `/api/application` | 员工发起资产申请、管理员审批 |
| 资产分配 | `/api/allocation` | 资产分配记录管理（支持自动分配） |
| 资产归还 | `/api/return` | 员工归还资产、管理员归还审核 |
| 部门管理 | `/api/dept` | 部门树维护、导入导出 |
| 用户管理 | `/api/user` | 系统用户的管理 |
| 角色管理 | `/api/role` | 角色与权限分配 |
| 菜单管理 | `/api/menu` | 系统菜单树配置 |
| 统计报表 | `/api/statistics` | 仪表盘、资产状态/分类分布统计 |
| 文件管理 | `/api/file` | 文件上传、预览、删除（支持本地/OSS 双模式） |
| 操作日志 | `/api/log` | 操作日志的分页查询 |

## 二、技术栈

- **Java 17** + **Spring Boot 3.2.5**
- **Spring Security** + **JJWT 0.11.5**：JWT 无状态认证，Token 有效期 24 小时
- **MyBatis-Plus 3.5.5**：ORM 框架（下划线转驼峰、逻辑删除）
- **MySQL 8.x**：数据库（utf8mb4 编码）
- **MinIO 8.5.12**：可选的 S3 兼容对象存储
- **springdoc-openapi 2.5.0**：Swagger 在线接口文档
- **Lombok**：简化实体代码

## 三、环境准备

1. **JDK 17** 及以上
2. **MySQL 8.x** 数据库
3. **Maven 3.6+**（或使用 IDE 内置 Maven）

## 四、快速启动

### 步骤 1：初始化数据库

依次在 MySQL 中执行以下两个脚本（无需手动建库，`schema.sql` 自带 `CREATE DATABASE` 语句）：

1. `backend/src/main/resources/sql/schema.sql` —— 自动创建 `asset_management` 库及所有数据表
2. `backend/src/main/resources/sql/data.sql` —— 写入基础数据（角色、菜单、分类等）

命令行方式示例：

```bash
mysql -u root -p < src/main/resources/sql/schema.sql
mysql -u root -p < src/main/resources/sql/data.sql
```

### 步骤 2：修改数据库配置

编辑 `backend/src/main/resources/application.yml`，按本机实际情况调整：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/asset_management
    username: root      # 改为你的数据库用户名
    password: 123456    # 改为你的数据库密码
```

> 如需使用 MinIO/OSS 存储文件，请在 `application.yml` 的 `storage` 配置节中把 `mode` 改为 `oss` 并填写相应连接信息；默认 `local` 模式会将上传文件保存到 `./data/upload` 目录。

### 步骤 3：启动服务

```bash
cd backend
mvn spring-boot:run
```

或在 IDE 中直接运行启动类 `com.asset.management.AssetManagementApplication`。

服务默认运行在 **http://localhost:8080**，在线接口文档访问 **http://localhost:8080/swagger-ui.html**。

### 步骤 4：使用默认账号登录

首次启动时，系统会通过 `DataInitializer` 自动创建三个演示账号（幂等操作，已存在则跳过）：

| 账号 | 密码 | 角色 | 说明 |
|------|------|------|------|
| `admin` | `admin123` | 超级管理员 | 拥有全部权限 |
| `manager` | `123456` | 资产管理员 | 资产、审批、分配等业务权限 |
| `employee` | `123456` | 普通员工 | 申请、归还等基础权限 |

> 生产环境部署后请立即修改默认密码（可通过修改密码接口或前端页面修改）。

## 五、登录与接口调用

### 1. 登录获取 Token

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

响应中会返回 JWT Token（有效期 24 小时）。

### 2. 携带 Token 调用接口

除登录、文件预览、Swagger 文档外，所有接口均需在请求头携带 Token：

```http
Authorization: Bearer <你的Token>
```

### 3. 接口权限

接口采用 Spring Security 方法级权限控制（如 `@PreAuthorize("hasAuthority('asset:list')")`），权限由角色的菜单权限数据决定。未授权访问将返回 403。

## 六、典型业务流程

1. **管理员**在"资产管理"中录入资产，在"资产分类"中维护分类树；
2. **员工**在"资产申请"中发起申请；
3. **管理员**在"资产申请"中审批，通过后在"资产分配"中执行分配（支持自动分配）；
4. **员工**使用完毕后在"资产归还"中发起归还；
5. **管理员**在"资产归还"中审核归还结果，资产状态自动流转；
6. 全程操作由 AOP 切面自动记录到"操作日志"，可在 `/api/log` 中查询；
7. "统计报表"提供仪表盘与资产状态/分类分布的可视化数据。

## 七、常见问题

| 问题 | 解决方法 |
|------|----------|
| 启动报数据库连接失败 | 确认 MySQL 已启动、账号密码正确、`asset_management` 库已通过脚本创建 |
| 登录返回 401 | 检查用户名密码；Token 过期（24 小时）后需重新登录 |
| 接口返回 403 | 当前账号角色无该接口权限，用 admin 登录或调整角色权限 |
| 文件上传失败 | 检查 `storage.mode` 配置；local 模式确认 `./data/upload` 目录可写；oss 模式确认 MinIO 连接信息 |
| 升级文件存储方案 | 参考 `src/main/resources/sql/upgrade_file_storage.sql` 说明执行升级脚本 |

## 八、目录结构

```
backend/
├── pom.xml
└── src/main/
    ├── java/com/asset/management/
    │   ├── AssetManagementApplication.java   启动类
    │   ├── aspect/        操作日志切面（AOP）
    │   ├── common/        通用注解、枚举常量、异常、统一返回结果
    │   ├── config/        安全配置、数据初始化、MyBatis-Plus、OpenAPI 配置
    │   ├── controller/    13 个业务 Controller
    │   ├── dto/           请求/响应 DTO
    │   ├── entity/        15 个数据库实体
    │   ├── mapper/        数据访问层
    │   ├── security/      JWT 工具、认证过滤器、用户详情服务
    │   ├── service/       15 个业务 Service
    │   ├── storage/       文件存储抽象（local/OSS 可切换）
    │   └── util/          业务编号生成工具
    └── resources/
        ├── application.yml        应用配置
        └── sql/
            ├── schema.sql                 建库建表脚本
            ├── data.sql                   基础数据脚本
            └── upgrade_file_storage.sql   文件存储升级脚本
```
**
