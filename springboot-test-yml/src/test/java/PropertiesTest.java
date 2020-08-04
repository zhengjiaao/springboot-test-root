import com.dist.YmlApplication;
import com.dist.properties1.SecurityProperties;
import com.dist.properties2.Unions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-03 14:40
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YmlApplication.class)
public class PropertiesTest {

    //从spring容器中拿出"大盒子"类
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private Unions unions;

    @Test
    public void getProperties() {

        //获得传到"大盒子"类中的参数值
        System.out.println("方式1--properties里没配置值,默认值browser--------->" +
                securityProperties.getBrowser().getLoginPage());
        System.out.println("方式1--properties里配置了值Android,默认值app------>" +
                securityProperties.getApp().getLoginPage());

        System.out.println("方式2---properties里没配置值,默认值yoyo------>"+unions.getPartI().getName());
        System.out.println("方式2---properties里配置了值10,默认值20------>"+unions.getPartII().getAge());
    }
    /*打印结果：
     * 方式1--properties里没配置值,默认值browser--------->browser
     * 方式1--properties里配置了值Android,默认值app------>android
     * 方式2---properties里没配置值,默认值yoyo------>yoyo
     * 方式2---properties里配置了值10,默认值20------>10
     */

}
