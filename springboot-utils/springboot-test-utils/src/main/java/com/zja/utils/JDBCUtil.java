package com.zja.utils;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

	private static Properties properties = new Properties();
	private static String DRIVER = "";
	private static String URL = "";
	private static String USERNAME = "";
	private static String PASSWORD = "";

	static {
		FileInputStream in = null;
		String path = Thread.currentThread().getContextClassLoader().getResource("jdbc.properties").getPath().replace("%20", " ");
		try {
			in = new FileInputStream(path);
			properties.load(in);
			in.close();
			DRIVER = properties.getProperty("jdbc.driver");
			URL = properties.getProperty("jdbc.url");
			USERNAME = properties.getProperty("jdbc.username");
			PASSWORD = properties.getProperty("jdbc.password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开数据库连接
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			// 驱动加载
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}