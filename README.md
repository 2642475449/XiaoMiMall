## 系统介绍

本系统是使用SpringBoot开发的支付+电商平台双系统，除了实现基本的登录、查看商品列表、购物车、下单等功能，项目中添加了支付宝与微信的支付功能

## 基本结构

![](http://jianglan.ltd/wp-content/uploads/2021/02/Snipaste_2021-02-21_23-03.png)

## 功能演示

### 登录模块

![image-20210222122621788](http://jianglan.ltd/wp-content/uploads/2021/02/image-20210222122621788.png)

### 商品模块

![image-20210222122710397](http://jianglan.ltd/wp-content/uploads/2021/02/image-20210222122710397.png)

### 购物车模块

![image-20210222122808470](http://jianglan.ltd/wp-content/uploads/2021/02/image-20210222122808470.png)

### 订单模块

![image-20210222122442052](http://jianglan.ltd/wp-content/uploads/2021/02/image-20210222122442052.png)

### 支付模块

![image-20210222122530295](http://jianglan.ltd/wp-content/uploads/2021/02/image-20210222122530295.png)



[体验入口](http://47.110.82.125/#/index)

用户名与密码为：admin
## 开发工具

IntelliJ IDEA + Navicat + Sublime Text3 + Git + Chrome

## 测试工具

Postman + JUnit4

### 技术选项

| 后端技术          | 名称                | 备注 |
| ----------------- | ------------------- | ---- |
| SpringBoot        | springboot框架      |      |
| MyBatis           | SQL模板引擎         |      |
| MyBatis generator | 代码生成            |      |
| PageHelper        | MyBatis物理分页插件 |      |
| Log4J             | 日志组件            |      |
| Maven             | 项目构建管理        |      |
| GSON              | 谷歌json            |      |

| 中间件技术 | 名称        | 备注                   |
| ---------- | ----------- | ---------------------- |
| nginx      | Web服务器   |                        |
| Redis      | NoSQL数据库 |                        |
| RabbitMQ   | 消息队列    | 业务解耦、提高支付性能 |



## 实现技术点

### 1.支付宝与微信支付

**支付宝：**使用扫码支付(支付宝pc)，当面付扫码支付采用 **商家/系统服务商后台转发** 方式接入，商家先预下单到商家后台，再请求到支付宝。

**微信：**Native支付，商户系统按微信支付协议生成支付二维码，用户再用微信“扫一扫”完成支付的模式

### 2.Spring拦截器

实现了Spring的HandlerInterceptor接口，在调用当前请求前进行一个预处理，保证业务的安全与可靠性。

### 3. 全局异常统一处理

通过拦截所有异常，对异常进行相应的处理保证客户端可以收到友好的提示

基于@ControllerAdvice注解的全局异常统一处理针对于Controller层的异常进行响应。

### 异常处理类：

RuntimeExceptionHandler

```java
@ControllerAdvice
public class RuntimeExceptionHandler {

    /**
     *全局异常捕捉处理、
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)//更改状态码为403
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ResponseEnum.ERROR,e.getMessage());
    }
    @ResponseBody
   @ExceptionHandler(UserLoginException.class)
    public ResponseVo userLoginHandle() {
       return ResponseVo.error(ResponseEnum.NEED_LOGIN);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseBody
    public ResponseVo notValidException(MethodArgumentNotValidException e) {
       BindingResult bindingResult = e.getBindingResult();
       Objects.requireNonNull(bindingResult.getFieldError());
       return ResponseVo.error(ResponseEnum.PARAM_ERROR,
               bindingResult.getFieldError().getField()  + bindingResult.getFieldError().getDefaultMessage());
   }

}
```

### 4. session共享

验证用户账号密码都正确情况下，通过UUID生成唯一id作为token令牌