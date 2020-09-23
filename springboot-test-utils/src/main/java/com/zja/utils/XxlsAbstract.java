package com.zja.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
 
 
public abstract class XxlsAbstract extends DefaultHandler { 

	private final String NUMBER="0123456789";
	
    private SharedStringsTable sst;
    private String lastContents; 
    private boolean nextIsString; 
    private int sheetIndex = -1; 
    private Map<String,String> rowMap = new LinkedHashMap<String, String>();
    private int curRow = 0; 
    private String columnName = null;
    
    /**
     * 代表excel的row的
     * key
     */
    private List<String> keys = new ArrayList<String>();
     
    /**
     * excel记录行操作方法，以sheet索引，行索引和行元素列表为参数，对sheet的一行元素进行操作，元素为String类型 
     * 该方法自动被调用，每读一行调用一次，在方法中写自己的业务逻辑即可 
     * @param sheetIndex 工作簿序号 
     * @param curRow 处理到第几行 
     * @param rowList 当前数据行的数据集合 
     * @throws SQLException
     */
    public abstract void optRows(int sheetIndex,int curRow, Map<String,String> rowMap) throws SAXException; 
     
    /**
     * 只遍历一个sheet，其中sheetId为要遍历的sheet索引，从1开始，1-3 
     * @param filename
     * @param sheetId
     * @throws Exception
     */
    public void processOneSheet(String filename,int sheetId) throws Exception { 
    	curRow = 0; 
        OPCPackage pkg = OPCPackage.open(filename); 
        XSSFReader r = new XSSFReader(pkg); 
        SharedStringsTable sst = r.getSharedStringsTable(); 
         
        XMLReader parser = fetchSheetParser(sst); 
 
        // rId2 found by processing the Workbook 
        // 根据 rId# 或 rSheet# 查找sheet 
        InputStream sheet2 = r.getSheet("rId"+sheetId); 
        sheetIndex++; 
        InputSource sheetSource = new InputSource(sheet2); 
        parser.parse(sheetSource); 
        sheet2.close(); 
        pkg.close();
    }
 
     
    /**
     * 解析整个EXCEL 
     * @param filename
     * @throws Exception
     */
    public void process(String filename) throws Exception { 
        OPCPackage pkg = OPCPackage.open(filename); 
        XSSFReader r = new XSSFReader(pkg); 
        SharedStringsTable sst = r.getSharedStringsTable(); 
 
        XMLReader parser = fetchSheetParser(sst); 
 
        Iterator<?> sheets = r.getSheetsData(); 
        while (sheets.hasNext()) { 
            curRow = 0; 
            sheetIndex++; 
            InputStream sheet = (InputStream) sheets.next(); 
            InputSource sheetSource = new InputSource(sheet); 
            parser.parse(sheetSource); 
            sheet.close(); 
        } 
        pkg.close();
    } 
 
    public XMLReader fetchSheetParser(SharedStringsTable sst) 
            throws SAXException { 
    	XMLReader parser = XMLReaderFactory.createXMLReader(); 
        this.sst = sst; 
        parser.setContentHandler(this); 
        return parser; 
    } 
 
    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
    	
        // c => 单元格 
        if (name.equals("c") ) { 
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true 
            String cellType = attributes.getValue("t"); 
            if (cellType != null && cellType.equals("s")) { 
                nextIsString = true; 
            } else { 
                nextIsString = false; 
            } 
            columnName = deleteNumber(attributes.getValue(0));
            
        } 
        
        
        // 置空 
        lastContents = ""; 
    } 
 

	@Override
    public void endElement(String uri, String localName, String name)  throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串 
        // 这时characters()方法可能会被调用多次 
        if (nextIsString) { 
            try { 
                int idx = Integer.parseInt(lastContents); 
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString(); 
            } catch (Exception e) { 
            } 
        } 
 
        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引 
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符 
        if (name.equals("v")) { 
            String value = lastContents.trim(); 
            value = value.equals("")?" ":value; 
            
            if(StringUtils.isBlank(columnName)){
            	throw new IllegalArgumentException("columnName must not blank");
            }
            rowMap.put(columnName, value);
        }else { 
            //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法 
            if (name.equals("row")) { 
                optRows(sheetIndex,curRow,rowMap); 
                rowMap.clear();
                columnName = StringUtils.EMPTY;
                curRow++; 
            } 
        } 
    } 
 
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException { 
        //得到单元格内容的值 
        lastContents += new String(ch, start, length); 
    } 
    
    
    /**
     * 删除字符串中的数字部分
     * @param rowName is not null
     * @return
     */
    private String deleteNumber(String rowName) {
    	char[] ch=rowName.toCharArray();
    	int position=-1;
    	
    	for(int i=ch.length-1; i >= 0;i--){
    		char a=ch[i];
    		if(NUMBER.indexOf(a)>-1){
    			continue;
    		}else{
    			position = i;
    			break;
    		}
    	}
    	
    	if(position == -1){
    		throw new  IllegalArgumentException(" error position in rowName "+ rowName);
    	}
    	
    	rowName = StringUtils.substring(rowName, 0,position+1);
    	
 		return rowName;
 	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
    
    
} 
