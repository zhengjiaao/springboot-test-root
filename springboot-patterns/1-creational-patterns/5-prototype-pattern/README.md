# 5-prototype-pattern

**说明**

原型模式（Prototype Pattern）：原型模式是一种创建型设计模式，它通过复制（克隆）现有对象来创建新对象，而不是通过实例化类来创建。这种模式的核心思想是通过复制现有对象的状态来创建新对象，从而避免了昂贵的对象创建过程。

在原型模式中，原型对象是被克隆的对象，它定义了一个克隆方法（通常是实现了Cloneable接口的clone()
方法），用于创建对象的副本。新对象可以根据原型对象的状态进行初始化，然后再根据需要进行进一步的修改。

原型模式的实现方式可以分为两种：

* 浅拷贝（Shallow Copy）：通过复制对象本身和其基本属性的引用，新对象和原型对象共享相同的引用。这意味着如果原型对象的某些属性是引用类型，那么新对象和原型对象将共享相同的引用，对其中一个对象的修改会影响到另一个对象。
* 深拷贝（Deep Copy）：通过复制对象本身和其所有引用对象，新对象和原型对象拥有相互独立的引用。这意味着新对象和原型对象之间的修改互不影响。

## 原型模式-浅拷贝 示例

浅拷贝（Shallow Copy）：
浅拷贝是指创建一个新对象，新对象的属性值与原对象相同，但对于引用类型的属性，新对象与原对象共享相同的引用。在Java中，实现浅拷贝可以通过Object类的clone()
方法来实现，前提是被克隆的类需要实现Cloneable接口。

```java
// 原型接口
interface Prototype extends Cloneable {
    Prototype clone();
}

// 具体原型类
class ConcretePrototype implements Prototype {
    private int value;

    public ConcretePrototype(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Prototype clone() {
        try {
            return (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            // 处理异常
            return null;
        }
    }
}
```

客户端代码:

```java
// 客户端代码
public class Main {
    public static void main(String[] args) {
        ConcretePrototype prototype = new ConcretePrototype(10);
        ConcretePrototype clone = (ConcretePrototype) prototype.clone();

        // 修改克隆对象的值
        clone.setValue(20);

        System.out.println("原型对象的值：" + prototype.getValue());
        System.out.println("克隆对象的值：" + clone.getValue());
    }
}
```

在上述示例中，`Prototype`类实现了`Cloneable`接口，并重写了`clone()`方法。在`clone()`方法中，调用了`super.clone()`方法来实现浅拷贝。注意，如果引用类型的属性需要进行深拷贝，还需要在`clone()`方法中进行相应的处理。

在上述示例中，我们定义了一个原型接口`Prototype`，其中包含了一个`clone()`
方法用于创建对象的副本。然后，我们实现了具体的原型类`ConcretePrototype`，它实现了`Prototype`接口，并重写了`clone()`
方法来创建对象的副本。在客户端代码中，我们创建了一个原型对象`prototype`，然后通过调用其`clone()`
方法来创建一个克隆对象`clone`。接着，我们修改了克隆对象的值，并输出原型对象和克隆对象的值。

通过原型模式，我们可以避免重复的对象创建过程，提高对象的创建效率。同时，原型模式还可以在运行时动态地添加和删除对象，并根据对象的状态进行定制化的克隆。原型模式适用于需要创建大量相似对象或者对象的创建过程比较耗时的场景。

## 原型模式-深拷贝 示例

深拷贝（Deep Copy）：
深拷贝是指创建一个新对象，新对象与原对象完全独立，包括引用类型的属性也是独立的。在Java中，实现深拷贝可以通过序列化和反序列化来实现。

```java
import java.io.*;

class ConcretePrototype implements Serializable {
    private int value;
    private ReferenceType reference;

    public ConcretePrototype(int value, ReferenceType reference) {
        this.value = value;
        this.reference = reference;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setReference(ReferenceType reference) {
        this.reference = reference;
    }

    public ReferenceType getReference() {
        return reference;
    }

    public ConcretePrototype deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (ConcretePrototype) ois.readObject();
    }
}
```

在上述示例中，`ConcretePrototype`类实现了`Serializable`接口，通过序列化和反序列化来实现深拷贝。在`deepClone()`
方法中，将对象写入字节数组输出流`ByteArrayOutputStream`，然后再通过字节数组输入流`ByteArrayInputStream`进行读取，从而实现对象的深拷贝。

需要注意的是，被拷贝的引用类型属性也必须实现`Serializable`接口，否则在进行深拷贝时可能会抛出`NotSerializableException`异常。

使用原型模式时，可以根据需要选择浅拷贝或深拷贝，具体取决于对象的属性类型和拷贝需求。