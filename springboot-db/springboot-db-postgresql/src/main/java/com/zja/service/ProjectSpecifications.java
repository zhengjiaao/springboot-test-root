package com.zja.service;

import com.zja.entity.Project;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: zhengja
 * @Date: 2025-05-09 11:28
 */
public class ProjectSpecifications {

    public static Specification<Project> withCreateDateBetween(String fieldName , LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start == null) return cb.lessThanOrEqualTo(root.get(fieldName), end);
            if (end == null) return cb.greaterThanOrEqualTo(root.get(fieldName), start);
            return cb.between(root.get(fieldName), start, end);
        };
    }

    public static Specification<Project> withApprovalTimeBetween(String fieldName ,Date start, Date end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return null;
            if (start == null) return cb.lessThanOrEqualTo(root.get("approvalTime"), end);
            if (end == null) return cb.greaterThanOrEqualTo(root.get("approvalTime"), start);
            return cb.between(root.get("approvalTime"), start, end);
        };
    }
}
