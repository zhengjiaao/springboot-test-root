package com.dist.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HGR on 2017/9/27.
 */
public class XmlUtil {

    /**
     *
     * @param XML 待处理的XML字符串
     * @param name 命名节点名称，从根节点中选取命名节点。
     * @param mapping 命名节点下需要被处理的节点名称
     * @param translateClasses XML序列化的类信息，这些类需要使用XML注解。
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws JAXBException
     */
    public static List getNameNodeChildren(String XML, String name, String[] mapping, Class[] translateClasses) throws ParserConfigurationException, IOException, SAXException, JAXBException {
        return getNameNodeChildren(new ByteArrayInputStream(XML.getBytes()),name,mapping,translateClasses);
    }

    public static List getNameNodeChildren(InputStream inputStream, String name, String[] mapping, Class[] translateClasses) throws ParserConfigurationException, IOException, SAXException, JAXBException {

        //创建document
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document dt = db.parse(inputStream);
        //取道指定命名节点
        Element root = dt.getDocumentElement();

        return getNameNodeChildren(root,name,mapping,translateClasses);
    }

    public static List getNameNodeChildren(Element root, String name, String[] mapping, Class[] translateClasses) throws ParserConfigurationException, IOException, SAXException, JAXBException {
        //设置上下文，并创建解析器
        JAXBContext context = JAXBContext.newInstance(translateClasses);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //创建返回List
        List reList = new ArrayList();

        NodeList nameNodes = root.getElementsByTagName(name);

        for(int i =0;i<nameNodes.getLength();i++){
            Node nameNode = nameNodes.item(i);
            for(int j = 0;j<nameNode.getChildNodes().getLength();j++){
                Node node = nameNode.getChildNodes().item(j);
                int k;
                for(k = 0 ;k<mapping.length;k++){
                    if(mapping[k].equals(node.getNodeName())) {
                        break;
                    }
                }
                if(k == mapping.length) {
                    continue;
                }
                Object val = unmarshaller.unmarshal(node);
                reList.add(val);
            }
        }
        return reList;
    }

}
