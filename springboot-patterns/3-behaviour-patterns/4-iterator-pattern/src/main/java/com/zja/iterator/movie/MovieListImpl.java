/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:53
 * @Since:
 */
package com.zja.iterator.movie;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:53
 */
// 具体电影列表类
class MovieListImpl implements MovieList {
    private List<Movie> movies;

    public MovieListImpl() {
        movies = new ArrayList<>();
    }

    @Override
    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    @Override
    public Iterator createIterator() {
        return new MovieIterator();
    }

    // 具体迭代器类
    private class MovieIterator implements Iterator {
        private int position;

        public MovieIterator() {
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < movies.size();
        }

        @Override
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