# Bean Validation与Hibernate Validation 后台参数验证       

1、与springboot整合添加Maven依赖
```pom
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <!-- Web 依赖 - 包含了 hibernate-validator 依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```

2、Hibernate Validator 是 Bean Validation 的参考实现 . Hibernate Validator 提供了 JSR 303 规范中所有内置 constraint 的实现，除此之外还有一些附加的 constraint。如果想了解更多有关 Hibernate Validator 的信息，请查看 http://www.hibernate.org/subprojects/validator.html

Bean Validation 中的 constraint
表 1. Bean Validation 中内置的 constraint
Constraint	详细信息
@Null	被注释的元素必须为 null
@NotNull	被注释的元素必须不为 null
@AssertTrue	被注释的元素必须为 true
@AssertFalse	被注释的元素必须为 false
@Min(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max, min)	被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction)	被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past	被注释的元素必须是一个过去的日期
@Future	被注释的元素必须是一个将来的日期
@Pattern(value)	被注释的元素必须符合指定的正则表达式
表 2. Hibernate Validator 附加的 constraint
Constraint	详细信息
@Email	被注释的元素必须是电子邮箱地址
@Length	被注释的字符串的大小必须在指定的范围内
@NotEmpty	被注释的字符串的必须非空
@Range	被注释的元素必须在合适的范围内
——摘自http://www.ibm.com/developerworks/cn/java/j-lo-jsr303/

3、No validator错误及解决方法
    javax.validation.UnexpectedTypeException: No validator could be found for type: java.lang.Integer


    使用hibernate validator出现上面的错误， 需要 注意

        @NotNull 和 @NotEmpty  和@NotBlank 区别

        @NotEmpty 用在集合类上面
        @NotBlank 用在String上面
        @NotNull    用在基本类型上

    当@NotEmpty用在Integer类型上时将会出现上面的错误，换成@NotNull问题解决。

    ——摘自http://blog.csdn.net/dracotianlong/article/details/23181729


4、使用案例

@Id
@GeneratedValue
private Long id;
@NotEmpty(message = "姓名不能为空")
@Size(min = 2, max = 8, message = "姓名长度必须大于 2 且小于 20 字")
private String name;
@NotNull(message = "年龄不能为空")
@Min(value = 0, message = "年龄大于 0")
@Max(value = 300, message = "年龄不大于 300")
private Integer age;
@NotEmpty(message = "出生时间不能为空")
private String birthday;
// 必须不为 null, 大小是 10
@NotNull
@Size(min = 10, max = 10)
private String orderId;
// 必须不为空
@NotEmpty
private String customer;
// 必须是一个电子信箱地址
@Email
private String email;
// 必须不为空
@NotEmpty
private String address;
// 必须不为 null, 必须是下面四个字符串'created', 'paid', 'shipped', 'closed'其中之一
//@Status 是一个定制化的 contraint
@NotNull
@Status
private String status;
// 必须不为 null
@NotNull
private Date createDate;
// 嵌套验证
@Valid
private Product product;


5、Controller返回json处理

@PostMapping(value = "create")
@ResponseBody
public R createUserForm(@Valid User user,BindingResult bindingResult){
    if(bindingResult.hasErrors()){
       //获取错误信息，返回json
        return R.error("请求失败，请重试！");
    }
    return R.ok("请求成功！");
}
6、返回给页面处理

    <label for="user_name" class="col-sm-2 control-label">名称:</label>
    <div class="col-xs-4">
        <!--/*@thymesVar id="name" type="java.lang.String"*/-->
        <input type="text" class="form-control" id="user_name" name="name" th:value="${user.name}" th:field="*{user.name}" />
    </div>
    <label class="col-sm-2 control-label text-danger" th:if="${#fields.hasErrors('user.name')}" th:errors="*{user.name}">姓名有误!</label>
</div>

<div class="form-group">
    <label for="user_age" class="col-sm-2 control-label">年龄:</label>
    <div class="col-xs-4">
        <input type="text" class="form-control" id="user_age" name="age" th:value="${user.age}" th:field="*{user.age}" />
    </div>
    <label class="col-sm-2 control-label text-danger" th:if="${#fields.hasErrors('user.age')}" th:errors="*{user.age}">年龄有误!</label>
</div>