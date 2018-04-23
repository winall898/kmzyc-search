package com.kmzyc.search.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.kmzyc.search.vo.ProductItem;

public class Shop implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3720968190968586256L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCorpAddr() {
        return corpAddr;
    }

    public void setCorpAddr(String corpAddr) {
        this.corpAddr = corpAddr;
    }

    public String getManageBrand() {
        return manageBrand;
    }

    public void setManageBrand(String manageBrand) {
        this.manageBrand = manageBrand;
    }

    public double getDescScore() {
        return descScore;
    }

    public void setDescScore(double descScore) {
        this.descScore = descScore;
    }

    public double getDistScore() {
        return distScore;
    }

    public void setDistScore(double distScore) {
        this.distScore = distScore;
    }

    public double getSaleScore() {
        return saleScore;
    }

    public void setSaleScore(double saleScore) {
        this.saleScore = saleScore;
    }

    public double getSpeedScore() {
        return speedScore;
    }

    public void setSpeedScore(double speedScore) {
        this.speedScore = speedScore;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(double comprehensive) {
        this.comprehensive = comprehensive;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getContacType() {
        return contacType;
    }

    public void setContacType(int contacType) {
        this.contacType = contacType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    private String id;
    private String name;
    private int type;
    private String typeName;
    private String corpAddr;
    private String manageBrand;
    private double descScore;
    private double distScore;
    private double saleScore;
    private double speedScore;
    private double comprehensive;
    private String site;
    private String contact;
    private int contacType;
    private String province;
    private String city;
    private String area;

    private List<ProductItem> products;

}
