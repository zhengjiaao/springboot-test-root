package com.zja.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zja.api.TelihuiService;
import com.zja.base.Constants.BusinessTypeEnum;
import com.zja.base.Constants.OperationTypeEnum;
import com.zja.dto.OrganizationADDDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-05-19 11:38
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RestController
@RequestMapping(value = "rest/remote")
@Api(tags = {"REMOTE-第三方对接远程接口"})
@Slf4j
public class TelihuiController {

    @Autowired
    TelihuiService telihuiService;

    @GetMapping("v1/organizations")
    @ApiOperation(value = "获取所有组织机构", notes = "同步第三方的组织机构")
    public Object organizations() {
        Object organizations = telihuiService.organizations();
        //Map<String,Map<String,Map<String, String>>> maps = (Map)JSONObject.parseObject(JSON.toJSONString(organizations));
        Map<String, List<Map<String, Object>>> maps = (Map) JSONObject.parseObject(JSON.toJSONString(organizations));
        System.out.println(maps.get("content"));
        List<Map<String, Object>> content = maps.get("content");
        if (content != null && content.size() > 0) {
            for (Map<String, Object> map : content) {
                System.out.println(map.get("name")); //组织机构名称
                System.out.println(map.get("id")); //组织机构id
                System.out.println(map.get("displayOrder")); //顺序
            }
        }
        return maps.get("content");
    }

    @GetMapping("v2/organizations")
    @ApiOperation(value = "同步指定组织机构", notes = "同步第三方的组织机构")
    public Object organizations(String id) {
        Object organizations = telihuiService.organizations(id);
        //Map<String,Map<String,Map<String, String>>> maps = (Map)JSONObject.parseObject(JSON.toJSONString(organizations));
        Map<String, List<Map<String, Object>>> maps = (Map) JSONObject.parseObject(JSON.toJSONString(organizations));
        System.out.println(maps.get("content"));
        List<Map<String, Object>> content = maps.get("content");
        if (content != null && content.size() > 0) {
            for (Map<String, Object> map : content) {
                System.out.println(map.get("name")); //组织机构名称
                System.out.println(map.get("id")); //组织机构id
                System.out.println(map.get("displayOrder")); //顺序
                OrganizationADDDTO adddto = new OrganizationADDDTO();
                adddto.setFullname(String.valueOf(map.get("name")));
                System.out.println(adddto);
            }
        }
        return maps.get("content");
    }

    @GetMapping("v1/departments")
    @ApiOperation(value = "同步所有部门", notes = "同步第三方的部门")
    public Object departments() {
        Object departments = telihuiService.departments();
        Map<String, List<Map<String, Object>>> maps = (Map) JSONObject.parseObject(JSON.toJSONString(departments));
        List<Map<String, Object>> content = maps.get("content");
        if (content != null && content.size() > 0) {
            for (Map<String, Object> map : content) {
                System.out.println(map.get("name")); //部门名称
                System.out.println(map.get("id")); //部门id
                System.out.println(map.get("organizationId")); //组织机构id
                System.out.println(map.get("displayOrder")); //顺序
            }
        }
        return maps.get("content");
    }

    @GetMapping("v2/departments")
    @ApiOperation(value = "同步指定部门", notes = "同步第三方的部门")
    public Object departments(String id) {
        Object departments = telihuiService.departments(id);
        Map<String, List<Map<String, Object>>> maps = (Map) JSONObject.parseObject(JSON.toJSONString(departments));
        List<Map<String, Object>> content = maps.get("content");
        if (content != null && content.size() > 0) {
            for (Map<String, Object> map : content) {
                System.out.println(map.get("name")); //部门名称
                System.out.println(map.get("id")); //部门id
                System.out.println(map.get("organizationId")); //组织机构id
                System.out.println(map.get("displayOrder")); //顺序
            }
        }
        return maps.get("content");
    }

    @GetMapping("v1/users")
    @ApiOperation(value = "同步所有用户", notes = "同步第三方的用户")
    public Object users() {
        Object users = telihuiService.users();
        Map<String, List<Map<String, Object>>> maps = (Map) JSONObject.parseObject(JSON.toJSONString(users));
        List<Map<String, Object>> content = maps.get("content");
        if (content != null && content.size() > 0) {
            for (Map<String, Object> map : content) {
                System.out.println(map.get("name")); //用户名称
                System.out.println(map.get("id"));  //用户id
                System.out.println(map.get("organizationId")); //组织机构id
                System.out.println(map.get("departmentId")); //部门id
                System.out.println(map.get("displayOrder")); //顺序
                System.out.println(map.get("loginName"));
                System.out.println(map.get("email"));
                System.out.println(map.get("lastLoginTime"));
                System.out.println(map.get("mobilePhone"));
                System.out.println(map.get("registerDate"));
            }
        }
        return maps.get("content");
    }

    @GetMapping("v2/users")
    @ApiOperation(value = "同步指定用户", notes = "同步第三方的用户")
    public Object users(String id) {
        Object users = telihuiService.users(id);
        Map<String, List<Map<String, Object>>> maps = (Map) JSONObject.parseObject(JSON.toJSONString(users));
        List<Map<String, Object>> content = maps.get("content");
        if (content != null && content.size() > 0) {
            for (Map<String, Object> map : content) {
                System.out.println(map.get("name")); //用户名称
                System.out.println(map.get("id"));  //用户id
                System.out.println(map.get("organizationId")); //组织机构id
                System.out.println(map.get("departmentId")); //部门id
                System.out.println(map.get("displayOrder")); //顺序
                System.out.println(map.get("loginName"));
                System.out.println(map.get("email"));
                System.out.println(map.get("lastLoginTime"));
                System.out.println(map.get("mobilePhone"));
                System.out.println(map.get("registerDate"));
            }
        }
        return maps.get("content");
    }

    @GetMapping("v1/notice/{id}/{businessType}/{operationType}")
    @ApiOperation(value = "第三方特力惠请求通知同步数据", notes = "同步第三方的组织机构、部门、用户同步")
    public void notice(@ApiParam(value = "业务编号") @PathVariable String id,
                       @ApiParam(value = "业务类型") @PathVariable Integer businessType,
                       @ApiParam(value = "操作类型") @PathVariable Integer operationType) {

        BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.getByCode(businessType);
        OperationTypeEnum operationTypeEnum = OperationTypeEnum.getByCode(operationType);
        if (null == businessTypeEnum) {
            log.error("业务类型错误-businessType:{}", businessType);
            return;
        }
        if (null == operationTypeEnum) {
            log.error("操作类型错误-operationType:{}", operationType);
            return;
        }
        //同步组织机构-数据处理
        if (businessType.equals(BusinessTypeEnum.ORGANIZATION.code())) {
            this.organizationSynchronization(id, operationType);
        }
        //同步部门-数据处理
        if (businessType.equals(BusinessTypeEnum.DEPARTMENT.code())) {
            this.organizationSynchronization(id, operationType);
        }
        //同步用户-数据处理
        if (businessType.equals(BusinessTypeEnum.USER.code())) {
            this.userSynchronization(id, operationType);
        }

    }

    /**
     * 组织机构同步操作
     *
     * @param id            操作id
     * @param operationType 操作类型：增删改
     */
    private void organizationSynchronization(String id, Integer operationType) {
        boolean result = false;
        try {
            //同步-新增组织结构
            if (operationType.equals(OperationTypeEnum.NEW_DATA.code())) {
                //不可直接使用：异常处理
                //OrganizationADDDTO org = new OrganizationADDDTO();
                //this.organizationService.saveOrganization(org);
                log.info("新增组织结构id:{}", id);
            }
            //同步-修改组织机构
            if (operationType.equals(OperationTypeEnum.CHANGE_DATA.code())) {
                //
                //OrganizationUPDATEDTO org = new OrganizationUPDATEDTO();
                //this.organizationService.updateOrganization(org)
                log.info("修改组织机构id:{}", id);
            }
            //同步-删除组织机构
            if (operationType.equals(OperationTypeEnum.DELETE_DATA.code())) {
                //
                String[] orgcodes = new String[1];
                orgcodes[0] = "";
                //this.organizationService.deleteOrganizations(orgcodes);
                log.info("删除组织机构id:{}", id);
            }
            result = true;
        } catch (Exception e) {
            log.error("组织机构同步失败信息-error {} {} {}", e.getMessage(), "操作类型:", OperationTypeEnum.getByCode(operationType).desc());
        } finally {
            log.info("组织机构同步信息 {} {} {} {} {} {}", "操作结果:", result, "操作类型:", OperationTypeEnum.getByCode(operationType).desc(), "操作组织机构guid:", id);
        }
    }

    /**
     * 用户同步操作
     *
     * @param id            操作id
     * @param operationType 操作类型：增删改
     */
    private void userSynchronization(String id, Integer operationType) {
        boolean result = false;
        try {
            //同步-新增用户
            if (operationType.equals(OperationTypeEnum.NEW_DATA.code())) {
                //新增用户-接口可直接使用：异常处理
                //UserADDDTO user = new UserADDDTO();
                //this.userService.saveUser(user);
                log.info("用户新增id:{}", id);
            }
            //同步-修改用户
            if (operationType.equals(OperationTypeEnum.CHANGE_DATA.code())) {
                //修改用户信息-接口不能直接使用：组织结构、异常处理
                //UserUPDATEDTO userUPDATEDTO = new UserUPDATEDTO();
                //this.userService.updateUser(userUPDATEDTO);
                log.info("用户修改id:{}", id);
            }
            //同步-删除用户
            if (operationType.equals(OperationTypeEnum.DELETE_DATA.code())) {
                //删除用户-接口可直接使用-异常处理
                String[] usercodes = new String[1];
                //用户guid
                usercodes[0] = "";
                log.info("用户删除id:{}", id);
                //this.userService.deleteUser(usercodes);
            }
            result = true;
        } catch (Exception e) {
            log.error("用户同步失败信息-error {} {} {}", e.getMessage(), "操作类型:", OperationTypeEnum.getByCode(operationType).desc());
        } finally {
            log.info("用户同步信息 {} {} {} {} {} {}", "操作结果:", result, "操作类型:", OperationTypeEnum.getByCode(operationType).desc(), "操作用户guid:", id);
        }
    }

}
