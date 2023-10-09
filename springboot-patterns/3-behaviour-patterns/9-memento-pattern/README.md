# 9-memento-pattern

**说明**

备忘录模式（Memento Pattern）：备忘录模式是一种行为设计模式，它允许捕获对象的内部状态并在需要时恢复该状态。
该模式通过将对象的状态封装在称为备忘录的对象中，从而实现状态的保存和恢复，同时保证了对象的封装性。

在备忘录模式中，有三个主要的角色：

1. 发起人（Originator）：它是要保存状态的对象。它可以创建一个备忘录对象，用于保存当前状态，并可以从备忘录对象中恢复以前的状态。
2. 备忘录（Memento）：它是用于存储发起人对象状态的对象。备忘录对象提供了发起人对象的状态的快照，并且只有发起人对象可以访问备忘录的内容。
3. 管理者（Caretaker）：它负责保存和恢复备忘录对象。管理者对象可以在需要时请求发起人对象保存状态到备忘录对象，或者从备忘录对象中恢复发起人对象的状态。

备忘录模式的操作流程如下：

1. 发起人对象创建一个备忘录对象，并将当前的状态保存到备忘录对象中。
2. 发起人对象可以根据需要修改其状态。
3. 如果需要恢复之前的状态，发起人对象可以从备忘录对象中获取之前保存的状态，并将其恢复。

## 备忘录模式优点、缺点和应用场景

## 备忘录模式的实例

### 文本编辑器

下面是一个简单的备忘录模式示例.

> 假设我们有一个文本编辑器，用户可以在编辑器中输入文本，并且可以通过撤销操作恢复之前的文本状态。

首先，我们定义发起人（Originator）类，表示文本编辑器。

```java
public class TextEditor {
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TextEditorMemento save() {
        return new TextEditorMemento(text);
    }

    public void restore(TextEditorMemento memento) {
        text = memento.getState();
    }
}
```

然后，我们定义备忘录（Memento）类，表示文本的状态快照。

```java
public class TextEditorMemento {
    private final String state;

    public TextEditorMemento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
```

接下来，我们定义管理者（Caretaker）类，用于保存和恢复备忘录对象。

```java
public class UndoManager {
    private TextEditorMemento memento;

    public void saveMemento(TextEditorMemento memento) {
        this.memento = memento;
    }

    public TextEditorMemento retrieveMemento() {
        return memento;
    }
}
```

最后，我们可以在客户端代码中使用备忘录模式。
客户端示例：

```java
public class Client {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        UndoManager undoManager = new UndoManager();

        // 编辑文本
        editor.setText("Hello, World!");

        // 保存当前状态
        undoManager.saveMemento(editor.save());

        // 修改文本
        editor.setText("Goodbye!");

        // 输出当前文本
        System.out.println(editor.getText()); // 输出: Goodbye!

        // 恢复到之前的状态
        editor.restore(undoManager.retrieveMemento());

        // 输出恢复后的文本
        System.out.println(editor.getText()); // 输出: Hello, World!
    }
}
```

输出结果：

```text

```

在上述示例中，我们通过备忘录模式实现了文本编辑器的撤销功能。
我们定义了TextEditor类作为发起人，用于保存和恢复文本状态。
TextEditorMemento类作为备忘录，用于存储文本的状态快照。
UndoManager类作为管理者，负责保存和恢复备忘录对象。
在客户端代码中，我们首先设置文本的初始内容并保存当前状态。
然后，我们修改文本内容并输出修改后的文本。最后，我们从管理者中恢复之前保存的状态，并输出恢复后的文本。

通过使用备忘录模式，我们可以轻松地保存和恢复对象的状态，实现撤销和恢复功能。
该模式将状态的保存和恢复封装在备忘录对象中，保证了对象的封装性，同时备忘录模式（Memento
Pattern）是一种设计模式，用于在不破坏封装性的前提下，捕获和恢复对象的内部状态。
该模式将对象的状态保存在备忘录对象中，并提供了一种方式来恢复对象到之前的状态。

### 在线文档编辑器

> 一个在线文档编辑器，其中用户可以创建、编辑和删除文档，并且可以使用撤销和重做功能。

在这个实例中，备忘录模式可以用于保存和恢复文档的状态。我们有以下角色：

1. 发起人（Originator）：表示文档编辑器。它包含一个文档对象，并负责创建备忘录对象、从备忘录对象中恢复状态以及修改文档对象。
2. 备忘录（Memento）：表示文档的状态快照。它包含一个文档对象的副本。
3. 管理者（Caretaker）：负责保存和恢复备忘录对象。它维护一个备忘录对象的堆栈，用于跟踪多个状态的历史记录。

下面是一个简化的示例代码：

```java
import java.util.Stack;

// 发起人（Originator）类
class DocumentEditor {
    private Document document;

    public void createDocument() {
        document = new Document();
    }

    public void editDocument(String content) {
        document.setContent(content);
    }

    public DocumentMemento save() {
        return new DocumentMemento(document.copy());
    }

    public void restore(DocumentMemento memento) {
        document = memento.getDocument().copy();
    }

    public void printDocument() {
        System.out.println(document.getContent());
    }
}

// 备忘录（Memento）类
class DocumentMemento {
    private Document document;

    public DocumentMemento(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}

// 管理者（Caretaker）类
class DocumentHistory {
    private Stack<DocumentMemento> stack = new Stack<>();

    public void push(DocumentMemento memento) {
        stack.push(memento);
    }

    public DocumentMemento pop() {
        return stack.pop();
    }
}

// 文档类
class Document {
    private String content;

    public Document copy() {
        Document copy = new Document();
        copy.setContent(content);
        return copy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```

客户端代码示例：

```java

// 客户端代码
public class Client {
    public static void main(String[] args) {
        DocumentEditor editor = new DocumentEditor();
        DocumentHistory history = new DocumentHistory();

        // 创建文档并编辑内容
        editor.createDocument();
        editor.editDocument("Hello, world!");

        // 保存当前状态到备忘录
        history.push(editor.save());

        // 修改文档内容
        editor.editDocument("Hello, GPT!");

        // 输出当前文档内容
        editor.printDocument();

        // 恢复到之前的状态
        editor.restore(history.pop());

        // 输出恢复后的文档内容
        editor.printDocument();
    }
}
```

输出结果：

```text
Hello, GPT!
Hello, world!
```

在这个示例中，我们有一个文档编辑器（DocumentEditor）类，它包含了一个文档对象。
用户可以通过创建文档、编辑内容来修改文档对象。编辑器类实现了保存和恢复状态的方法，其中保存方法返回一个备忘录对象（DocumentMemento），并将其压入历史记录（DocumentHistory）对象的堆栈中。
恢复方法从历史记录堆栈中弹出备忘录对象，并将编辑器的状态恢复为备忘录对象中保存的状态。

文档类（Document）是一个简单的示例类，表示文档的内容。在客户端代码中，我们创建一个文档编辑器对象和一个历史记录对象。
然后，我们通过创建文档和编辑内容来修改文档的状态，并使用保存和恢复方法来保存和恢复状态。最后，我们输出文档的当前状态。










