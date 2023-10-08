# 4-builder-pattern

**说明**

建造者模式（Builder
Pattern）：建造者模式是一种创建型设计模式，它提供了一种`创建复杂对象`的方式，将对象的构建过程与表示分离。建造者模式的主要作用是通过定义一个独立的建造者类来封装对象的构建过程，使得同样的构建过程可以创建不同的表示。

## 建造者模式的一些优势和作用

以下是建造者模式的一些优势和作用：

* 封装复杂对象的构建过程：建造者模式将复杂对象的构建过程封装在建造者类中，客户端只需要与建造者进行交互，无需关注对象的具体构建细节。这样可以降低客户端与具体对象之间的耦合度，并提供了一种高层次的抽象。
* 支持创建不同表示的对象：建造者模式允许同样的构建过程可以创建不同的表示。通过定义不同的具体建造者，可以构建出具有不同属性和配置的对象。这种灵活性使得建造者模式特别适用于构建具有一定复杂性和变化性的对象。
* 可以逐步构建对象：建造者模式可以逐步构建对象。通过向建造者逐步添加不同的部件或配置，最终构建出完整的对象。这种逐步构建的方式可以灵活地控制对象的创建过程，并且可以在任意时刻获取部分构建的对象。
* 隔离构建和表示：建造者模式将对象的构建过程与表示分离开来。这样，同样的构建过程可以创建不同的表示，而不影响客户端对于对象的使用。通过隔离构建和表示，可以简化对象的创建和使用，提高代码的可维护性和可读性。
* 提供更好的对象控制：建造者模式提供了更好的对象控制能力。通过建造者类，可以对对象的构建过程进行精细的控制，可以根据需要设置对象的属性和配置。这种灵活性使得建造者模式在创建复杂对象时非常有用，可以满足特定的需求和条件。

总而言之，建造者模式通过`封装复杂对象`的构建过程，支持创建不同表示的对象，可以逐步构建对象，隔离构建和表示，并提供更好的对象控制能力。它在需要构建复杂对象且对象的属性和配置有变化的情况下特别有用，可以提供一种灵活的方式来创建和组合对象，并且提高代码的可维护性和可读性。

## 建造者模式的实际例子

### 建造者模式可以被用于创建玩家角色对象

> 当我们考虑一个例子时，假设我们正在开发一个游戏，并且需要创建玩家角色。玩家角色拥有多个属性，例如角色名称、等级、血量、魔法值等。同时，玩家角色还具有多个可选的装备，例如武器、护甲和饰品，每个装备都有不同的属性和效果。

在这种情况下，建造者模式可以被用于创建玩家角色对象。下面是一个简化的示例代码：

```java
// 产品类 - 玩家角色
class PlayerCharacter {
    private String name;
    private int level;
    private int health;
    private int mana;
    // ...其他属性和方法

    public PlayerCharacter(String name, int level, int health, int mana) {
        this.name = name;
        this.level = level;
        this.health = health;
        this.mana = mana;
    }

    // ...其他属性的设置方法
}

// 抽象建造者
interface PlayerBuilder {
    void setName(String name);

    void setLevel(int level);

    void setHealth(int health);

    void setMana(int mana);

    void equipWeapon(String weapon);

    void equipArmor(String armor);

    void equipAccessory(String accessory);

    PlayerCharacter build();
}

// 具体建造者
class WarriorBuilder implements PlayerBuilder {
    private PlayerCharacter character;

    public WarriorBuilder() {
        character = new PlayerCharacter("Warrior", 1, 100, 50);
    }

    @Override
    public void setName(String name) {
        character.setName(name);
    }

    @Override
    public void setLevel(int level) {
        character.setLevel(level);
    }

    @Override
    public void setHealth(int health) {
        character.setHealth(health);
    }

    @Override
    public void setMana(int mana) {
        character.setMana(mana);
    }

    @Override
    public void equipWeapon(String weapon) {
        // 设置武器属性和效果
    }

    @Override
    public void equipArmor(String armor) {
        // 设置护甲属性和效果
    }

    @Override
    public void equipAccessory(String accessory) {
        // 设置饰品属性和效果
    }

    @Override
    public PlayerCharacter build() {
        return character;
    }
}

// 指导者
class PlayerDirector {
    private PlayerBuilder builder;

    public PlayerDirector(PlayerBuilder builder) {
        this.builder = builder;
    }

    public PlayerCharacter createPlayer() {
        builder.setName("John");
        builder.setLevel(10);
        builder.setHealth(200);
        builder.setMana(100);
        builder.equipWeapon("Sword");
        builder.equipArmor("Plate Mail");
        builder.equipAccessory("Ring of Strength");
        return builder.build();
    }
}
```

客户端代码:

```java
// 客户端代码
public class Main {
    public static void main(String[] args) {
        PlayerBuilder warriorBuilder = new WarriorBuilder();
        PlayerDirector director = new PlayerDirector(warriorBuilder);
        PlayerCharacter warrior = director.createPlayer();

        // 使用创建的玩家角色
        System.out.println(warrior.getName());
        System.out.println(warrior.getLevel());
        System.out.println(warrior.getHealth());
        System.out.println(warrior.getMana());
        // ...其他操作
    }
}
```

在上述示例中，我们首先定义了产品类`PlayerCharacter`
，表示玩家角色，具有属性如名称、等级、血量和魔法值等。然后，我们定义了抽象建造者`PlayerBuilder`
，其中包含了设置角色属性和装备的方法，以及构建角色对象的方法`build()`。接着，我们实现了具体的建造者`WarriorBuilder`
，用于创建战士角色，其中包含了特定的装备设置方法。最后，我们定义了指导者`PlayerDirector`，通过使用特定的建造者对象，按照一定的顺序和规则构建玩家角色对象。

在客户端代码中，我们创建了一个指导者对象，并将特定的建造者对象传递给指导者。然后，通过指导者调用相应的方法来创建玩家角色对象。最终，我们可以使用创建的玩家角色对象进行后续操作。

使用建造者模式，我们可以使用建造者模式，我们可以灵活地创建具有不同属性和装备的玩家角色对象。通过指导者和具体建造者的组合，我们可以封装复杂对象的构建过程，使得客户端代码与具体构建过程解耦，同时提供了更好的对象控制和灵活性。对于游戏开发中的复杂对象创建场景，建造者模式是一个有效的设计模式选择。

### 建造者模式可以被用于创建订单对象

> 当我们考虑一个实际的Java例子时，假设我们正在开发一个订单系统，并且需要创建订单对象。订单对象包含多个属性，例如订单编号、客户信息、商品列表等。同时，订单对象可能具有一些可选的属性，例如配送地址、优惠券等。

在这种情况下，建造者模式可以被用于创建订单对象。下面是一个简化的示例代码：

```java
// 产品类 - 订单
class Order {
    private String orderNumber;
    private String customerName;
    private List<String> items;
    private String shippingAddress;
    private String couponCode;
    // ...其他属性和方法

    public Order(String orderNumber, String customerName, List<String> items) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.items = items;
    }

    // ...其他属性的设置方法
}

// 抽象建造者
interface OrderBuilder {
    void setOrderNumber(String orderNumber);

    void setCustomerName(String customerName);

    void setItems(List<String> items);

    void setShippingAddress(String shippingAddress);

    void setCouponCode(String couponCode);

    Order build();
}

// 具体建造者
class StandardOrderBuilder implements OrderBuilder {
    private Order order;

    public StandardOrderBuilder() {
        order = new Order(null, null, null);
    }

    @Override
    public void setOrderNumber(String orderNumber) {
        order.setOrderNumber(orderNumber);
    }

    @Override
    public void setCustomerName(String customerName) {
        order.setCustomerName(customerName);
    }

    @Override
    public void setItems(List<String> items) {
        order.setItems(items);
    }

    @Override
    public void setShippingAddress(String shippingAddress) {
        order.setShippingAddress(shippingAddress);
    }

    @Override
    public void setCouponCode(String couponCode) {
        order.setCouponCode(couponCode);
    }

    @Override
    public Order build() {
        return order;
    }
}

// 指导者
class OrderDirector {
    private OrderBuilder builder;

    public OrderDirector(OrderBuilder builder) {
        this.builder = builder;
    }

    public Order createOrder(String orderNumber, String customerName, List<String> items) {
        builder.setOrderNumber(orderNumber);
        builder.setCustomerName(customerName);
        builder.setItems(items);
        return builder.build();
    }
}
```

客户端代码:

```java
// 客户端代码
public class Main {
    public static void main(String[] args) {
        OrderBuilder standardOrderBuilder = new StandardOrderBuilder();
        OrderDirector director = new OrderDirector(standardOrderBuilder);

        List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");

        Order standardOrder = director.createOrder("12345", "John Doe", items);

        // 使用创建的订单对象
        System.out.println(standardOrder.getOrderNumber());
        System.out.println(standardOrder.getCustomerName());
        System.out.println(standardOrder.getItems());
        // ...其他操作
    }
}
```

在上述示例中，我们首先定义了产品类`Order`
，表示订单，具有属性如订单编号、客户名称、商品列表等。然后，我们定义了抽象建造者`OrderBuilder`
，其中包含了设置订单属性的方法，以及构建订单对象的方法`build()`。接着，我们实现了具体的建造者`StandardOrderBuilder`
，用于创建标准订单，其中包含了特定的属性设置方法。最后，我们定义了指导者`OrderDirector`，通过使用特定的建造者对象，按照一定的顺序和规则构建订单对象。

在客户端代码中，我们创建了一个指导者对象，并将特定的建造者对象传递给指导者。然后，通过指导者调用相应的方法来创建订单对象。最终，我们可以使用创建的订单对象进行后续操作。

使用建造者模式，我们可以灵活地创建具有不同属性和可选项的订单对象。通过指导者和具体建造者的组合，我们可以封装复杂对象的构建过程，使得客户端代码与具体构建过程解耦，同时提供了更好的对象控制和灵活性。对于订单系统中的订单创建场景，建造者模式是一个有效的设计模式选择。

