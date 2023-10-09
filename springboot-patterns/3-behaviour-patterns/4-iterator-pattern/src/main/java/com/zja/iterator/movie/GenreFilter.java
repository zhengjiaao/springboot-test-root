/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:53
 * @Since:
 */
package com.zja.iterator.movie;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:53
 */
// 具体过滤器实现类 - 按指定类型过滤
class GenreFilter implements Filter {
    private String genre;

    public GenreFilter(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean apply(Movie movie) {
        return movie.getGenre().equals(genre);
    }
}