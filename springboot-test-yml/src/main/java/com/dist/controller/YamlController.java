package com.dist.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
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
        System.out.println("Resource" + YamlController.class.getResource("").getPath());
        // "/"  则表示从根目录../../target/classes/ 目录开始搜索
        System.out.println("Resource2" + YamlController.class.getResource("/").getPath());
        // ""  则表示从根目录../../target/classes/ 目录开始搜索
        System.out.println("ClassLoader" + YamlController.class.getClassLoader().getResource("").getPath());

        //Map<String, Object> map = YamlUtils.yamlHandler(new ClassPathResource("application.yml"));

        URL url = YamlController.class.getClassLoader().getResource("application.yml");
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);
        Yaml yaml = new Yaml(dumperOptions);
        Map map = yaml.load(new FileInputStream(url.getFile()));
        return map;
    }

    @PutMapping("v1/update/yml")
    public Object updateYml(@RequestBody Object mapvalue) throws Exception {
        URL url = YamlController.class.getClassLoader().getResource("application.yml");
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setPrettyFlow(false);
        Yaml yaml = new Yaml(dumperOptions);
        //yaml.load(new FileInputStream(url.getFile()));

        yaml.dump(mapvalue, new OutputStreamWriter(new FileOutputStream(url.getFile())));

        return mapvalue;
    }

}
