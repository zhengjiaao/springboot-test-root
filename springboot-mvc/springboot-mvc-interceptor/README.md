# springboot-mvc-interceptor

**说明**

在Spring Boot中，拦截器（Interceptor）是一种用于拦截HTTP请求和响应的组件。它可以在请求到达控制器之前或响应返回给客户端之前执行预定义的操作。

注意，拦截器仅对Spring MVC的请求有效，对于静态资源等其他请求可能不会生效。如果你需要拦截所有请求，包括静态资源，你可以考虑使用过滤器（Filter）来实现。

