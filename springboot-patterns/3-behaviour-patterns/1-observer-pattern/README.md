# 1-observer-pattern

**说明**

观察者模式（Observer Pattern）：观察者模式是一种行为型设计模式，它定义了一种一对多的依赖关系，使得当一个对象的状态发生变化时，所有依赖于它的对象都会得到通知并自动更新。

在观察者模式中，存在两类角色：

1. Subject（主题）：也称为被观察者或可观察对象，它是具有状态的对象。主题维护一个观察者列表，可以注册、取消注册和通知观察者。当主题的状态发生变化时，会通知所有注册的观察者。
2. Observer（观察者）：观察者是依赖于主题的对象，它定义了一个更新接口，用于接收主题的通知。当主题的状态发生变化时，观察者通过更新接口获取最新的状态，并进行相应的处理。

观察者模式的实现步骤如下：

1. 定义主题接口（Subject）：主题接口中包含注册、取消注册和通知观察者的方法。
2. 定义观察者接口（Observer）：观察者接口中包含更新方法，用于接收主题的通知并进行相应的处理。
3. 实现具体主题类（ConcreteSubject）：具体主题类继承自主题接口，实现注册、取消注册和通知观察者的方法。在状态发生变化时，调用通知方法通知所有注册的观察者。
4. 实现具体观察者类（ConcreteObserver）：具体观察者类实现观察者接口的更新方法，以定义自己接收到主题通知后的处理逻辑。

通过观察者模式，主题和观察者之间的耦合度较低，主题对象可以动态地添加或移除观察者，而观察者对象也可以根据需要订阅或取消订阅主题，从而实现了松耦合的设计。
这样的设计使得主题和观察者之间的关系更加灵活，同时也提高了系统的可维护性和扩展性。

总结来说，观察者模式通过定义了主题和观察者之间的依赖关系，使得多个对象之间能够实现松耦合的通信。
这种模式适用于当一个对象的改变需要影响其他多个对象，并且对象之间的关系需要动态管理的场景。

## 观察者模式的实例

### 主题的状态发生变化时，观察者会收到通知并更新

以下是一个简单的Java代码示例，展示了观察者模式的应用。

> 假设我们有一个名为Subject的主题类和两个观察者类ObserverA和ObserverB，当主题的状态发生变化时，观察者会收到通知并更新。

```java
import java.util.ArrayList;
import java.util.List;

// 主题接口
interface Subject {
    void registerObserver(Observer observer);

    void unregisterObserver(Observer observer);

    void notifyObservers();
}

// 具体主题类
class ConcreteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}

// 观察者接口
interface Observer {
    void update(Subject subject);
}

// 具体观察者类A
class ObserverA implements Observer {
    @Override
    public void update(Subject subject) {
        ConcreteSubject concreteSubject = (ConcreteSubject) subject;
        System.out.println("Observer A: Received update. New state: " + concreteSubject.getState());
    }
}

// 具体观察者类B
class ObserverB implements Observer {
    @Override
    public void update(Subject subject) {
        ConcreteSubject concreteSubject = (ConcreteSubject) subject;
        System.out.println("Observer B: Received update. New state: " + concreteSubject.getState());
    }
}
```

客户端示例：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        ObserverA observerA = new ObserverA();
        ObserverB observerB = new ObserverB();

        subject.registerObserver(observerA);
        subject.registerObserver(observerB);

        subject.setState(5);
        subject.setState(10);

        subject.unregisterObserver(observerA);

        subject.setState(15);
    }
}
```

输出结果：

```text
Observer A: Received update. New state: 5
Observer B: Received update. New state: 5
Observer A: Received update. New state: 10
Observer B: Received update. New state: 10
Observer B: Received update. New state: 15
```

在上述示例中，`ConcreteSubjec`实现了`Subject`接口，并包含一个状态`state`。当`state`发生变化时，调用`notifyObservers()`
方法通知所有观察者。`ObserverA`和`ObserverB`实现了`Observer`接口，它们在构造函数中注册到主题对象上，并实现了`update()`
方法，在收到通知时更新状态。

在示例的`main()`方法中，我们创建了一个具体主题对象`subject`，并创建了两个具体观察者对象`observerA和observerB`
，然后通过改变主题对象的状态来触发观察者的更新。你可以运行这段代码，看到观察者收到通知并输出新的状态。

这个示例展示了观察者模式的基本结构和工作原理。通过注册和通知的方式，主题和观察者之间实现了解耦，使得主题和观察者能够灵活地交互和更新。

### 一个新闻发布系统

以下是一个更复杂的观察者模式应用场景的例子：一个新闻发布系统。

> 在这个场景中，我们有一个新闻发布者（Subject）和多个订阅者（Observers）。新闻发布者负责发布新闻并通知订阅者，而订阅者可以选择订阅自己感兴趣的新闻类别，并在有相关新闻发布时接收通知。

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 主题接口
interface Subject {
    void registerObserver(String category, Observer observer);

    void unregisterObserver(String category, Observer observer);

    void notifyObservers(String category, String news);
}

// 具体主题类
class NewsPublisher implements Subject {
    private Map<String, List<Observer>> observersMap = new HashMap<>();

    public void publishNews(String category, String news) {
        System.out.println("Publishing news: " + news);
        notifyObservers(category, news);
    }

    @Override
    public void registerObserver(String category, Observer observer) {
        List<Observer> observers = observersMap.getOrDefault(category, new ArrayList<>());
        observers.add(observer);
        observersMap.put(category, observers);
    }

    @Override
    public void unregisterObserver(String category, Observer observer) {
        List<Observer> observers = observersMap.getOrDefault(category, new ArrayList<>());
        observers.remove(observer);
        observersMap.put(category, observers);
    }

    @Override
    public void notifyObservers(String category, String news) {
        List<Observer> observers = observersMap.getOrDefault(category, new ArrayList<>());
        for (Observer observer : observers) {
            observer.update(category, news);
        }
    }
}

// 观察者接口
interface Observer {
    void update(String category, String news);
}

// 具体观察者类
class Subscriber implements Observer {
    private String name;

    public Subscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(String category, String news) {
        System.out.println(name + " received news in category " + category + ": " + news);
    }
}
```

客户端示例：

```java
// 示例代码
public class ObserverPatternExample {
    public static void main(String[] args) {
        NewsPublisher publisher = new NewsPublisher();

        Observer subscriber1 = new Subscriber("Subscriber1");
        Observer subscriber2 = new Subscriber("Subscriber2");
        Observer subscriber3 = new Subscriber("Subscriber3");

        publisher.registerObserver("Sports", subscriber1);
        publisher.registerObserver("Politics", subscriber2);
        publisher.registerObserver("Sports", subscriber3);

        publisher.publishNews("Sports", "New record set in the Olympics");
        publisher.publishNews("Politics", "Election results announced");

        publisher.unregisterObserver("Sports", subscriber3);

        publisher.publishNews("Sports", "Football league match canceled");
    }
}
```

输出结果：

```text
Publishing news: New record set in the Olympics
Subscriber1 received news in category Sports: New record set in the Olympics
Subscriber3 received news in category Sports: New record set in the Olympics
Publishing news: Election results announced
Subscriber2 received news in category Politics: Election results announced
Publishing news: Football league match canceled
Subscriber1 received news in category Sports: Football league match canceled
```

在上述示例中，我们有一个`NewsPublisher`作为具体主题类，它实现了`Subject`接口。`NewsPublisher`
维护了一个观察者映射表`observersMap`，其中键为新闻类别，值为观察者列表。`NewsPublisher`负责发布新闻并通知订阅了相关类别的观察者。

我们还有一个`Subscriber`作为具体观察者类，它实现了`Observer`接口。`Subscriber`有一个名字属性，并在收到新闻通知时打印相关信息。

在示例的`main()`方法中，我们创建了一个`NewsPublisher`对象和多个`Subscriber`
对象。然后，我们注册了一些观察者并发布了一些新闻。可以看到，只有订阅了相关类别的观察者才会收到相应的新闻通知。

这个例子展示了观察者模式在新闻发布系统中的应用。通过观察者模式，新闻发布者能够动态地管理订阅者，根据不同的新闻类别通知对应的观察者，实现了新闻的定向发布和订阅。
