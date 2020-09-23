package com.zja.service;

import com.zja.dto.ShpDatas;
import com.zja.dto.ShpInfo;
import com.zja.util.geotools.ShpTools;
import com.zja.util.result.ResponseMessage;
import com.zja.util.result.ResponseResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**shp业务逻辑类
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 15:35
 */
@Service
public class ShpService {

    //查询一个shp文件
    public ResponseResult getShpDatas(String shpPath, Integer limit) throws  Exception{
        ShpDatas shpDatas = ShpTools.readShpByPath(shpPath, limit);
        return  new ResponseResult(ResponseMessage.OK,shpDatas);
    }

    //将shp文件转换成png图片
    public  void showShp(String shpPath,String imagePath,String color, HttpServletResponse response) throws  Exception{
        ShpTools.shp2Image(shpPath, imagePath ,color,response);
    }

    //写一个shp文件
    public  ResponseResult writeShp(ShpInfo shpInfo) throws  Exception{
        return  ShpTools.writeShpByGeom(shpInfo);
    }
}
