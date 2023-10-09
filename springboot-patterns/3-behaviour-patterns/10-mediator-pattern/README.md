# 10-mediator-pattern

**说明**

中介者模式（Mediator Pattern）：中介者模式是一种行为型设计模式，它通过集中化控制多个对象之间的交互，将对象之间的通信通过中介者对象进行协调和管理。
中介者模式有助于减少对象之间的直接依赖关系，提供了一种松耦合的方式，使得对象之间的交互更加灵活和可扩展。

在中介者模式中，有以下几个角色：

1. 中介者（Mediator）：定义了对象之间的通信接口，负责协调对象之间的交互关系。
2. 具体中介者（Concrete Mediator）：实现了中介者接口，协调具体的对象之间的交互关系。
3. 同事（Colleague）：定义了自身行为的接口，并维护一个对中介者对象的引用，用于与其他同事进行通信。
4. 具体同事（Concrete Colleague）：实现了同事接口，通过中介者进行与其他同事的通信。

中介者模式可以帮助解决对象之间的复杂交互问题，尤其在对象之间的关系呈现网状结构时，使用中介者模式可以简化对象之间的通信流程。

## 中介者模式优点、缺点和应用场景

中介者模式的优点包括：

1. 减少了对象之间的直接耦合：中介者模式通过将对象之间的通信集中在中介者中，减少了对象之间的直接耦合关系。对象只需要与中介者进行通信，而不需要了解其他对象的细节，从而简化了对象之间的交互。
2. 提高了系统的可扩展性：由于对象之间的通信逻辑集中在中介者中，当需要增加新的对象或修改现有对象时，只需要调整中介者而不需要修改其他对象的代码，从而提高了系统的可扩展性。
3. 促进了代码的重用性：中介者模式将通信逻辑封装在中介者中，可以被多个对象共享和重用，避免了重复编写通信代码的情况。
4. 简化了对象间的交互：中介者模式将复杂的交互逻辑集中在中介者中，使得对象之间的交互变得简单明了，易于理解和维护。

中介者模式的缺点包括：

1. 中介者的职责可能过重：由于中介者需要管理和协调多个对象之间的通信，当系统变得复杂时，中介者的职责可能会变得过重，导致中介者本身变得复杂和庞大。
2. 增加了系统的复杂性：引入中介者模式会增加一个中介者对象，可能会使系统的结构变得更加复杂，增加了理解和维护的难度。

中介者模式适用于以下情况：

1. 当对象之间存在复杂的交互关系，并且彼此之间的依赖关系较强时，可以考虑使用中介者模式来简化对象之间的通信。
2. 当系统中的对象数量较大且交互复杂时，可以使用中介者模式来减少对象之间的直接耦合，提高系统的可维护性和可扩展性。
3. 当对象之间的交互逻辑需要集中管理和调度时，中介者模式可以将这些逻辑封装在中介者中，使得对象之间的交互更加简单和可控。

常见的应用场景包括聊天室应用程序、多用户协作系统、航空管制系统等。在这些场景中，对象之间的通信逻辑较为复杂，并且需要通过中介者进行协调和管理，中介者模式可以有效地简化系统的设计和实现。

## 中介者模式的实例

### 聊天室应用

以下是一个简单的示例代码，演示了中介者模式在聊天室应用中的应用：

```java
import java.util.ArrayList;
import java.util.List;

// 中介者接口
interface ChatroomMediator {
    void sendMessage(String message, User sender);

    void addUser(User user);
}

// 具体中介者
class Chatroom implements ChatroomMediator {
    private List<User> users;

    public Chatroom() {
        this.users = new ArrayList<>();
    }

    public void sendMessage(String message, User sender) {
        for (User user : users) {
            if (user != sender) {
                user.receiveMessage(message);
            }
        }
    }

    public void addUser(User user) {
        users.add(user);
    }
}

// 同事接口
interface User {
    void sendMessage(String message);

    void receiveMessage(String message);
}

// 具体同事
class ChatUser implements User {
    private String name;
    private ChatroomMediator mediator;

    public ChatUser(String name, ChatroomMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public void sendMessage(String message) {
        System.out.println(name + " sends message: " + message);
        mediator.sendMessage(message, this);
    }

    public void receiveMessage(String message) {
        System.out.println(name + " receives message: " + message);
    }
}
```

客户端示例：

```java
// 客户端代码
public class Client {
    public static void main(String[] args) {
        ChatroomMediator chatroom = new Chatroom();
        User user1 = new ChatUser("Alice", chatroom);
        User user2 = new ChatUser("Bob", chatroom);
        User user3 = new ChatUser("Charlie", chatroom);

        chatroom.addUser(user1);
        chatroom.addUser(user2);
        chatroom.addUser(user3);

        user1.sendMessage("Hello everyone!");
        user2.sendMessage("Nice to meet you, Alice!");
    }
}
```

输出结果：

```text
Alice sends message: Hello everyone!
Bob receives message: Hello everyone!
Charlie receives message: Hello everyone!
Bob sends message: Nice to meet you, Alice!
Alice receives message: Nice to meet you, Alice!
Charlie receives message: Nice to meet you, Alice!
```

在这个示例中，中介者模式被应用于聊天室应用。聊天室（Chatroom）充当中介者角色，负责协调用户（User）之间的交互。具体的用户（ChatUser）实现了用户接口，通过中介者进行消息的发送和接收。

当一个用户发送消息时，中介者将消息传递给其他用户，其他用户接收到消息后进行处理。通过中介者模式，用户之间的通信被集中到中介者对象中，用户之间不需要直接知道对方的存在，从而减少了对象之间的耦合度。

中介者模式在实际应用中有很多场景，例如多用户聊天室、飞机调度系统、电子商务平台等，都可以使用中介者模式来简化对象之间的交互和通信。

