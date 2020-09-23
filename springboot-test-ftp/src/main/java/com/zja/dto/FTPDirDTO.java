package com.zja.dto;

import java.io.Serializable;
import java.util.List;

/**
 * ftp 目录结构
 *
 */
public class FTPDirDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;
	private String type;
	private String fullPath;
	private String extension;
	private List<FTPDirDTO> children;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public List<FTPDirDTO> getChildren() {
		return children;
	}
	public void setChildren(List<FTPDirDTO> children) {
		this.children = children;
	}
}
