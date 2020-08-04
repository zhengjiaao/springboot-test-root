package dist.controller;

import dist.dao.PersonRepository;
import dist.entity.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengja@dist.com.cn
 * @data 2020/07/16 11:41
 */
@RestController
@Api(tags = {"JdbcController"}, description = "spring-data-jdbc")
public class JdbcController {

    @Autowired
    PersonRepository repository;

    @ApiOperation(value = "新增-用户", httpMethod = "POST")
    @PostMapping(value = "v1/person/add")
    public Object addPerson() {

        Person person = new Person();
        person.setFirstname("Jens");
        person.setLastname("Schauder");
        repository.save(person);

        List<Person> lastNameResults = repository.findByLastname("Schauder");
        List<Person> firstNameResults = repository.findByFirstnameLike("Je%");
        Map map =new ConcurrentHashMap();
        map.put("lastNameResults",lastNameResults);
        map.put("firstNameResults",firstNameResults);
        return map;
    }

    @ApiOperation(value = "查询全部内容", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/all")
    public Object srarch() {
        return null;
    }

}
