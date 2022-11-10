# springboot-file-csv

- [opencsv 官网](https://opencsv.sourceforge.net/)
- [API 参考](https://blog.csdn.net/qq_41609208/article/details/111461171)

## 依赖引入

```xml
        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>5.7.1</version>
        </dependency>
```

## 简单读取和写入csv 示例

```java

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
```
