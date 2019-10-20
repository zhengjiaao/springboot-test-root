package com.dist.utils;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

/**
 * POI工具类 功能点： 
 * 1、实现excel的sheet复制，复制的内容包括单元的内容、样式、注释
 * @author Administrator
 */
public final class POISheetCopyUtils {

	/**
	 * 功能：拷贝sheet
	 * 实际调用 	copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true)
	 * @param targetSheet
	 * @param sourceSheet
	 * @param targetWork
	 * @param sourceWork                                                                   
	 */
	public static void copySheet(XSSFSheet targetSheet, XSSFSheet sourceSheet,
			XSSFWorkbook targetWork, XSSFWorkbook sourceWork) throws Exception{
		assertNotNull(targetSheet);
		assertNotNull(sourceSheet);
		assertNotNull(targetWork);
		assertNotNull(sourceWork);
		
		copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true);
	}

	/**
	 * 功能：拷贝sheet
	 * @param targetSheet
	 * @param sourceSheet
	 * @param targetWork
	 * @param sourceWork
	 * @param copyStyle					boolean 是否拷贝样式
	 */
	public static void copySheet(XSSFSheet targetSheet, XSSFSheet sourceSheet,
			XSSFWorkbook targetWork, XSSFWorkbook sourceWork, boolean copyStyle)throws Exception {
		assertNotNull(targetSheet);
		assertNotNull(sourceSheet);
		assertNotNull(targetWork);
		assertNotNull(sourceWork);
		
		//复制源表中的行
		int maxColumnNum = 0;

		Map<String,XSSFCellStyle> styleMap = (copyStyle) ? new HashMap<String,XSSFCellStyle>() : null;
		
		XSSFDrawing patriarch = targetSheet.createDrawingPatriarch(); //用于复制注释
		for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
			XSSFRow sourceRow = sourceSheet.getRow(i);
			XSSFRow targetRow = targetSheet.createRow(i);
			if (sourceRow != null) {
				copyRow(targetRow, sourceRow, targetWork, sourceWork,patriarch, styleMap);
				if (sourceRow.getLastCellNum() > maxColumnNum) {
					maxColumnNum = sourceRow.getLastCellNum();
				}
			}
		}
		
		//复制源表中的合并单元格
		mergerRegion(targetSheet, sourceSheet);
		
		//设置目标sheet的列宽
		for (int i = 0; i <= maxColumnNum; i++) {
			targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
		}
		
	}
	
	/**
	 * 功能：拷贝row
	 * @param targetRow
	 * @param sourceRow
	 * @param styleMap
	 * @param targetWork
	 * @param sourceWork
	 * @param targetPatriarch
	 */
	private static void copyRow(XSSFRow targetRow, XSSFRow sourceRow,
			XSSFWorkbook targetWork, XSSFWorkbook sourceWork,XSSFDrawing targetPatriarch, Map<String,XSSFCellStyle> styleMap) throws Exception {
		
		assertNotNull(targetRow);
		assertNotNull(sourceRow);
		assertNotNull(targetWork);
		assertNotNull(sourceWork);
		assertNotNull(targetPatriarch);
		
		if(sourceRow.getFirstCellNum()<0){
		   return;
		}
		
		for (int i = sourceRow.getFirstCellNum(); i <= sourceRow.getLastCellNum(); i++) {
			XSSFCell sourceCell = sourceRow.getCell(i);
			XSSFCell targetCell = targetRow.getCell(i);
			
			if (sourceCell != null) {
				if (targetCell == null) {
					targetCell = targetRow.createCell(i);
				}
				
				//拷贝单元格，包括内容和样式
				copyCell(targetCell, sourceCell, targetWork, sourceWork, styleMap);
				
				//拷贝单元格注释
				copyComment(targetCell,sourceCell,targetPatriarch);
			}
		}
		
		//设置行高sourceRow.getHeight()
		targetRow.setHeight((short) -1);
	}
	
	/**
	 * 功能：拷贝cell，依据styleMap是否为空判断是否拷贝单元格样式
	 * @param targetCell			不能为空
	 * @param sourceCell			不能为空
	 * @param targetWork			不能为空
	 * @param sourceWork			不能为空
	 * @param styleMap				可以为空				
	 */
	private static void copyCell(XSSFCell targetCell, XSSFCell sourceCell, XSSFWorkbook targetWork, XSSFWorkbook sourceWork,Map<String,XSSFCellStyle> styleMap) {
		
		assertNotNull(targetCell);
		assertNotNull(sourceCell);
		assertNotNull(targetWork);
		assertNotNull(sourceWork);
		
		//处理单元格样式
		if(styleMap != null){
			if (targetWork == sourceWork) {
				targetCell.setCellStyle(sourceCell.getCellStyle());
			} else {
				String stHashCode =  String.valueOf(sourceCell.getCellStyle().hashCode());
				XSSFCellStyle targetCellStyle = (XSSFCellStyle) styleMap .get(stHashCode);
				if (targetCellStyle == null) {
					targetCellStyle = targetWork.createCellStyle();
					targetCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
					styleMap.put(stHashCode, targetCellStyle);
				}
				
				targetCell.setCellStyle(targetCellStyle);
			}
		}
		
		//处理单元格内容
		switch (sourceCell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			targetCell.setCellValue(sourceCell.getRichStringCellValue());
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			targetCell.setCellValue(sourceCell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			targetCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			targetCell.setCellValue(sourceCell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			targetCell.setCellFormula(sourceCell.getCellFormula());
			break;
		default:
			break;
		}
	}
	
	/**
	 * 功能：拷贝comment
	 * @param targetCell
	 * @param sourceCell
	 * @param targetPatriarch
	 */
	private static void copyComment(XSSFCell targetCell,XSSFCell sourceCell,XSSFDrawing targetPatriarch)throws Exception{
		
		assertNotNull(targetCell);
		assertNotNull(sourceCell);
		assertNotNull(targetPatriarch);
		
		//处理单元格注释
		XSSFComment comment = sourceCell.getCellComment();
		
		if(comment != null){
			XSSFComment newComment = targetPatriarch.createCellComment(new XSSFClientAnchor());
			
			newComment.setAuthor(comment.getAuthor());
			newComment.setColumn(comment.getColumn());
			//newComment.setFillColor(comment.getFillColor());
			//newComment.setHorizontalAlignment(comment.getHorizontalAlignment());
			//newComment.setLineStyle(comment.getLineStyle());
			//newComment.setLineStyleColor(comment.getLineStyleColor());
			//newComment.setLineWidth(comment.getLineWidth());
			//newComment.setMarginBottom(comment.getMarginBottom());
			//newComment.setMarginLeft(comment.getMarginLeft());
			//newComment.setMarginTop(comment.getMarginTop());
			//newComment.setMarginRight(comment.getMarginRight());
			//newComment.setNoFill(comment.isNoFill());
			newComment.setRow(comment.getRow());
			//newComment.setShapeType(comment.getShapeType());
			newComment.setString(comment.getString());
			//newComment.setVerticalAlignment(comment.getVerticalAlignment());
			newComment.setVisible(comment.isVisible());
			
			targetCell.setCellComment(newComment);
		}
	}
	
	/**
	 * 功能：复制原有sheet的合并单元格到新创建的sheet
	 * 
	 * @param targetSheet
	 * @param sourceSheet
	 */
	private static void mergerRegion(XSSFSheet targetSheet, XSSFSheet sourceSheet)throws Exception {
		 
		assertNotNull(targetSheet);
		assertNotNull(sourceSheet);
		
		for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
			CellRangeAddress oldRange =   sourceSheet.getMergedRegion(i);
			CellRangeAddress newRange = new CellRangeAddress( oldRange.getFirstRow(), oldRange.getLastRow(), oldRange.getFirstColumn(), oldRange.getLastColumn());
			targetSheet.addMergedRegion(newRange);
		}
	}

 
	
	public static void main(String[] args) throws Exception {
		XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream("H:\\_1.xlsx"));
		
		XSSFWorkbook sourceBook = new XSSFWorkbook(new FileInputStream("H:\\OS_Report.xlsx")); 
		XSSFSheet sourceSheet = sourceBook.getSheetAt(0);
		
		XSSFSheet destSheet = workBook.getSheetAt(4);
		
		copySheet(destSheet,sourceSheet,workBook,sourceBook,true);
		
		FileOutputStream out = new FileOutputStream("H:/output.xlsx");
		workBook.write(out);
	    out.close();
	}
}

	
	

