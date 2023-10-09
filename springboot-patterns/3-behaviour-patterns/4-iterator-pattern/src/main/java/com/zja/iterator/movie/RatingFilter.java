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
// 具体过滤器实现类 - 按指定评分过滤
class RatingFilter implements Filter {
    private int minRating;

    public RatingFilter(int minRating) {
        this.minRating = minRating;
    }

    @Override
    public boolean apply(Movie movie) {
        return movie.getRating() >= minRating;
    }
}