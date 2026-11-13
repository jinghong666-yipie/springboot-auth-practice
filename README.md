# Spring Boot 用户名密码登录认证实战

本工程按配套课件的技术路线实现：Spring Boot 3 + Spring MVC + MyBatis-Plus + MySQL + BCrypt + JWT。

## 1. 环境要求

- JDK 17+
- MySQL 8+
- Maven 3.8+（也可在 IntelliJ IDEA 中导入 Gradle 工程）

## 2. 创建数据库

执行 `src/main/resources/schema.sql`。默认连接配置为：
- 数据库：`auth_demo`
- 用户名：`root`
- 密码：`root`

可通过 `DB_USERNAME`、`DB_PASSWORD` 环境变量覆盖。

## 3. 设置 JWT 密钥

JWT 使用 HMAC-SHA 密钥。先生成至少 32 字节的随机密钥，再进行 Base64 编码。

Linux / macOS：
```bash
openssl rand -base64 32
export JWT_SECRET_BASE64='把生成的内容粘贴到这里'
```

Windows PowerShell：
```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET_BASE64 = [Convert]::ToBase64String($bytes)
```

不要把真实密钥提交到 Git，也不要在生产环境使用固定示例密钥。

## 4. 启动项目

在项目根目录执行：
```bash
mvn spring-boot:run
```

首次启动（非 `prod` Profile）会自动创建演示用户：
- 用户名：`alice`
- 密码：`Passw0rd!`

演示用户密码通过 `BCryptPasswordEncoder.encode()` 生成哈希后保存。不要在生产环境使用演示账号。

## 5. 测试接口

### 登录

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

```json
{
  "username": "alice",
  "password": "Passw0rd!"
}
```

成功响应示例：
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "token": "eyJ...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

### 访问受保护接口

把登录响应中的 token 复制到请求头：

```http
GET http://localhost:8080/api/users/me
Authorization: Bearer eyJ...
```

无 Token、Token 格式错误、签名无效或过期时返回 HTTP 401。

## 6. 核心代码位置

| 功能 | 文件 |
|---|---|
| BCrypt Bean | `config/PasswordConfig.java` |
| 用户表实体与 Mapper | `entity/User.java`、`mapper/UserMapper.java` |
| 登录业务、matches() | `service/AuthService.java` |
| JWT 签发与解析 | `util/JwtUtil.java` |
| 登录接口 | `controller/AuthController.java` |
| 受保护接口 | `controller/UserController.java` |
| Token 拦截器 | `security/AuthInterceptor.java` |
| 拦截器注册 | `config/WebConfig.java` |

## 7. 课堂讲解顺序

1. 先看 `PasswordConfig`，理解 PasswordEncoder 注入。
2. 看 `DemoUserInitializer`，观察注册/初始化时 `encode()` 如何保存哈希。
3. 看 `AuthService.login()`，重点讲 `matches(rawPassword, encodedPassword)`。
4. 看 `JwtUtil.generate()`，理解登录成功后签发 Token。
5. 看 `AuthInterceptor.preHandle()`，理解后续请求如何验证 Token。
6. 用 Postman 或 Apifox 完成登录和 `/api/users/me` 测试。

## 8. 教学工程的边界

这是用于教学演示的最小可运行示例，不等同于完整生产级安全方案。正式项目还应补充 HTTPS、登录限流、刷新令牌、登出失效机制、密钥轮换、审计日志、权限控制，以及更完善的异常与监控处理。JWT Payload 仅做 Base64URL 编码并签名，不是加密容器，不要放入密码等敏感信息。


## Maven 构建

```bash
mvn clean package
java -jar target/springboot-auth-practice-1.0.0.jar
```

本项目使用 Maven，依赖和 Spring Boot 打包插件统一配置在根目录 `pom.xml`。在 IntelliJ IDEA 中导入根目录 `pom.xml` 即可。
