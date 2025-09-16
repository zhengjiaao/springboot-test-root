package com.zja.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zja.easyexcel.model.UserDTO;
import com.zja.easyexcel.model.UserDTOMockData;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 采用注解方式 读取Excel中数据
 *
 * @Author: zhengja
 * @Date: 2025-09-16 14:08
 */
public class ReadUserTest {

    /**
     * 准备测试数据，写入target/output_user_data.xlsx文件
     * 用于后续读取测试
     */
    @Test
    public void prepareUserDataForReadTest() throws Exception {
        // 获取模拟数据
        List<UserDTO> userDTOList = UserDTOMockData.getUserDTOList();

        // 输出文件到target目录
        String outputPath = getTargetPath("output_user_data.xlsx");

        // 写入数据到Excel文件
        EasyExcel.write(outputPath, UserDTO.class)
                .sheet("用户数据")
                .doWrite(userDTOList);

        System.out.println("测试数据已写入: " + outputPath);
        System.out.println("共写入 " + userDTOList.size() + " 条用户数据");
    }

    /**
     * 准备多sheet测试数据
     */
    @Test
    public void prepareMultiSheetUserDataForReadTest() throws Exception {
        // 获取模拟数据
        List<UserDTO> userDTOList = UserDTOMockData.getUserDTOList();

        // 输出文件到target目录
        String outputPath = getTargetPath("output_multi_sheet_user_data.xlsx");

        // 写入多个sheet的数据
        EasyExcel.write(outputPath, UserDTO.class)
                .sheet("用户数据1")
                .doWrite(userDTOList.subList(0, Math.min(5, userDTOList.size())));

        // 追加第二个sheet
        EasyExcel.write(outputPath, UserDTO.class)
                .sheet("用户数据2")
                .doWrite(userDTOList.subList(Math.min(5, userDTOList.size()),
                        Math.min(10, userDTOList.size())));

        System.out.println("多sheet测试数据已写入: " + outputPath);
    }

    /**
     * 简单读取User数据
     * 使用doReadSync()方法直接读取Excel文件中的所有User数据到内存中
     * 适用于数据量较小的场景
     */
    @Test
    public void readUserData() throws Exception {
        String filePath = "target/output_user_data.xlsx"; // 假设这是之前写入的文件

        // 读取Excel文件中的User数据
        List<UserDTO> userList = EasyExcel.read(filePath)
                .head(UserDTO.class)
                // .sheet()
                .sheet("用户数据")
                .doReadSync();

        // 输出读取的数据
        userList.forEach(System.out::println);
    }

    @Test
    public void readMultiSheetUserData() throws Exception {
        String filePath = "target/output_multi_sheet_user_data.xlsx";

        // 创建监听器
        UserDataListener listener = new UserDataListener();

        // 读取第一个sheet
        EasyExcel.read(filePath, UserDTO.class, listener)
                .sheet("用户数据1")
                .doRead();

        // 清空监听器数据或创建新监听器读取第二个sheet
        List<UserDTO> sheet1Data = listener.getUserList();
        System.out.println("Sheet1数据量: " + sheet1Data.size());

        // 重新创建监听器读取第二个sheet
        UserDataListener listener2 = new UserDataListener();
        EasyExcel.read(filePath, UserDTO.class, listener2)
                .sheet("用户数据2")
                .doRead();

        List<UserDTO> sheet2Data = listener2.getUserList();
        System.out.println("Sheet2数据量: " + sheet2Data.size());

        // 合并数据
        List<UserDTO> allData = new ArrayList<>();
        allData.addAll(sheet1Data);
        allData.addAll(sheet2Data);

        System.out.println("总数据量: " + allData.size());
    }

    /**
     * 使用监听器方式读取User数据
     * 通过自定义监听器逐行处理数据，适用于大数据量场景，节省内存
     * 读取完成后通过监听器获取结果
     */
    @Test
    public void readUserDataWithListener() throws Exception {
        String filePath = "target/output_user_data.xlsx";

        // 创建监听器
        UserDataListener listener = new UserDataListener();

        // 读取数据
        EasyExcel.read(filePath, UserDTO.class, listener)
                .sheet()
                .doRead();

        // 获取读取的数据
        List<UserDTO> userList = listener.getUserList();
        System.out.println("读取到 " + userList.size() + " 条用户数据");
        userList.forEach(System.out::println);
    }

    /**
     * User数据监听器
     * 实现AnalysisEventListener接口，用于逐行处理读取到的User数据
     */
    public class UserDataListener extends AnalysisEventListener<UserDTO> {
        private List<UserDTO> userList = new ArrayList<>();

        /**
         * 每解析一行数据时调用此方法
         *
         * @param userDTO 解析到的用户数据对象
         * @param context 解析上下文
         */
        @Override
        public void invoke(UserDTO userDTO, AnalysisContext context) {
            System.out.println("解析到一条数据: " + userDTO);
            userList.add(userDTO);
        }

        /**
         * 所有数据解析完成后调用此方法
         *
         * @param context 解析上下文
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("所有数据解析完成！");
        }

        /**
         * 获取解析到的用户数据列表
         *
         * @return 用户数据列表
         */
        public List<UserDTO> getUserList() {
            return userList;
        }
    }

    /**
     * 从指定sheet读取User数据
     * 可以通过sheet名称或索引指定要读取的工作表
     * 适用于多sheet的Excel文件
     */
    @Test
    public void readUserDataFromSpecificSheet() throws Exception {
        String filePath = "target/output_user_data.xlsx";

        // 读取指定sheet的数据
        List<UserDTO> userList = EasyExcel.read(filePath)
                .head(UserDTO.class)
                .sheet("用户数据")  // 指定sheet名称
                .doReadSync();

        // 或者通过索引指定sheet
        // List<UserDTO> userList = EasyExcel.read(filePath)
        //         .head(UserDTO.class)
        //         .sheet(0)  // 指定sheet索引
        //         .doReadSync();

        userList.forEach(System.out::println);
    }

    /**
     * 读取并转换User数据
     * 在读取过程中对数据进行处理和转换
     * 适用于需要数据清洗或格式转换的场景
     */
    @Test
    public void readAndConvertUserData() throws Exception {
        String filePath = "target/output_user_data.xlsx";

        // 读取数据并进行转换处理
        List<UserDTO> userList = EasyExcel.read(filePath)
                .head(UserDTO.class)
                .registerReadListener(new AnalysisEventListener<UserDTO>() {
                    @Override
                    public void invoke(UserDTO userDTO, AnalysisContext context) {
                        // 在读取过程中对数据进行处理
                        // 例如：数据清洗、格式转换等
                        if (userDTO.getUserName() != null) {
                            userDTO.setUserName(userDTO.getUserName().trim());
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("数据读取和转换完成");
                    }
                })
                .sheet()
                .doReadSync();

        userList.forEach(System.out::println);
    }

    /**
     * 带错误处理的User数据读取
     * 在读取过程中进行数据验证和异常处理
     * 适用于数据质量不确定的场景
     */
    @Test
    public void readUserDataWithErrorHandling() throws Exception {
        String filePath = "target/output_user_data.xlsx";

        try {
            List<UserDTO> userList = EasyExcel.read(filePath)
                    .head(UserDTO.class)
                    .registerReadListener(new UserDataListenerWithErrorHandling())
                    .sheet()
                    .doReadSync();

            System.out.println("成功读取 " + userList.size() + " 条数据");
        } catch (Exception e) {
            System.err.println("读取Excel文件时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 带错误处理的User数据监听器
     * 在数据处理过程中进行验证和异常捕获
     */
    public class UserDataListenerWithErrorHandling extends AnalysisEventListener<UserDTO> {
        private List<UserDTO> userList = new ArrayList<>();

        /**
         * 处理每行数据，包含数据验证和异常处理
         *
         * @param userDTO 用户数据对象
         * @param context 解析上下文
         */
        @Override
        public void invoke(UserDTO userDTO, AnalysisContext context) {
            try {
                // 数据验证
                if (userDTO.getUserId() == null || userDTO.getUserId().isEmpty()) {
                    System.err.println("发现无效数据，用户ID为空: " + userDTO);
                    return;
                }
                userList.add(userDTO);
            } catch (Exception e) {
                System.err.println("处理数据时发生错误: " + e.getMessage());
            }
        }

        /**
         * 所有数据处理完成后调用
         *
         * @param context 解析上下文
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("数据读取完成，共处理 " + userList.size() + " 条有效数据");
        }

        /**
         * 获取处理后的用户数据列表
         *
         * @return 有效用户数据列表
         */
        public List<UserDTO> getUserList() {
            return userList;
        }
    }

    /**
     * 读取数据到Map列表
     * 将Excel数据读取为Map格式，不进行对象映射
     * 适用于需要灵活处理原始数据的场景
     */
    @Test
    public void readUserDataToMap() throws Exception {
        String filePath = "target/output_user_data.xlsx";

        // 读取数据到Map列表
        List<Map<Integer, String>> dataList = EasyExcel.read(filePath)
                .sheet()
                .doReadSync();

        // 输出读取的数据
        dataList.forEach(row -> {
            System.out.println("行数据: " + row);
        });
    }

    /**
     * 完整的User数据读取示例
     * 包含文件存在性检查、资源管理和异常处理的完整流程
     * 适用于生产环境的数据读取
     */
    @Test
    public void completeReadExample() throws Exception {
        String filePath = "target/output_user_data.xlsx";

        // 检查文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("文件不存在: " + filePath);
            return;
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            // 读取Excel文件
            List<UserDTO> userList = EasyExcel.read(inputStream)
                    .head(UserDTO.class)
                    .sheet()
                    .doReadSync();

            System.out.println("总共读取到 " + userList.size() + " 条用户数据:");
            userList.forEach(user -> {
                System.out.println("用户ID: " + user.getUserId() +
                        ", 用户名称: " + user.getUserName() +
                        ", 用户手机: " + user.getUserPhone());
            });

        } catch (Exception e) {
            System.err.println("读取文件时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取target目录下的文件路径
     *
     * @param fileName 文件名
     * @return target目录下的完整路径
     */
    private String getTargetPath(String fileName) throws Exception {
        // 获取target目录
        File targetDir = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile();
        // 确保target目录存在
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        // 返回文件路径
        return new File(targetDir, fileName).getAbsolutePath();
    }

}
