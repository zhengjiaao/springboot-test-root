# 7-chain-of-responsibility-pattern

**说明**

职责链模式（Chain of Responsibility Pattern）：职责链模式是一种行为设计模式，用于将请求的发送者和接收者解耦，并将请求沿着一个链传递，直到有一个接收者能够处理该请求。

在职责链模式中，请求被封装为一个对象，并沿着一个预先定义的处理链进行传递。
每个处理者（或接收者）都有机会处理请求，如果处理者能够处理请求，则处理请求并结束链。
如果处理者无法处理请求，则将请求传递给链中的下一个处理者，直到找到能够处理请求的处理者或达到链的末尾。

以下是职责链模式的一些关键角色：

1. 请求（Request）：封装了待处理的请求信息。
2. 处理者（Handler）：定义了处理请求的接口，并持有下一个处理者的引用。
3. 具体处理者（Concrete Handler）：实现了处理请求的方法，并决定是否将请求传递给下一个处理者。

## 职责链模式优点、缺点和应用场景

职责链模式具有以下优点：

1. 解耦发送者和接收者：职责链模式将请求的发送者和接收者解耦，发送者不需要知道请求最终由哪个接收者处理，而接收者也不需要知道请求的发送者是谁。这增强了系统的灵活性和可扩展性。
2. 灵活性和可扩展性：职责链模式通过添加、移除或重新排列处理者，可以灵活地调整处理链，以适应不同的业务需求。可以动态地修改处理链，而无需修改客户端代码。
3. 可以动态地决定请求的处理方式：职责链模式允许在运行时动态地决定请求由哪个处理者处理。处理者可以根据自身的逻辑和条件判断是否处理请求，从而实现灵活的请求处理方式。

职责链模式的一些缺点包括：

1. 请求的处理不一定保证被处理：由于请求从链的头部开始传递，直到找到能够处理请求的处理者或达到链的末尾，因此不能保证请求一定会被处理。如果没有合适的处理者，请求可能会被忽略。
2. 可能导致性能问题：由于请求需要沿着整个链传递，如果链比较长或者链中的处理逻辑比较复杂，可能会导致性能问题。需要合理设计和控制链的长度和处理逻辑，以避免性能瓶颈。

职责链模式适用于以下场景：

1. 有多个对象可以处理请求，但具体处理者在运行时才能确定。
2. 希望动态地指定处理请求的对象集合。
3. 可以将处理者的顺序灵活地排列组合。
4. 需要在不同处理者之间进行解耦，以避免请求发送者和接收者之间的直接耦合关系。
5. 需要对一组对象进行统一的操作，但不确定是哪个对象将执行操作。

常见的应用场景包括审批流程、日志记录、事件处理等。
例如，一个审批流程中的请求可以依次传递给不同的审批者进行处理，直到最终得到审批结果。

总结起来，职责链模式通过解耦发送者和接收者、灵活调整处理链和动态决定请求处理方式，提供了一种灵活、可扩展和可维护的方式来处理复杂的请求处理逻辑。
然而，需要注意处理者的选择和链的设计，以避免请求被忽略或导致性能问题。

## 职责链模式的实例

### 使用职责链模式实现日志记录系统

假设我们有一个日志记录系统，其中有三个处理者：控制台处理者、文件处理者和数据库处理者。
控制台处理者负责将日志打印到控制台，文件处理者负责将日志写入文件，数据库处理者负责将日志存储到数据库。

首先，我们定义一个抽象处理者类 Handler，其中包含一个处理请求的方法 handleRequest() 和一个设置下一个处理者的方法
setNextHandler()。

```java
public abstract class Handler {
    private Handler nextHandler;

    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }

    public void handleRequest(String log) {
        processLog(log);
        if (nextHandler != null) {
            nextHandler.handleRequest(log);
        }
    }

    protected abstract void processLog(String log);
}
```

接下来，我们创建具体的处理者类 ConsoleHandler、FileHandler 和 DatabaseHandler，它们继承自抽象处理者类，并实现自己的处理逻辑。

```java
public class ConsoleHandler extends Handler {
    @Override
    protected void processLog(String log) {
        System.out.println("Log message printed to console: " + log);
    }
}

public class FileHandler extends Handler {
    @Override
    protected void processLog(String log) {
        System.out.println("Log message written to file: " + log);
    }
}

public class DatabaseHandler extends Handler {
    @Override
    protected void processLog(String log) {
        System.out.println("Log message saved to database: " + log);
    }
}
```

现在，我们可以创建一个职责链并设置处理者的顺序，并通过调用第一个处理者的 handleRequest() 方法来处理请求。
客户端示例：

```java
public class Main {
    public static void main(String[] args) {
        // 创建处理者对象
        Handler consoleHandler = new ConsoleHandler();
        Handler fileHandler = new FileHandler();
        Handler databaseHandler = new DatabaseHandler();

        // 设置处理者的顺序
        consoleHandler.setNextHandler(fileHandler);
        fileHandler.setNextHandler(databaseHandler);

        // 处理日志请求
        consoleHandler.handleRequest("This is a log message.");
    }
}
```

输出结果：

```text
Log message printed to console: This is a log message.
Log message written to file: This is a log message.
Log message saved to database: This is a log message.
```

在上述示例中，我们创建了一个职责链，其中控制台处理者负责将日志打印到控制台，文件处理者负责将日志写入文件，数据库处理者负责将日志存储到数据库。
日志请求从控制台处理者开始，依次传递给下一个处理者，直到到达链的末尾。

### 银行贷款申请的审批流程

以下是一个更复杂的示例，展示如何使用职责链模式来处理一个银行贷款申请的审批流程。

> 假设银行的审批流程包括三个处理者：初级审批员、中级审批员和高级审批员。贷款申请必须经过这三个处理者的审批才能最终决定是否批准。

首先，我们定义一个抽象处理者类 LoanApprover，其中包含一个处理请求的方法 approveLoan() 和一个设置下一个处理者的方法
setNextApprover()。

```java
public abstract class LoanApprover {
    private LoanApprover nextApprover;

    public void setNextApprover(LoanApprover approver) {
        this.nextApprover = approver;
    }

    public void approveLoan(double amount) {
        if (canApproveLoan(amount)) {
            doApproveLoan(amount);
        } else if (nextApprover != null) {
            nextApprover.approveLoan(amount);
        } else {
            System.out.println("Loan cannot be approved.");
        }
    }

    protected abstract boolean canApproveLoan(double amount);

    protected abstract void doApproveLoan(double amount);
}
```

接下来，我们创建具体的处理者类 JuniorLoanApprover、MidLevelLoanApprover 和 SeniorLoanApprover，它们继承自抽象处理者类，并实现自己的处理逻辑。

```java
public class JuniorLoanApprover extends LoanApprover {
    private static final double MAX_JUNIOR_APPROVAL_AMOUNT = 10000.0;

    @Override
    protected boolean canApproveLoan(double amount) {
        return amount <= MAX_JUNIOR_APPROVAL_AMOUNT;
    }

    @Override
    protected void doApproveLoan(double amount) {
        System.out.println("Loan approved by Junior Loan Approver. Amount: $" + amount);
    }
}

public class MidLevelLoanApprover extends LoanApprover {
    private static final double MAX_MIDLEVEL_APPROVAL_AMOUNT = 50000.0;

    @Override
    protected boolean canApproveLoan(double amount) {
        return amount <= MAX_MIDLEVEL_APPROVAL_AMOUNT;
    }

    @Override
    protected void doApproveLoan(double amount) {
        System.out.println("Loan approved by Mid-level Loan Approver. Amount: $" + amount);
    }
}

public class SeniorLoanApprover extends LoanApprover {
    private static final double MAX_SENIOR_APPROVAL_AMOUNT = 100000.0;

    @Override
    protected boolean canApproveLoan(double amount) {
        return amount <= MAX_SENIOR_APPROVAL_AMOUNT;
    }

    @Override
    protected void doApproveLoan(double amount) {
        System.out.println("Loan approved by Senior Loan Approver. Amount: $" + amount);
    }
}
```

现在，我们可以创建一个审批流程的职责链，并设置处理者的顺序，并通过调用第一个处理者的 approveLoan() 方法来处理贷款申请。

```java
public class Main {
    public static void main(String[] args) {
        // 创建处理者对象
        LoanApprover juniorApprover = new JuniorLoanApprover();
        LoanApprover midLevelApprover = new MidLevelLoanApprover();
        LoanApprover seniorApprover = new SeniorLoanApprover();

        // 设置处理者的顺序
        juniorApprover.setNextApprover(midLevelApprover);
        midLevelApprover.setNextApprover(seniorApprover);

        // 处理贷款申请
        double loanAmount = 75000.0; // 贷款金额
        juniorApprover.approveLoan(loanAmount);
    }
}
```

输出结果：

```text
Loan approved by Senior Loan Approver. Amount: $75000.0
```

在上述示例中，我们创建了一个职责链，其中初级审批员负责批准最小金额的贷款申请，中级审批员负责批准中等金额的贷款申请，高级审批员负责批准最大金额的贷款申请。
贷款申请从初级审批员开始，依次传递给下一个处理者，直到最终批准或拒绝。

### 购买请求的流程

以下是一个简单的示例，展示了如何使用职责链模式来处理一个购买请求的流程。

> 假设我们有三个处理者：优惠券处理者、折扣处理者和包邮处理者。购买请求必须经过这三个处理者的处理才能最终完成购买。

首先，我们定义一个抽象处理者类 PurchaseHandler，其中包含一个处理请求的方法 handlePurchase() 和一个设置下一个处理者的方法
setNextHandler()。

```java
public abstract class PurchaseHandler {
    private PurchaseHandler nextHandler;

    public void setNextHandler(PurchaseHandler handler) {
        this.nextHandler = handler;
    }

    public void handlePurchase(PurchaseRequest request) {
        if (canHandlePurchase(request)) {
            processPurchase(request);
        } else if (nextHandler != null) {
            nextHandler.handlePurchase(request);
        } else {
            System.out.println("Purchase cannot be processed.");
        }
    }

    protected abstract boolean canHandlePurchase(PurchaseRequest request);

    protected abstract void processPurchase(PurchaseRequest request);
}
```

接下来，我们创建具体的处理者类 CouponHandler、DiscountHandler 和 FreeShippingHandler，它们继承自抽象处理者类，并实现自己的处理逻辑。

```java
public class CouponHandler extends PurchaseHandler {
    @Override
    protected boolean canHandlePurchase(PurchaseRequest request) {
        return request.hasCoupon();
    }

    @Override
    protected void processPurchase(PurchaseRequest request) {
        System.out.println("Applying coupon to purchase.");
    }
}

public class DiscountHandler extends PurchaseHandler {
    @Override
    protected boolean canHandlePurchase(PurchaseRequest request) {
        return request.getAmount() > 100;
    }

    @Override
    protected void processPurchase(PurchaseRequest request) {
        System.out.println("Applying discount to purchase.");
    }
}

public class FreeShippingHandler extends PurchaseHandler {
    @Override
    protected boolean canHandlePurchase(PurchaseRequest request) {
        return request.getAmount() > 200;
    }

    @Override
    protected void processPurchase(PurchaseRequest request) {
        System.out.println("Applying free shipping to purchase.");
    }
}
```

现在，我们可以创建一个购买请求的职责链，并设置处理者的顺序，并通过调用第一个处理者的 handlePurchase() 方法来处理购买请求。

```java
public class Main {
    public static void main(String[] args) {
        // 创建处理者对象
        PurchaseHandler couponHandler = new CouponHandler();
        PurchaseHandler discountHandler = new DiscountHandler();
        PurchaseHandler freeShippingHandler = new FreeShippingHandler();

        // 设置处理者的顺序
        couponHandler.setNextHandler(discountHandler);
        discountHandler.setNextHandler(freeShippingHandler);

        // 创建购买请求
        PurchaseRequest purchaseRequest = new PurchaseRequest(150.0, true);

        // 处理购买请求
        couponHandler.handlePurchase(purchaseRequest);
    }
}
```

输出结果：

```text
Applying coupon to purchase.
```

在上述示例中，我们创建了一个职责链，其中优惠券处理者负责处理购买请求中包含优惠券的情况，折扣处理者负责处理购买请求中金额超过100的情况，包邮处理者负责处理购买请求中金额超过200的情况。
购买请求从优惠券处理者开始，依次传递给下一个处理者，直到最终完成购买或无法处理。
