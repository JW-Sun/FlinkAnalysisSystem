package com.jw.yewu;

public class ProductType {

    private Long id;
    private String productTypeName;
    private Long productTypeLevel;
    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public Long getProductTypeLevel() {
        return productTypeLevel;
    }

    public void setProductTypeLevel(Long productTypeLevel) {
        this.productTypeLevel = productTypeLevel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
