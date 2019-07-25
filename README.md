# easy-spring-boot-starter

English README Version

easy-spring-boot-starter是一个基于配置的Restful Web开发框架。根据配置提供多种登陆模式，定制标准输出和错误输出的数据结构等功能，代码生成器。

## 开始
在start.spring.io新建一个spring-boot的项目，根据实际情况包含如下依赖：
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- mysql-connector-java (Optional)
- spring-security-oauth2-client (如果使用OAuth)
- spring-boot-devtools (Optional)
- spring-boot-starter-actuator (Optional)
- spring-boot-configuration-processor (Optional)
- spring-boot-starter-test (Optional)
- spring-security-test (Optional)

### Maven依赖
```xml
<dependency>
    <groupId>com.lephix</groupId>
    <artifactId>easy-spring-boot-starter</artifactId>
    <version>${easy-version}</version>
</dependency>
```

### 应用配置
最简单的application.properties配置。
- 管理员用户名密码：admin/admin 角色：ROLE_ADMIN
- 普通用户名密码：user/user 角色：ROLE_USER

```properties
easy:
  security:
    source: memory
```
启动并打开浏览器，访问 [首页](http://localhost:8080)。

当前的配置，框架将接管API输出格式，错误时输出格式，/api/admin/**下的所有API需要有ROLE_ADMIN权限。


## 代码生成器
在项目的src/main/java的任何包名下创建一个Generator类，示例如下。运行main函数之后，将生成entity,repository,service,controller。至此基本的增删改查后台功能就已经完成。
```java
public class Generator {
    public static void main(String[] args) throws Exception {
        CodeGenerator cg = new CodeGenerator("com.lephix.easy-example", null, 
                "jdbc:mysql://localhost:3306/easy_jdbc", "root", "");
        cg.generateAll();
    }
}
```

### 命名规范

#### 代码命名规范
- 实体类：大写开头的驼峰命名，例如`User`。
- 与实体类相关的类：包括Repository, Service, Controller等，都已`实体类名 + 类型`进行命名，例如`UserRepository`。

#### 数据库表名
- 表名：表名采用单词+下划线的命名，例如`love_song`。
- 字段名：同表名命名规范。
