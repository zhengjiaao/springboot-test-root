package com.dist.utils.excel;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.Assert;

import java.text.NumberFormat;
import java.util.*;

/**
 * User: yangy
 * Date: 2018/3/22
 * Time: 10:31
 * Description:Excel报表导出
 */
public class ExportReport {

    private int startLevel = 0;
    private int endLevel = 0;
    private int level = 0;
    private int currentRow = 0;
    private int maxCol = 0;
    private static CellStyle style;
    private static Font font;

    private NumberFormat nbf = null;
    private Double percent;

    private Double zero;

    private Double sumConsRate;

    private Double sumLandArea;

    private Double sumPerLandArea;

    {
        nbf = NumberFormat.getInstance();
        nbf.setMaximumFractionDigits(2);
        nbf.setMinimumFractionDigits(2);
        percent = Double.valueOf(nbf.format(100.00));
        zero = Double.valueOf(nbf.format(0.00));
        sumLandArea = Double.valueOf(nbf.format(0.00));
        sumConsRate = Double.valueOf(nbf.format(0.00));
        sumPerLandArea = Double.valueOf(nbf.format(0.00));
    }


    public HSSFSheet createSheet(HSSFWorkbook wkb, JSONObject jsonStr, String sheetName, String selectTopic, String diffTopic, String xzq, int diff) {
        HSSFSheet sheet = wkb.createSheet(sheetName);
        Map<String, Object> map = new HashMap<>();
        map.put(sheetName, jsonStr);
        JSONObject jsonObject = new JSONObject(map);
        Assert.notNull(jsonObject);
        JSONObject jsonObj = jsonObject.getJSONObject(sheetName);
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);

        for (int i = 0; i < jsonArray.size(); i++) {
            Double sumArea = 0.00;
            currentRow++;
            JSONObject jsObj = (JSONObject) jsonArray.get(i);
            //String type=jsObj.getString("Code")+"_"+jsObj.getString("Name");
            //用地名称属性
            String ydmc = jsObj.getString("ydmc");
            //面积属性
            Double area = jsObj.getDouble("area");
            //面积占比
            Double areaScale = jsObj.getDouble("scale");
            //地块数量
            Integer numCount = jsObj.getInteger("numCount");
            //地块数量占比
            Double countScale = jsObj.getDouble("countScale");
            HSSFRow headRow = sheet.createRow(currentRow);
            headRow.createCell(level).setCellValue(selectTopic);
            if (0 == diff) {
                headRow.createCell(level + 1).setCellValue(diffTopic);
                headRow.getCell(level + 1).setCellStyle(style);
            }
            headRow.createCell(level + 2 - diff).setCellValue(xzq);
            headRow.createCell(level + 3- diff).setCellValue(ydmc);
            headRow.createCell(level + 4- diff).setCellValue(nbf.format(area));
            headRow.createCell(level + 5- diff).setCellValue(nbf.format(area / 10000));
            headRow.createCell(level + 6- diff).setCellValue(nbf.format(area * 0.0015));
            headRow.createCell(level + 7- diff).setCellValue(nbf.format(areaScale));
            headRow.createCell(level + 8- diff).setCellValue(numCount + "");
            headRow.createCell(level + 9- diff).setCellValue(nbf.format(countScale));
            headRow.getCell(level).setCellStyle(style);
            headRow.getCell(level + 2- diff).setCellStyle(style);
            headRow.getCell(level + 3- diff).setCellStyle(style);
            headRow.getCell(level + 4- diff).setCellStyle(style);
            headRow.getCell(level + 5- diff).setCellStyle(style);
            headRow.getCell(level + 6- diff).setCellStyle(style);
            headRow.getCell(level + 7- diff).setCellStyle(style);
            headRow.getCell(level + 8- diff).setCellStyle(style);
            headRow.getCell(level + 9- diff).setCellStyle(style);
            JSONArray children = jsObj.getJSONArray("children");
            if (children != null && children.size() > 0) {
                level++;
                sheet = sheetLevel(sheet, children, selectTopic, diffTopic, xzq, diff);
                level--;
            }
        }
        return sheet;
    }

    public int check(String diffTopic) {
        if (null == diffTopic || "".equals(diffTopic)) {
            return  1;
        }
        return 0;
    }

    public HSSFSheet sheetLevel(HSSFSheet sheet, JSONArray jsonArray, String selectTopic, String diffTopic, String xzq, int diff) {
        startLevel++;
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);

        for (int i = 0; i < jsonArray.size(); i++) {
            currentRow++;
            JSONObject jsObj = (JSONObject) jsonArray.get(i);
//            String type=jsObj.getString("Code")+"_"+jsObj.getString("Name");
            String ydmc = jsObj.getString("ydmc");
            //面积属性
            Double area = jsObj.getDouble("area");
            //面积占比
            Double areaScale = jsObj.getDouble("scale");
            //地块数量
            Integer numCount = jsObj.getInteger("numCount");
            //地块数量占比
            Double countScale = jsObj.getDouble("countScale");

            HSSFRow headRow = sheet.createRow(currentRow);
            headRow.createCell(level).setCellValue(selectTopic);
            if (0 == diff) {
                headRow.createCell(level + 1).setCellValue(diffTopic);
                headRow.getCell(level + 1).setCellStyle(style);
            }
            headRow.createCell(level + 2 - diff).setCellValue(xzq);
            headRow.createCell(level + 3- diff).setCellValue(ydmc);
            headRow.createCell(level + 4- diff).setCellValue(nbf.format(area));
            headRow.createCell(level + 5- diff).setCellValue(nbf.format(area / 10000));
            headRow.createCell(level + 6- diff).setCellValue(nbf.format(area * 0.0015));
            headRow.createCell(level + 7- diff).setCellValue(nbf.format(areaScale));
            headRow.createCell(level + 8- diff).setCellValue(numCount + "");
            headRow.createCell(level + 9- diff).setCellValue(nbf.format(countScale));
            headRow.getCell(level).setCellStyle(style);
            headRow.getCell(level + 2- diff).setCellStyle(style);
            headRow.getCell(level + 3- diff).setCellStyle(style);
            headRow.getCell(level + 4- diff).setCellStyle(style);
            headRow.getCell(level + 5- diff).setCellStyle(style);
            headRow.getCell(level + 6- diff).setCellStyle(style);
            headRow.getCell(level + 7- diff).setCellStyle(style);
            headRow.getCell(level + 8- diff).setCellStyle(style);
            headRow.getCell(level + 9- diff).setCellStyle(style);
            JSONArray children = jsObj.getJSONArray("children");
            if (children != null && children.size() > 0) {
                level++;
                sheet = sheetLevel(sheet, children, selectTopic, diffTopic, xzq, diff);
                level--;
            }
        }
        return sheet;
    }

    public HSSFSheet sort(HSSFSheet sheet, int diff) {
        // 有效列
        int startIndex = maxCol - 6;
        int endIndex = 4 - diff;
        // 总记录行数
        int rowNum = sheet.getLastRowNum();
        for (int i = startIndex-1; i > endIndex-1; i--) {
            for (int j = 1; j <= rowNum; j++) {
                String cellValue = sheet.getRow(j).getCell(i).getStringCellValue();
                if (!"".equals(cellValue)) {
                    String s = sheet.getRow(j - 1).getCell(i - 1).getStringCellValue();
                    HSSFCell cell = sheet.getRow(j).getCell(i - 1);
                    if (null == cell) {
                        sheet.getRow(j).createCell(i - 1).setCellValue(s);
                        System.out.println(i+"=="+j);
                    } else {
                        cell.setCellValue(s);
                    }
                }
            }
        }
        return sheet;
    }

    public HSSFSheet sortSheet(HSSFSheet sheet, String type, int diff) {
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        int rowNum = sheet.getLastRowNum();
        for (int k = 1; k < rowNum; k++) {
            HSSFRow row = sheet.getRow(k);
            int colNum = row.getLastCellNum();
            if (colNum > maxCol) {
                maxCol = colNum;
            }
        }

        System.out.println("rowNum:" + rowNum + ";colNum:" + maxCol);
        for (int i = 1; i < rowNum + 1; i++) {
            HSSFRow currentRow = sheet.getRow(i);
            int colNum = currentRow.getLastCellNum();

            if (colNum <= maxCol && colNum > 0) {
                if (!"33".equals(type)) {
                    HSSFCell cellSelectBefore = currentRow.getCell(colNum - 10 + diff);
                    HSSFCell cellSelect = currentRow.createCell(maxCol - 13 + diff);
                    cellSelect.setCellValue(String.valueOf(cellSelectBefore.getRichStringCellValue()));
                    cellSelect.setCellStyle(style);
                    cellSelectBefore.setCellValue("");

                    if (diff == 0) {
                        HSSFCell cellDiffBefore = currentRow.getCell(colNum - 9);
                        HSSFCell cellDiff = currentRow.createCell(maxCol - 12);
                        cellDiff.setCellValue(String.valueOf(cellDiffBefore.getRichStringCellValue()));
                        cellDiffBefore.setCellValue("");
                        cellDiff.setCellStyle(style);
                    }

                    HSSFCell cellContentBefore = currentRow.getCell(colNum - 8);
                    HSSFCell cellContent = currentRow.createCell(maxCol - 11);
                    cellContent.setCellValue(String.valueOf(cellContentBefore.getRichStringCellValue()));
                    cellContent.setCellStyle(style);
                    cellContentBefore.setCellValue("");
                }

                HSSFCell cellAreaBefore = currentRow.getCell(colNum - 6);
                HSSFCell cellAreaGQBefore = currentRow.getCell(colNum - 5);
                HSSFCell cellAreaMBefore = currentRow.getCell(colNum - 4);
                HSSFCell cellAreaScaleBefore = currentRow.getCell(colNum - 3);
                HSSFCell cellNumBefore = currentRow.getCell(colNum - 2);
                HSSFCell cellNumScaleBefore = currentRow.getCell(colNum - 1);
                HSSFCell cellArea = currentRow.createCell(maxCol - 6);
                HSSFCell cellAreaGQ = currentRow.createCell(maxCol - 5);
                HSSFCell cellAreaM = currentRow.createCell(maxCol - 4);
                HSSFCell cellAreaScale = currentRow.createCell(maxCol - 3);
                HSSFCell cellNum = currentRow.createCell(maxCol - 2);
                HSSFCell cellNumScale = currentRow.createCell(maxCol - 1);
                cellArea.setCellValue(String.valueOf(cellAreaBefore.getRichStringCellValue()));
                cellArea.setCellStyle(style);
                cellAreaGQ.setCellValue(String.valueOf(cellAreaGQBefore.getRichStringCellValue()));
                cellAreaGQ.setCellStyle(style);
                cellAreaM.setCellValue(String.valueOf(cellAreaMBefore.getRichStringCellValue()));
                cellAreaM.setCellStyle(style);
                cellAreaScale.setCellValue(String.valueOf(cellAreaScaleBefore.getRichStringCellValue()));
                cellAreaScale.setCellStyle(style);
                cellNum.setCellValue(String.valueOf(cellNumBefore.getRichStringCellValue()));
                cellNum.setCellStyle(style);
                cellNumScale.setCellValue(String.valueOf(cellNumScaleBefore.getRichStringCellValue()));
                cellNumScale.setCellStyle(style);
                cellAreaBefore.setCellValue("");
                cellAreaScaleBefore.setCellValue("");
                cellNumBefore.setCellValue("");
                cellNumScaleBefore.setCellValue("");
                cellAreaGQBefore.setCellValue("");
                cellAreaMBefore.setCellValue("");
            }
        }

        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("选择数据");
        if (diff == 0) {
            titleRow.createCell(1).setCellValue("对比数据");
        }
        titleRow.createCell(2 - diff).setCellValue("行政区域");
        titleRow.createCell(3 - diff).setCellValue("统计内容");
        titleRow.createCell(maxCol - 6).setCellValue("面积(平方米)");
        titleRow.createCell(maxCol - 5).setCellValue("面积(公顷)");
        titleRow.createCell(maxCol - 4).setCellValue("面积(亩)");
        titleRow.createCell(maxCol - 3).setCellValue("面积占比(%)");
        titleRow.createCell(maxCol - 2).setCellValue("图斑个数(个)");
        titleRow.createCell(maxCol - 1).setCellValue("图斑个数占比(%)");

        for (int j = 0; j < maxCol; j++) {
            sheet.autoSizeColumn(j, true);
        }
        return sheet;
    }


    public HSSFSheet mergeSheet(HSSFSheet sheet) {
        int rowNum = sheet.getLastRowNum();
        int startNum = rowNum;
        int endNum = rowNum;
        foreach(sheet, rowNum, startNum, endNum);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol - 5));
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFRow titleRow = sheet.getRow(0);
        //titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(maxCol - 4).setCellStyle(style);
        titleRow.getCell(maxCol - 3).setCellStyle(style);
        titleRow.getCell(maxCol - 2).setCellStyle(style);
        titleRow.getCell(maxCol - 1).setCellStyle(style);
        return sheet;
    }

    public HSSFSheet createLandProvideAndNeedSheet(HSSFWorkbook wkb, JSONArray jsonArray, String sheetName) {

        HSSFSheet sheet = wkb.createSheet(sheetName);
        Map<String, Object> map = new HashMap<>(16);
        map.put(sheetName, jsonArray);
        JSONObject jsonObject = new JSONObject(map);
        Assert.notNull(jsonObject);
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        for (int i = 0; i < jsonArray.size(); i++) {
            currentRow++;
            JSONObject jsObj = (JSONObject) jsonArray.get(i);
            sumConsRate = 0.00d;
            sumPerLandArea = 0.00d;
            //用地名称属性
            Set<String> set = jsObj.keySet();
            Iterator<String> it = set.iterator();
            String key = it.next();
            JSONObject jsonObject1 = jsObj.getJSONObject(key);
            String name = jsonObject1.getString("name");
            JSONArray children = jsonObject1.getJSONArray("children");
            HSSFRow headRow = sheet.createRow(currentRow);
            if (children != null && children.size() > 0) {
                level++;
                sheet = sheetLandProvideAndNeedLevel(sheet, children);
                level--;
            }
            headRow.createCell(level).setCellValue(key);
            headRow.createCell(level + 1).setCellValue(name);
            headRow.createCell(level + 2).setCellValue(nbf.format(sumLandArea));
            headRow.createCell(level + 3).setCellValue("");
            headRow.createCell(level + 4).setCellValue("");
            headRow.getCell(level).setCellStyle(style);
            headRow.getCell(level + 1).setCellStyle(style);
            headRow.getCell(level + 2).setCellStyle(style);
            headRow.getCell(level + 3).setCellStyle(style);
            headRow.getCell(level + 4).setCellStyle(style);
        }
        return sheet;
    }

    private HSSFSheet sheetLandProvideAndNeedLevel(HSSFSheet sheet, JSONArray jsonArray) {
        sumLandArea = 0.00d;
        startLevel++;
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        for (int i = 0; i < jsonArray.size(); i++) {
            currentRow++;
            JSONObject jsObj = (JSONObject) jsonArray.get(i);
            String subLandTypeNo = jsObj.getString("subLandTypeNo");
            String subLandType = jsObj.getString("subLandType");
            Double landArea = jsObj.getDouble("landArea");
            sumLandArea += landArea;
            Double consRate = jsObj.getDouble("consRate");
            sumConsRate = consRate;
            Double perLandArea = jsObj.getDouble("perLandArea");
            sumPerLandArea = perLandArea;
            HSSFRow headRow = sheet.createRow(currentRow);
            headRow.createCell(level).setCellValue(subLandTypeNo);
            headRow.createCell(level + 1).setCellValue(subLandType);
            headRow.createCell(level + 2).setCellValue(nbf.format(landArea));
            headRow.createCell(level + 3).setCellValue(nbf.format(consRate));
            headRow.createCell(level + 4).setCellValue(nbf.format(perLandArea));
            headRow.getCell(level).setCellStyle(style);
            headRow.getCell(level + 1).setCellStyle(style);
            headRow.getCell(level + 2).setCellStyle(style);
            headRow.getCell(level + 3).setCellStyle(style);
            headRow.getCell(level + 4).setCellStyle(style);
            JSONArray children = jsObj.getJSONArray("children");
            if (children != null && children.size() > 0) {
                level++;
//                sheet = sheetLevel(sheet, children);
                level--;
            }
        }
        return sheet;
    }

    public HSSFSheet sortLandProvideAndNeedSheet(HSSFSheet sheet) {
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        int rowNum = sheet.getLastRowNum();
        for (int k = 1; k < rowNum; k++) {
            HSSFRow row = sheet.getRow(k);
            int colNum = row.getLastCellNum();
            if (colNum > maxCol) {
                maxCol = colNum;
            }
        }

        System.out.println("rowNum:" + rowNum + ";colNum:" + maxCol);
        for (int i = 1; i < rowNum + 1; i++) {
            HSSFRow currentRow = sheet.getRow(i);
            int colNum = currentRow.getLastCellNum();

            if (colNum < maxCol && colNum > 0) {
                HSSFCell cellLandAreaBefore = currentRow.getCell(colNum - 3);
                HSSFCell cellConsRateBefore = currentRow.getCell(colNum - 2);
                HSSFCell cellPerLandAreaBefore = currentRow.getCell(colNum - 1);
                HSSFCell cellLandArea = currentRow.createCell(maxCol - 3);
                HSSFCell cellConsRate = currentRow.createCell(maxCol - 2);
                HSSFCell cellPerLandArea = currentRow.createCell(maxCol - 1);
                cellLandAreaBefore.setCellValue(String.valueOf(cellLandArea.getRichStringCellValue()));
                cellLandAreaBefore.setCellStyle(style);
                cellConsRate.setCellValue(String.valueOf(cellConsRateBefore.getRichStringCellValue()));
                cellConsRate.setCellStyle(style);
                cellPerLandArea.setCellValue(String.valueOf(cellPerLandAreaBefore.getRichStringCellValue()));
                cellPerLandArea.setCellStyle(style);
            }
        }

        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("统计要素");
        titleRow.createCell(maxCol - 3).setCellValue("用地面积");
        titleRow.createCell(maxCol - 2).setCellValue("占建设用地面积比例");
        titleRow.createCell(maxCol - 1).setCellValue("人均用地面积");

        for (int j = 0; j < maxCol; j++) {
            sheet.autoSizeColumn(j, true);
        }

        return sheet;
    }

    public HSSFSheet mergeLandProvideAndNeedSheet(HSSFSheet sheet) {
        int rowNum = sheet.getLastRowNum();
        int startNum = rowNum;
        int endNum = rowNum;
        foreach(sheet, rowNum, startNum, endNum);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol - 4));
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFRow titleRow = sheet.getRow(0);
        //titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(maxCol - 3).setCellStyle(style);
        titleRow.getCell(maxCol - 2).setCellStyle(style);
        titleRow.getCell(maxCol - 1).setCellStyle(style);
        return sheet;
    }

    private void foreach(HSSFSheet sheet, int rowNum, int startNum, int endNum) {
        for (int j = maxCol - 6; j >= 0; j--) {
            for (int m = rowNum; m > 0; m--) {
                HSSFRow row = sheet.getRow(m);
                HSSFCell cell = row.getCell(j);
                String data = "";
                if (cell != null) {
                    data = String.valueOf(cell.getRichStringCellValue().getString());
                }
                if (cell != null && data != null && data != "") {
                    if (startNum == endNum) {
                        endNum = m;
                        startNum = m;
                    } else {
                        if (m == rowNum) {
                            endNum = m;
                            startNum = m;
                        } else {
                            HSSFRow temprow = sheet.getRow(m + 1);
                            HSSFCell tempcell = temprow.getCell(j + 1);
                            String tempdata = "";
                            if (!(tempcell == null)) {
                                tempdata = String.valueOf(tempcell.getRichStringCellValue().getString());
                            }
                            if (tempcell != null && tempdata != null && tempdata != "") {
                                sheet.addMergedRegion(new CellRangeAddress(startNum, endNum - 1, j, j));
                                endNum = m;
                                startNum = m;
                            } else {
                                endNum = m;
                                startNum = m;
                            }
                        }
                    }
                } else {
                    startNum = m;
                }
            }
        }
    }

    public HSSFSheet createPopuForcastSheet(HSSFWorkbook wkb, JSONObject jsonObject, String sheetName) {

        HSSFSheet sheet = wkb.createSheet(sheetName);
        Assert.notNull(jsonObject);
        Set<String> set = jsonObject.keySet();
        Iterator<String> it = set.iterator();
        String key = it.next();
        JSONArray jsonArray = (JSONArray) jsonObject.get(key);
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        for (int i = 0; i < jsonArray.size(); i++) {
            currentRow++;
            JSONObject jsObj = (JSONObject) jsonArray.get(i);
            //用地名称属性
            String year = (String) jsObj.get("year");
            String xzq = (String) jsObj.get("xzq");
            Integer population = (Integer) jsObj.get("population");
            JSONArray children = jsObj.getJSONArray("children");
            HSSFRow headRow = sheet.createRow(currentRow);
            if (children != null && children.size() > 0) {
                level++;
                sheet = sheetLandProvideAndNeedLevel(sheet, children);
                level--;
            }
            headRow.createCell(level).setCellValue(year);
            headRow.createCell(level + 1).setCellValue(xzq);
            headRow.createCell(level + 2).setCellValue(population + "");
            headRow.getCell(level).setCellStyle(style);
            headRow.getCell(level + 1).setCellStyle(style);
            headRow.getCell(level + 2).setCellStyle(style);
        }
        return sheet;
    }

    public HSSFSheet sortPopuForcastSheet(HSSFSheet sheet) {
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        int rowNum = sheet.getLastRowNum();
        for (int k = 1; k < rowNum; k++) {
            HSSFRow row = sheet.getRow(k);
            int colNum = row.getLastCellNum();
            if (colNum > maxCol) {
                maxCol = colNum;
            }
        }

        System.out.println("rowNum:" + rowNum + ";colNum:" + maxCol);
        for (int i = 1; i < rowNum + 1; i++) {
            HSSFRow currentRow = sheet.getRow(i);
            int colNum = currentRow.getLastCellNum();

            if (colNum < maxCol && colNum > 0) {
                HSSFCell cellYearBefore = currentRow.getCell(colNum - 3);
                HSSFCell cellXzqBefore = currentRow.getCell(colNum - 2);
                HSSFCell cellPopulationBefore = currentRow.getCell(colNum - 1);
                HSSFCell cellYearArea = currentRow.createCell(maxCol - 3);
                HSSFCell cellXzqRate = currentRow.createCell(maxCol - 2);
                HSSFCell cellPopulationArea = currentRow.createCell(maxCol - 1);
                cellYearArea.setCellValue(String.valueOf(cellYearBefore.getRichStringCellValue()));
                cellYearArea.setCellStyle(style);
                cellXzqRate.setCellValue(String.valueOf(cellXzqBefore.getRichStringCellValue()));
                cellPopulationArea.setCellStyle(style);
                cellPopulationBefore.setCellValue(String.valueOf(cellPopulationBefore.getRichStringCellValue()));
                cellPopulationBefore.setCellStyle(style);
            }
        }

        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("年份");
        titleRow.createCell(maxCol - 2).setCellValue("行政区");
        titleRow.createCell(maxCol - 1).setCellValue("人口");

        for (int j = 0; j < maxCol; j++) {
            sheet.autoSizeColumn(j, true);
        }

        return sheet;
    }

    public HSSFSheet mergePopuForcastSheet(HSSFSheet sheet) {
        int rowNum = sheet.getLastRowNum();
        int startNum = rowNum;
        int endNum = rowNum;
        foreach(sheet, rowNum, startNum, endNum);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol - 3));
        Workbook tempwkb = sheet.getWorkbook();
        font = tempwkb.createFont();
        style = tempwkb.createCellStyle();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFRow titleRow = sheet.getRow(0);
        //titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(0).setCellStyle(style);
        titleRow.getCell(maxCol - 2).setCellStyle(style);
        titleRow.getCell(maxCol - 1).setCellStyle(style);
        return sheet;
    }
}
