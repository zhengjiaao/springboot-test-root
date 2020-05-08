package com.dist.api;

import com.dist.dto.UserDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**调用远程服务器http接口
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 11:27
 */
@Api(value = "Feign调用远程接口-API")
public interface FeignTestService {

    @ApiOperation(value ="提供get方法测试",notes = "不传参数",httpMethod = "GET")
    @RequestLine("GET /rest/v1/get/userdto")
    Object getUserDTO();

    @ApiOperation(value ="提供get方法测试",notes = "传参数",httpMethod = "GET")
    @RequestLine("GET /rest/v1/get/userdto2/?param={param}")
    Object getUserDTO(@Param(value = "param")String param);

    @ApiOperation(value ="提供post方法测试",httpMethod = "POST")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestLine("POST /rest/v1/post/userdto")
    Object postUserDTO(UserDTO userDto);

    @ApiOperation(value ="提供put方法测试",httpMethod = "PUT")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @RequestLine("PUT /rest/v1/put/userdto")
    Object putUserDTO(UserDTO userDto);

    @ApiOperation(value ="提供delete方法测试",httpMethod = "DELETE")
    @RequestLine("DELETE /rest/v1/delete/userdto")
    Object deleteUserDTO();

    @ApiOperation(value ="提供lsit<UserDTO>方法测试",httpMethod = "GET")
    @RequestLine("GET /rest/v1/get/userdtolsit")
    List<UserDTO> getUserDTOS();

    @ApiOperation(value ="提供listobject方法测试",httpMethod = "GET")
    @RequestLine("GET /rest/v1/get/userdtolsit")
    Object getUserDTOList();


    /*@RequestLine("GET rest/material/v2/fileRetrieveFromFtp/?dir={dir}")
    Object getFileRetrieveFromFtps(@Param(value = "dir")String dir);*/

    /**
     *远程接口 ：http://192.168.1.99:8080/dgp-server-web/rest/material/v2/fileRetrieveFromFtp?dir=root
     *
     @ApiOperation(value="获取ftp下相对文件目录", notes = "获取ftp下相对文件目录")
     @RequestMapping(value = "/v2/fileRetrieveFromFtp", method = {RequestMethod.GET})
     public Result getFileRetrieveFromFtps(@ApiParam(value = "专题关联的目录，如果是根目录，则传入ROOT", required = true)
     @RequestParam String dir){
     getCorrectEncode();
     try {
     FTPClient ftpClient = FtpUtil.connectServer(cadFtpConfig);
     String ftpPath;
     if (dir == null || "ROOT".equalsIgnoreCase(dir)) {
     ftpPath = cadFtpConfig.getServerPath();
     } else {
     ftpPath = dir;
     }
     List<FoldersDTO> pathVos = FtpUtil.getNextDirectoryList(ftpClient,ftpPath);
     //关闭FTP连接
     FtpUtil.closeServer(ftpClient);
     return super.successResult(pathVos);
     } catch (IOException e) {
     e.printStackTrace();
     return super.failResult("ftp IO流解析异常");
     }
     }
     */

    @ApiOperation(value ="提供服务降级测试",httpMethod = "GET")
    @RequestLine("GET /rest/v1/get/hystrix")
    Object getHystrix();

}
