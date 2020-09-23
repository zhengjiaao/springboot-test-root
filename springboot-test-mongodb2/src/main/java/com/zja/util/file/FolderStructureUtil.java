package com.zja.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建目录结构工具类
 *
 * @author yujx
 * @date 2019/05/06 16:21
 */
public abstract class FolderStructureUtil {

    /**
     * 遍历指定路径下的文件夹，并构建目录树信息
     *
     * @param dirPath
     * @return
     */
    public static FolderStructure getDirTree(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new RuntimeException("文件路径不存在！");
        }
        String effectiveDirPath = getEffectiveDirPath(dirPath);
        FolderStructure root = new FolderStructure();
        root.setPath(effectiveDirPath);
        root.setDepth(-1L);
        dirTreeInfo(root);      // 递归读取文件目录信息
        return root;
    }

    /**
     * 获取有效路径
     * zip打包解压之后可能会存在无效的目录层级,需要去除无效目录层级
     * C:\Users\Desktop\资源文件\949a32a4-8851-4352-bea6-bc636cc4a00f\949a32a4-8851-4352-bea6-bc636cc4a00f
     * 一级949a32a4-8851-4352-bea6-bc636cc4a00f目录属于无效目录
     *
     * @param dirPath
     * @return
     */
    private static String getEffectiveDirPath(String dirPath) {
        File dir = new File(dirPath);
        File[] nodes = dir.listFiles();
        if (nodes.length == 1) {
            File node = nodes[0];
            if (node.getName().equals(dir.getName())) {
                return getEffectiveDirPath(node.getPath());
            }
        }
        return dirPath;
    }

    /**
     * 递归遍历指定路径下的文件夹，并构建目录树信息
     *
     * @param folderStructure
     * @return
     */
    private static void dirTreeInfo(FolderStructure folderStructure) {
        File file = new File(folderStructure.getPath());
        if (file.isDirectory()) {
            folderStructure.setBeDir(true);
            folderStructure.setExpand(true);
            folderStructure.setName(file.getName());
            folderStructure.setPath(file.getPath());
            // 计算文件夹深度
            folderStructure.setDepth(folderStructure.getDepth() + 1);
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    FolderStructure childNode = new FolderStructure();
                    childNode.setPath(f.getPath());
                    childNode.setDepth(folderStructure.getDepth());
                    dirTreeInfo(childNode);       // 递归
                    if (null==folderStructure.getChildren()) {
                        List<FolderStructure> children = new ArrayList<>();
                        children.add(childNode);
                        folderStructure.setChildren(children);
                    } else {
                        folderStructure.getChildren().add(childNode);
                    }
                }
            }
        } else {
            folderStructure.setBeDir(false);
            folderStructure.setExpand(true);
            folderStructure.setName(file.getName());
            folderStructure.setPath(file.getPath());
            folderStructure.setDepth(null);
            folderStructure.setChildren(null);
        }
    }

}
