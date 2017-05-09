## oneconf
集中式的配置管理服务

虽然有disconf这样完备的分布式配置，但是对于小规模的又想简单点的多服务项目，特别是很多配置是都一致情况下，分散很多个配置文件在不同服务中非常难以维护。

1.在classpath下放置conf.properties文件

```properties
 #配置服务的启动ip
confserver.ip=127.0.0.1
 #配置服务的启动端口
confserver.port=8806
 #配置项分组,逗号分隔
conf.proj1=jdbc,redis

 #配置项
jdbc.url=jdbc:mysql://localhost/test
jdbc.username=test
jdbc.password=test

redis.host=localhost
redis.port=6379
```

2.启动配置服务

```
$ java com.code1024.oneconf.ConfServer
```

可以用工具访问检查下

```
$ curl 127.0.0.1:8806/proj1
```

会返回具体分组的配置。

3.在spring配置文件，修改以前的配置

```xml
<context:property-placeholder location="classpath:conf.properties" />
```

为：

```xml
<bean id="confProperties" class="com.code1024.oneconf.ConfPropertiesFactoryBean"
		p:location="/conf2.properties" />
<context:property-placeholder properties-ref="confProperties" />
```

其中conf2.properties的配置为：

```properties
confserver.url=http://127.0.0.1:8806/proj1
```

具体代码可以参见oneconf-sample。
