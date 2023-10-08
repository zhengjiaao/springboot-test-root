# 5-composite-pattern

**说明**

组合模式（Composite Pattern）：组合模式是一种结构型设计模式，允许将对象组合成树形结构以表示部分-整体的层次结构。组合模式使得客户端可以统一处理单个对象和组合对象，无需区分它们的差异，从而简化了客户端的代码。

在组合模式中，有两种核心角色：

1. 组件（Component）：定义了组合对象和叶子对象的共同接口，可以定义一些默认的行为。该接口可以用于管理子组件，并提供了增加、删除和获取子组件的方法。
2. 叶子（Leaf）：表示组合对象中的叶子节点，叶子节点没有子节点。
3. 组合（Composite）：表示组合对象中的组合节点，组合节点可以包含子节点，可以对子节点进行管理。

## 组合模式的优缺点

组合模式具有以下优点：

1. 简化客户端代码：组合模式使得客户端可以统一处理单个对象和组合对象，无需区分它们的差异。这样可以简化客户端的代码，提高代码的可读性和可维护性。
2. 灵活性和可扩展性：组合模式通过将对象组合成树形结构，使得可以动态地添加、删除和替换对象，从而提供了灵活性和可扩展性。可以方便地增加新的组件，而无需修改现有代码。
3. 统一操作接口：组合模式通过共同的接口定义，使得对单个对象和组合对象的操作具有一致性。这样可以使得操作代码更加简洁和易于维护。
4. 递归遍历：组合模式可以方便地使用递归算法来遍历整个对象树。这样可以简化对复杂层次结构的操作和处理。

组合模式的缺点：

1. 设计复杂性增加：由于组合模式涉及到树形结构的管理和操作，因此其设计较为复杂。需要仔细考虑组件和组合对象之间的关系，以及如何进行递归遍历和操作。
2. 不适合所有情况：组合模式适用于具有部分-整体层次结构的场景，但并非所有情况都适合使用组合模式。如果对象结构相对简单，且不需要递归遍历和操作，使用组合模式可能会增加不必要的复杂性。
3. 可能导致性能问题：由于组合模式使用了递归算法来遍历整个对象树，如果树的层次很深或者树中的节点数量很大，可能会导致性能问题。递归操作可能会导致多次重复的遍历和操作，影响性能。
4. 限制特定操作：组合模式对于组合对象和叶子对象的操作是统一的，这可能会限制一些特定操作。例如，某些特定操作只能在叶子对象上执行，但组合模式要求在组合对象上也要实现这些操作。

组合模式适用于以下场景：

1. 需要表示部分-整体层次结构的场景：当对象具有树形结构，并且希望以统一的方式处理单个对象和组合对象时，可以使用组合模式。
2. 希望客户端忽略对象的具体类型时：组合模式使得客户端可以一致地操作单个对象和组合对象，无需关心它们的具体类型。
3. 需要灵活地组织和管理对象的场景：组合模式提供了一种灵活的方式来组织和管理对象，可以动态地增加、删除和替换对象，从而提供了较大的灵活性和可扩展性。
4. 需要递归遍历对象的场景：组合模式可以方便地使用递归算法来遍历整个对象树，从而可以对复杂层次结构进行操作和处理。

## 组合模式的应用场景

## 组合模式示例

### 构建一个文件系统的层次结构

下面是一个简单的组合模式示例，假设我们要构建一个文件系统的层次结构：

```java
// 组件（Component）
public interface FileSystemComponent {
    void display();
}

// 叶子（Leaf）
public class File implements FileSystemComponent {
    private String name;

    public File(String name) {
        this.name = name;
    }

    public void display() {
        System.out.println("File: " + name);
    }
}

// 组合（Composite）
public class Directory implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> children;

    public Directory(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    public void display() {
        System.out.println("Directory: " + name);
        for (FileSystemComponent component : children) {
            component.display();
        }
    }
}
```

使用组合模式，我们可以创建文件系统的层次结构，包括文件和目录：

```java
public class Main {
    public static void main(String[] args) {
        // 创建文件
        FileSystemComponent file1 = new File("file1.txt");
        FileSystemComponent file2 = new File("file2.txt");
        FileSystemComponent file3 = new File("file3.txt");

        // 创建目录
        Directory dir1 = new Directory("dir1");
        Directory dir2 = new Directory("dir2");
        Directory dir3 = new Directory("dir3");

        // 组合文件和目录
        dir1.add(file1);
        dir1.add(dir2);
        dir2.add(file2);
        dir2.add(dir3);
        dir3.add(file3);

        // 显示文件系统层次结构
        dir1.display();
    }
}
```

输出结果：

```text
Directory: dir1
File: file1.txt
Directory: dir2
File: file2.txt
Directory: dir3
File: file3.txt
```

在这个示例中，组合模式允许我们使用统一的方式处理文件和目录，无需关心它们的具体类型。
通过将文件和目录视为统一的组件，我们可以轻松地构建和操作文件系统的层次结构。
这种设计模式提供了一种清晰、灵活的方式来处理部分-整体的关系。

### 建立一个组织架构的层次结构

当然！以下是一个更复杂的组合模式示例：组织架构。

> 假设我们要建立一个组织架构的层次结构，包括公司、部门和员工。一个公司可以包含多个部门，而每个部门可以包含多个员工。我们可以使用组合模式来管理这种层次结构。

首先，我们定义组件接口 Component：

```java
public interface Component {
    void display();
}
```

接下来，我们定义叶子节点 Employee：

```java
public class Employee implements Component {
    private String name;
    private String position;

    public Employee(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void add(Component component) {
    }

    public void remove(Component component) {
    }

    public void display() {
        System.out.println("Employee: " + name + ", Position: " + position);
    }
}
```

然后，我们定义组合节点 Department：

```java
import java.util.ArrayList;
import java.util.List;

public class Department implements Component {
    private String name;
    private List<Component> children;

    public Department(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(Component component) {
        children.add(component);
    }

    public void remove(Component component) {
        children.remove(component);
    }

    public void display() {
        System.out.println("Department: " + name);
        for (Component component : children) {
            component.display();
        }
    }
}
```

最后，我们定义组合节点 Company，它可以包含多个部门：

```java
import java.util.ArrayList;
import java.util.List;

public class Company implements Component {
    private String name;
    private List<Component> children;

    public Company(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(Component component) {
        children.add(component);
    }

    public void remove(Component component) {
        children.remove(component);
    }

    public void display() {
        System.out.println("Company: " + name);
        for (Component component : children) {
            component.display();
        }
    }
}
```

现在，我们可以使用组合模式构建组织架构的层次结构：

```java
public class Main {
    public static void main(String[] args) {
        // 创建员工
        Component employee1 = new Employee("John Doe", "Manager");
        Component employee2 = new Employee("Jane Smith", "Engineer");
        Component employee3 = new Employee("Bob Johnson", "Designer");

        // 创建部门
        Component department1 = new Department("Engineering");
        Component department2 = new Department("Design");

        // 创建公司
        Component company = new Company("ABC Company");

        // 组合部门和员工
        department1.add(employee1);
        department1.add(employee2);
        department2.add(employee3);
        company.add(department1);
        company.add(department2);

        // 显示组织架构
        company.display();
    }
}
```

输出结果：

```text
Company: ABC Company
Department: Engineering
Employee: John Doe, Position: Manager
Employee: Jane Smith, Position: Engineer
Department: Design
Employee: Bob Johnson, Position: Designer
```

在这个示例中，我们使用组合模式将公司、部门和员工组织成一个层次结构。
通过统一的 Component 接口，我们可以以一致的方式处理公司、部门和员工，无需关心它们的具体类型。
这种设计模式提供了一种灵活、可扩展的方式来管理组织架构，并且能够方便地进行操作和显示。




