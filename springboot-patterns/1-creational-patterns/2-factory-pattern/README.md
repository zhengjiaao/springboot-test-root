#           

**说明**

工厂模式（Factory Pattern）：工厂模式是一种创建型设计模式，其主要作用是封装对象的创建过程，并提供一个统一的接口来创建对象。

## 工厂模式的作用和优势

以下是工厂模式的作用和优势：

* 封装对象创建逻辑：工厂模式将对象的创建过程封装在工厂类中，客户端只需要通过工厂类的接口来创建对象，无需关心具体的创建细节。这样可以降低客户端与具体实现类之间的耦合度。
* 统一接口：工厂模式提供一个统一的接口来创建对象，客户端只需要与工厂类进行交互，无需直接与具体实现类打交道。这样可以降低客户端的复杂性，并提供了灵活性，使得可以轻松切换和替换不同的产品实现。
* 代码复用：工厂模式可以在工厂类中实现对象的创建逻辑，然后在多个客户端代码中重复使用，避免了代码的重复编写。这样可以提高代码的复用性和维护性。
* 隔离变化：工厂模式通过将对象的创建过程封装在工厂类中，使得客户端与具体实现类解耦。当需要更换或添加新的产品实现时，只需修改工厂类的代码，而无需修改客户端代码，实现了变化的隔离。
* 可扩展性：工厂模式支持通过扩展工厂类来添加新的产品实现。当需要添加新的产品时，只需创建一个新的具体产品类和对应的具体工厂类，无需修改现有代码，符合开放封闭原则。
* 控制对象的创建：工厂模式可以在创建对象时进行一些额外的控制逻辑，例如实施缓存、池化、对象池复用等。这样可以对对象的创建过程进行更加精细的控制和管理。

总之，工厂模式通过封装对象的创建逻辑、提供统一接口、隔离变化和提供灵活性等优势，使得对象的创建过程更加灵活、可控和可扩展。这样可以提高代码的可维护性、可读性和可测试性，同时也符合设计原则中的单一职责和依赖倒置原则。

抽象工厂模式也有一些缺点：

* 不易扩展新的产品等级结构：当需要新增产品等级结构时，需要修改抽象工厂接口及其所有的具体工厂类，这样可能会影响到已有代码的稳定性和可维护性。
* 增加了系统的复杂性：引入抽象工厂模式会增加额外的抽象层次，增加了系统的复杂性。对于简单的产品结构和需求，使用抽象工厂模式可能会显得过于繁琐，不利于代码的理解和维护。
* 难以支持新种类的产品：抽象工厂模式在设计初期就已经确定了产品族和产品等级结构，如果需要支持新种类的产品，可能需要修改抽象工厂接口及其所有的具体工厂类，这样会对现有代码造成较大影响。

## 工厂模式的详细分类

具体实现方式可以有以下几种常见的方式：

* 简单工厂模式（Simple Factory Pattern）：使用一个工厂类来封装对象的创建逻辑。该工厂类通常包含一个静态方法，根据传入的参数或条件决定创建哪种具体对象并返回。客户端通过调用工厂类的静态方法来创建对象。
* 工厂方法模式（Factory Method
  Pattern）：将对象的创建逻辑抽象成一个工厂接口，具体对象的创建由实现该接口的具体工厂类来完成。每个具体工厂类负责创建一种具体对象。客户端通过与工厂接口进行交互来创建对象，无需关心具体工厂类。
* 抽象工厂模式（Abstract Factory Pattern）：提供一个抽象的工厂接口，该接口定义了一系列相关对象的创建方法。每个具体工厂类负责创建一族相关的具体对象。客户端通过与抽象工厂接口进行交互来创建一族相关的对象。

## 工厂模式的实际应用例子

> 这些实现方式都将对象的创建逻辑封装在工厂类中，客户端只需要与工厂类进行交互，无需直接与具体实现类打交道。
> 工厂类根据客户端的请求或条件，在内部进行对象的创建并返回给客户端。
> 这样客户端就无需关心对象的具体创建过程，实现了创建逻辑的封装。

### 1.简单工厂类

以下是一个简单工厂模式的示例：

```java
// 抽象产品
interface Product {
    void doSomething();
}

// 具体产品A
class ConcreteProductA implements Product {
    public void doSomething() {
        System.out.println("ConcreteProductA doSomething");
    }
}

// 具体产品B
class ConcreteProductB implements Product {
    public void doSomething() {
        System.out.println("ConcreteProductB doSomething");
    }
}

// 简单工厂类
class SimpleFactory {
    public static Product createProduct(String type) {
        if (type.equals("A")) {
            return new ConcreteProductA();
        } else if (type.equals("B")) {
            return new ConcreteProductB();
        }
        return null;
    }
}

// 客户端代码
public class Client {
    public static void main(String[] args) {
        Product productA = SimpleFactory.createProduct("A");
        productA.doSomething();  // 输出：ConcreteProductA doSomething

        Product productB = SimpleFactory.createProduct("B");
        productB.doSomething();  // 输出：ConcreteProductB doSomething
    }
}
```

### 2.工厂方法

> 假设你正在开发一个电商平台，其中有多个商家可以在平台上销售自己的产品。
> 每个商家可以发布不同种类的商品，例如服装、电子产品、家具等。
> 你需要实现一个订单处理系统，根据用户选择的商品类型，自动创建相应的订单处理器。

首先，定义一个抽象订单处理器接口：

```java
public interface OrderHandler {
    void handleOrder();
}
```

然后，定义具体的订单处理器类，每个商家都可以提供自己的订单处理器实现：

```java
public class ClothingOrderHandler implements OrderHandler {
    public void handleOrder() {
        System.out.println("处理服装订单");
    }
}

public class ElectronicsOrderHandler implements OrderHandler {
    public void handleOrder() {
        System.out.println("处理电子产品订单");
    }
}

public class FurnitureOrderHandler implements OrderHandler {
    public void handleOrder() {
        System.out.println("处理家具订单");
    }
}
```

接下来，定义一个抽象工厂类，用于创建订单处理器：

```java
public abstract class OrderHandlerFactory {
    public abstract OrderHandler createOrderHandler();
}
```

然后，实现具体的工厂类，每个商家可以提供自己的订单处理器工厂实现：

```java
public class ClothingOrderHandlerFactory extends OrderHandlerFactory {
    public OrderHandler createOrderHandler() {
        return new ClothingOrderHandler();
    }
}

public class ElectronicsOrderHandlerFactory extends OrderHandlerFactory {
    public OrderHandler createOrderHandler() {
        return new ElectronicsOrderHandler();
    }
}

public class FurnitureOrderHandlerFactory extends OrderHandlerFactory {
    public OrderHandler createOrderHandler() {
        return new FurnitureOrderHandler();
    }
}
```

最后，客户端代码可以根据用户选择的商品类型，使用相应的订单处理器工厂创建订单处理器：

```java
public class Client {
    public static void main(String[] args) {
        // 假设用户选择了服装商品类型
        OrderHandlerFactory factory = new ClothingOrderHandlerFactory();
        OrderHandler orderHandler = factory.createOrderHandler();
        orderHandler.handleOrder();  // 输出：处理服装订单

        // 假设用户选择了电子产品商品类型
        factory = new ElectronicsOrderHandlerFactory();
        orderHandler = factory.createOrderHandler();
        orderHandler.handleOrder();  // 输出：处理电子产品订单

        // 假设用户选择了家具商品类型
        factory = new FurnitureOrderHandlerFactory();
        orderHandler = factory.createOrderHandler();
        orderHandler.handleOrder();  // 输出：处理家具订单
    }
}
```

在这个示例中，工厂方法模式被用于根据用户选择的商品类型创建相应的订单处理器。每个商家都提供自己的订单处理器工厂实现，客户端根据具体的商品类型选择相应的工厂，然后使用工厂创建订单处理器。这种方式使得客户端与具体订单处理器的实现解耦，同时支持不同商品类型的灵活扩展和替换。

### 3.1抽象工厂模式的实际例子1

以下是一个抽象工厂模式的示例：

```java
// 抽象产品A
interface ProductA {
    void doSomething();
}

// 具体产品A1
class ConcreteProductA1 implements ProductA {
    public void doSomething() {
        System.out.println("ConcreteProductA1 doSomething");
    }
}

// 具体产品A2
class ConcreteProductA2 implements ProductA {
    public void doSomething() {
        System.out.println("ConcreteProductA2 doSomething");
    }
}

// 抽象产品B
interface ProductB {
    void doSomething();
}

// 具体产品B1
class ConcreteProductB1 implements ProductB {
    public void doSomething() {
        System.out.println("ConcreteProductB1 doSomething");
    }
}

// 具体产品B2
class ConcreteProductB2 implements ProductB {
    public void doSomething() {
        System.out.println("ConcreteProductB2 doSomething");
    }
}

// 抽象工厂
interface AbstractFactory {
    ProductA createProductA();

    ProductB createProductB();
}

// 具体工厂1
class ConcreteFactory1 implements AbstractFactory {
    public ProductA createProductA() {
        return new ConcreteProductA1();
    }

    public ProductB createProductB() {
        return new ConcreteProductB1();
    }
}

// 具体工厂2
class ConcreteFactory2 implements AbstractFactory {
    public ProductA createProductA() {
        return new ConcreteProductA2();
    }

    public ProductB createProductB() {
        return new ConcreteProductB2();
    }
}

// 客户端代码
public class Client {
    public static void main(String[] args) {
        AbstractFactory factory1 = new ConcreteFactory1();
        ProductA productA1 = factory1.createProductA();
        productA1.doSomething();  // 输出：ConcreteProductA1 doSomething
        ProductB productB1 = factory1.createProductB();
        productB1.doSomething();  // 输出：ConcreteProductB1 doSomething

        AbstractFactory factory2 = new ConcreteFactory2();
        ProductA productA2 = factory2.createProductA();
        productA2.doSomething();  // 输出：ConcreteProductA2 doSomething
        ProductB productB2 = factory2.createProductB();
        productB2.doSomething();  // 输出：ConcreteProductB2 doSomething
    }
}
```

在上述示例中，我们定义了两个产品族，每个产品族都有对应的抽象产品。具体产品A有两个实现类：`ConcreteProductA1`
和`ConcreteProductA2`，具体产品B也有两个实现类：`ConcreteProductB1`和`ConcreteProductB2`。

然后，我们定义了一个抽象工厂接口`AbstractFactory`，其中包含了创建产品A和产品B的方法。具体工厂类`ConcreteFactory1`
和`ConcreteFactory2`分别实现了`AbstractFactory`接口，负责创建对应的产品族。

在客户端代码中，我们可以根据需要选择具体的工厂类，然后通过工厂类的方法创建对应的产品A和产品B。客户端无需关心具体产品的实现细节，只需要与抽象工厂接口进行交互，实现了对象创建逻辑的封装和隔离。

这样，抽象工厂模式提供了一种创建一族相关对象的方式，允许客户端在运行时选择不同的具体工厂来创建不同的产品族。这种方式可以增加灵活性，并支持产品族的扩展和演化。

### 3.2抽象工厂模式的实际例子2

当你设计一个电子设备的生产线时，可以使用抽象工厂模式来创建不同类型的设备组件。例如，假设你正在设计一个智能手机生产线，其中包含屏幕和处理器两种组件。不同品牌的智能手机可能采用不同的屏幕和处理器组件，因此可以使用抽象工厂模式来创建这些组件。

首先，定义屏幕和处理器的抽象产品接口：

```java
// 抽象屏幕产品
interface Screen {
    void display();
}

// 抽象处理器产品
interface Processor {
    void process();
}
```

然后，定义不同品牌的屏幕和处理器的具体产品类，例如华为和小米：

```java
// 华为屏幕产品
class HuaweiScreen implements Screen {
    public void display() {
        System.out.println("华为屏幕显示");
    }
}

// 华为处理器产品
class HuaweiProcessor implements Processor {
    public void process() {
        System.out.println("华为处理器运行");
    }
}

// 小米屏幕产品
class XiaomiScreen implements Screen {
    public void display() {
        System.out.println("小米屏幕显示");
    }
}

// 小米处理器产品
class XiaomiProcessor implements Processor {
    public void process() {
        System.out.println("小米处理器运行");
    }
}
```

接下来，定义抽象工厂接口，用于创建屏幕和处理器的组合：

```java
// 抽象工厂接口
interface DeviceFactory {
    Screen createScreen();

    Processor createProcessor();
}
```

然后，实现具体的工厂类，例如华为工厂和小米工厂：

```java
// 华为工厂
class HuaweiFactory implements DeviceFactory {
    public Screen createScreen() {
        return new HuaweiScreen();
    }

    public Processor createProcessor() {
        return new HuaweiProcessor();
    }
}

// 小米工厂
class XiaomiFactory implements DeviceFactory {
    public Screen createScreen() {
        return new XiaomiScreen();
    }

    public Processor createProcessor() {
        return new XiaomiProcessor();
    }
}
```

最后，客户端代码可以根据需要选择具体的工厂类，创建对应品牌的屏幕和处理器：

```java
public class Client {
    public static void main(String[] args) {
        DeviceFactory huaweiFactory = new HuaweiFactory();
        Screen huaweiScreen = huaweiFactory.createScreen();
        Processor huaweiProcessor = huaweiFactory.createProcessor();

        huaweiScreen.display();      // 输出：华为屏幕显示
        huaweiProcessor.process();   // 输出：华为处理器运行

        DeviceFactory xiaomiFactory = new XiaomiFactory();
        Screen xiaomiScreen = xiaomiFactory.createScreen();
        Processor xiaomiProcessor = xiaomiFactory.createProcessor();

        xiaomiScreen.display();      // 输出：小米屏幕显示
        xiaomiProcessor.process();   // 输出：小米处理器运行
    }
}
```

在这个示例中，我们使用抽象工厂模式创建了不同品牌智能手机的屏幕和处理器组合。客户端可以根据需要选择具体品牌的工厂，并使用工厂创建对应的屏幕和处理器。这种方式使得客户端与具体产品的实现解耦，同时支持不同品牌组件的灵活替换和扩展。


