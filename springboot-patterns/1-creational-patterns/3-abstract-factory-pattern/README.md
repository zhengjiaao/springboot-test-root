# 3-abstract-factory-pattern

**说明**

抽象工厂模式（Abstract Factory
Pattern）：抽象工厂模式是一种创建型设计模式，它提供了一种封装对象创建的方式。抽象工厂模式的主要作用是通过定义抽象工厂接口和一组相关的抽象产品接口，将具体产品的创建逻辑封装起来，使客户端代码与具体产品的实现解耦。

## 抽象工厂模式的作用和优势

以下是抽象工厂模式的一些优势和作用：

* 封装对象创建逻辑：抽象工厂模式将对象的创建过程封装在工厂接口中，客户端只需要与工厂接口进行交互，无需直接关注具体对象的创建过程。这样可以降低客户端与具体类之间的耦合度，并且提供了一种高层次的抽象，隐藏了对象的创建细节。
*

支持产品族的创建：抽象工厂模式可以创建一组相关或依赖的产品对象，这些产品对象通常属于同一个产品族。例如，在游戏开发中，可以使用抽象工厂模式创建不同操作系统下的UI组件，这些组件属于同一个UI组件族。抽象工厂模式能够确保所创建的产品之间相互配合、相互依赖。

* 客户端与具体类解耦：抽象工厂模式将具体类的实例化操作封装在工厂中，客户端无需直接创建具体类的实例，从而实现了客户端与具体类的解耦。这样也便于替换具体工厂，以及在运行时动态切换产品。
* 保持一致性：抽象工厂模式确保了一系列相关对象的一致性。由于工厂负责创建整个产品族，它可以确保所创建的产品之间相互配合、相互依赖。这有助于确保系统中的对象是一致的，并且符合某种特定的约束。
* 支持扩展：抽象工厂模式可以通过增加新的具体工厂和产品类来扩展系统功能。当需要添加新的产品族时，只需要实现相应的抽象工厂接口和产品接口，并提供具体的实现类。这样可以在不修改现有代码的情况下引入新的产品。

抽象工厂模式也有一些缺点：

* 扩展困难：当需要添加新的产品时，需要修改抽象工厂的接口以及所有具体工厂的实现。这样会导致抽象工厂及其所有具体工厂的代码修改，违反了开闭原则。因此，在需要频繁添加新产品的情况下，抽象工厂模式的扩展性可能受到一定的限制。
* 类的数量增加：抽象工厂模式涉及多个抽象产品和多个具体产品类，因此可能会导致类的数量增加。这可能会增加系统的复杂性和理解难度。
* 系统抽象层级增加：引入抽象工厂模式会增加系统的抽象层级，增加了代码的抽象程度和理解难度。这可能会使得系统更加复杂，不适合简单的场景。

## 抽象工厂模式的实际应用例子

### 实际应用例子1

>
假设你正在开发一个多平台游戏，需要在不同的操作系统上展示不同风格的用户界面（UI），例如Windows系统使用Windows风格的UI，Mac系统使用Mac风格的UI。你希望能够在运行时动态选择适当的UI风格，而不是在代码中直接依赖于具体的UI实现。

首先，定义UI组件的抽象类或接口：

```java
public interface Button {
    void render();
}

public interface TextBox {
    void render();
}
```

然后，定义具体的Windows风格和Mac风格的UI组件：

```java
public class WindowsButton implements Button {
    public void render() {
        System.out.println("渲染Windows风格的按钮");
    }
}

public class WindowsTextBox implements TextBox {
    public void render() {
        System.out.println("渲染Windows风格的文本框");
    }
}

public class MacButton implements Button {
    public void render() {
        System.out.println("渲染Mac风格的按钮");
    }
}

public class MacTextBox implements TextBox {
    public void render() {
        System.out.println("渲染Mac风格的文本框");
    }
}
```

接下来，定义抽象工厂类来创建UI组件：

```java
public interface UIFactory {
    Button createButton();

    TextBox createTextBox();
}
```

然后，实现具体的Windows和Mac风格的UI工厂：

```java
public class WindowsUIFactory implements UIFactory {
    public Button createButton() {
        return new WindowsButton();
    }

    public TextBox createTextBox() {
        return new WindowsTextBox();
    }
}

public class MacUIFactory implements UIFactory {
    public Button createButton() {
        return new MacButton();
    }

    public TextBox createTextBox() {
        return new MacTextBox();
    }
}
```

最后，客户端代码根据当前操作系统选择合适的UI工厂，并使用工厂创建对应的UI组件：

```java
public class Client {
    public static void main(String[] args) {
        String os = "Windows";  // 假设当前操作系统为Windows

        UIFactory factory;
        if (os.equals("Windows")) {
            factory = new WindowsUIFactory();
        } else {
            factory = new MacUIFactory();
        }

        Button button = factory.createButton();
        TextBox textBox = factory.createTextBox();

        button.render();  // 输出：渲染Windows风格的按钮
        textBox.render();  // 输出：渲染Windows风格的文本框
    }
}
```

在这个示例中，抽象工厂模式被用于根据不同的操作系统选择适当的UI工厂，并使用工厂创建对应的UI组件。
通过抽象工厂模式，客户端代码无需直接依赖具体的UI组件实现，而是通过接口与抽象工厂进行交互。
这样可以实现在运行时动态选择不同的UI风格，提高了代码的可扩展性和灵活性。

### 实际应用例子2

>
假设你正在开发一个汽车制造系统，需要根据不同的市场要求生产不同类型的汽车，包括经济型汽车和豪华型汽车。每种类型的汽车都包含引擎、轮胎和座椅等组件，而且每个组件都有不同的规格和特点。你希望能够根据市场需求动态选择合适的汽车类型并生产对应的汽车及其组件。

首先，定义汽车及其组件的抽象类或接口：

```java
public interface Car {
    void assemble();
}

public interface Engine {
    void design();
}

public interface Tire {
    void produce();
}

public interface Seat {
    void install();
}
```

然后，定义经济型汽车及其组件的具体类：

```java
public class EconomyCar implements Car {
    public void assemble() {
        System.out.println("组装经济型汽车");
    }
}

public class EconomyEngine implements Engine {
    public void design() {
        System.out.println("设计经济型引擎");
    }
}

public class EconomyTire implements Tire {
    public void produce() {
        System.out.println("生产经济型轮胎");
    }
}

public class EconomySeat implements Seat {
    public void install() {
        System.out.println("安装经济型座椅");
    }
}
```

接下来，定义豪华型汽车及其组件的具体类：

```java
public class LuxuryCar implements Car {
    public void assemble() {
        System.out.println("组装豪华型汽车");
    }
}

public class LuxuryEngine implements Engine {
    public void design() {
        System.out.println("设计豪华型引擎");
    }
}

public class LuxuryTire implements Tire {
    public void produce() {
        System.out.println("生产豪华型轮胎");
    }
}

public class LuxurySeat implements Seat {
    public void install() {
        System.out.println("安装豪华型座椅");
    }
}
```

接下来，定义汽车工厂的抽象类或接口：

```java
public interface CarFactory {
    Car createCar();

    Engine createEngine();

    Tire createTire();

    Seat createSeat();
}
```

然后，实现经济型汽车工厂和豪华型汽车工厂：

```java
public class EconomyCarFactory implements CarFactory {
    public Car createCar() {
        return new EconomyCar();
    }

    public Engine createEngine() {
        return new EconomyEngine();
    }

    public Tire createTire() {
        return new EconomyTire();
    }

    public Seat createSeat() {
        return new EconomySeat();
    }
}

public class LuxuryCarFactory implements CarFactory {
    public Car createCar() {
        return new LuxuryCar();
    }

    public Engine createEngine() {
        return new LuxuryEngine();
    }

    public Tire createTire() {
        return new LuxuryTire();
    }

    public Seat createSeat() {
        return new LuxurySeat();
    }
}
```

最后，客户端代码根据市场需求选择合适的汽车工厂，并使用工厂创建对应的汽车及其组件：

```java
public class Client {
    public static void main(String[] args) {
        String market = "Economy";  // 假设当前市场需求为经济型汽车

        CarFactory factory;
        if (market.equals("Economy")) {
            factory = new EconomyCarFactory();
        } else {
            factory = new LuxuryCarFactory();
        }

        Car car = factory.createCar();
        Engine engine = factory.createEngine();
        Tire tire = factory.createTire();
        Seat seat = factory.createSeat();

        car.assemble();  // 输出：组装经济型汽车
        engine.design();  // 输出：设计经济型引擎
        tire.produce();  // 输出：生产经济型轮胎
        seat.install();  // 输出：安装经济型座椅
    }
}
```

在这个示例中，抽象工厂模式被用于根据市场需求选择合适的汽车工厂，并使用工厂创建对应的汽车及其组件。这样，无论是经济型汽车工厂还是豪华型汽车工厂，都能够按照定义的规则创建对应的汽车和组件，实现了客户端与具体类的解耦。同时，如果需要添加新的汽车类型，只需要实现对应的具体类和工厂，无需修改客户端代码，符合开闭原则。

