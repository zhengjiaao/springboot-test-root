package com.zja;

import com.zja.dto.FtpCfgDTO;
import com.zja.util.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestFtp {

	private static Logger logger = LoggerFactory.getLogger(TestFtp.class);

	@Autowired
	@Qualifier("default")
	private FtpCfgDTO ftpCfgDTO;

	/**
	 * ftp文件上传
	 * @throws Exception
	 */
	@Test
	public void uploadFileFtpCfgDTO() throws Exception {
		String filePathName= "D:\\doc\\wordReportTemplate.ftl";
		String  newName="wordReportTemplate.ftl";
		FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
		boolean uploadFile = FtpUtil.uploadFile(ftpClient, filePathName, newName);
		logger.info("文件上传是否成功:"+uploadFile);
	}

	/**
	 * ftp文件下载
	 */
	@Test
	public void downloadFileFtpCfgDTO(){
		String localFileName ="D:\\doc\\aa\\wordReportTemplate.ftl";
		String remoteFilePath ="wordReportTemplate.ftl";
		FTPClient ftpClient = FtpUtil.connectServer(ftpCfgDTO);
		boolean downloadFile = false;
		try {
			downloadFile = FtpUtil.download(ftpClient, remoteFilePath, localFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("文件下载是否成功:"+downloadFile);
	}

}
