#  trade-order



## 说明

* 项目编码：UTF-8
* JDK:1.8
* IDE：不限
* 项目开发方式  
  - 分支开发，发布时合并主干
  - 环境隔离（profile：local、dev、uat、prod）

## 模块

* biz：公共业务模块（供其它模块调用）<br/>
* biz-base:基础业务 <br/>
* biz-req-trace:请求链路跟踪<br/>
* biz-glue:<br/>
* trade-order-api：业务代码 <br/>
* trade-order-gateway：路由服务 <br/>


###  代码提交规范

#### 提交的原则

* 保持较小的提交粒度，一次提交只做一件事情。提交的粒度可以是一个小功能点或者一个bugfix。更细粒度的提交便于追踪bug，撤回具体的改动比撤回一大块改动更容易。
* 提交记录一定要明确，避免大量重复及语焉不详。
* 无直接关联的文件不要在同一次提交。
* 只有编译通过的代码才可以提交。
* 每次提交代码，都要写Commit message（提交说明），认真对待提交备注，很有可能以后看备注的人是你自己。
* git push无须过于频繁。不要每提交一次就推送一次，多积攒几个提交后再推送，这样可以避免在进行一次提交后发现代码中还有小错误。毕竟git push之后要再撤销公共分支的代码，还是要麻烦一些。
* 功能需求仅一个人进行开发时，在功能完成之前不要着急创建远程分支。


#### commit message前缀

```
提交规则：

feature或feat:新功能
add : 新增相关内容
fix:修复bug
docs:文档添加、修改，如README, CHANGELOG。
style:格式（不影响代码运行的变动,如格式化，缩进等）
refactor:重构（即不是新增功能，也不是修改bug的代码变动）
test:增加测试
chore:构建过程或辅助工具的变动(如package.sh)
deps:依赖变更（比如guava版本变更)
revert:撤销以前的commit(必须写清楚)
log:增加、调整log输出等
perf:性能优化
config:配置文件修改（如第三方接口url调整）
remove:移除
experience:体验优化
ui:纯粹CSS样式变动，不影响功能代码
other:其他原因，如上述不能覆盖，才用。如：合并代码，解决代码冲突等

eg：
refactor：优化调用链
```

## 功能介绍与解析

### 1、实现类Nginx请求转发功能

**service.LbService.java:**实现了随机、轮询、hash、权重四种负载均衡算法，本项目默认使用的是轮询算法

**GatewayService.java:**实现了loadLalancing方法，首先获取接口，在根据GatewayController中对应接口的信息选择合适的请求转发方法进行转发

### 2、查询订单详情

**WorkerController.java:**在该类中编写各个需求的controller，具体代码实现在对应的service中实现

**QueryOrderInfoService.java：**在本层实现了查询订单详情的功能，具体流程如下：

1）首先去redis中查询订单详情是否存在，若存在则直接返回数据

2）通过request获得upsteam信息，根据传入的id到ksc_trade_order中查询出对应的user_id、region_id、price_value

3）根据得到的用户id调用远端接口获得用户信息，本接口耗时2秒

4）先去redis中查看是否有对应的地区信息，若没有则去接口中得到所有的地区信息存入redis中，并返回需要的地区信息

5）根据orderId查询数据库的ksc_trade_product_config查询需要的参数信息

6）将以上的数据组装成DTO，存入redis中并返回封装好的对象

为了减少处理时间，采用了redis做缓存，程序内则使用了多线程去分别访问接口

### 3、根据机房Id查询机房名称

**QueryRegionNameService.java：**使用了Springboot的`@Retryable`实现了调用重试，其他内容与功能2流程基本类似，从远程接口中取出数据，并封装成DTO返回

### 4、订单优惠卷抵扣公摊

**DeductService.java:**实现思路如下：（此处有两种实现思路，一种先查优惠卷是否重复，若重复则直接返回；一种是先查该订单是否存在，若存在再查优惠卷是否重复，不考虑请求时间的话。两种思路均可）

1）先看一下该优惠卷是否用过,用过了则返回提醒“该优惠卷已使用过，无法再用”

2）判断是否存在该订单号对应的数据条数，

3）如果条数大于0，则取出最新的一条数据，在ksc_voucher_deduct表中插入对应的orderId,voucherNo,amount,BeforeDeductAmount，afterDeductAmount需要计算，小于或等于0按0处理

4）如果条数等于0，则取出k s c_trade_order表中的priceValue，在ksc_voucher_deduct表中插入对应的orderId,voucherNo,amount,BeforeDeductAmount，afterDeductAmount需要计算，小于或等于0按0处理

### 5、基于Redis实现漏桶限流算法，并在API调用上体现

本部分采用redis+注解+过滤器实现

redis采用lua脚本实现，保证了操作的原子性。记录上次请求后桶中水量，上次请求的时间，计算出当前桶中的水量，判断桶中水是否满了，未满则继续进入，满了则直接返回

注解包含了key，最大蓄水量，水流速率

使用Interceptor进行全局的处理，即只要被该注解修饰的方法都需要经过漏桶。

设置漏桶大小为5，每秒处理5个请求，传入10个请求，立即执行，只能接受5个请求，其余请求被拒绝

![image-20230722230804975](/Users/yuanzilvdong/Library/Application Support/typora-user-images/image-20230722230804975.png)

![image-20230723223959534](/Users/yuanzilvdong/Library/Application Support/typora-user-images/image-20230723223959534.png)

### 6、简单的链路跟踪实现

本部分采用aop实现。编写一下切面类，作用于所有的controller上，赋给requestId并用MDC存入日志中，保证后续流程中都可以取到requestId，如果header中没有传过来requestId，则利用uuid生成一个进行链路跟踪记录。



