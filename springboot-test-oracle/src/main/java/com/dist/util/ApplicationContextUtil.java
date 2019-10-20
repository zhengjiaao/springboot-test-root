package com.dist.util;

import dist.common.procedure.define.ProcedureCaller;
import dist.common.procedure.define.ProcedureFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * @author jin 重要 加载procedure/*.xml存储过程文件
 */
@Component
public final class ApplicationContextUtil implements ApplicationContextAware {

	/**
	 * 存放存储过程文件夹的名称
	 */
	private static final String folderName = "procedure";

	private static Logger logger = LoggerFactory.getLogger(ApplicationContextUtil.class);

	@Autowired
	private static ApplicationContext appContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
		ProcedureCaller.appContext=applicationContext;
		loadProcedureModels();
	}

	public static void loadProcedureModels(){
		logger.info("loadProcedureModels files: ");

		URL procedure = Thread.currentThread().getContextClassLoader().getResource("procedure");
		String prcPath = "";
		try {
			prcPath = URLDecoder.decode(procedure.getPath(), "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		//File procedureDirectory = new File(procedure.getFile());
		File procedureDirectory = new File(prcPath);
		File[] files = FileUtils.getFile(procedureDirectory).listFiles();
		ArrayList<String> proList = new ArrayList<>();
		for(File f : files){
			String fileName = f.getName();
			String fileType = fileName.substring(fileName.indexOf(".") + 1);
			if("xml".equals(fileType)){
				proList.add(folderName + "/" + fileName);
				logger.info("Loading procedureXML List: [{}]", folderName + "/" + fileName);
			}
		}

		String[] result = proList.toArray(new String[proList.size()]);

		try{
			ProcedureFile.loadProcedureModels(result);
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
	}

	public static ApplicationContext getAppContext() {
		return appContext;
	}
	
	/**
	 * 
	 * 方法名 : getBean
	 *
	 * 描述 : 
	 *
	 * @param name
	 *    bean 唯一标签名称
	 * @return
	 *    返回实体对象
	 *
	 */
	public static Object getBean(String name) {
		return appContext.getBean(name);
	}

}
