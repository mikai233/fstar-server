# 繁星课程表服务端

这个是繁星课程表客户端 [fstar-client](https://github.com/mikai233/fstar-client) 的后台服务应用，需要和客户端配合使用

## 技术栈

SpringBoot+JPA+Redis+MySQL

开发语言用的Kotlin，Java版本用的1.8

## 运行前配置

- 在MySQL中创建数据库fstar，然后执行`src/main/kotlin/resources/fstar_sql.sql`建表
- 安装Redis
- 配置七牛云存储（可选）
- 把配置文件中数据库的连接地址、用户名、密码更改成你自己的