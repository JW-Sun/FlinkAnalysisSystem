package com.jw.yewu;

public class Product {

    private Long id;
    private String productName;
    private Long productTypeId;
    private Double originalPrice;
    private Double huoDongPrice;
    private Long shopId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getHuoDongPrice() {
        return huoDongPrice;
    }

    public void setHuoDongPrice(Double huoDongPrice) {
        this.huoDongPrice = huoDongPrice;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
