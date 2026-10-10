# UserCenter 后端

这是一个基于 Spring Boot、MyBatis-Plus 和 MySQL 的用户中心后端项目。

## 环境要求

- JDK 25
- MySQL
- Maven（也可以使用项目自带的 Maven Wrapper）


### IntelliJ IDEA 配置

打开项目的运行配置，在 **application.yml** 文件中添加：

```text
DB_URL=jdbc:mysql://localhost:3306/你的数据库名
DB_USERNAME=你的数据库用户名（一般是 root）
DB_PASSWORD=你的数据库密码