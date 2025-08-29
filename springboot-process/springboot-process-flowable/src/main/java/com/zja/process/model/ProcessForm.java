package com.zja.process.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-08-15 14:41
 */
@Data
@Entity
public class ProcessForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String processInstanceId;
    private String taskId;
    private String formKey; // form-A, form-B, form-C
    private LocalDateTime submitTime;

    @Convert(converter = FormDataConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> formData;
}

