package com.kmzyc.search.facade.constants;

import com.kmzyc.search.facade.config.Configuration;

/**
 * @ClassName: SupplierType
 * @Description: 供应商类型定义 (1为自营,2为入驻,3为代销,4为康美时代)
 * @author river
 * @date 2015年3月3日 上午10:56:35
 * 
 */
public enum SupplierType {

    /**
     * 自营
     */
    T1(1) {
        @Override
        public String getTitle() {
            return Configuration.getString("kmzyc.shop.name");
        }
    },
    /**
     * 第三方供应商
     */
    T2(2) {},
    /**
     * 第三方委托(目前属于代销类型)
     */
    T3(3) {
        @Override
        public String getTitle() {
            return T1.getTitle();
        }
    },
    /**
     * 康美时代
     */
    T4(4) {
        @Override
        public String getTitle() {
            return Configuration.getString("km.era.name");
        }
    };

    private int code;

    SupplierType(int code) {
        this.code = code;
    }

    /**
     * 
     * @Title: getCode @Description: 获取供应商类型编码 @param @return @return int @throws
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 
     * @Title: getTitle @Description: 获取店铺名称 @param @return @return String @throws
     */
    public String getTitle() {
        return "";
    }

    /**
     * 
     * @Title: $ @Description: TODO @param @param code @param @return @return SupplierType @throws
     */
    public static SupplierType $(int code) {
        return SupplierType.valueOf("T" + code);
    }
}
