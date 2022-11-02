package com.zja.base.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author yangmin
 * @date 2018/9/23 10:50
 * @desc 分页处理的封装类，优化序列化处理
 */
public class PageDTO implements Serializable{
    private List content;
    private Boolean last;
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;

    @Override
    public String toString() {
        return "PageDTO{" +
                "content=" + content +
                ", last=" + last +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", size=" + size +
                ", number=" + number +
                ", numberOfElements=" + numberOfElements +
                '}';
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
