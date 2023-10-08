# 2-decorator-pattern

**说明**

装饰者模式（Decorator Pattern）：装饰者模式是一种结构设计模式，它允许你动态地将新功能附加到对象上。它提供了一种灵活的替代继承的方式，以扩展对象的功能。

在装饰者模式中，存在一个核心组件（`Component`），它定义了基本的对象接口。然后，有一个抽象装饰者（`Decorator`
），它实现了核心组件的接口，并且内部持有一个核心组件的引用。抽象装饰者可以附加额外的行为或功能，同时调用核心组件的方法。具体装饰者（`Concrete Decorator`
）是抽象装饰者的具体实现，它可以通过继承抽象装饰者类来实现附加的功能。

## 装饰者模式的优缺点

装饰者模式具有以下优点：

1. 动态扩展功能：装饰者模式允许在不修改现有代码的情况下，动态地扩展对象的功能。通过使用不同的具体装饰者对象以各种组合方式进行装饰，可以在运行时动态地添加、移除或修改对象的行为，提供更大的灵活性。
2. 遵循开放封闭原则：装饰者模式支持开放封闭原则，即允许在不修改现有代码的情况下扩展功能。通过将核心组件和具体装饰者对象解耦，新增功能可以通过创建新的具体装饰者类来实现，而无需修改现有代码。
3. 继承和组合的灵活结合：装饰者模式将继承和组合结合在一起，通过继承核心组件和组合具体装饰者，可以灵活地扩展对象的功能。相比于使用继承来扩展功能，装饰者模式更加灵活，避免了类爆炸的问题。
4. 保持对象接口的一致性：装饰者模式在不改变核心组件的接口的情况下，扩展了对象的功能。这意味着装饰者可以透明地使用核心组件，并且对外部调用者来说，无论是使用核心组件还是装饰者，其接口是一致的。

然而，装饰者模式也有一些缺点：

1. 复杂性增加：由于装饰者模式引入了许多具体装饰者类，因此代码结构可能变得更加复杂。需要注意装饰者的顺序和组合方式，以避免出现意外的行为。
2. 多层装饰的影响：当使用多个装饰者对核心组件进行嵌套装饰时，可能会导致装饰层级过深，增加代码的理解和维护的复杂性。
3. 装饰者与核心组件紧密耦合：具体装饰者与核心组件之间是紧密耦合的，特别是在具体装饰者中需要调用核心组件的方法时。这种紧密耦合可能会导致装饰者的复用性较低。

尽管有一些缺点，但装饰者模式在需要动态扩展对象功能并保持接口一致性的情况下，仍然是一个强大而灵活的设计模式。

## 装饰者模式实例

### 扩展咖啡的功能

以下是一个简单的装饰者模式的示例，假设我们有一个基本的咖啡对象，并希望能够动态地添加调料（例如牛奶、糖等）来扩展咖啡的功能：

```java
// 核心组件接口
public interface Coffee {
    String getDescription();

    double getCost();
}

// 基本咖啡类（核心组件的具体实现）
public class BasicCoffee implements Coffee {
    public String getDescription() {
        return "Basic Coffee";
    }

    public double getCost() {
        return 2.0;
    }
}

// 抽象装饰者
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        return coffee.getDescription();
    }

    public double getCost() {
        return coffee.getCost();
    }
}

// 具体装饰者：牛奶调料
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public double getCost() {
        return coffee.getCost() + 0.5;
    }
}

// 具体装饰者：糖调料
public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Sugar";
    }

    public double getCost() {
        return coffee.getCost() + 0.3;
    }
}
```

客户端代码：

```java
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 创建基本咖啡对象
        Coffee basicCoffee = new BasicCoffee();
        System.out.println(basicCoffee.getDescription() + " - $" + basicCoffee.getCost());

        // 使用装饰者动态添加调料
        Coffee milkCoffee = new MilkDecorator(basicCoffee);
        System.out.println(milkCoffee.getDescription() + " - $" + milkCoffee.getCost());

        Coffee sugarMilkCoffee = new SugarDecorator(milkCoffee);
        System.out.println(sugarMilkCoffee.getDescription() + " - $" + sugarMilkCoffee.getCost());
    }
}
```

在上述示例中，`Coffee`接口定义了咖啡对象的基本接口。`BasicCoffee`是核心组件的具体实现，它实现了`Coffee`接口。

`CoffeeDecorator`是抽象装饰者，它实现了`Coffee`接口，并持有一个`Coffee`对象的引用。具体装饰者`MilkDecorator`
和`SugarDecorator`分别继承了`CoffeeDecorator`，并在其方法中附加了额外的功能（添加调料）。

在客户端代码中，我们首先创建一个基本咖啡对象`BasicCoffee`
，然后使用装饰者模式动态地添加调料来扩展咖啡的功能。通过创建具体装饰者对象，并将核心组件对象传递给它们的构造函数，我们可以方便地组合多个装饰者对象，以实现不同的装饰组合。

通过装饰者模式，我们可以在运行时动态扩展对象的功能，而无需修改其原始代码。这种灵活性使得装饰者模式成为在不破坏现有代码的前提下，对现有对象进行功能扩展的有力工具。

### 更复杂的装饰者模式的示例

以下是一个更复杂的装饰者模式示例，假设我们有一个咖啡馆应用程序，需要实现不同种类的咖啡和调料的组合，并计算其总价：

```java
// 核心组件接口 - 咖啡
public interface Coffee {
    String getDescription();

    double getCost();
}

// 具体咖啡类 - 浓缩咖啡
public class Espresso implements Coffee {
    public String getDescription() {
        return "Espresso";
    }

    public double getCost() {
        return 1.99;
    }
}

// 具体咖啡类 - 卡布奇诺
public class Cappuccino implements Coffee {
    public String getDescription() {
        return "Cappuccino";
    }

    public double getCost() {
        return 2.99;
    }
}

// 抽象装饰者 - 调料
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        return coffee.getDescription();
    }

    public double getCost() {
        return coffee.getCost();
    }
}

// 具体装饰者 - 牛奶调料
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public double getCost() {
        return coffee.getCost() + 0.5;
    }
}

// 具体装饰者 - 摩卡调料
public class MochaDecorator extends CoffeeDecorator {
    public MochaDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Mocha";
    }

    public double getCost() {
        return coffee.getCost() + 0.75;
    }
}
```

客户端代码:

```java
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 制作一杯浓缩咖啡
        Coffee espresso = new Espresso();
        System.out.println(espresso.getDescription() + " - $" + espresso.getCost());

        // 添加牛奶
        Coffee mochaLatte = new MilkDecorator(new MochaDecorator(espresso));
        System.out.println(mochaLatte.getDescription() + " - $" + mochaLatte.getCost());

        // 制作一杯卡布奇诺
        Coffee cappuccino = new Cappuccino();
        System.out.println(cappuccino.getDescription() + " - $" + cappuccino.getCost());

        // 添加牛奶和摩卡
        Coffee mochaCappuccino = new MochaDecorator(new MilkDecorator(cappuccino));
        System.out.println(mochaCappuccino.getDescription() + " - $" + mochaCappuccino.getCost());
    }
}
```

在这个更复杂的示例中，我们有多个具体的咖啡类（`Espresso`和`Cappuccino`），它们实现了`Coffee`接口。

我们还有两个具体的装饰者类：`MilkDecorator`和`MochaDecorator`，它们分别继承了`CoffeeDecorator`
。这些具体装饰者类可以与具体的咖啡类组合，以实现不同的咖啡调料组合。

在客户端代码中，我们制作了不同种类的咖啡，并使用装饰者模式动态地添加调料。我们可以通过创建具体装饰者对象，并将核心组件对象传递给它们的构造函数，来组合多个装饰者对象。

通过装饰者模式，我们可以轻松地创建各种咖啡和调料的组合，并根据需要计算其总价。这种灵活性使得我们可以根据客户的需求动态地扩展和组合对象的功能。

### 使用装饰者模式来实现促销功能的动态添加实例

当涉及到动态添加功能或行为的情况时，装饰者模式非常有用。以下是一个实际应用场景的示例：

假设你正在开发一个电子商务平台，其中有多个商品类别（例如衣服、鞋子、配饰等），而且你希望能够动态地添加促销功能，如打折、满减等，而无需修改商品类别的原始代码。

以下是一个简化的示例：

```java
// 核心组件接口 - 商品
public interface Product {
    String getDescription();

    double getPrice();
}

// 具体商品类 - 衣服
public class Clothing implements Product {
    private String description;
    private double price;

    public Clothing(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}

// 抽象装饰者 - 促销
public abstract class PromotionDecorator implements Product {
    protected Product product;

    public PromotionDecorator(Product product) {
        this.product = product;
    }

    public String getDescription() {
        return product.getDescription();
    }

    public double getPrice() {
        return product.getPrice();
    }
}

// 具体装饰者 - 打折促销
public class DiscountPromotionDecorator extends PromotionDecorator {
    private double discount;

    public DiscountPromotionDecorator(Product product, double discount) {
        super(product);
        this.discount = discount;
    }

    public double getPrice() {
        double originalPrice = super.getPrice();
        return originalPrice - (originalPrice * discount);
    }
}

// 具体装饰者 - 满减促销
public class DiscountedPricePromotionDecorator extends PromotionDecorator {
    private double threshold;
    private double discountAmount;

    public DiscountedPricePromotionDecorator(Product product, double threshold, double discountAmount) {
        super(product);
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    public double getPrice() {
        double originalPrice = super.getPrice();
        if (originalPrice >= threshold) {
            return originalPrice - discountAmount;
        } else {
            return originalPrice;
        }
    }
}
```

客户端代码:

```java
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 创建一个衣服商品
        Product clothing = new Clothing("T-shirt", 20.0);
        System.out.println(clothing.getDescription() + " - $" + clothing.getPrice());

        // 添加打折促销
        Product discountedClothing = new DiscountPromotionDecorator(clothing, 0.2);
        System.out.println(discountedClothing.getDescription() + " - $" + discountedClothing.getPrice());

        // 添加满减促销
        Product discountedPriceClothing = new DiscountedPricePromotionDecorator(clothing, 30.0, 5.0);
        System.out.println(discountedPriceClothing.getDescription() + " - $" + discountedPriceClothing.getPrice());
    }
}
```

在上述示例中，`Product`接口定义了商品的基本接口。`Clothing`是商品的具体实现。

`PromotionDecorator`是抽象装饰者，它实现了`Product`接口，并持有一个`Product`
对象的引用。具体装饰者`DiscountPromotionDecorator`和`DiscountedPricePromotionDecorator`分别继承了`PromotionDecorator`
，并在其方法中实现了具体的促销功能。

在客户端代码中，我们首先创建一个衣服商品对象`Clothing`
，然后通过创建具体装饰者对象并将商品对象传递给它们的构造函数，动态地添加促销功能。通过不同的装饰者组合，我们可以方便地实现不同的促销策略，如打折、满减等。

使用装饰者模式，我们可以在不修改商品类别代码的情况下，动态地添加促销功能。这使得我们能够灵活地应对不同的促销需求，并且能够轻松地组合不同的促销方式，甚至可以在运行时动态地添加、移除或修改促销功能。
