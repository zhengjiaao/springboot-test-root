package com.zja.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/29 10:59
 */
@Table(name = "ARS_MODEL_CALC_RESULT")
@Entity
@SequenceGenerator(name = "ID_SEQ", sequenceName = "SEQ_ARS_HIBERNATE", allocationSize = 1)
public class ArsModel implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
    private Long id;

    @Column(name = "KEY")
    private String key;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String value;

    // 审查任务id   @See ReviewTask.id
    @Column(name = "REVIEWTASKID")
    private Long reviewTaskId;

    // 审查要点id   @See ReviewPoint.id
    @Column(name = "REVIEWPOINTID")
    private Long reviewPointId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getReviewTaskId() {
        return reviewTaskId;
    }

    public void setReviewTaskId(Long reviewTaskId) {
        this.reviewTaskId = reviewTaskId;
    }

    public Long getReviewPointId() {
        return reviewPointId;
    }

    public void setReviewPointId(Long reviewPointId) {
        this.reviewPointId = reviewPointId;
    }

    public ArsModel() {
    }

    public ArsModel(String key, String value, Long reviewTaskId, Long reviewPointId) {
        this.key = key;
        this.value = value;
        this.reviewTaskId = reviewTaskId;
        this.reviewPointId = reviewPointId;
    }
}
