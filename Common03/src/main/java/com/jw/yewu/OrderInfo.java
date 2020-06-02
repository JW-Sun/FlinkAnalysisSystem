package com.jw.yewu;

import java.util.Date;

public class OrderInfo {

    private Long id;
    private Long userId;
    private Long productId;
    private Long shopId;
    private Long merchartId;
    private Double orderAmount;
    private Double payAmount;
    private Date createTime;
    private Integer payType;//'1 微信支付 2 支付宝支付 3 银联支付 4 京东支付',
    private Date payTime;
    private Integer status;// '0未支付 1 已支付 2 已退款 3 未支付已失效',
    private Integer number;
    private Long hongbaoId;
    private Double hongbaoAmount;
    private Long conpusId;
    private Double conpusAmount;
    private Long huodongId;
    private Long miaoshaId;
    private Long tuangouId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getMerchartId() {
        return merchartId;
    }

    public void setMerchartId(Long merchartId) {
        this.merchartId = merchartId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getHongbaoId() {
        return hongbaoId;
    }

    public void setHongbaoId(Long hongbaoId) {
        this.hongbaoId = hongbaoId;
    }

    public Double getHongbaoAmount() {
        return hongbaoAmount;
    }

    public void setHongbaoAmount(Double hongbaoAmount) {
        this.hongbaoAmount = hongbaoAmount;
    }

    public Long getConpusId() {
        return conpusId;
    }

    public void setConpusId(Long conpusId) {
        this.conpusId = conpusId;
    }

    public Double getConpusAmount() {
        return conpusAmount;
    }

    public void setConpusAmount(Double conpusAmount) {
        this.conpusAmount = conpusAmount;
    }

    public Long getHuodongId() {
        return huodongId;
    }

    public void setHuodongId(Long huodongId) {
        this.huodongId = huodongId;
    }

    public Long getMiaoshaId() {
        return miaoshaId;
    }

    public void setMiaoshaId(Long miaoshaId) {
        this.miaoshaId = miaoshaId;
    }

    public Long getTuangouId() {
        return tuangouId;
    }

    public void setTuangouId(Long tuangouId) {
        this.tuangouId = tuangouId;
    }
}
