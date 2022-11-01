package com.zja.util;

import com.zja.dto.ModuleDTO;
import com.zja.dto.YmlDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-27 14:25
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：文件管理
 */
public class FileUtils {

    public static void main(String[] args) throws Exception {
        /*List<ModuleDTO> moduleDTOList = getModuleDirectory();
        System.out.println(moduleDTOList.toString());*/
    }

    /**
     * 获取模块目录 仅显示yml模块
     */
    public static List<ModuleDTO> getModuleDirectory(HttpServletRequest request) throws Exception {

        List<ModuleDTO> moduleDTOList = new ArrayList<>();

        // 第二种：获取项目路径    D:\IDEAWorkspace\hs-bluetooth-lock
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        String separator = File.separator;
        String[] strings = courseFile.split(separator);
        System.out.println("courseFile" + courseFile);
        String pathPrefix = strings[0];
        System.out.println(pathPrefix);

        //获取当前模块路径
        /*ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:*.yml");
        *//*for (int i = 0; i < resources.length; i++) {
            System.out.println(resources[i]);
        }*//*
        //originPath: /D:/GitHub_Private/springboot-test-root/springboot-test-yml/target/classes/application-dev.yml
        String firstModuleYmlPath = Arrays.stream(resources).findFirst().get().getURL().toString();
        // 获取当前模块名称
        String firstModulePath = "";
        String[] prevModulenameArr = firstModuleYmlPath.split("/");
        for (int i = 0; i < prevModulenameArr.length; i++) {
            if (prevModulenameArr[i].equals("target") && prevModulenameArr[i + 1].equals("classes")) {
                firstModulePath = prevModulenameArr[i - 1];
            }
        }

        //获取所有模块名称
        String path = firstModuleYmlPath.substring(6);
        String pathPrefix = path.substring(0, path.indexOf(firstModulePath));*/

        File file = new File(pathPrefix);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File folderElem : files) {
                if (folderElem.isDirectory()) {
                    ModuleDTO moduleDTO = new ModuleDTO();
                    List<YmlDTO> ymlDTOS = new ArrayList<>();
                    isExistsYml(folderElem, ymlDTOS);
                    if (ymlDTOS != null && !ymlDTOS.isEmpty()) {
                        moduleDTO.setModuleName(folderElem.getName());
                        moduleDTO.setModulePath(pathPrefix);
                        moduleDTO.setYmlDTOS(ymlDTOS);
                        moduleDTOList.add(moduleDTO);
                    }
                }
            }
        }
        return moduleDTOList;
    }


    /**
     * 获取模块yml配置内容
     */
    public List<String> getModuleYmlFileContent() {

        return null;
    }

    /**
     * 更新模块yml配置内容
     */
    public List<String> updeteModuleYmlFileContent() {

        return null;
    }


    /**
     * 获取yml文件信息
     *
     * @param classesPath classes路径
     * @param ymlDTOList  yml文件信息集合
     */
    private static void isExistsYml(File classesPath, List<YmlDTO> ymlDTOList) {
        if (classesPath.exists()) {
            File[] files = classesPath.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile()) {
                        if (getFileSufix(f.getName()).equals("yml")) {
                            YmlDTO ymlDTO = new YmlDTO();
                            ymlDTO.setYmlName(f.getName());
                            ymlDTO.setYmlPath(f.getAbsolutePath());
                            ymlDTOList.add(ymlDTO);
                        }
                    }
                }
            }
        }
    }


    // 获取文件后缀
    private static String getFileSufix(String filePath) {
        int splitIndex = filePath.lastIndexOf('.');
        return filePath.substring(splitIndex + 1);
    }

    //获取url切割
    private static String getUrlSufix(String url) {
        int splitIndex = url.lastIndexOf('/');
        return url.substring(splitIndex + 1);
    }

}
