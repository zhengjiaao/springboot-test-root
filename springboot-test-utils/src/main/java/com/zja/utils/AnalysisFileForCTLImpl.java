package com.zja.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class AnalysisFileForCTLImpl {
	private static String temp = "";

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static void readFileByChars(String fileName) {
		Reader reader = null;
		try {
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
					temp += new String(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							temp += tempchars[i];
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static Map<String, String> getInfo(String filePath) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			AnalysisFileForCTLImpl.readFileByChars(filePath);
			int _class = AnalysisFileForCTLImpl.temp.indexOf("ABLOCK") > 0 ? AnalysisFileForCTLImpl.temp.indexOf("ABLOCK") : 0;
			int _label = AnalysisFileForCTLImpl.temp.indexOf("LABEL") > 0 ? AnalysisFileForCTLImpl.temp.indexOf("LABEL") : 0;

			if (_class >= _label) {
				return map;
			}
			String strTemp = AnalysisFileForCTLImpl.temp.substring(_class, _label);
			strTemp = strTemp.replaceAll("\"", "").replaceAll("\'", "");

			if (strTemp.contains("CLASS=")) {
				temp = strTemp.substring(strTemp.indexOf("CLASS=") + 6, strTemp.length());
				String CLASS = temp.substring(0, temp.indexOf(" ") >= 0 ? temp.indexOf(" ") : 0);
				map.put("CLASS", CLASS);
			}
			if (strTemp.contains("NAME=")) {
				temp = strTemp.substring(strTemp.indexOf("NAME=") + 5, strTemp.length());
				String name = temp.substring(0, temp.indexOf(" ") >= 0 ? temp.indexOf(" ") : 0);
				map.put("NAME", name);
			}
			if (strTemp.contains("VAR=")) {
				temp = strTemp.substring(strTemp.indexOf("VAR=") + 4, strTemp.length());
				String var = temp.substring(0, temp.indexOf(" ") >= 0 ? temp.indexOf(" ") : 0);
				map.put("VAR", var);
			}
			if (strTemp.contains("REV=")) {
				temp = strTemp.substring(strTemp.indexOf("REV=") + 4, strTemp.length());
				String rev = temp.substring(0, temp.indexOf(" ") >= 0 ? temp.indexOf(" ") : 0);
				map.put("REV", rev);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
//		Map map = getInfo("C:\\Documents and Settings\\IBM\\桌面\\BE\\ABKVP.ctl");
		Map map = getInfo("C:\\Documents and Settings\\IBM\\桌面\\BE\\DBKVPE_5.10_1.ctl");
//		Map map = getInfo("C:\\Documents and Settings\\IBM\\桌面\\BE\\FKT_Alt_Demand_1.0.0_0\\_doc\\ABKVP_1.80_2.ctl");
		System.err.println("NAME:" + map.get("NAME"));
		System.err.println("CLASS:" + map.get("CLASS"));
		System.err.println("VAR:" + map.get("VAR"));
		System.err.println("REV:" + map.get("REV"));
	}
}