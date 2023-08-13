# microservice-demo

## 服务注册与发现

成功在本机部署了一个微服务项目，并部署了一个消费者和两个服务提供者实例。

![image-20230725164730635](/Users/yuanzilvdong/Library/Application Support/typora-user-images/image-20230725164730635.png)

## 实现了基于Nginx的负载均衡，实现了客户端到多个服务端间的调用

1、服务端提供一个 `/api/getDateTime` 接口，返回形如 yyyy-MM-dd HH:mm:ss 格式的 JSON 字符串数据，代表今日的日期时间。

2、采用现有的服务端侧软负载均衡工具（nginx），实现客户端到多个服务端（至少2个）间的调用。

3、客户端通过 nginx 调用服务端接口，并通过暴露 `/api/echo` 接口输出回应，回应内容包含从服务端获取到的 `datetime` 属性，其回应格式如下 Hello Kingsoft Cloud - yyyy-MM-dd HH:mm:ss

![image-20230725165117763](/Users/yuanzilvdong/Library/Application Support/typora-user-images/image-20230725165117763.png)

当其中一个服务挂了后，会有一秒左右的请求失败可能，然后请求恢复正常。即负载均衡生效
