# 7-chain-of-responsibility-pattern

**说明**

访问者模式（Visitor Pattern）：访问者模式是一种行为型设计模式，它允许在不修改已有对象结构的情况下，定义对该结构中元素的新操作。
通过将操作封装在访问者对象中，可以让我们在不改变元素类的前提下，根据需要新增或改变元素的操作。

访问者模式的核心思想是将数据结构和对数据的操作分离，使得操作可以独立变化，同时遵循开闭原则。
它适用于元素结构稳定，但操作需要频繁变化的场景。

访问者模式的主要参与者包括：

* Visitor（访问者）：定义了对每个具体元素的操作，可以通过多个具体访问者实现不同的操作。
* ConcreteVisitor（具体访问者）：实现了Visitor接口，对具体元素进行具体操作。
* Element（元素）：定义了一个接受访问者的方法，通过该方法将自身传递给访问者。
* ConcreteElement（具体元素）：实现了Element接口，实现了接受访问者的方法。
* ObjectStructure（对象结构）：包含元素的集合，提供了遍历元素的方法，可以接受访问者的访问。

## 访问者模式优点、缺点和应用场景

## 访问者模式的实例

### 电商平台的商品操作

下面我们通过一个简单的例子来说明访问者模式的用法。

> 假设我们有一个电商平台，有不同类型的商品（如书籍、电子产品等），并且需要对这些商品进行不同的操作（如计算折扣、计算运费等）。

首先，我们定义一个Visitor接口，其中包含了对不同类型商品的操作方法。

```java
public interface Visitor {
    void visit(Book book);

    void visit(Electronics electronics);
}
```

然后，我们创建具体的访问者类，实现Visitor接口，并在每个具体访问者类中定义对应的操作逻辑。

```java
public class DiscountVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        // 对书籍应用折扣逻辑
        double discountedPrice = book.getPrice() * 0.9;
        book.setPrice(discountedPrice);
        System.out.println("Applied discount to book: " + book.getName());
    }

    @Override
    public void visit(Electronics electronics) {
        // 对电子产品应用折扣逻辑
        double discountedPrice = electronics.getPrice() * 0.8;
        electronics.setPrice(discountedPrice);
        System.out.println("Applied discount to electronics: " + electronics.getName());
    }
}

public class ShippingVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        // 计算书籍的运费逻辑
        double shippingCost = book.getWeight() * 0.5;
        System.out.println("Shipping cost for book " + book.getName() + ": $" + shippingCost);
    }

    @Override
    public void visit(Electronics electronics) {
        // 计算电子产品的运费逻辑
        double shippingCost = electronics.getWeight() * 1.2;
        System.out.println("Shipping cost for electronics " + electronics.getName() + ": $" + shippingCost);
    }
}
```

接下来，我们定义Element接口，并在具体元素类中实现接口中的方法。

```java
public interface Element {
    void accept(Visitor visitor);
}

public class Book implements Element {
    private String name;
    private double price;
    private double weight;

    // 省略构造函数和其他方法

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

public class Electronics implements Element {
    private String name;
    private double price;
    private double weight;

    // 省略构造函数和其他方法

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
```

最后，我们定义一个对象结构类ObjectStructure，用于存储和操作元素对象。

```java
public class ObjectStructure {
    private List<Element> elements = new ArrayList<>();

    public void addElement(Element element) {
        elements.add(element);
    }

    public void removeElement(Element element) {
        elements.remove(element);
    }

    public void accept(Visitor visitor) {
        for (Element element : elements) {
            element.accept(visitor);
        }
    }
}
```

现在，我们可以在客户端代码中使用访问者模式。
客户端示例：

```java
public class Client {
    public static void main(String[] args) {
        // 创建具体元素对象
        Book book = new Book("Design Patterns", 50.0, 1.5);
        Electronics electronics = new Electronics("Smartphone", 500.0, 0.3);

        // 创建具体访问者对象
        Visitor discountVisitor = new DiscountVisitor();
        Visitor shippingVisitor = new ShippingVisitor();

        // 创建对象结构
        ObjectStructure objectStructure = new ObjectStructure();
        objectStructure.addElement(book);
        objectStructure.addElement(electronics);

        // 应用访问者操作
        objectStructure.accept(discountVisitor);
        objectStructure.accept(shippingVisitor);
    }
}
```

输出结果：

```text
Applied discount to book: Design Patterns
Applied discount to electronics: Smartphone
Shipping cost for book Design Patterns: $0.75
Shipping cost for electronics Smartphone: $0.36
```

在上面的例子中，我们创建了一个访问者模式的示例，其中有两个具体访问者（DiscountVisitor和ShippingVisitor），两个具体元素（Book和Electronics），以及一个对象结构（ObjectStructure）。
客户端代码通过创建具体元素对象和具体访问者对象，并将它们添加到对象结构中，然后通过调用对象结构的accept方法来应用访问者的操作。

通过访问者模式，我们可以在不改变元素类的情况下，新增或修改对元素的操作。
这种分离操作的方式可以使得代码更加灵活，易于扩展和维护。同时，访问者模式也符合单一职责原则，将操作逻辑集中在访问者中，使得元素类可以专注于自身的属性和行为。

### 动物园系统

> 假设我们有一个动物园系统，其中包含不同类型的动物，如狗、猫和鸟。我们需要对这些动物进行不同的操作，如喂食、清洁和观察等。

首先，我们定义一个抽象的动物元素（AnimalElement）接口，表示不同类型的动物。

```java
public interface AnimalElement {
    void accept(Visitor visitor);
}
```

然后，我们创建具体的动物类，实现AnimalElement接口，并在每个具体类中实现accept方法。

```java
public class Dog implements AnimalElement {
    // 实现具体的狗类

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

public class Cat implements AnimalElement {
    // 实现具体的猫类

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

public class Bird implements AnimalElement {
    // 实现具体的鸟类

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

// 其他具体动物类的实现
```

接下来，我们定义一个抽象的访问者（Visitor）接口，其中包含对不同类型动物的操作方法。

```java
public interface Visitor {
    void visit(Dog dog);

    void visit(Cat cat);

    void visit(Bird bird);
    // 添加其他动物类型的访问方法
}
```

然后，我们创建具体的访问者类，实现Visitor接口，并在每个具体类中定义对应的操作逻辑。

```java
public class ZooVisitor implements Visitor {
    @Override
    public void visit(Dog dog) {
        System.out.println("Feeding the dog: " + dog.getName());
    }

    @Override
    public void visit(Cat cat) {
        System.out.println("Cleaning the cat: " + cat.getName());
    }

    @Override
    public void visit(Bird bird) {
        System.out.println("Observing the bird: " + bird.getName());
    }

    // 添加其他动物类型的访问方法的具体实现
}
```

最后，我们可以在客户端代码中使用访问者模式。
客户端示例：

```java
public class Client {
    public static void main(String[] args) {
        // 创建具体动物对象
        Dog dog = new Dog("Buddy");
        Cat cat = new Cat("Whiskers");
        Bird bird = new Bird("Tweety");

        // 创建具体访问者对象
        Visitor zooVisitor = new ZooVisitor();

        // 应用访问者操作
        dog.accept(zooVisitor);
        cat.accept(zooVisitor);
        bird.accept(zooVisitor);
    }
}
```

输出结果：

```text
Feeding the dog: Buddy
Cleaning the cat: Whiskers
Observing the bird: Tweety
```

在上述示例中，我们使用访问者模式来实现动物园系统的操作。我们定义了抽象的动物元素接口（AnimalElement）和访问者接口（Visitor），并创建了具体的动物类和访问者类。通过访问者模式，我们可以将不同类型动物的操作逻辑从动物类中分离出来，并在具体访问者中实现这些操作。
在客户端代码中，我们创建了具体动物对象（狗、猫和鸟），并创建了具体访问者对象（ZooVisitor），然后通过调用动物对象的accept方法应用访问者的操作。

### 电子设备的系统

> 假设我们有一个电子设备的系统，其中包含不同类型的设备，如手机、电视和电脑。我们需要对这些设备进行不同的操作，如开机、关机和充电等。

首先，我们定义一个抽象的设备元素（DeviceElement）接口，表示不同类型的设备。

```java
public interface DeviceElement {
    void accept(DeviceVisitor visitor);
}
```

然后，我们创建具体的设备类，实现DeviceElement接口，并在每个具体类中实现accept方法。

```java
public class Phone implements DeviceElement {
    // 实现具体的手机类

    @Override
    public void accept(DeviceVisitor visitor) {
        visitor.visit(this);
    }
}

public class TV implements DeviceElement {
    // 实现具体的电视类

    @Override
    public void accept(DeviceVisitor visitor) {
        visitor.visit(this);
    }
}

public class Computer implements DeviceElement {
    // 实现具体的电脑类

    @Override
    public void accept(DeviceVisitor visitor) {
        visitor.visit(this);
    }
}

// 其他具体设备类的实现
```

接下来，我们定义一个抽象的设备访问者（DeviceVisitor）接口，其中包含对不同类型设备的操作方法。

```java
public interface DeviceVisitor {
    void visit(Phone phone);

    void visit(TV tv);

    void visit(Computer computer);
    // 添加其他设备类型的访问方法
}
```

然后，我们创建具体的设备访问者类，实现DeviceVisitor接口，并在每个具体类中定义对应的操作逻辑。

```java
public class DeviceOperationVisitor implements DeviceVisitor {
    @Override
    public void visit(Phone phone) {
        System.out.println("Turning on the phone: " + phone.getModel());
    }

    @Override
    public void visit(TV tv) {
        System.out.println("Turning off the TV: " + tv.getModel());
    }

    @Override
    public void visit(Computer computer) {
        System.out.println("Charging the computer: " + computer.getModel());
    }

    // 添加其他设备类型的访问方法的具体实现
}
```

最后，我们可以在客户端代码中使用访问者模式。
客户端代码:

```java
public class Client {
    public static void main(String[] args) {
        // 创建具体设备对象
        Phone phone = new Phone("iPhone 12");
        TV tv = new TV("Samsung QLED");
        Computer computer = new Computer("MacBook Pro");

        // 创建具体设备访问者对象
        DeviceVisitor deviceVisitor = new DeviceOperationVisitor();

        // 应用访问者操作
        phone.accept(deviceVisitor);
        tv.accept(deviceVisitor);
        computer.accept(deviceVisitor);
    }
}
```

输出结果：

```text
Turning on the phone: iPhone 12
Turning off the TV: Samsung QLED
Charging the computer: MacBook Pro
```

在上述示例中，我们使用访问者模式来实现电子设备系统的操作。我们定义了抽象的设备元素接口（DeviceElement）和设备访问者接口（DeviceVisitor），并创建了具体的设备类和设备访问者类。通过访问者模式，我们可以将不同类型设备的操作逻辑从设备类中分离出来，并在具体设备访问者中实现这些操作。在客户端代码中，我们创建了具体设备对象（手机、电视和电脑），
并创建了具体设备访问者对象（DeviceOperationVisitor），然后通过调用设备对象的accept方法应用访问者的操作。