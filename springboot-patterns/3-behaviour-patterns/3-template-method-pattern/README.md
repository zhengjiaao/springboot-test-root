# 3-template-method-pattern

**说明**

模板方法模式（Template Method
Pattern）：模板方法模式是一种行为设计模式，它定义了一个算法的骨架，将一些步骤的实现延迟到子类中。模板方法模式通过将公共的算法逻辑放在父类中，而将具体的实现细节留给子类来实现，以达到代码复用和扩展的目的。

模板方法模式由以下几个角色组成：

* AbstractClass（抽象类）：定义了一个模板方法，它包含了算法的骨架，其中的某些步骤由子类来实现。抽象类还可以定义一些具体方法，用于被子类直接调用。
* ConcreteClass（具体类）：继承抽象类，实现其中的抽象方法，完成算法的具体实现。

## 模板方法模式的优点、缺点和应用场景

## 模板方法模式的实例

### 使用模板方法模式定义一个游戏骨架

下面是一个示例代码来说明模板方法模式的结构和使用：

```java
// 抽象类
abstract class Game {
    // 模板方法，定义了游戏的骨架
    public final void play() {
        initialize();
        startGame();
        endGame();
    }

    // 初始化游戏
    abstract void initialize();

    // 开始游戏
    abstract void startGame();

    // 结束游戏
    abstract void endGame();
}

// 具体类A
class FootballGame extends Game {
    @Override
    void initialize() {
        System.out.println("Football game initialized.");
    }

    @Override
    void startGame() {
        System.out.println("Football game started. Enjoy the match!");
    }

    @Override
    void endGame() {
        System.out.println("Football game ended. Thank you for playing!");
    }
}

// 具体类B
class ChessGame extends Game {
    @Override
    void initialize() {
        System.out.println("Chess game initialized.");
    }

    @Override
    void startGame() {
        System.out.println("Chess game started. Good luck!");
    }

    @Override
    void endGame() {
        System.out.println("Chess game ended. Well played!");
    }
}
```

客户端示例：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        Game game1 = new FootballGame();
        game1.play();

        System.out.println();

        Game game2 = new ChessGame();
        game2.play();
    }
}
```

输出结果：

```text

```

在上述示例中，我们定义了一个抽象类`Game`，其中包含了一个模板方法`play()`
，它定义了游戏的骨架。抽象类还声明了三个抽象方法`initialize()`、`startGame()`和`endGame()`，这些方法由具体的子类来实现。

我们创建了两个具体类`FootballGame`和`ChessGame`，它们继承了抽象类`Game`并实现了其中的抽象方法。每个具体类都提供了自己的实现细节，完成了特定游戏的初始化、开始和结束逻辑。

在示例代码中，我们创建了一个`FootballGame`对象和一个`ChessGame`对象，并调用它们的`play()`
方法。这样，整个游戏的流程就被执行了，但具体的实现细节由子类来决定。

通过使用模板方法模式，我们可以在父类中定义算法的骨架，使得子类可以灵活地实现自己的具体逻辑。这样就提供了代码复用和扩展的便利性，同时将算法的控制权交给了父类。

### 咖啡和茶的制作过程

使用模板方法模式的例子：咖啡和茶的制作过程。

```java
// 抽象类
abstract class Beverage {
    // 模板方法，定义了饮料的制作过程
    public final void prepareBeverage() {
        boilWater();
        brew();
        pourIntoCup();
        addCondiments();
    }

    // 把水煮沸
    private void boilWater() {
        System.out.println("Boiling water...");
    }

    // 冲泡饮料
    abstract void brew();

    // 把饮料倒入杯子
    private void pourIntoCup() {
        System.out.println("Pouring into cup...");
    }

    // 添加调料
    abstract void addCondiments();
}

// 具体类A：咖啡
class Coffee extends Beverage {
    @Override
    void brew() {
        System.out.println("Brewing coffee...");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding sugar and milk...");
    }
}

// 具体类B：茶
class Tea extends Beverage {
    @Override
    void brew() {
        System.out.println("Steeping tea...");
    }

    @Override
    void addCondiments() {
        System.out.println("Adding lemon...");
    }
}
```

客户端示例代码：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        Beverage coffee = new Coffee();
        coffee.prepareBeverage();

        System.out.println();

        Beverage tea = new Tea();
        tea.prepareBeverage();
    }
}
```

输出结果：

```text
Boiling water...
Brewing coffee...
Pouring into cup...
Adding sugar and milk...

Boiling water...
Steeping tea...
Pouring into cup...
Adding lemon...
```

在这个例子中，我们有一个抽象类`Beverage`，它定义了制作饮料的模板方法`prepareBeverage()`
。抽象类还定义了一些具体方法，如`boilWater()`和`pourIntoCup()`，它们在模板方法中被调用，但不能被子类重写。

我们创建了两个具体类`Coffee`和`Tea`，它们继承了抽象类`Beverage`并实现了其中的抽象方法。每个具体类根据自己的特点提供了冲泡饮料和添加调料的具体实现。

在示例代码中，我们创建了一个`Coffee`对象和一个`Tea`对象，并分别调用它们的`prepareBeverage()`
方法。这样，制作咖啡和茶的过程就被执行了，但具体的实现细节由子类来决定。

通过使用模板方法模式，我们可以定义一个算法的骨架，并将一些步骤的实现留给子类。这样，我们可以在父类中控制算法的流程，同时允许子类根据自己的需求定制具体的步骤。这种模式使得代码复用和扩展变得更加简单和灵活。

### 数据导入操作

使用模板方法模式的例子：数据导入操作。

> 假设我们有一个数据导入系统，可以从不同的数据源（如数据库、文件、API等）中导入数据，并执行一系列的操作，如数据清洗、转换、验证等。
> 我们可以使用模板方法模式来定义数据导入的骨架，而将具体的数据源和操作留给子类来实现。

```java
// 抽象类
abstract class DataImporter {
    // 模板方法，定义了数据导入的骨架
    public final void importData() {
        connect();
        extractData();
        transformData();
        validateData();
        loadIntoDatabase();
        disconnect();
    }

    // 连接到数据源
    protected void connect() {
        System.out.println("Connecting to data source...");
    }

    // 从数据源提取数据
    protected abstract void extractData();

    // 转换数据
    protected abstract void transformData();

    // 验证数据
    protected abstract void validateData();

    // 将数据加载到数据库
    protected void loadIntoDatabase() {
        System.out.println("Loading data into database...");
    }

    // 断开与数据源的连接
    protected void disconnect() {
        System.out.println("Disconnecting from data source...");
    }
}

// 具体类A：从文件导入数据
class FileImporter extends DataImporter {
    @Override
    protected void extractData() {
        System.out.println("Extracting data from file...");
    }

    @Override
    protected void transformData() {
        System.out.println("Transforming data...");
    }

    @Override
    protected void validateData() {
        System.out.println("Validating data...");
    }
}

// 具体类B：从数据库导入数据
class DatabaseImporter extends DataImporter {
    @Override
    protected void extractData() {
        System.out.println("Extracting data from database...");
    }

    @Override
    protected void transformData() {
        System.out.println("Transforming data...");
    }

    @Override
    protected void validateData() {
        System.out.println("Validating data...");
    }
}
```

客户端示例：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        DataImporter fileImporter = new FileImporter();
        fileImporter.importData();

        System.out.println();

        DataImporter databaseImporter = new DatabaseImporter();
        databaseImporter.importData();
    }
}
```

输出结果：

```text
Connecting to data source...
Extracting data from file...
Transforming data...
Validating data...
Loading data into database...
Disconnecting from data source...

Connecting to data source...
Extracting data from database...
Transforming data...
Validating data...
Loading data into database...
Disconnecting from data source...
```

在这个例子中，我们有一个抽象类`DataImporter`，它定义了数据导入的模板方法`importData()`
。抽象类还定义了一些具体方法，如`connect()`
和`disconnect()`，它们在模板方法中被调用，但不能被子类重写。

我们创建了两个具体类`FileImporter`和`DatabaseImporter`，它们继承了抽象类`DataImporter`
并实现了其中的抽象方法。每个具体类根据自己的特点提供了从不同数据源中提取、转换和验证数据的具体实现。

在示例代码中，我们创建了一个`FileImporter`对象和一个`DatabaseImporter`对象，并分别调用它们的`importData()`
方法。这样，数据从不同的数据源中导入，并执行了一系列的操作，如数据清洗、转换、验证和加载到数据库等。

通过使用模板方法模式，我们可以定义一个数据导入操作的骨架，并将一些步骤的实现留给子类。这样，我们可以在父类中控制整个数据导入的流程，同时允许子类根据具体的数据源和操作需求定制自己的实现。这种模式使得数据导入操作变得更加灵活和可扩展。