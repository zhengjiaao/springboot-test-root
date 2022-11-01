package com.zja.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author yangmin
 * @date 2019/1/23 17:14
 * @desc
 */
public class WordReportTemplateDTO implements Serializable {

    private String name;

    private String paragraph;

    private Boolean hasImage;

    private String imagename;

    private String imagedata;

    private List<WordReportTemplateDTO> sections;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public Boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImagedata() {
        return imagedata;
    }

    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }

    public List<WordReportTemplateDTO> getSections() {
        return sections;
    }

    public void setSections(List<WordReportTemplateDTO> sections) {
        this.sections = sections;
    }
}
