//package com.dist.controller;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.dist.interfaces.UserService;
//import com.dist.model.entity.TicketEntity;
//import com.dist.model.entity.UserEntity;
//import com.dist.response.ResponseData;
//import com.dist.response.ResponseUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
///**
// * desc 模拟sso单点登录
// */
//@Slf4j
//@Api(tags = {"SsoController"}, description = "模拟dasc单点登录")
//@RestController("restSsoController")
//@RequestMapping(value="rest/sso")
//public class SsoController extends BaseController {
//
//    @Value("${sso.ticket.timeout}")
//    private String timeOut;
//
//    @Reference
//    private UserService userService;
//
//
//    /**
//     * 根据登录名来验证用户的有效性，并生成票据
//     * @param loginName 登录名
//     * session http会话，用于保存当前票据信息,使用
//     * @return 票据号,假如没有用户信息，返回空值
//     */
//    @ApiOperation(value = "单点登录获取票据",notes = "根据登录名来验证用户的有效性，并生成票据")
//    @RequestMapping(value="/{loginName}",method = RequestMethod.GET)
//    public ResponseData getTicket(@ApiParam(value = "登录名") @PathVariable String loginName){
//        UserEntity userEntity = userService.getUserEntityByName(loginName);
//        if(null != userEntity){
//            String ticketNo = UUID.randomUUID().toString();
//            TicketEntity ticketEntity = new TicketEntity();
//            ticketEntity.setTicketNo(ticketNo);
//            ticketEntity.setLoginName(userEntity.getName());
//            ticketEntity.setTicketCreateTime(System.currentTimeMillis());
//            ticketEntity.setUserEntity(userEntity);
//            session.setAttribute(ticketNo, ticketEntity);
//            return ResponseUtil.success(ticketEntity);
//        }
//        return ResponseUtil.fail("用户不存在");
//    }
//
//    /**
//     * 根据票据来获取登录名，返回登录名
//     * 不破坏session的生命周期，通过创建日期和票据的生存期来判断是否过期
//     * 弊端：假如web不设置session的过期期限，那么在服务器运行期间无法定时销毁票据信息
//     *       考虑到session的安全性，应当在web里面设置过期时间，不影响对票据具体的过期
//     *       时间造成影响
//     *
//     * @param ticketNo 票据编号
//     *  session http会话，用于保存当前票据信息
//     * @return 假如票据过期，返回空值
//     */
//    @ApiOperation(value = "根据票据来获取登录名，返回登录名",httpMethod = "GET")
//    @RequestMapping(value="/ticket/{ticketNo}",method = RequestMethod.GET)
//    public ResponseData checkTicket(@ApiParam(value = "票据编号") @PathVariable String ticketNo){
//        TicketEntity tiketEntity = (TicketEntity) session.getAttribute(ticketNo);
//        //移除票据信息
//        session.removeAttribute(ticketNo);
//        long timeOutL = Long.valueOf(timeOut)* 1000;
//        boolean check = (null != tiketEntity)
//                && ((System.currentTimeMillis()-tiketEntity.getTicketCreateTime()) <= timeOutL);
//        if(check){
//            String loginName = tiketEntity.getLoginName();
//            UserEntity userEntity = tiketEntity.getUserEntity();
//            return ResponseUtil.success(userEntity);
//        }
//        return ResponseUtil.fail("票据已过期");
//    }
//
//}
