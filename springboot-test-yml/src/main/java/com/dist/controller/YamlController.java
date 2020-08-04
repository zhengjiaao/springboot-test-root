package com.dist.controller;


import com.dist.util.YamlUtils;
import io.swagger.annotations.Api;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-03 15:58
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RestController
@Api(tags = {"YamlController"}, description = "yml配置解析")
public class YamlController {

    @GetMapping("v1/get/yml")
    public Object getYml() throws Exception {
        //"" 则表示从当前class类文件所在目录开始搜索
        System.out.println("Resource"+YamlController.class.getResource("").getPath());
        // "/"  则表示从根目录../../target/classes/ 目录开始搜索
        System.out.println("Resource2"+YamlController.class.getResource("/").getPath());
        // ""  则表示从根目录../../target/classes/ 目录开始搜索
        System.out.println("ClassLoader"+YamlController.class.getClassLoader().getResource("").getPath());

        Map<String, Object> map = YamlUtils.yamlHandler(new ClassPathResource("application.yml"));

        //URL url = getClassLoader().getResource("application.yml");
        /*DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);
        Yaml yaml = new Yaml(dumperOptions);
        Map map =(Map)yaml.load(new FileInputStream(url.getFile()));*/
        return map;
    }
}
