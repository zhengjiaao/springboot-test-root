import com.zja.FileYmlApplication;
import com.zja.dto.DynamicValue;
import com.zja.dto.MyAttributes;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-03 14:29
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileYmlApplication.class)
public class YmlTest {

    @Autowired
    private MyAttributes myAttributes;

    @Test
    public void propsTest() {
        System.out.println("myAttributes: " + myAttributes.toString());
    }
    //打印结果 myAttributes: MyAttributes(value=345, valueArray=[1, 2, 3, 4, 5, 6, 7, 8, 9], valueList=[13579, 246810], valueMap={name=张三, age=20, sex=female}, valueMapList=[{name=李四, age=21}, {name=caven, age=31}], valueUserList=[User(name=李四, age=24), User(name=张三, age=22)], valueUser=User(name=安其拉, age=18))

    @Value("isOne")
    private String isOne;

    @Autowired
    private DynamicValue dynamicValue;

    /**
     * 动态修改yml配置
     */
//    @DynamicTest
    public void updateYaml(){
        try {
            URL url = Test.class.getClassLoader().getResource("application.yml");
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
            dumperOptions.setPrettyFlow(false);
            Yaml yaml = new Yaml(dumperOptions);
            Map map =(Map)yaml.load(new FileInputStream(url.getFile()));
            System.out.println("修改前isOne："+map.get("isOne"));
            System.out.println("修改前string: "+isOne);
            System.out.println("修改前getData: "+dynamicValue.getData());
            map.put("isOne",false);
            Map dynamicvalue = (Map) map.get("dynamicvalue");
            dynamicvalue.put("data","李四");
            yaml.dump(map, new OutputStreamWriter(new FileOutputStream(url.getFile())));
            Map map2 =(Map)yaml.load(new FileInputStream(url.getFile()));
            System.out.println("修改后isOne: "+map2.get("isOne"));
            System.out.println("动态修改后string: "+isOne);

            System.out.println("修改前getData: "+dynamicValue.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
