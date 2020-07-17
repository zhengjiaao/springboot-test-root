package com.dist.controller;

import com.dist.service.ExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-05-19 9:42
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RequestMapping(value = "rest/exception")
@RestController
@Api(tags = {"ExceptionController"}, description = "异常捕获处理")
@Slf4j
public class ExceptionController {

    @Autowired
    ExceptionService exceptionService;

    @ApiOperation(value = "异常返回给前端", httpMethod = "GET")
    @GetMapping("/v1/test")
    public Object exceptionTest1(boolean result) throws Exception {
        return exceptionService.exceptionTest(result);
    }

    @ApiOperation(value = "异常后端捕获", httpMethod = "GET")
    @GetMapping("/v2/test")
    public Object exceptionTest2(boolean result) {
        try {
            return exceptionService.exceptionTest(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户查询失败-error-:{}",e.toString());
            log.error("用户查询失败-error-:{}",e.getMessage());
            log.error("用户查询失败-error-:{} {} {}",e.getMessage(),"参数-result-:{}",result);
        }finally {
            log.error("用户查询失败-finally-:{} {}","参数-result-:{}",result);
            return null;
        }
    }

    @ApiOperation(value = "异常不处理", httpMethod = "GET")
    @GetMapping("/v3/test")
    public Object exceptionTest3(boolean result) throws Exception {
        try {
            return exceptionService.exceptionTest(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            return null;
        }
    }
}
