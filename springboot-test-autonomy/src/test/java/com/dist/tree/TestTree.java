package com.dist.tree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dist.utils.tree.FileTreeUtil;
import com.dist.utils.tree.TreeUtil;
import com.dist.utils.tree.dto.TreeDto;
import com.dist.utils.tree.dto.TreeNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**生成树形结构的两种方式
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 14:54
 */
public class TestTree {

    @Test
    public void test(){
        Map<String, TreeDto> nodeMap = new HashMap<>();
        String[] paths = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
        TreeUtil.loadDirs(paths, nodeMap);
        String tree=new JSONObject().toJSONString(nodeMap);
        System.out.println(tree);
    }

    @Test
    public void test1(){
        String[] trees = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
        List<TreeNode> treeNodes = FileTreeUtil.getTree(trees);
        JSONArray jsonarr = JSONArray.parseArray(JSON.toJSONString(treeNodes));
        System.out.println(jsonarr.toJSONString());
    }

}
