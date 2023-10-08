# 6-facade-pattern

**说明**

外观模式（Facade Pattern）：外观模式是一种结构型设计模式，它提供了一个统一的接口，用于访问子系统中的一组接口。外观模式隐藏了子系统的复杂性，为客户端提供了一个简单的接口，从而简化了客户端与子系统之间的交互。

外观模式的结构包含以下几个角色：

* 外观（Facade）：提供了一个简单的接口，用于访问子系统中的一组接口。它知道哪些子系统类负责处理特定的请求，将客户端的请求委派给适当的子系统对象。
* 子系统类（Subsystem Classes）：实现子系统的功能。客户端通过外观模式与这些类进行交互。

工作原理:

外观模式通过引入一个外观类作为客户端与子系统之间的中间层，实现了客户端与子系统之间的解耦。客户端只需与外观类进行交互，而不需要直接与子系统类打交道。外观类封装了子系统的复杂性，根据客户端的请求将其委派给适当的子系统类进行处理。

总之，外观模式通过引入一个外观类，简化了客户端与子系统之间的交互，提供了一个简单的接口给客户端使用。它隐藏了子系统的复杂性，提高了代码的可维护性和可扩展性。

## 外观模式的优缺点及应用场景

外观模式的一些优点：

* 简化接口：外观模式提供了一个简化的接口，隐藏了子系统的复杂性，使得客户端更加容易使用子系统。
* 解耦：外观模式将客户端与子系统之间的依赖解耦，使得它们可以独立演化。当子系统的实现发生变化时，客户端的代码不需要变化。
* 提高可扩展性：由于客户端与子系统之间通过外观类进行通信，因此可以在不修改客户端代码的情况下添加新的子系统类。

也存在一些缺点：

* 过多的依赖：外观模式可能会导致客户端过度依赖外观类。如果外观类发生变化，客户端可能需要进行相应的修改。这种依赖关系可能增加代码的脆弱性，使得系统更难以维护和扩展。
* 违背开闭原则：当需要添加新的子系统或修改现有子系统时，可能需要修改外观类。这可能违反了开闭原则，因为对外观类的修改可能会影响客户端代码。
* 增加了系统的复杂性：虽然外观模式可以简化客户端与子系统的交互，但在某些情况下，它也可能增加系统的复杂性。引入外观类可能会引入新的抽象层次，增加了代码的理解和维护的难度。
* 不符合单一职责原则：外观模式的外观类通常需要了解和协调子系统中的多个类。这可能导致外观类承担过多的职责，违反了单一职责原则。
* 隐藏了子系统的复杂性：尽管隐藏子系统的复杂性是外观模式的优点之一，但有时候也可能成为缺点。通过外观模式，客户端无法直接访问子系统的所有功能和细节，这可能限制了客户端的灵活性。

外观模式适用于以下情况：

* 当一个复杂子系统的接口很多，且它们之间存在依赖关系时，可以使用外观模式简化客户端的调用。
* 当需要将子系统的实现从客户端代码中解耦出来，使得客户端代码更加简洁、可维护和可扩展时，可以使用外观模式。
* 当希望封装子系统，提供一个统一的接口给客户端使用时，可以使用外观模式。

## 外观模式的实例

使用外观模式来隐藏复杂的子系统，并为客户端提供简单的接口。

### 电子商务平台-客户下单

下面是一个简单的示例，展示了如何使用外观模式来隐藏复杂的子系统，并为客户端提供简单的接口。

> 假设有一个电子商务平台，客户下单时需要经过一系列的步骤，包括验证库存、生成订单、进行支付等。我们可以使用外观模式来隐藏这些步骤的复杂性。

首先，定义一个外观类 OrderFacade，它封装了下单的过程：

```java
public class OrderFacade {
    private InventoryService inventoryService;
    private OrderService orderService;
    private PaymentService paymentService;

    public OrderFacade() {
        inventoryService = new InventoryService();
        orderService = new OrderService();
        paymentService = new PaymentService();
    }

    public void placeOrder(String productId, int quantity, String paymentMethod) {
        if (inventoryService.checkStock(productId, quantity)) {
            Order order = orderService.createOrder(productId, quantity);
            paymentService.processPayment(order, paymentMethod);
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Insufficient stock.");
        }
    }
}
```

然后，定义子系统中的各个服务类：

```java
public class InventoryService {
    public boolean checkStock(String productId, int quantity) {
        // 检查库存
        // 返回库存是否足够
    }
}

public class OrderService {
    public Order createOrder(String productId, int quantity) {
        // 创建订单
        // 返回订单对象
    }
}

public class PaymentService {
    public void processPayment(Order order, String paymentMethod) {
        // 处理支付
    }
}
```

最后，客户端可以通过调用外观类的方法来下单，而不需要直接与子系统中的服务类交互：

```java
public class Client {
    public static void main(String[] args) {
        OrderFacade orderFacade = new OrderFacade();
        orderFacade.placeOrder("12345", 2, "credit_card");
    }
}
```

在上述示例中，客户端只需要与外观类 OrderFacade 进行交互，而不需要知道下单的具体步骤和子系统的实现细节。外观类封装了库存检查、订单创建和支付处理等复杂操作，使得客户端代码更加简洁和易于理解。

通过外观模式，我们可以隐藏子系统的复杂性，提供一个简单的接口给客户端使用，同时也提高了代码的可维护性和可扩展性。

### 家庭影院系统-子系统

下面是一个更复杂的外观模式示例，展示了如何使用外观模式来简化一个多层次系统的操作。

> 假设我们有一个家庭影院系统，包括投影仪、音响、灯光和屏幕等多个子系统。我们可以使用外观模式来提供一个简单的接口，以便客户端可以方便地控制整个家庭影院系统。

首先，定义一个外观类 HomeTheaterFacade，它封装了家庭影院系统的操作：

```java
public class HomeTheaterFacade {
    private Projector projector;
    private Amplifier amplifier;
    private Lights lights;
    private Screen screen;

    public HomeTheaterFacade() {
        projector = new Projector();
        amplifier = new Amplifier();
        lights = new Lights();
        screen = new Screen();
    }

    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        lights.dimLights();
        screen.down();
        projector.on();
        amplifier.on();
        amplifier.setVolume(10);
        projector.setInput(movie);
        projector.setAspectRatio("16:9");
        projector.start();
    }

    public void endMovie() {
        System.out.println("Shutting down the home theater...");
        projector.stop();
        projector.off();
        amplifier.off();
        screen.up();
        lights.on();
    }
}
```

然后，定义子系统中的各个类：

```java
public class Projector {
    public void on() {
        // 打开投影仪
    }

    public void off() {
        // 关闭投影仪
    }

    public void setInput(String input) {
        // 设置投影仪输入源
    }

    public void setAspectRatio(String ratio) {
        // 设置投影仪屏幕比例
    }

    public void start() {
        // 开始投影
    }

    public void stop() {
        // 停止投影
    }
}

public class Amplifier {
    public void on() {
        // 打开音响
    }

    public void off() {
        // 关闭音响
    }

    public void setVolume(int volume) {
        // 设置音量
    }
}

public class Lights {
    public void dimLights() {
        // 调暗灯光
    }

    public void on() {
        // 打开灯光
    }
}

public class Screen {
    public void down() {
        // 放下屏幕
    }

    public void up() {
        // 升起屏幕
    }
}
```

最后，客户端可以通过调用外观类的方法来控制家庭影院系统：

```java
public class Client {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade();

        // 观看电影
        homeTheater.watchMovie("Avengers: Endgame");

        // 结束观影
        homeTheater.endMovie();
    }
}
```

在上述示例中，客户端只需要通过调用外观类 HomeTheaterFacade
提供的方法来控制家庭影院系统，而不需要直接与各个子系统类交互。外观类封装了复杂的操作和子系统之间的协调，使得客户端代码更加简洁和易于理解。

通过外观模式，我们可以隐藏子系统的复杂性，提供一个简单的接口给客户端使用，同时也提高了代码的可维护性和可扩展性。客户端可以方便地操作整个家庭影院系统，而无需了解其内部的具体实现细节。


### 电子邮件发送系统

下面是一个更简单的外观模式示例，展示了如何使用外观模式来隐藏复杂的子系统，并为客户端提供简单的接口。

> 假设我们有一个电子邮件发送系统，包括验证邮箱、设置邮件内容和发送邮件等多个子系统。我们可以使用外观模式来提供一个简单的接口，以便客户端可以方便地发送电子邮件。

首先，定义一个外观类 EmailFacade，它封装了邮件发送的过程：
```java
public class EmailFacade {
    private EmailValidator emailValidator;
    private EmailContentBuilder contentBuilder;
    private EmailSender emailSender;

    public EmailFacade() {
        emailValidator = new EmailValidator();
        contentBuilder = new EmailContentBuilder();
        emailSender = new EmailSender();
    }

    public void sendEmail(String recipient, String subject, String message) {
        if (emailValidator.validateEmail(recipient)) {
            String emailContent = contentBuilder.buildEmailContent(subject, message);
            emailSender.sendEmail(recipient, emailContent);
            System.out.println("Email sent successfully.");
        } else {
            System.out.println("Invalid email address.");
        }
    }
}
```
然后，定义子系统中的各个服务类：
```java
public class EmailValidator {
    public boolean validateEmail(String email) {
        // 验证邮箱地址的有效性
        // 返回验证结果
    }
}

public class EmailContentBuilder {
    public String buildEmailContent(String subject, String message) {
        // 构建邮件内容
        // 返回邮件内容
    }
}

public class EmailSender {
    public void sendEmail(String recipient, String content) {
        // 发送邮件
    }
}
```
最后，客户端可以通过调用外观类的方法来发送邮件，而不需要直接与子系统中的服务类交互：
```java
public class Client {
    public static void main(String[] args) {
        EmailFacade emailFacade = new EmailFacade();
        emailFacade.sendEmail("recipient@example.com", "Hello", "This is a test email.");
    }
}
```
在上述示例中，客户端只需要与外观类 EmailFacade 进行交互，而不需要知道发送邮件的具体步骤和子系统的实现细节。外观类封装了邮箱地址验证、邮件内容构建和邮件发送等复杂操作，使得客户端代码更加简洁和易于理解。

通过外观模式，我们可以隐藏子系统的复杂性，提供一个简单的接口给客户端使用，同时也提高了代码的可维护性和可扩展性。客户端可以方便地使用外观类发送电子邮件，而无需了解其内部的具体实现细节。
