package com.dist.server.rest;

import com.dist.dto.UserDTO;
import com.dist.rest.annotation.DistGet;
import com.dist.rest.annotation.DistPost;
import com.dist.rest.annotation.DistRequestBody;
import com.dist.rest.annotation.DistRest;

import java.util.List;

/**RestProxy 代理接口
 * @author zhengja@dist.com.cn
 * @data 2019/7/9 16:53
 */
@DistRest(url="${dist.rest.url.remoteservice}")
//@DistRequest(value="/user")
public interface RestProxyService {

    @DistGet(value="get/userdto3",resultClass = UserDTO.class)
    UserDTO getUserDTO3();

    @DistPost(value="post/userdto4",resultClass = UserDTO.class)
    UserDTO getUserDTO4(@DistRequestBody UserDTO userDTO);

    @DistGet(value = "get/userdtolsit2",resultClass = UserDTO.class)
    List<UserDTO> getUserDTOS2();

}
