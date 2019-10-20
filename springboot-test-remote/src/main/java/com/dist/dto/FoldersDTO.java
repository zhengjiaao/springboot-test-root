package com.dist.dto;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by HGR on 2017/9/27.
 */
public class FoldersDTO {
	
	
    private String id;
    private String text;
    private String fileExtension;
    //fileType="directory","file"
    private String fileType;
    private boolean children;
    private String ftpPath;

    public String getId() {
        return id;
    }
    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public boolean isChildren() {
        return children;
    }

    public void setChildren(boolean children) {
        this.children = children;
    }
}
