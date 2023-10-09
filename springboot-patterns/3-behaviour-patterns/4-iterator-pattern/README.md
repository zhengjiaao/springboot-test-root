# 4-iterator-pattern

**说明**

迭代器模式（Iterator Pattern）：迭代器模式是一种行为设计模式，它提供了一种顺序访问集合对象元素的方法，而无需暴露集合对象的内部表示。
迭代器模式将遍历和集合分离，使得集合的结构和遍历算法能够独立地变化。

迭代器模式的主要参与者包括以下几个角色：

1. 迭代器（Iterator）：定义了访问和遍历元素的接口，通常包括方法如获取下一个元素、判断是否还有下一个元素等。
2. 具体迭代器（Concrete Iterator）：实现迭代器接口，负责实现具体的遍历逻辑。
3. 集合（Collection）：定义了创建迭代器的接口，通常包括方法如获取迭代器等。
4. 具体集合（Concrete Collection）：实现集合接口，负责创建具体迭代器对象。

## 迭代器模式的优点、缺点和应用场景

迭代器模式是一种行为设计模式，它提供了一种统一的方式来遍历集合对象中的元素，同时隐藏了集合的内部实现细节。

以下是迭代器模式的优点、缺点和应用场景：

优点：

1. 封装性：迭代器模式将遍历集合的责任从集合对象中分离出来，使得集合类可以专注于自身的核心功能，同时将遍历逻辑封装在迭代器中。
2. 统一的遍历接口：迭代器模式定义了统一的遍历接口，使得客户端代码可以以统一的方式访问不同类型的集合对象，无需关心集合的内部结构。
3. 支持多种遍历方式：迭代器模式可以为集合类提供多种遍历方式，例如正向遍历、反向遍历、跳跃式遍历等，而无需修改集合类的代码。
4. 迭代器与集合解耦：迭代器模式将集合类与迭代器对象解耦，使得集合类的变化不会影响到迭代器的实现，可以独立地修改和扩展两者。

缺点：

1. 额外的开销：使用迭代器模式会增加一定的额外开销，因为迭代器需要维护迭代的状态信息，可能会占用一些额外的内存和计算资源。
2. 适用性限制：迭代器模式适用于遍历集合对象，如果数据结构不是集合或者不需要遍历，使用迭代器模式可能会显得过于复杂和冗余。

应用场景：

1. 需要遍历不同类型集合的客户端代码，但又不希望依赖具体集合类的内部结构。
2. 需要在遍历过程中对集合进行修改时，为了避免遍历过程中的并发修改异常，可以使用迭代器模式。
3. 需要提供多种不同的遍历策略或迭代方式，使得客户端可以根据需求选择合适的遍历方式。
4. 在面向对象设计中，希望将集合类的遍历操作与集合类本身分离，以提高代码的可维护性和可扩展性。

总结起来，迭代器模式适用于需要统一遍历接口、隐藏集合内部结构、支持多种遍历方式以及降低集合类与遍历逻辑之间耦合度的场景。
在这些场景下，迭代器模式可以提供一种优雅且灵活的解决方案。

## 迭代器模式的实例

### 遍历一个自定义集合类中的元素

以下是一个简单的示例，演示如何使用迭代器模式来遍历一个自定义集合类中的元素：

```java
// 迭代器接口
interface Iterator {
    boolean hasNext();

    Object next();
}

// 自定义集合类
class CustomCollection {
    private String[] elements;
    private int size;

    public CustomCollection(int capacity) {
        elements = new String[capacity];
        size = 0;
    }

    public void add(String element) {
        if (size < elements.length) {
            elements[size] = element;
            size++;
        }
    }

    public Iterator createIterator() {
        return new CustomIterator();
    }

    // 具体迭代器类
    private class CustomIterator implements Iterator {
        private int position;

        public CustomIterator() {
            position = 0;
        }

        public boolean hasNext() {
            return position < size;
        }

        public Object next() {
            if (hasNext()) {
                String element = elements[position];
                position++;
                return element;
            }
            return null;
        }
    }
}
```

客户端示例代码：

```java

// 示例代码
public class Client {
    public static void main(String[] args) {
        CustomCollection collection = new CustomCollection(5);
        collection.add("Apple");
        collection.add("Banana");
        collection.add("Orange");

        Iterator iterator = collection.createIterator();

        while (iterator.hasNext()) {
            Object element = iterator.next();
            System.out.println(element);
        }
    }
}
```

输出结果：

```text
Apple
Banana
Orange
```

在这个示例中，我们首先定义了一个迭代器接口`Iterator`，其中包含`hasNext()`和`next()`方法，用于判断是否还有下一个元素，并返回下一个元素。

然后，我们创建了一个自定义集合类`CustomCollection`，该类具有一个内部类`CustomIterator`
作为具体的迭代器实现。自定义集合类中还有一个`add()`
方法，用于向集合中添加元素。

在示例代码中，我们创建了一个自定义集合对象`collection`，并添加了几个元素。然后，我们通过调用`createIterator()`方法创建了一个迭代器对象。

最后，我们使用迭代器的`hasNext()`和`next()`方法来遍历集合中的元素，并将它们打印出来。

这个示例展示了迭代器模式的基本用法。通过将迭代器与集合分离，我们可以在不暴露集合内部实现的情况下，通过迭代器遍历集合中的元素。

### 遍历一个电影列表的元素，并根据指定的条件进行过滤

以下是一个更复杂的示例，展示如何使用迭代器模式来遍历一个电影列表的元素，并根据指定的条件进行过滤：

```java
import java.util.ArrayList;
import java.util.List;

// 电影类
class Movie {
    private String title;
    private String genre;
    private int rating;

    public Movie(String title, String genre, int rating) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getRating() {
        return rating;
    }
}

// 迭代器接口
interface Iterator {
    boolean hasNext();

    Object next();
}

// 集合接口
interface MovieList {
    Iterator createIterator();
}

// 具体电影列表类
class MovieListImpl implements MovieList {
    private List<Movie> movies;

    public MovieListImpl() {
        movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public Iterator createIterator() {
        return new MovieIterator();
    }

    // 具体迭代器类
    private class MovieIterator implements Iterator {
        private int position;

        public MovieIterator() {
            position = 0;
        }

        public boolean hasNext() {
            return position < movies.size();
        }

        public Object next() {
            if (hasNext()) {
                Movie movie = movies.get(position);
                position++;
                return movie;
            }
            return null;
        }
    }
}

// 过滤器接口
interface Filter {
    boolean apply(Movie movie);
}

// 具体过滤器实现类 - 按指定类型过滤
class GenreFilter implements Filter {
    private String genre;

    public GenreFilter(String genre) {
        this.genre = genre;
    }

    public boolean apply(Movie movie) {
        return movie.getGenre().equals(genre);
    }
}

// 具体过滤器实现类 - 按指定评分过滤
class RatingFilter implements Filter {
    private int minRating;

    public RatingFilter(int minRating) {
        this.minRating = minRating;
    }

    public boolean apply(Movie movie) {
        return movie.getRating() >= minRating;
    }
}
```

客户端示例代码：

```java
// 示例代码
public class Client {
    public static void main(String[] args) {
        MovieList movieList = new MovieListImpl();
        movieList.addMovie(new Movie("The Shawshank Redemption", "Drama", 9));
        movieList.addMovie(new Movie("The Dark Knight", "Action", 8));
        movieList.addMovie(new Movie("Inception", "Sci-Fi", 9));
        movieList.addMovie(new Movie("Pulp Fiction", "Crime", 8));

        // 创建迭代器
        Iterator iterator = movieList.createIterator();

        // 过滤器 - 按指定类型过滤
        Filter genreFilter = new GenreFilter("Action");

        // 过滤器 - 按指定评分过滤
        Filter ratingFilter = new RatingFilter(9);

        // 使用迭代器遍历并过滤电影列表
        while (iterator.hasNext()) {
            Movie movie = (Movie) iterator.next();

            // 应用过滤器
            if (genreFilter.apply(movie) && ratingFilter.apply(movie)) {
                System.out.println("Title: " + movie.getTitle() + ", Genre: " + movie.getGenre() + ", Rating: " + movie.getRating());
            }
        }
    }
}
```

输出结果：

```text

```

在这个示例中，我们有一个电影类`Movie`，它包含了电影的标题、类型和评分。然后，我们创建了一个具体电影列表类`MovieListImpl`
，它实现了`MovieList`接口，并负责存储电影对象。

我们还定义了一个迭代器接口`Iterator`，其中包含`hasNext()`和`next()`
方法，用于遍历电影列表。具体的迭代器实现在`MovieListImpl`类内部的`MovieIterator`类中。

在示例代码中，我们创建了一个电影列表对象`movieList`，并向其中添加了几部电影。然后，我们通过调用`createIterator()`
方法创建了一个迭代器对象。

我们还定义了一个过滤器接口`Filter`，其中包含`apply()`
方法，用于根据指定的条件过滤电影。在示例中，我们实现了两个具体的过滤器类`GenreFilter`和`RatingFilter`，分别按照电影类型和评分进行过滤。

在示例代码的主函数中，我们创建了两个过滤器对象：一个按照指定类型过滤的过滤器`genreFilter`
，和一个按照指定评分过滤的过滤器`ratingFilter`。

然后，我们使用迭代器遍历电影列表，并在遍历过程中应用过滤器。只有满足过滤条件的电影才会被打印出来。

这个示例展示了迭代器模式的更复杂用法。通过使用迭代器和过滤器，我们可以方便地遍历和过滤电影列表，而不需要直接操作集合内部的数据结构。

### 遍历自定义集合类中的元素

以下是另一个示例代码，演示如何使用迭代器模式来遍历自定义集合类中的元素：

```java
import java.util.Iterator;

// 自定义集合类
class MyCollection implements Iterable<String> {
    private String[] elements;
    private int size;

    public MyCollection(int capacity) {
        elements = new String[capacity];
        size = 0;
    }

    public void add(String element) {
        if (size < elements.length) {
            elements[size] = element;
            size++;
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new MyIterator();
    }

    // 内部迭代器类
    private class MyIterator implements Iterator<String> {
        private int currentIndex;

        public MyIterator() {
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public String next() {
            String element = elements[currentIndex];
            currentIndex++;
            return element;
        }
    }
}
```

客户端示例代码：

```java

// 示例代码
public class IteratorPatternExample {
    public static void main(String[] args) {
        MyCollection collection = new MyCollection(5);
        collection.add("Apple");
        collection.add("Banana");
        collection.add("Orange");

        // 使用迭代器遍历集合
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element);
        }
    }
}
```

输出结果：

```text
Apple
Banana
Orange
```

在这个示例代码中，我们定义了一个自定义集合类`MyCollection`，它实现了`Iterable`接口，这意味着它支持迭代器模式。

`MyCollection`类内部有一个字符串数组`elements`和一个整数`size`，用于存储集合元素和当前元素的数量。

通过`add()`方法，我们可以向集合中添加元素。

`MyCollection`类还实现了`iterator()`方法，该方法返回一个内部迭代器类`MyIterator`的实例。

`MyIterator`类实现了`Iterator`接口，它包含`currentIndex`变量来跟踪当前元素的索引。`hasNext()`
方法检查是否还有下一个元素，`next()`
方法返回下一个元素并将索引指向下一个位置。

在示例代码的`main()`方法中，我们创建了一个`MyCollection`对象，并向其中添加了几个元素。

然后，我们通过调用`iterator()`方法获取集合的迭代器对象，并使用迭代器遍历集合中的元素。在每次迭代中，我们通过调用`next()`
方法获取当前元素，并将其打印出来。

这个示例展示了如何实现自定义集合类并使用迭代器模式来遍历集合中的元素。通过实现迭代器接口，我们可以在自定义集合类中提供一种通用的方式来遍历和访问元素，使得代码更加模块化、可维护和可扩展。
