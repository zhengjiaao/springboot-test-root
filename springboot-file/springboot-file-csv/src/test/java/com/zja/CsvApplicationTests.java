/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 9:13
 * @Since:
 */
package com.zja;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CsvApplicationTests {

    /**
     * 把beans写入到csv文件
     */
    @Test
    public void test() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        UserEntity user = new UserEntity("张三");

        Writer writer = new FileWriter("user.csv");
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        beanToCsv.write(user);
        writer.close();
    }

    /**
     * 读取csv转bean  带注解，不会读取未带注解的字段
     */
    @Test
    public void CsvToBean_Test() throws FileNotFoundException {
        List<UserEntity> beans = new CsvToBeanBuilder(new FileReader("user.csv"))
                .withType(UserEntity.class).build().parse();
        System.out.println(beans);
    }

    /**
     * 读取csv转map
     * .readMap() : 第一次调用获取第一行与第二行数据Map,第二次调用获取第一行与第三行数据Map,.....
     */
    @Test
    public void CsvToMap_Test() throws IOException, CsvValidationException {
        //注意，此方法不能读取所有数据，仅读取一行数据(行头+一行数据)
        Map<String, String> values = new CSVReaderHeaderAware(new FileReader("user.csv")).readMap();
        System.out.println(values);
    }

    /**
     * 读取csv转map
     * .readMap() : 第一次调用获取第一行与第二行数据Map,第二次调用获取第一行与第三行数据Map,.....
     */
    @Test
    public void CsvToMap2_Test() throws IOException, CsvException {
        CSVReaderHeaderAware aware = new CSVReaderHeaderAware(new FileReader("user.csv"));
        for (int i = 0; i < 2; i++) {
            Map<String, String> values = aware.readMap();
            System.out.println(values);
        }
    }

}
