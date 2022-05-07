# Test-Assured

**我们的目标:以更加优雅的方式设计自动化接口与自动化场景**

## 技术选型

- Java 1.8
- RestAssured
- Maven
- Hutool

## 使用文档

### 0.准备阶段

- 添加Maven依赖

```xml

<dependency>
  <groupId>io.github.diduweiwu</groupId>
  <artifactId>test-assured</artifactId>
  <version>1.1.0-REALEASE</version>
</dependency>
```

### 1.开发阶段

**待依赖拉取完成后,即可开启我们的接口开发之旅**
针对接口自动化调用,目前有两种场景

- 针对单接口用例,组合不同的入参并对接口响应做断言
- 针对多接口用例,即场景化用例,组合和串联不同的接口调用,针对每个阶段接口响应做断言

#### 1.1 单接口用例

**注: 单接口用例是场景化用例的基础**

---

#### 使用

##### 1.1.1 新建实体类

**一个实体类对应一个接口,此处假设这是个查询用户信息的接口**

```java
class UserInfo {

}
```

##### 1.1.2 添加注解

```java

@Post
@Host("http://0.0.0.0:9001")
@ContentType("application/json")
class UserInfo implements Api{

}
```

**解释**

- @Post 标注请求接口为POST类型,目前支持的动词有:
  - @Get
  - @Post
  - @Put
  - @Delete
  - @Options
- @Host 标注请求地址,注解value值为地址
- @ContentType 标注配置header头信息 ContentType
- 实现了Api接口,后续可直接在场景构造时候传入对象,而不需要手动调用RequestUtil方法

##### 1.1.3 接口调用

当定义好接口对象后,就可以进行调用了

**初始化接口对象实例,作为入参使用RequestUtil的一系列send方法进行调用:**

- send(new UserInfo()) 单接口调用
- send(new UserInfo(), setUps, postChecks)
  - setUps:前置处理列表,是实现了ISetUp接口的对象集合,支持在接口调用前自定义请求信息

    ```text
      // 建议使用lambda表达式传入,req是rest-assured的原生Request对象
         List<ISetUp> setUps = ListUtil.of(req -> req.header("FITURE", "Yes"));
    ```
  - postChecks:后置检查列表,是实现了IPostCheck接口的对象集合,支持在接口调用完成对响应进行断言和校验逻辑

    ```text
      // 建议使用lambda表达式传入,res是rest-assured的原生response对象
         List<IPostCheck> postChecks=ListUtil.of(
             res->Assert.notContain(res.body().asString(),"name","返回值不能包含关键字")
         );
    ``` 
- 还有其他一些方法参考RequestUtil静态类,详细可参考RequestUtil静态类,常用方法都添加了注释说明

#### 1.2 多接口调用

```text
当单接口调试通过后,某些情况下需要将一些接口组合成场景用例,比如某个业务域对象的CRUD流程
```

#### 使用

##### 1.2.1 初始化场景

**使用RequestScene静态函数相关方法构建场景**

```java
//将第一个接口调用的response返回值作为入参来初始化场景
RequestScene.of(RequestUtil.send(new UserInfo()));
```

或者,如果UserInfo对象实现了Api接口,则可以直接传入对象

```java
//将第一个接口调用的response返回值作为入参来初始化场景
RequestScene.of(new UserInfo());
```

##### 1.2.2 链式串联接口调用

**理论上大部分接口都是链式调用,故暂未考虑并行接口调用的场景**

**调用then()方法并传入1+个接口调用作为入参**
**then()方法也支持Function<Response,Response>类型的lambda表达式作为入参,此时会将上次接口调用的response作为入参传入,如此可从上个接口取值进行使用**
**then()方法也支持传入实现了Api接口的请求对象传入**
```java
RequestScene.of(RequestUtil.send(new User()))
        .then(RequestUtil.send(new User()))
        .then(
        RequestUtil.send(new User()),
        RequestUtil.send(new User())
        )
        .then(new User())
        .then(r->RequestUtil.send(new User()));
```

##### 1.2.3 触发场景接口调用

**链式接口调用串联完成后,调用.complete()方法即可触发整个场景接口的调用**
**complete()方法返回的是最后一个接口的响应**

 ```java
RequestScene.of(RequestUtil.send(new User()))
        .then(RequestUtil.send(new User()))
        .then(
        RequestUtil.send(new User()),
        RequestUtil.send(new User())
        ).then(r->RequestUtil.send(new User()))
        .complete();
```

### 2.扩展阶段

**作为一个框架,支持OPEN-CLOSE原则是最基本的,接下来是扩展自定义注解的方法**

#### 2.1 自定义注解

**新建一个功能注解,定义好该注解的相关类型,然后对其使用IAnnotation注解,仅标注了该注解才会被识别**

```java

@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface My {
    String value();
}
```

**如上所示,由于请求信息配置大部分是key-value或者单独value的形式,所以按需配置key或者value即可**
**这里定义了一个My注解,该注解有value一个字段并且必填**

#### 2.2 自定义处理器

**新建一个功能处理类,该类需要实现IProcessor接口,并重写execute方法**

```java
public class MyProcessor implements IProcessor {
    @Override
    public void execute(Object api, AnnotatedElement element, Class<? extends Annotation> clazzAnnotation, RequestSpecification request) {
        this.executeSingValue(api, element, clazzAnnotation, value -> request.cookie("My", value));
    }
}
```

**如上所示,我们需要在execute里面实现自定义的请求处理逻辑**
**由于自定义注解My只有一个value参数,故调用上层接口的executeSingValue方法并传入对应注解和逻辑处理代码**
**executeSingValue方法需要注意第四个参数为value获取后的lambda表达式处理逻辑**
**此处我们将My注解上配置的value设置为cookie里面的键为My的值**

#### 2.3 关联注解与注解处理器

**最终需要在My注解中设置processor参数为MyProcessor的类型**

```java

@Inherited
@IAnnotation
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface My {
    String value();

    Class<? extends IProcessor> processor() default MyProcessor.class;
}
```

#### 2.4 使用注解

**此时可在定义的接口对象上使用自定义注解**

```java
@My(value = "abandon")
private String search;
```

**执行接口调用之前在请求中设置一个cookie,键值对为 My->abandon**

**至此自定义注解扩充流程完成**

## 附

**可用注解,注解可以添加在属性上,也可以添加在方法上,通过返回值来动态赋值**

- 请求动词
  - @Delete
  - @Get
  - @Options
  - @Post
  - @Put
- 请求参数
  - @Body
  - @BodyFile
  - @BodyFilePath
  - @FormParam
  - @Query

- 框架基础
  - @IAnnotation

- 请求头信息
  - @Header
  - @Headers

- Cookie信息
  - @Cookie
  - @Cookies

- 其他请求配置
  - @ContentType
  - @Host

- BearToken配置
  - @BearToken

## 写在最后

**由于这是比较初始的版本,肯定有很多考虑步骤和可优化的地方,欢迎大家使用、反馈和参与扩充功能:)**

