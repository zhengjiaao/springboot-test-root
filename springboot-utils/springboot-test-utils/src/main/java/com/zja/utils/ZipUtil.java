package com.zja.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {

	private static final int BUFFEREDSIZE = 1024;

	/**
	 * zip文件解压缩,支持中文文件名,支持多层目录解压,此方法解压出的文件目录结构和文件数据都与原来的一样
	 *
	 * @param zipFileName zip压缩文件的文件名,如:E:\\ok.zip
	 * @param targetDir 解压到目标文件夹,如:E:\\deal\\
	 * @throws IOException
	 */
	public static void unZip(String zipFileName, String targetDir) throws IOException {
		log.info("unZip file :" + zipFileName + ",target dir is:" + targetDir);
		unZip(zipFileName, targetDir, null, true);
	}

	/**
	 * zip文件解压缩,支持中文文件名,支持多层目录解压
	 *
	 * @param zipFileName zip压缩文件的文件名,如:E:\\ok.zip
	 * @param targetDir 解压到目标文件夹,如:E:\\deal\\
	 * @param filter 如果filter不为空，则只解压某此文件，filter支持简单文件名（my.txt）和*.扩展名(*.xml)
	 * @param keepDir 是否保持原来的目录结构不变，如果是false，则解压出来的文件都在目标文件夹下，没有子目录
	 * @return
	 * @throws IOException
	 */
	public static void unZip(String zipFileName, String targetDir, Set<String> filter, boolean keepDir) {
		try {
			unZip(new File(zipFileName), targetDir, filter, keepDir);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("zip file error ,the exception is -->{}", e);
		}
	}

	/**
	 * zip文件解压缩,支持中文文件名,支持多层目录解压
	 *
	 * @param zipfile zip压缩文件的文件
	 * @param targetDir 解压到目标文件夹,如:E:\\deal\\
	 * @param filter 如果filter不为空，则只解压某此文件，filter支持简单文件名（my.txt）和*.扩展名(*.xml)
	 * @param keepDir 是否保持原来的目录结构不变，如果是false，则解压出来的文件都在目标文件夹下，没有子目录
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized void unZip(File zipfile, String targetDir, Set<String> filter, boolean keepDir) throws IOException {
		InputStream in = null;
		FileOutputStream out = null;
		ZipFile zipFile = new ZipFile(zipfile);
		try {
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					if (keepDir) {
						File file = new File(targetDir + File.separator + zipEntry.getName());
						file.mkdirs();
					}
				} else {
					String fullFileName = zipEntry.getName();
					fullFileName = fullFileName.replaceAll("\\\\", "/");
					String dirName = targetDir;
					if (keepDir) {
						dirName += fullFileName.substring(0, fullFileName.lastIndexOf("/") > 0 ? fullFileName.lastIndexOf("/") : fullFileName.length());
					}
					String fileName = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
					// 先判断文件的目录是否存在，如果不存在，则先创建目录
					File file = new File(dirName);
					if (!file.exists()) {
						file.mkdirs();
					}
					// 创建文件
					File f = new File(dirName + File.separator + fileName);
					f.createNewFile();
					in = zipFile.getInputStream(zipEntry);
					out = new FileOutputStream(f);
					int c;
					byte[] by = new byte[BUFFEREDSIZE];
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	/**
	 * 解压缩文件到指定的目录
	 *
	 * @param zipFileName
	 * @param extPlace
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void unzip(String zipFileName, String extPlace) throws IOException {
		InputStream in = null;
		OutputStream os = null;
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				String entryName = zipEntry.getName();
				String names[] = entryName.split("/");
				int length = names.length;
				String path = extPlace;
				for (int v = 0; v < length; v++) {
					if (v < length - 1) {
						path += names[v] + "/";
						new File(path).mkdir();
					} else { // 最后一个
						if (entryName.endsWith("/")) { // 为目录,则创建文件夹
							new File(extPlace + entryName).mkdir();
						} else {
							in = zipFile.getInputStream(zipEntry);
							os = new FileOutputStream(new File(extPlace + entryName));
							byte[] buf = new byte[BUFFEREDSIZE];
							int len;
							while ((len = in.read(buf)) > 0) {
								os.write(buf, 0, len);
							}
						}
					}
				}
			}
			zipFile.close();
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}

	/**
	 * 压缩指定的多个文件
	 *
	 * @param files
	 *            文件绝对路径列表
	 * @param zipfile
	 * @return
	 */
	public static synchronized void zipFiles(List<String> files, String zipfile) throws IOException {
		File ff = new File(zipfile);
		if (!ff.exists()) {
			ff.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(zipfile); // 1
		// 创建ZIP数据输出流对象
		ZipOutputStream zipOut = new ZipOutputStream(out);
		FileInputStream in = null;
		for (int i = 0; i < files.size(); i++) {
			String filepath = files.get(i);
			File f = new File(filepath);
			if (!f.exists()) {
				continue;
			}
			try {
				// 创建文件输入流对象
				in = new FileInputStream(filepath);
				// 创建指向压缩原始文件的入口
				ZipEntry entry = new ZipEntry(filepath.substring(filepath.lastIndexOf('/') + 1, filepath.length())); // 0
				zipOut.putNextEntry(entry);
				// 向压缩文件中输出数据
				int nNumber;
				byte[] buffer = new byte[BUFFEREDSIZE];
				while ((nNumber = in.read(buffer)) != -1) {
					zipOut.write(buffer, 0, nNumber);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
				}
				if (zipOut != null) {
					zipOut.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}
	}

	/**
	 * 压缩zip格式的压缩文件
	 * @param inputFile 需压缩文件
	 * @param zipFilename 输出文件及详细路径
	 * @throws IOException
	 */
	public static synchronized void zip(File inputFile, String zipFilename, Set<String> filter) throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
		try {
			zip(inputFile, out, inputFile.getName(), filter);
		} catch (IOException e) {
			throw e;
		} finally {
			out.close();
		}
	}

	public static synchronized void zip(String inputFilePath, String zipFilename, Set<String> filter) throws IOException {
		File file = new File(inputFilePath);
		zip(file, zipFilename, filter);
	}

    /**
     * 压缩zip格式的压缩文件
     * @param inputFile
     * @param out
     * @throws IOException
     */
	public static synchronized void zip(File inputFile, OutputStream out, Set<String> filter) throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(out);
		try {
			zip(inputFile, zipout, inputFile.getName(), filter);
		} catch (IOException e) {
			throw e;
		} finally {
			zipout.close();
		}
	}

	/**
	 * 压缩zip格式的压缩文件，结果用OutputStream向外面输出，不出现过渡文件
	 * @param inputFileName
	 * @param out
	 */
	public static synchronized void zip(String inputFileName, OutputStream out, Set<String> filter) throws IOException {
		File file = new File(inputFileName);
		zip(file, out, filter);
	}

	/**
	 * 压缩zip格式的压缩文件
	 * @param inputFile 需压缩文件
	 * @param out 输出压缩文件
	 * @param base 结束标识
	 * @throws IOException
	 */
	private static synchronized void zip(File inputFile, ZipOutputStream out, String base, Set<String> filter) throws IOException {
		if (filter.contains(base)) {
			return;
		}

		if (inputFile.isDirectory()) {
			File[] inputFiles = inputFile.listFiles();
			if (inputFiles.length == 0) {
				out.putNextEntry(new ZipEntry(base + File.separator));
			} else {
				for (int i = 0; i < inputFiles.length; i++) {
					zip(inputFiles[i], out, base + File.separator + inputFiles[i].getName(), filter);
				}
			}
		} else {
			if (base.length() > 0) {
				out.putNextEntry(new ZipEntry(base));
			} else {
				out.putNextEntry(new ZipEntry(inputFile.getName()));
			}

			FileInputStream in = new FileInputStream(inputFile);
			try {
				int c;
				byte[] by = new byte[BUFFEREDSIZE];
				while ((c = in.read(by)) != -1) {
					out.write(by, 0, c);
				}
			} catch (IOException e) {
				throw e;
			} finally {
				in.close();
			}
		}
	}

	/**
	 * 压缩文件或者目录
	 * @param baseDirName 压缩的根目录
	 * @param fileName 根目录下待压缩的文件或文件夹名， 星号*表示压缩根目录下的全部文件。
	 * @param targetFileName 目标ZIP文件
	 */
	public static void zipFile(String baseDirName, String targetFileName) {
		try {
			zip(new File(baseDirName), targetFileName, new HashSet<String>());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("zip file error ,the exception is -->{}", e);
		}
	}

    /**
     * 解压文件操作
     * @param zipFilePath zip文件路径
     * @param descDir 解压出来的文件保存的目录
     */
    public static void unZiFiles(String zipFilePath,String descDir){
        File zipFile=new File(zipFilePath);
        File pathFile=new File(descDir);

        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip=null;
        InputStream in=null;
        OutputStream out=null;

        try {
            zip=new ZipFile(zipFile);
            Enumeration<?> entries=zip.getEntries();
            while(entries.hasMoreElements()){
                ZipEntry entry=(ZipEntry) entries.nextElement();
                String zipEntryName=entry.getName();
                in=zip.getInputStream(entry);

                String outPath=(descDir+"/"+zipEntryName).replaceAll("\\*", "/");
                //判断路径是否存在，不存在则创建文件路径
                File file=new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经创建,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                out=new FileOutputStream(outPath);

                byte[] buf=new byte[4*1024];
                int len;
                while((len=in.read(buf))>=0){
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(zip!=null){
                    zip.close();
                }
                if(in!=null){
                    in.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	public static void main(String[] args) {
		String filePath = "E:\\123\\v13-7";

		try {
			Set<String>  filters = new HashSet<String>();
			filters.add("v13-7"+File.separator+"_bin");
			filters.add("v13-7"+File.separator+"_old");
			filters.add("v13-7"+File.separator+"CANECM"+File.separator+"workunit.lws.cc.db3");
			ZipUtil.zip(filePath, "e:\\11.zip",filters);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
	public static void compress(File sourceFile, ZipOutputStream zos, String name,boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[2 * 1024];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }

            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }

                }
            }
        }
    }
}
