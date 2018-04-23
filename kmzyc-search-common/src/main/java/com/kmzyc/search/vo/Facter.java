package com.kmzyc.search.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 前端过滤条件显示VO
 * 
 * @author river
 * 
 */
public class Facter implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1258433097106973246L;

    public class Field implements Serializable {
        /**
         * 序列号
         */
        private static final long serialVersionUID = -4910675321243092944L;
        private String name; // 中文名称
        private long count; // 产品个数
        private int order; // 序号
        private String code; // 所属索引名称
        private int selected; // 是否选中 0：未选中； 1：选中

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

    }

    private String code; // 索引名称
    private String name; // 中文名称
    private List<Field> fields; // 过滤条件值列表
    private int order; // 序号
    private int selected; // 是否选中 0：未选中； 1：选中

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

}
