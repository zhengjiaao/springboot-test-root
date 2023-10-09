# 5-command-pattern

**说明**

命令模式（Command Pattern）：是一种行为设计模式，它将请求（命令）封装成一个对象，从而使得可以将请求的发送者和接收者解耦。
命令模式允许请求发送者发送一个请求，而无需知道请求的具体操作或接收者的信息。

以下是命令模式的参与者：

* Command（命令）：定义了执行操作的接口。具体的命令类将实现该接口，并实现具体的操作逻辑。
* ConcreteCommand（具体命令）：实现了Command接口，负责执行具体的操作。
* Receiver（接收者）：执行具体操作的对象。具体命令类将与接收者相关联，并在调用时执行接收者的操作。
* Invoker（调用者）：负责发送命令并要求命令执行请求。通常会持有命令对象，并在需要时调用命令的执行方法。
* Client（客户端）：创建具体命令对象，并将其与接收者关联。通过调用调用者的方法来触发命令的执行。

命令模式的核心思想是将请求封装成一个对象，使得请求的发送者和接收者之间解耦。
这样可以灵活地组合和扩展命令，同时也可以支持撤销、重做等操作。
命令模式常用于实现日志、事务、队列等功能。

## 命令模式的优点、缺点和应用场景

命令模式具有以下优点、缺点和适用场景：

优点：

1. 解耦请求发送者和接收者：命令模式通过将请求封装成一个对象，实现了请求发送者和接收者的解耦。发送者不需要知道接收者的具体实现，只需通过命令对象发送请求即可。
2. 容易扩展和组合命令：由于命令对象是独立的，可以方便地扩展和组合多个命令，从而实现复杂的操作。
3. 支持撤销和重做：由于命令对象封装了操作，可以轻松地实现撤销和重做功能，只需保存命令的历史记录并逆序执行即可。
4. 支持日志和事务：命令模式可以方便地记录操作日志或实现事务处理。通过将命令对象放入日志队列或事务队列中，可以统一管理和执行。

缺点：

1. 增加类和对象的数量：引入命令对象会增加类和对象的数量，特别是在有大量不同操作的情况下，可能会导致类和对象的爆炸式增长。
2. 可能引入额外的复杂性：命令模式可能会引入一定的复杂性，例如需要创建和管理多个具体命令类，以及维护命令执行的顺序和状态。

应用场景：

1. 需要将请求发送者和接收者解耦的情况，以便灵活地组合和扩展命令。
2. 需要支持撤销、重做、日志记录或事务处理的场景。
3. 需要实现队列、线程池等功能的情况下，可以使用命令模式来管理和执行命令队列。
4. 需要实现回调机制或事件驱动的系统，可以使用命令模式来封装和处理事件。

总结起来，命令模式适用于需要解耦请求发送者和接收者，支持撤销、重做、日志记录或事务处理等功能的场景。
它可以提供灵活的命令组合和扩展能力，但也可能引入额外的复杂性和类对象的增加。

## 命令模式的实例

###                     

下面是一个简单的命令模式示例代码：

```java
// Command
interface Command {
    void execute();
}

// ConcreteCommand
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

// Receiver
class Light {
    public void turnOn() {
        System.out.println("Light is on");
    }

    public void turnOff() {
        System.out.println("Light is off");
    }
}

// Invoker
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}
```

客户端示例：

```java
// Client
public class CommandPatternExample {
    public static void main(String[] args) {
        Light light = new Light();
        Command lightOnCommand = new LightOnCommand(light);

        RemoteControl remoteControl = new RemoteControl();
        remoteControl.setCommand(lightOnCommand);
        remoteControl.pressButton();
    }
}
```

输出结果：

```text
Light is on
```

在上述示例中，有一个命令接口`Command`，具体命令`LightOnCommand`实现了该接口。`Light`
类是接收者，负责实际执行操作。`RemoteControl`是调用者，持有命令对象，并通过`pressButton()`方法触发命令的执行。

客户端代码中，创建了一个`Light`对象和一个`LightOnCommand`对象，然后将命令对象设置到调用者`RemoteControl`
中。最后，调用者调用`pressButton()`方法，会执行具体命令对象的`execute()`方法，从而触发接收者的操作。

这个示例展示了命令模式的基本结构和工作原理。通过使用命令模式，可以将请求与执行操作解耦，提高代码的灵活性和可维护性。

### 一个文本编辑器中需要实现撤销（Undo）和重做（Redo）功能

当一个文本编辑器中需要实现撤销（Undo）和重做（Redo）功能时，可以使用命令模式来管理和执行这些操作。

在这个例子中，我们可以定义一个通用的命令接口Command，其中包含execute()和undo()
方法。然后，针对不同的编辑操作（如插入文本、删除文本、格式化文本等），创建具体的命令类实现该接口。

下面是一个简化的示例代码：

```java
// Command
interface Command {
    void execute();

    void undo();
}

// Concrete Command - 插入文本命令
class InsertTextCommand implements Command {
    private TextEditor textEditor;
    private String text;

    public InsertTextCommand(TextEditor textEditor, String text) {
        this.textEditor = textEditor;
        this.text = text;
    }

    @Override
    public void execute() {
        textEditor.insertText(text);
    }

    @Override
    public void undo() {
        textEditor.deleteText(text);
    }
}

// Concrete Command - 删除文本命令
class DeleteTextCommand implements Command {
    private TextEditor textEditor;
    private String text;

    public DeleteTextCommand(TextEditor textEditor, String text) {
        this.textEditor = textEditor;
        this.text = text;
    }

    @Override
    public void execute() {
        textEditor.deleteText(text);
    }

    @Override
    public void undo() {
        textEditor.insertText(text);
    }
}

// Receiver - 文本编辑器
class TextEditor {
    private StringBuilder content;

    public TextEditor() {
        content = new StringBuilder();
    }

    public void insertText(String text) {
        content.append(text);
        System.out.println("Inserted text: " + text);
    }

    public void deleteText(String text) {
        int index = content.indexOf(text);
        if (index != -1) {
            content.delete(index, index + text.length());
            System.out.println("Deleted text: " + text);
        }
    }

    public void printContent() {
        System.out.println("Content: " + content.toString());
    }
}

// Invoker - 命令管理者
class CommandManager {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}
```

客户端示例：

```java
// Client
public class CommandPatternExample {
    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        CommandManager commandManager = new CommandManager();

        Command insertCommand = new InsertTextCommand(textEditor, "Hello ");
        commandManager.executeCommand(insertCommand);

        Command deleteCommand = new DeleteTextCommand(textEditor, "World");
        commandManager.executeCommand(deleteCommand);

        textEditor.printContent(); // Output: Content: Hello 

        commandManager.undo();
        textEditor.printContent(); // Output: Content: Hello World

        commandManager.redo();
        textEditor.printContent(); // Output: Content: Hello
    }
}
```

输出结果：

```text
Output: Content: Hello 
Output: Content: Hello World
Output: Content: Hello
```

在上述示例中，TextEditor类是接收者，负责实际的文本编辑操作。Command接口定义了命令操作的执行和撤销方法。具体命令类InsertTextCommand和DeleteTextCommand分别实现了插入文本和删除文本的命令。

CommandManager类是命令管理者，负责执行命令、撤销和重做操作。它使用堆栈来保存执行的命令，实现撤销和重做功能。

在客户端代码中，首先创建了文本编辑器对象TextEditor和命令管理者对象CommandManager。然后创建插入文本和删除文本的具体命令对象，并通过命令管理者执行这些命令。最后，通过撤销和重做操作来演示命令模式的功能。

### 使用命令模式来处理订单的创建、取消和支付等操作

当一个电商平台需要实现订单管理系统时，可以使用命令模式来处理订单的创建、取消和支付等操作。

在这个例子中，我们可以定义一个通用的命令接口Command，其中包含execute()和undo()
方法。然后，针对不同的订单操作（如创建订单、取消订单、支付订单等），创建具体的命令类实现该接口。

下面是一个简化的示例代码：

```java
// Command
interface Command {
    void execute();

    void undo();
}

// Concrete Command - 创建订单命令
class CreateOrderCommand implements Command {
    private OrderService orderService;
    private Order order;

    public CreateOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }

    @Override
    public void execute() {
        orderService.createOrder(order);
    }

    @Override
    public void undo() {
        orderService.cancelOrder(order);
    }
}

// Concrete Command - 取消订单命令
class CancelOrderCommand implements Command {
    private OrderService orderService;
    private Order order;

    public CancelOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }

    @Override
    public void execute() {
        orderService.cancelOrder(order);
    }

    @Override
    public void undo() {
        orderService.createOrder(order);
    }
}

// Concrete Command - 支付订单命令
class PayOrderCommand implements Command {
    private OrderService orderService;
    private Order order;

    public PayOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }

    @Override
    public void execute() {
        orderService.payOrder(order);
    }

    @Override
    public void undo() {
        orderService.cancelPayment(order);
    }
}

// Receiver - 订单服务
class OrderService {
    public void createOrder(Order order) {
        System.out.println("Order created: " + order.getId());
    }

    public void cancelOrder(Order order) {
        System.out.println("Order cancelled: " + order.getId());
    }

    public void payOrder(Order order) {
        System.out.println("Order paid: " + order.getId());
    }

    public void cancelPayment(Order order) {
        System.out.println("Payment cancelled for order: " + order.getId());
    }
}

// Order - 订单类
class Order {
    private String id;

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

// Invoker - 命令管理者
class CommandManager {
    private Stack<Command> commands;

    public CommandManager() {
        commands = new Stack<>();
    }

    public void executeCommand(Command command) {
        command.execute();
        commands.push(command);
    }

    public void undo() {
        if (!commands.isEmpty()) {
            Command command = commands.pop();
            command.undo();
        }
    }
}
```

客户端示例：

```java
// Client
public class Client {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        CommandManager commandManager = new CommandManager();

        Order order1 = new Order("1001");
        Order order2 = new Order("1002");

        Command createCommand = new CreateOrderCommand(orderService, order1);
        commandManager.executeCommand(createCommand);

        Command cancelCommand = new CancelOrderCommand(orderService, order1);
        commandManager.executeCommand(cancelCommand);

        Command payCommand = new PayOrderCommand(orderService, order2);
        commandManager.executeCommand(payCommand);

        commandManager.undo(); // 撤销支付命令

        commandManager.undo(); // 撤销取消命令
    }
}
```

输出结果：

```text
Order created: 1001
Order cancelled: 1001
Order paid: 1002
Payment cancelled for order: 1002
Order created: 1001
```

在上述示例中，OrderService类是接收者，负责实际的订单操作，如创建订单、取消订单、支付订单等。

Command接口定义了命令操作的执行和撤销方法。具体命令类CreateOrderCommand、CancelOrderCommand和PayOrderCommand分别实现了创建订单、取消订单和支付订单的命令。

CommandManager类是命令管理者，负责执行命令和撤销操作。它使用堆栈来保存执行的命令，实现撤销功能。

在客户端代码中，首先创建了订单服务对象OrderService和命令管理者对象CommandManager。然后创建具体的订单对象，并通过命令管理者执行相应的命令，例如创建订单、取消订单、支付订单等。通过命令管理者的undo()
方法可以撤销上一次执行的命令。

这个例子中的命令模式可以帮助电商平台实现订单管理系统的灵活性和扩展性。它将订单操作封装成独立的命令对象，使得可以动态地添加新的命令，而无需修改现有的代码。同时，命令模式也支持撤销操作，可以方便地撤销前一步或多步操作。











