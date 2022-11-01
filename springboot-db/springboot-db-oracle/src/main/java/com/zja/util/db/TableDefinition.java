package com.zja.util.db;

import java.io.Serializable;

/**
 * @author wsw
 * @Date 2019/7/19
 */
public class TableDefinition implements Serializable {

    // 列名
    private String columnName;

    // 数据类型
    private String typeName;

    // 长度
    private String size;

    // 是否可为空
    private String isNull;

    // 字段说明信息
    private String remark;

    private String key;

    private TableDefinition(Builder builder) {
        this.columnName = builder.columnName;
        this.typeName = builder.typeName;
        this.isNull = builder.isNull;
        this.size = builder.size;
        this.remark = builder.remark;
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static class Builder {
        // 列名
        private String columnName;

        // 数据类型
        private String typeName;

        // 长度
        private String size;

        // 是否可为空
        private String isNull;

        // 字段说明信息
        private String remark;

        public Builder columnName(String columnName) {
            this.columnName = columnName;
            return this;
        }
        public Builder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }
        public Builder size(String size) {
            this.size = size;
            return this;
        }
        public Builder isNull(int isNull) {
            this.isNull = isNull == 0 ? "False" : "True";
            return this;
        }
        public Builder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public TableDefinition build() {
            return new TableDefinition(this);
        }
    }

    @Override
    public String toString() {
        return "TableDefinition{" +
                "columnName='" + columnName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", size='" + size + '\'' +
                ", isNull='" + isNull + '\'' +
                ", remark='" + remark + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
