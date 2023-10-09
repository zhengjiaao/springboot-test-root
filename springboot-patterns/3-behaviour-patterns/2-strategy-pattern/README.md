# 2-strategy-pattern

**说明**

策略模式（Strategy Pattern）：策略模式是一种行为设计模式，它允许在运行时动态地选择算法或行为，而不是在编译时固定使用一种算法。
该模式通过定义一组算法或行为，并将其封装在各自的策略类中，使得它们可以互相替换。

使用策略模式可以将算法的实现与使用算法的客户端代码解耦，同时提供了灵活性和可扩展性。
客户端可以根据需要选择不同的策略对象，而不必关心实际的算法实现细节。

以下是策略模式的基本结构和示例代码：

* Context（上下文）：定义了客户端使用的接口，维护一个对策略对象的引用。上下文将请求委派给策略对象来执行具体的算法或行为。
* Strategy（策略）：定义了算法或行为的接口，可以是抽象类或接口。具体的策略类实现了这个接口，提供了不同的算法实现。

## 策略模式的优点、缺点和应用场景

策略模式（Strategy Pattern）具有以下优点、缺点和适用场景：

优点：

1. 策略模式实现了开闭原则，使得新增或修改算法变得简单。可以通过添加新的策略类来扩展系统，而无需修改现有的代码。
2. 策略模式将算法的实现与调用方解耦，使得调用方可以独立于具体算法进行变化。客户端代码只需关注选择合适的策略对象，而不必关心算法的实现细节。
3. 策略模式提供了更好的代码重用性。不同的策略可以共享相同的接口或抽象类，从而避免了重复编写相似的代码。

缺点：

1. 策略模式增加了系统中类的数量，可能会导致代码变得更为复杂。每个具体策略类都需要一个独立的类来实现，如果策略较多，可能会增加类的数量。
2. 客户端需要了解不同的策略类，选择合适的策略对象。这可能会增加客户端的复杂性，特别是在策略较多时。

适用场景：

1. 当一个系统中有多个相似的类，它们之间的区别仅在于行为或算法的不同，可以使用策略模式来消除这些不同之处，避免代码的重复。
2. 当一个类需要在运行时根据不同的条件选择不同的算法或行为时，可以使用策略模式。它使得算法可以独立于客户端进行变化，客户端只需选择合适的策略对象即可。
3. 当一个类中包含了大量的条件语句来选择不同的行为时，可以考虑使用策略模式来简化代码，提高可维护性。

总而言之，策略模式适用于需要在运行时动态选择不同算法或行为的情况。它可以使系统更灵活、可扩展，并提供更好的代码重用性。然而，需要权衡增加的类数量和客户端的复杂性。

## 策略模式的实例

### 使用策略模式来实现支付方式的选择

下面是一个简单的购物时使用的支付策略实现方式。

```java
// 策略接口
interface PaymentStrategy {
    void pay(double amount);
}

// 具体策略类A
class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cvv;

    public CreditCardStrategy(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via credit card.");
        // 具体的支付逻辑
    }
}

// 具体策略类B
class PayPalStrategy implements PaymentStrategy {
    private String email;
    private String password;

    public PayPalStrategy(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via PayPal.");
        // 具体的支付逻辑
    }
}

// 上下文类
class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}
```

客户端示例：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        // 使用信用卡策略进行支付
        PaymentStrategy creditCardStrategy = new CreditCardStrategy("123456789", "123");
        cart.setPaymentStrategy(creditCardStrategy);
        cart.checkout(100.0);

        // 使用PayPal策略进行支付
        PaymentStrategy payPalStrategy = new PayPalStrategy("example@example.com", "password");
        cart.setPaymentStrategy(payPalStrategy);
        cart.checkout(200.0);
    }
}
```

输出结果：

```text
Paid 100.0 via credit card.
Paid 200.0 via PayPal.
```

在上述示例中，我们有一个上下文类`ShoppingCart`，它维护了一个对策略接口`PaymentStrategy`的引用。`ShoppingCart`的`checkout()`
方法将支付行为委托给具体的策略对象。

我们定义了两个具体的策略类：`CreditCardStrategy`和`PayPalStrategy`，它们实现了`PaymentStrategy`
接口并提供了不同的支付逻辑。客户端可以根据需要选择不同的策略，并将其设置到`ShoppingCart`对象中进行支付。

通过策略模式，我们可以轻松地添加新的支付策略，而无需修改`ShoppingCart`类的代码。这提供了灵活性和可扩展性，使得系统能够适应不同的支付需求。

### 使用策略模式来实现角色的移动行为

> 假设我们正在开发一个游戏，其中有不同类型的角色，每个角色都有不同的移动方式。我们可以使用策略模式来实现角色的移动行为。

```java
// 策略接口
interface MoveStrategy {
    void move();
}

// 具体策略类A
class WalkStrategy implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Walking...");
    }
}

// 具体策略类B
class RunStrategy implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Running...");
    }
}

// 具体策略类C
class FlyStrategy implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Flying...");
    }
}

// 上下文类
class Character {
    private MoveStrategy moveStrategy;

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public void move() {
        moveStrategy.move();
    }
}
```

客户端示例：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        Character character = new Character();

        // 角色使用步行策略
        MoveStrategy walkStrategy = new WalkStrategy();
        character.setMoveStrategy(walkStrategy);
        character.move();

        // 角色使用奔跑策略
        MoveStrategy runStrategy = new RunStrategy();
        character.setMoveStrategy(runStrategy);
        character.move();

        // 角色使用飞行策略
        MoveStrategy flyStrategy = new FlyStrategy();
        character.setMoveStrategy(flyStrategy);
        character.move();
    }
}
```

输出结果：

```text
Walking...
Running...
Flying...
```

在上述示例中，我们有一个上下文类`Character`，它具有一个移动策略`MoveStrategy`的引用。`Character的move()`方法将移动行为委托给具体的策略对象。

我们定义了三个具体的策略类：`WalkStrategy`、`RunStrategy`和`FlyStrategy`，它们实现了`MoveStrategy`
接口并提供了不同的移动行为。通过在`Character`对象中设置不同的移动策略，我们可以改变角色的移动方式。

在示例代码中，角色首先使用步行策略，然后切换到奔跑策略，最后切换到飞行策略。每次角色移动时，相应的策略对象被调用，执行具体的移动行为。

通过策略模式，我们可以灵活地更改角色的移动行为，而无需更改`Character`类的代码。这种解耦的设计使得我们能够轻松地添加新的移动策略，并根据需要选择不同的策略来适应不同的情况。