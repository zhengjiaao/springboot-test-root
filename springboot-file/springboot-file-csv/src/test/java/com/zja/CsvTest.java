package com.zja;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-01-21 16:29
 */
public class CsvTest {

    /**
     * 把 Object 写入到 csv 文件
     */
    @Test
    public void write_bean_test_1() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        UserEntity user = new UserEntity("张三");

        try (Writer writer = new FileWriter("user.csv")) {
            StatefulBeanToCsv<UserEntity> beanToCsv = new StatefulBeanToCsvBuilder<UserEntity>(writer).build();
            beanToCsv.write(user);
        }
    }

    /**
     * 把 List<Object> 写入到 csv 文件
     */
    @Test
    public void write_bean_test_2() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List<UserEntity> users = Lists.list(
                new UserEntity("张三", "111"),
                new UserEntity("李四", "222"));

        try (Writer writer = new FileWriter("users.csv")) {
            StatefulBeanToCsv<UserEntity> beanToCsv = new StatefulBeanToCsvBuilder<UserEntity>(writer).build();
            beanToCsv.write(users);
        }
    }

    /**
     * 读取csv转bean  带注解，不会读取未带注解的字段
     */
    @Test
    public void reader_bean_test() throws IOException, CsvException {
        try (FileReader reader = new FileReader("users.csv")) {
            List<UserEntity> beans = new CsvToBeanBuilder<UserEntity>(reader)
                    .withType(UserEntity.class).build().parse();
            System.out.println(beans);
        }
    }

    @Test
    public void reader_bean_test2() throws IOException, CsvException {
        List<DlbmRelDicDTO> dlbmRelDicDTOList = getDlbmRelDicDTOList();
        System.out.println(dlbmRelDicDTOList);
    }

    public List<DlbmRelDicDTO> getDlbmRelDicDTOList() throws IOException {
        // 使用 ClassLoader 获取资源文件的输入流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/DLBM_REL_DIC.csv");
        if (inputStream == null) {
            throw new FileNotFoundException("DLBM_REL_DIC.csv not found in resources");
        }
        // 若读取不到第1列情况，则重新创建csv文件，用notepad++打开csv文件，在“编码”处选择“使用utf-8编码”，然后导入
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            List<DlbmRelDicDTO> beans = new CsvToBeanBuilder<DlbmRelDicDTO>(reader)
                    .withType(DlbmRelDicDTO.class)
                    .build()
                    .parse();
            return beans;
        }
    }

    /**
     * 读取csv转map
     * .readMap() : 第一次调用获取第一行与第二行数据Map,第二次调用获取第一行与第三行数据Map,.....
     */
    @Test
    public void CsvToMap_Test() throws IOException, CsvValidationException {
        // 注意，此方法不能读取所有数据，仅读取一行数据(行头+一行数据)
        Map<String, String> values = new CSVReaderHeaderAware(new FileReader("users.csv")).readMap();
        System.out.println(values);
    }

    /**
     * 读取csv转map
     * .readMap() : 第一次调用获取第一行与第二行数据Map,第二次调用获取第一行与第三行数据Map,.....
     */
    @Test
    public void CsvToMap_Test_2() throws IOException, CsvValidationException {
        // 使用 ClassLoader 获取资源文件的输入流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("users.csv");
        if (inputStream == null) {
            throw new FileNotFoundException("users.csv not found in resources");
        }
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            Map<String, String> values = new CSVReaderHeaderAware(reader).readMap();
            System.out.println(values);
        }
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
