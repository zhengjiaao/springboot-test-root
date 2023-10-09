/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:54
 * @Since:
 */
package com.zja.iterator.movie;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:54
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        MovieList movieList = new MovieListImpl();
        movieList.addMovie(new Movie("The Shawshank Redemption", "Drama", 9));
        movieList.addMovie(new Movie("The Dark Knight", "Action", 10));
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

        //输出结果：
        //Title: The Dark Knight, Genre: Action, Rating: 10
    }
}
