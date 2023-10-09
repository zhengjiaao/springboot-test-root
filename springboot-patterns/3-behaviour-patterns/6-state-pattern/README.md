# 6-state-pattern

**说明**

状态模式（State Pattern）：状态模式是一种行为设计模式，它允许对象在内部状态发生改变时改变其行为，看起来就像是对象的类发生了改变。

状态模式的核心思想是将对象的状态封装成独立的状态类，并将状态的行为抽象为共同的接口。
通过将对象的行为委托给当前状态对象来实现状态的切换和行为的变化。

## 状态模式的优点、缺点和应用场景

状态模式具有以下优点：

1. 封装了状态：状态模式将每个状态都封装成一个独立的类，使得状态的实现与上下文对象解耦，提高了代码的可维护性和可扩展性。
2. 开闭原则：通过添加新的状态类，可以在不修改上下文类的情况下引入新的状态，符合开闭原则，使系统更加灵活。
3. 简化了条件语句：状态模式避免了使用大量的条件语句来判断对象的状态，使代码更加清晰、简洁。
4. 提高了可读性：通过将不同状态的行为进行封装和分类，状态模式使代码更具可读性，易于理解和维护。
5. 促进了状态转换的一致性：状态模式确保状态转换的一致性，即在切换状态时，对象的行为表现一致，不会出现不一致或错误的状态转换。

状态模式也有一些缺点：

1. 增加了类的数量：引入状态模式会增加系统中类的数量，每个状态需要一个对应的类，如果状态较多，将会增加类的数量和复杂度。
2. 状态转换的逻辑复杂性：状态模式将状态的转换逻辑分散到各个状态类中，可能会导致状态转换的逻辑变得复杂。

适用场景：

1. 对象的行为随着内部状态的改变而改变，且有多个状态存在，并且状态之间的转换比较复杂。
2. 有一些状态需要在运行时动态切换，而不是在编译时确定。
3. 当一个类的行为取决于它的状态，并且状态经常发生改变时，可以考虑使用状态模式。
4. 当需要消除大量的条件语句来判断对象的状态时，可以使用状态模式来提高代码的可读性和可维护性。

## 状态模式的实例

### 简单的例子

以下是一个简单的例子，展示了状态模式的应用：

```java
// State 接口
interface State {
    void handle();
}

// Concrete State - 待机状态
class StandbyState implements State {
    @Override
    public void handle() {
        System.out.println("当前处于待机状态");
    }
}

// Concrete State - 工作状态
class WorkState implements State {
    @Override
    public void handle() {
        System.out.println("当前处于工作状态");
    }
}

// Context - 上下文类
class Context {
    private State currentState;

    public Context() {
        currentState = new StandbyState(); // 初始状态设置为待机状态
    }

    public void setState(State state) {
        currentState = state;
    }

    public void request() {
        currentState.handle();
    }
}
```

客户端示例：

```java

// 示例代码
public class Client {
    public static void main(String[] args) {
        Context context = new Context();

        // 初始状态：待机
        context.request();

        // 切换状态：工作
        State workState = new WorkState();
        context.setState(workState);
        context.request();
    }
}
```

输出结果：

```text
当前处于待机状态
当前处于工作状态
```

在上述示例中，`State`接口定义了状态的共同行为，其中的`handle()`方法表示该状态的处理逻辑。

`StandbyState`和`WorkState`是具体的状态类，实现了`State`接口，并根据不同的状态提供了相应的行为实现。

`Context`类是上下文类，它维持了一个对当前状态对象的引用，并在需要时调用当前状态对象的方法。

在示例代码的主函数中，首先创建了一个上下文对象`Context`，初始状态为待机状态。然后通过`request()`方法触发当前状态对象的行为。

接下来，通过调用`setState()`方法将当前状态切换为工作状态，并再次调用`request()`方法触发工作状态的行为。

通过状态模式，对象可以根据内部状态的改变来改变其行为。这种状态的切换和行为的变化是相对独立的，使得对象的状态转换更加灵活和可扩展。状态模式也有助于减少大型条件语句的使用，提高代码的可读性和可维护性。

### 订单状态

以下是一个更复杂的例子，展示了状态模式在订单处理系统中的应用：

```java
// OrderState 接口
interface OrderState {
    void cancelOrder();

    void verifyPayment();

    void shipOrder();
}

// Concrete State - 待支付状态
class PendingPaymentState implements OrderState {
    private Order order;

    public PendingPaymentState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 处理取消订单的逻辑
        System.out.println("订单已取消");
        order.setState(new CancelledState(order));
    }

    @Override
    public void verifyPayment() {
        // 处理支付验证的逻辑
        System.out.println("订单支付已验证");
        order.setState(new PaymentVerifiedState(order));
    }

    @Override
    public void shipOrder() {
        // 待支付状态下不能发货
        System.out.println("订单尚未支付，无法发货");
    }
}

// Concrete State - 支付验证通过状态
class PaymentVerifiedState implements OrderState {
    private Order order;

    public PaymentVerifiedState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 处理取消订单的逻辑
        System.out.println("订单已取消");
        order.setState(new CancelledState(order));
    }

    @Override
    public void verifyPayment() {
        // 支付验证通过状态下无需重新验证支付
        System.out.println("订单支付已验证");
    }

    @Override
    public void shipOrder() {
        // 处理发货的逻辑
        System.out.println("订单已发货");
        order.setState(new ShippedState(order));
    }
}

// Concrete State - 已发货状态
class ShippedState implements OrderState {
    private Order order;

    public ShippedState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 已发货状态下不能取消订单
        System.out.println("订单已发货，无法取消");
    }

    @Override
    public void verifyPayment() {
        // 已发货状态下无需重新验证支付
        System.out.println("订单支付已验证");
    }

    @Override
    public void shipOrder() {
        // 已发货状态下无需重新发货
        System.out.println("订单已发货");
    }
}

// Concrete State - 已取消状态
class CancelledState implements OrderState {
    private Order order;

    public CancelledState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 已取消状态下无需重复取消订单
        System.out.println("订单已取消");
    }

    @Override
    public void verifyPayment() {
        // 已取消状态下无需验证支付
        System.out.println("订单已取消");
    }

    @Override
    public void shipOrder() {
        // 已取消状态下无法发货
        System.out.println("订单已取消");
    }
}

// Context 类 - 订单
class Order {
    private OrderState currentState;

    public Order() {
        this.currentState = new PendingPaymentState(this);
    }

    public void setState(OrderState state) {
        this.currentState = state;
    }

    public void cancelOrder() {
        currentState.cancelOrder();
    }

    public void verifyPayment() {
        currentState.verifyPayment();
    }

    public void shipOrder() {
        currentState.shipOrder();
    }
}
```

客户端示例：

```java
// 使用示例
public class OrderStatePatternExample {
    public static void main(String[] args) {
        Order order = new Order();

        order.verifyPayment(); // 输出：订单支付已验证
        order.cancelOrder(); // 输出：订单已取消
        order.verifyPayment(); // 输出：订单已取消
        order.shipOrder(); // 输出：订单已取消

        // 切换订单状态为订单已发货状态
        order.setState(new ShippedState(order));

        order.verifyPayment(); // 输出：订单支付已验证
        order.shipOrder(); // 输出：订单已发货
    }
}
```

输出结果：

```text
订单支付已验证
订单已取消
订单已取消
订单已取消
订单支付已验证
订单已发货
```

在这个示例中，`Order` 是订单类，负责维护当前状态并委托给具体的状态类处理相应的操作。`OrderState`
是状态接口，定义了订单可能的操作。`PendingPaymentState`、`PaymentVerifiedState`、`ShippedState` 和 `CancelledState`
是具体的状态类，分别表示待支付状态、支付验证通过状态、已发货状态和已取消状态。

在订单处理系统中，订单可以处于不同的状态，例如待支付、支付验证通过、已发货和已取消。不同状态下，订单可以执行不同的操作。例如，在待支付状态下，可以取消订单或验证支付；在支付验证通过状态下，可以取消订单或发货；在已发货状态下，不能取消订单但可以验证支付。












