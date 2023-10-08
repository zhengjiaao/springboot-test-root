# 4-bridge-pattern

**说明**

桥接模式（Bridge Pattern）：桥接模式是一种软件设计模式，它用于将抽象部分与其具体实现部分分离，使它们可以独立变化。桥接模式通过使用组合关系而不是继承关系来实现这种分离。

桥接模式的一般结构：

* 抽象部分（Abstraction）：定义抽象部分的接口和维护对实现部分的引用。
* 具体抽象部分（ConcreteAbstraction）：实现抽象部分的接口，维护对实现部分的引用，并将实际的实现委托给实现部分。
* 实现部分（Implementor）：定义实现部分的接口。
* 具体实现部分（ConcreteImplementor）：实现实现部分的接口。

桥接模式的核心思想是通过将抽象部分和实现部分分离，来实现系统的解耦和灵活性。

它适用于以下情况：

* 当需要在抽象部分和实现部分之间有多维度的变化时。
* 当希望在运行时切换抽象部分和实现部分的关系时。
* 当需要对抽象部分和实现部分进行独立扩展时。

通过桥接模式，可以使系统更加灵活、可扩展，并且符合面向对象设计的开闭原则。

## 桥接模式的优缺点

具有以下优点:

1. 分离抽象部分和实现部分：桥接模式通过将抽象部分和实现部分分离，使它们可以独立变化。这样可以更好地应对系统中多个维度的变化，让系统更灵活、可扩展和可维护。
2. 解耦抽象和实现：桥接模式通过组合关系而不是继承关系来连接抽象部分和实现部分，从而减少了它们之间的耦合程度。这使得抽象部分和实现部分可以独立演化，互不影响。
3. 可扩展性：桥接模式支持抽象部分和实现部分的独立扩展。通过添加新的具体抽象部分和具体实现部分，可以方便地扩展系统功能，而无需修改现有代码。
4. 适应变化：桥接模式允许在运行时动态地切换抽象部分和实现部分的关系，而无需修改代码。这种灵活性使得系统可以更好地适应变化，满足不同的需求。

另外，也存在一些缺点:

1. 增加复杂度：桥接模式引入了抽象部分和实现部分之间的额外层级关系，增加了系统的复杂度。这可能会增加开发和维护的成本。
2. 增加代码量：桥接模式需要定义抽象部分和实现部分的接口，并且需要编写额外的代码来将它们连接起来。这可能会增加代码量和开发时间。
3. 对系统理解要求高：使用桥接模式需要对系统中不同部分的关系有较深的理解。如果系统结构较为复杂，理解和设计桥接模式可能会有一定难度。

## 桥接模式示例

### 绘图应用程序

下面是一个简单的桥接模式示例，以展示如何将抽象部分和实现部分分离。

> 假设我们正在开发一个绘图应用程序，需要支持不同的形状（如圆形、正方形）和不同的绘制颜色（如红色、蓝色）。我们希望能够在运行时动态地组合形状和颜色，而不是在编译时固定它们的关系。

首先，我们定义一个形状（Shape）的抽象类，其中包含一个绘制方法（draw）：

```java
public abstract class Shape {
    protected Color color;

    public Shape(Color color) {
        this.color = color;
    }

    public abstract void draw();
}
```

然后，我们定义一个颜色（Color）的接口，其中包含一个填充方法（fill）：

```java
public interface Color {
    void fill();
}
```

接下来，我们实现不同的形状类，例如圆形（Circle）和正方形（Square）：

```java
public class Circle extends Shape {
    public Circle(Color color) {
        super(color);
    }

    @Override
    public void draw() {
        System.out.print("Draw a circle. ");
        color.fill();
    }
}

public class Square extends Shape {
    public Square(Color color) {
        super(color);
    }

    @Override
    public void draw() {
        System.out.print("Draw a square. ");
        color.fill();
    }
}
```

最后，我们实现不同的颜色类，例如红色（Red）和蓝色（Blue）：

```java
public class Red implements Color {
    @Override
    public void fill() {
        System.out.println("Fill with red color.");
    }
}

public class Blue implements Color {
    @Override
    public void fill() {
        System.out.println("Fill with blue color.");
    }
}
```

现在，我们可以在应用程序中动态地创建不同的形状和颜色，并将它们组合起来：

```java
public class Main {
    public static void main(String[] args) {
        Shape redCircle = new Circle(new Red());
        Shape blueSquare = new Square(new Blue());

        redCircle.draw();
        blueSquare.draw();
    }
}
```

输出结果：

```text
Draw a circle. Fill with red color.
Draw a square. Fill with blue color.
```

在这个示例中，桥接模式允许我们在运行时动态地选择形状和颜色的组合，而不需要修改形状和颜色的实现类。
这种分离抽象部分和实现部分的设计使得系统更加灵活和可扩展。

### 电视遥控器应用程序

下面是一个稍微复杂一些的桥接模式示例，以展示更多的抽象部分和实现部分之间的关系。

> 假设我们正在开发一个电视遥控器应用程序，需要支持不同的品牌的电视（如Sony、Samsung）和不同的功能（如开关、调节音量、切换频道）。我们希望能够在运行时动态地组合电视品牌和功能，而不是在编译时固定它们的关系。

首先，我们定义一个电视（TV）的抽象类，其中包含基本的功能方法：

```java
public abstract class TV {
    public abstract void powerOn();

    public abstract void powerOff();

    public abstract void setVolume(int volume);

    public abstract void nextChannel();

    public abstract void previousChannel();
}
```

然后，我们定义一个遥控器（Remote）的抽象类，其中包含基本的控制方法，以及一个维护电视对象的引用：

```java
public abstract class Remote {
    protected TV tv;

    public Remote(TV tv) {
        this.tv = tv;
    }

    public abstract void power();

    public abstract void volumeUp();

    public abstract void volumeDown();

    public abstract void nextChannel();

    public abstract void previousChannel();
}
```

接下来，我们实现不同品牌的电视类，例如SonyTV和SamsungTV：

```java
public class SonyTV extends TV {
    @Override
    public void powerOn() {
        System.out.println("Sony TV is powered on.");
    }

    @Override
    public void powerOff() {
        System.out.println("Sony TV is powered off.");
    }

    @Override
    public void setVolume(int volume) {
        System.out.println("Sony TV volume is set to: " + volume);
    }

    @Override
    public void nextChannel() {
        System.out.println("Sony TV switches to next channel.");
    }

    @Override
    public void previousChannel() {
        System.out.println("Sony TV switches to previous channel.");
    }
}

public class SamsungTV extends TV {
    @Override
    public void powerOn() {
        System.out.println("Samsung TV is powered on.");
    }

    @Override
    public void powerOff() {
        System.out.println("Samsung TV is powered off.");
    }

    @Override
    public void setVolume(int volume) {
        System.out.println("Samsung TV volume is set to: " + volume);
    }

    @Override
    public void nextChannel() {
        System.out.println("Samsung TV switches to next channel.");
    }

    @Override
    public void previousChannel() {
        System.out.println("Samsung TV switches to previous channel.");
    }
}
```

最后，我们实现具体的遥控器类，例如SonyRemote和SamsungRemote：

```java
public class SonyRemote extends Remote {
    public SonyRemote(TV tv) {
        super(tv);
    }

    @Override
    public void power() {
        tv.powerOn();
    }

    @Override
    public void volumeUp() {
        int volume = getCurrentVolume();
        tv.setVolume(volume + 1);
    }

    @Override
    public void volumeDown() {
        int volume = getCurrentVolume();
        tv.setVolume(volume - 1);
    }

    @Override
    public void nextChannel() {
        tv.nextChannel();
    }

    @Override
    public void previousChannel() {
        tv.previousChannel();
    }

    private int getCurrentVolume() {
        // 从遥控器中获取当前音量
        return 0;
    }
}

public class SamsungRemote extends Remote {
    public SamsungRemote(TV tv) {
        super(tv);
    }

    @Override
    public void power() {
        tv.powerOn();
    }

    @Override
    public void volumeUp() {
        int volume = getCurrentVolume();
        tv.setVolume(volume + 1);
    }

    @Override
    public void volumeDown() {
        int volume = getCurrentVolume();
        tv.setVolume(volume - 1);
    }

    @Override
    public void nextChannel() {
        tv.nextChannel();
    }

    @Override
    public void previousChannel() {
        tv.previousChannel();
    }

    private int getCurrentVolume() {
        // 从遥控器中获取当前音量
        return 0;
    }
}
```

现在，我们可以在应用程序中动态地创建不同品牌的电视和遥控器，并将它们组合起来：

```java
public class Main {
    public static void main(String[] args) {
        TV sonyTV = new SonyTV();
        TV samsungTV = new SamsungTV();

        Remote sonyRemote = new SonyRemote(sonyTV);
        Remote samsungRemote = new SamsungRemote(samsungTV);

        sonyRemote.power();
        sonyRemote.volumeUp();
        sonyRemote.nextChannel();

        samsungRemote.power();
        samsungRemote.volumeDown();
        samsungRemote.previousChannel();
    }
}
```

输出结果：

```text
Sony TV is powered on.
Sony TV volume is set to: 1
Sony TV switches to next channel.
Samsung TV is powered on.
Samsung TV volume is set to: -1
Samsung TV switches to previous channel.
```

在这个示例中，桥接模式允许我们在运行时动态地选择电视品牌和遥控器的组合，而不需要修改电视和遥控器的实现类。这种分离抽象部分和实现部分的设计使得系统更加灵活和可扩展。


