# spring-cloud-oauth2-server 基于Spring cloud实现的Oauth2服务

## Warning！练手做！

本项目基于某大神的oauth服务修改。由于修改时间已久，故未找到该大神的项目地址，如果有人知道请告诉我，谢谢！

前置条件：

  1. jdk1.8
  2. Eureka服务(![spring-cloud-eureka-server](https://github.com/Maxith/spring-cloud-eureka-server))
  3. config配置中心服务(![spring-cloud-server-consumer](https://github.com/Maxith/spring-cloud-server-consumer))
  4. 基于Eureka框架的用户信息服务(![spring-cloud-system](https://github.com/Maxith/spring-cloud-system)) 
  5. mysql数据库，其他数据库需自行实现。数据库sql文件在resource下的db文件夹中。

关于用户信息服务：
  1. 需修改服务名称：application.properties > feign.system.name
  2. 用户接口在：com.maxith.system.service.ISystemUserService,如自行实现用户信息服务，请实现该接口的方法并修改对应配置。

```java 
application.properties : 

###userServer
systemUserService.selectSystemUserByUsername=/system/api/login/selectSystemUserByUsername
systemUserService.selectUserByUsernameAndPassword=/system/api/login/selectUserByUsernameAndPassword
systemUserService.updateLoginTime=/system/api/login/updateLoginTime
```

## TODO

  1. fix bug
  2. more feature
  
## License
Apache License 2.0
