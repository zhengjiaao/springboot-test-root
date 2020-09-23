package com.zja.controller.tree;

import com.zja.utils.tree.FileTreeUtil;
import com.zja.utils.tree.TreeUtil;
import com.zja.utils.tree.dto.TreeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 15:29
 */
@Api(tags = {"TreeController"},description = "生成树形结构测试")
@RequestMapping(value = "rest/tree")
@RestController
public class TreeController {

    /*@Autowired
    private TreeNodeRepository repository;

    @ApiOperation(value = "查询数据库生成树形结构",httpMethod = "GET")
    @RequestMapping(value = "v1/gettreenode",method = RequestMethod.GET)
    public Object getTreeNode() {
        List<TreeNode> nodes = repository.findAll();
        List<Tree> treeNodes = new ArrayList<>();
        nodes.forEach(node -> {
            treeNodes.add(node);
        });
        return TreeUtils.getTree(treeNodes, 0);
    }*/

    @ApiOperation(value = "生成树形结构方式一",httpMethod = "GET")
    @RequestMapping(value = "v1/gettree",method = RequestMethod.GET)
    public Object treeNodes(){
        String[] trees = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
        List<com.zja.utils.tree.dto.TreeNode> treeNodes = FileTreeUtil.getTree(trees);
        return treeNodes;
    }

    @ApiOperation(value = "生成树形结构方式二",httpMethod = "GET")
    @RequestMapping(value = "v2/gettree",method = RequestMethod.GET)
    public Object nodeMap(){
        Map<String, TreeDto> nodeMap = new HashMap<>();
        String[] paths = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
        TreeUtil.loadDirs(paths, nodeMap);
        return nodeMap;
    }

}
