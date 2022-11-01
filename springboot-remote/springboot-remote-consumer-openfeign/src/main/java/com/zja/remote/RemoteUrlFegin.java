/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-07-25 13:29
 * @Since:
 */
package com.zja.remote;

import feign.RequestLine;

/**
 *
 */
public interface RemoteUrlFegin {
    @RequestLine("GET /dataexchangeserver/api/getCropInfo?userKey=be9e4d26-ab9f-45f4-b761-3524771b76c4&appKey=c9406731-50bc-4e23-b3f5-b7e7d3093875&time=1658716740106&sign=jjlo256hl1/TF21m4vaObQ%3D%3D")
    Object getCropInfo();
}
