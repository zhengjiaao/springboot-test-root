package dist.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-17 17:25
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class Person implements Serializable {
    @Id
    private Long id;
    private String firstname;
    private String lastname;
}
