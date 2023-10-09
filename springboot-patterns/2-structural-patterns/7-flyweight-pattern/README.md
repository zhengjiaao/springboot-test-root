# 7-flyweight-pattern

**说明**

享元模式（Flyweight Pattern）：享元模式是一种结构设计模式，旨在通过共享对象来减少内存使用和提高性能。该模式适用于存在大量相似对象的场景，通过共享这些对象的内部状态，可以有效减少内存消耗。

在享元模式中，有两种类型的状态：内部状态（Intrinsic State）和外部状态（Extrinsic
State）。内部状态是对象可共享的部分，它独立于对象的上下文，通常在对象创建时就确定下来，并且对于所有实例都是相同的。而外部状态取决于对象的上下文，因此不能被共享，需要在使用享元对象时传递给它。

以下是享元模式的一般结构：

* Flyweight（享元）：定义共享对象的接口，通过接受外部状态作为参数处理操作。
* ConcreteFlyweight（具体享元）：实现享元接口，并为内部状态提供具体实现。
* FlyweightFactory（享元工厂）：负责创建和管理享元对象，维护一个享元池（享元对象的缓存池）来确保对象的共享和复用。
* Client（客户端）：使用享元对象的客户端，通过享元工厂获取享元对象，并传递外部状态进行操作。

## 享元模式的优点、缺点和适用场景

享元模式是一种结构型设计模式，它通过共享对象来减少内存使用和提高系统性能。下面是享元模式的优点、缺点和适用场景：

优点：

* 资源利用率高：享元模式通过共享相同的对象实例，减少了重复对象的创建和内存消耗，提高了系统的资源利用率。
* 减少内存占用：共享对象可以减少系统中对象的数量，从而减少了内存的占用，特别在需要创建大量相似对象的情况下，可以显著降低内存消耗。
* 提高性能：由于减少了对象的创建和销毁，享元模式可以提高系统的性能，特别是在频繁创建和销毁对象的场景下，如大规模循环、高并发等。
* 状态外部化：享元模式将共享对象的内部状态和外部状态分离，内部状态由享元对象管理，而外部状态由客户端传入，这样可以简化对象的逻辑，提高系统的可维护性。

缺点：

* 对象共享可能引起线程安全问题：如果多个线程同时访问共享对象，并且修改了共享对象的状态，可能会导致线程安全问题。因此，在使用享元模式时需要考虑线程安全性，采取合适的同步措施。
* 共享对象的状态较多时，维护成本高：当共享对象具有多个状态时，需要维护这些状态的变化和管理，可能会增加代码复杂性和维护成本。

应用场景：

* 对象数量庞大且相似：当系统中存在大量相似的对象，且创建这些对象的代价很高时，可以使用享元模式来共享对象，减少内存消耗。
* 对象的状态可外部化：当对象的内部状态可以被外部状态替代，并且外部状态可以在对象创建之后进行修改时，可以考虑采用享元模式。
* 需要频繁创建和销毁对象的场景：当系统需要频繁创建和销毁对象，且对象的创建和销毁代价较高时，可以使用享元模式来缓存已经创建的对象，提高系统性能。
* 多线程环境下的对象共享：当多个线程需要共享对象，并且对象的状态不会发生变化时，可以使用享元模式来实现线程安全的对象共享。

需要注意的是，享元模式适用于那些需要大量相似对象的情况，并且可以明确区分内部状态和外部状态的场景。

如何区分内部状态和外部状态:

1. 内部状态：
    * 内部状态可以被多个对象共享。
    * 内部状态在对象创建后就不再发生变化。
    * 内部状态由享元对象自己管理和维护。

2. 外部状态：
    * 外部状态不能被多个对象共享。
    * 外部状态随着外部环境的变化而变化。
    * 外部状态由客户端传入享元对象，并在使用时传递给享元对象。

在设计享元模式时，需要明确哪些状态是内部状态，哪些状态是外部状态。通常，可以根据以下几个方面进行判断：

* 对象的属性：如果某个属性在对象创建后不再改变，并且可以被多个对象共享，那么它很可能是内部状态。如果属性的值随着外部环境的变化而变化，那么它是外部状态。
* 对象的行为：如果某个行为在对象创建后不再改变，并且可以被多个对象共享，那么它很可能是内部状态。如果行为的结果受到外部环境的影响，那么它是外部状态。
* 对象的生命周期：如果某个状态在对象创建后一直存在，并且可以被多个对象共享，那么它很可能是内部状态。如果状态在对象的生命周期中会发生变化，那么它是外部状态。

需要注意的是，正确地区分内部状态和外部状态对于享元模式的实现至关重要。
内部状态应该由享元对象自己管理和维护，而外部状态应该由客户端传递给享元对象，并在使用时传递给相应的方法。

## 享元模式的实例

### 使用享元模式来共享相同的图形对象

通过享元模式，当需要绘制相同颜色的圆形时，只需共享已经创建的对象，避免了重复创建相同的对象，从而减少了内存消耗。

下面是一个简单的示例，演示了如何使用享元模式来共享相同的图形对象：

```java
import java.util.HashMap;
import java.util.Map;

// Flyweight（享元）
interface Shape {
    void draw();
}

// ConcreteFlyweight（具体享元）
class Circle implements Shape {
    private String color;

    public Circle(String color) {
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a circle with color " + color);
    }
}

// FlyweightFactory（享元工厂）
class ShapeFactory {
    private static final Map<String, Shape> shapeCache = new HashMap<>();

    public static Shape getCircle(String color) {
        Circle circle = (Circle) shapeCache.get(color);

        if (circle == null) {
            circle = new Circle(color);
            shapeCache.put(color, circle);
            System.out.println("Creating a new circle with color " + color);
        }

        return circle;
    }
}

```

客户端调用：

```java
// Client（客户端）
public class Client {
    private static final String[] colors = {"Red", "Green", "Blue", "Yellow"};

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String color = colors[(int) (Math.random() * colors.length)];
            Shape circle = ShapeFactory.getCircle(color);
            circle.draw();
        }
    }
}
```

在上述示例中，Circle 类是具体享元类，它实现了 Shape 接口并提供了绘制圆形的实现。ShapeFactory 是享元工厂类，负责创建和管理圆形对象，并通过一个
shapeCache 哈希映射来维护已经创建的圆形对象。Client 是客户端代码，它使用享元工厂获取圆形对象，并在循环中绘制不同颜色的圆形。

通过享元模式，当需要绘制相同颜色的圆形时，只需共享已经创建的对象，避免了重复创建相同的对象，从而减少了内存消耗。

### 实现享元模式来共享网站用户的状态信息

下面是一个更复杂的示例，展示了如何在代码中实现享元模式来共享网站用户的状态信息。

```java
import java.util.HashMap;
import java.util.Map;

// 享元接口（Flyweight）
interface User {
    void login();

    void logout();
}

// 具体享元类（Concrete Flyweight）
class UserImpl implements User {
    private String username;

    public UserImpl(String username) {
        this.username = username;
    }

    @Override
    public void login() {
        System.out.println(username + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println(username + " logged out.");
    }
}

// 享元工厂类（Flyweight Factory）
class UserFactory {
    private static final Map<String, User> userCache = new HashMap<>();

    public static User getUser(String username) {
        User user = userCache.get(username);

        if (user == null) {
            user = new UserImpl(username);
            userCache.put(username, user);
            System.out.println("Creating a new user: " + username);
        }

        return user;
    }
}
```

客户端调用：

```java
// 客户端（Client）
public class Client {
    public static void main(String[] args) {
        User user1 = UserFactory.getUser("John");
        user1.login();

        User user2 = UserFactory.getUser("Jane");
        user2.login();

        User user3 = UserFactory.getUser("John");
        user3.login();

        user1.logout();
        user2.logout();
        user3.logout();
    }
}
```

在上述示例中，我们假设有一个网站，网站上有很多用户。`User` 接口定义了用户的登录和登出方法。`UserImpl`
类是具体享元类，实现了 `User` 接口，并在 `login()` 和 `logout()` 方法中打印出用户登录和登出的信息。`UserFactory`
类是享元工厂类，负责创建和管理用户对象。它维护一个 `userCache` 哈希映射来存储已经创建的用户对象。`Client`
类是客户端代码，它通过 `UserFactory` 获取用户对象，并调用登录和登出方法。

在客户端代码中，我们可以看到创建了三个用户对象：`John`、`Jane` 和 `John`
。但实际上只有第一次创建了新的用户对象，后续获取的都是已经创建的对象。通过享元模式，我们共享了相同用户名的用户对象，减少了内存消耗和创建对象的开销。

通过这个示例，我们可以看到享元模式的优势：当系统中存在大量相似对象时，通过共享内部状态，我们可以减少对象的创建次数，提高系统的性能和效率。

