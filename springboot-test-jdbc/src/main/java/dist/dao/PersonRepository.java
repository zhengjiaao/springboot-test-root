package dist.dao;

import dist.entity.Person;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-17 17:28
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Component
public interface PersonRepository extends CrudRepository<Person, Long> {
    @Query("SELECT * FROM person WHERE lastname = :lastname")
    List<Person> findByLastname(String lastname);

    @Query("SELECT * FROM person WHERE firstname LIKE :firstname")
    List<Person> findByFirstnameLike(String firstname);
}
