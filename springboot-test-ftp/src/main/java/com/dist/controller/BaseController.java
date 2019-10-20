package com.dist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* Created by jin on 2017/11/21.
 * com.dist.bdf.base.controller.BaseController 返回的数据处理有问题
 * java.lang.IllegalStateException: DataBinder is already initialized - call setAutoGrowCollectionLimit before other configuration methods
*/

public class BaseController {

   protected Logger logger = (Logger) LoggerFactory.getLogger(getClass());
   /**
    * session会话
    */
   protected HttpSession session;

   /**
    * httpRequest
    */

   protected HttpServletRequest request;
   /**
    * response
    */
   protected HttpServletResponse response;

   /**
    * @param request
    *            请求
    */
   @ModelAttribute
   public void setRequest(HttpServletRequest request, HttpServletResponse response) {
       this.request = request;
       this.response = response;
       this.session = request.getSession();
   }

   /**
    * 获取发送请求的客户端ip
    * @return 返回ip地址
    */
   protected String getRequestIp() {

       return this.request.getRemoteAddr();
   }
}
