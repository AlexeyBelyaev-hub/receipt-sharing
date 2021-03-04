# Receipt-sharing application 
**Share receipt with friends effortless**

### How does it work?
1. Scan receipt in bar
2. Recognize it and get into your device
3. Add persons
4. Choose products for each person
5. Get totals and mail results to your friends right from the app

You won't use calculator any more!
![Tool image](https://user-images.githubusercontent.com/56299712/109296753-ebd8e680-7841-11eb-8c00-33e0befa3f96.png)

Application allow to sign-in with email confirmation and save receipts history for mutual settlements control.

### Life-cycle's stage.
Product consist of 3 parts:
1. Backend - developing
2. Web app for presentation purposes - developing
3. Mobile apps for iOS and Android - not started

### Used technologies.
1. Backend:
    * Java 11
    * Spring Boot
    * Postgres, flyway
    * Spring JDBC
    * Spring Security
    * JUnit 4
2. Web:
    * Spring MVC
    * Thymeleaf
    * JavaScript
    * SCSS, Bootstrap 4
    
### Installation

1. Create database. In this example used postgresql and database name: receipt_sharing.

2. Add application.properties to resources folder and configure it considering your environment:

```
spring.flyway.baselineOnMigrate = true
spring.datasource.flyway.jdbc-url=jdbc:postgresql://localhost:5432/receipt_sharing

spring.datasource.username = postgres
spring.datasource.password = 
spring.datasource.jdbc-url = jdbc:postgresql://localhost:5432/receipt_sharing
spring.datasource.pool-size=30

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.startssl.enable=true

spring.main.allow-bean-definition-overriding=true
```
3. While first start flyway create data schema according script in V1_Create_schema.sql


*Written by @AlexeyBelyaev 
