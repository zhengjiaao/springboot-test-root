package com.zja.utils.tree;

import com.zja.utils.tree.dto.TreeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**生成树形结构方式二
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 14:46
 */
public class TreeUtil {
    private static final String FILE_TYPE = "file";
    private static final String DIRECTORY_TYPE = "directory";


    public static void main(String[] args) {
        Map<String, TreeDto> nodeMap = new HashMap<>();
        String[] paths = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
        loadDirs(paths, nodeMap);
        //JSONObject jsonObject = new JSONObject(nodeMap);
        //new JSONObject().toJSONString(nodeMap);
        System.out.println(nodeMap);
    }

    public static void loadDirs(String[] paths, Map<String, TreeDto> exists) {
        List<String> existsList = new ArrayList<>();
        for (String p : paths) {

            int idx = p.lastIndexOf("/");
            if (idx > -1) {
                String parentPath = p.substring(0, idx);
                //加载父节点
                loadRoom(parentPath, exists, existsList);
            }
            //加载当前文件
            loadRoom(p, exists, existsList);
        }
    }

    /**
     * 加载一条路径下的所有节点
     * @param path  路径
     * @param nodeMap  节点集（最后的节点收集器）
     * @param exists    已经存在的节点路径（用来判断当前节点是否已经被添加过）
     */
    public static void loadRoom(String path, Map<String, TreeDto> nodeMap, List<String> exists) {

        //当前路径已经存在
        if (exists.contains(path)) {
            return;
        }
        // 含文件的路径
        if (path.contains(".")) {
            int idx = path.lastIndexOf("/");
            String parentPath = path.substring(0, idx);
            //当前节点的名字
            String nodeName = path.substring(idx + 1);
            if (exists.contains(parentPath)) {

                TreeDto fileNode = new TreeDto();
                fileNode.setFullPath(path);
                fileNode.setLabel(nodeName);
                fileNode.setType(FILE_TYPE);

                //获取目标父节点
                TreeDto parentNode = getNodeByPath(parentPath, nodeMap);

                List<TreeDto> childrens = parentNode.getChildrens();
                if (childrens == null) {
                    childrens = new ArrayList<>();
                    parentNode.setChildrens(childrens);
                }
                childrens.add(fileNode);
            }
        } else {  //文件夹路径
            String nodeName = null;
            TreeDto parentNode = null;
            if (path.contains("/")) {
                int idx = path.lastIndexOf("/");
                String parentPath = path.substring(0, idx);
                nodeName = path.substring(idx + 1);
                loadRoom(parentPath, nodeMap, exists);
                parentNode = getNodeByPath(parentPath, nodeMap);
            } else {
                nodeName = path;
            }

            TreeDto dirNode = new TreeDto();
            dirNode.setFullPath(path);
            dirNode.setLabel(nodeName);
            dirNode.setType(DIRECTORY_TYPE);

            //子节点添加到父节点
            if (parentNode != null) {
                List<TreeDto> childrens = parentNode.getChildrens();
                if (childrens == null) {
                    childrens = new ArrayList<>();
                    parentNode.setChildrens(childrens);
                }
                childrens.add(dirNode);
            } else {
                nodeMap.put(path, dirNode);
            }
            //目录节点
            exists.add(path);
        }

    }

    /**
     * 根据路径获取该路径对应的节点
     * @param path 节点的路径（id）
     * @param nodeMap 节点集
     * @return
     */
    private static TreeDto getNodeByPath(String path, Map<String, TreeDto> nodeMap) {

        //根节点直接返回，nodeMap中第一层都是根节点
        if (nodeMap.containsKey(path)) {
            return nodeMap.get(path);
        }

        String[] ps = path.split("/");
        //根节点
        TreeDto node = nodeMap.get(ps[0]);
        return getDirNode(path, node, ps, 1);

    }

    /**
     * 根据路径获取该路径对应的节点
     * @param path 需要查找的节点路径
     * @param node 该节点所在的根节点
     * @param ps   该节点每一层的名称（不包括第一层，也是就根节点）
     * @param idx  当前所在层
     * @return
     */
    private static TreeDto getDirNode(String path, TreeDto node, String[] ps, int idx) {
        List<TreeDto> childrens = node.getChildrens();
        if (childrens == null) {
            childrens = new ArrayList<>();
            node.setChildrens(childrens);
        }
        for (TreeDto c_node : childrens) {
            String name = c_node.getLabel();
            //去掉多余的遍历
            if (!ps[idx].equals(name)) {
                continue;
            }
            if (path.equals(c_node.getFullPath())) {
                return c_node;
            }
            TreeDto targetNode = getDirNode(path, c_node, ps, idx + 1);
            if (targetNode != null) {
                return targetNode;
            }
        }
        return null;
    }
}
