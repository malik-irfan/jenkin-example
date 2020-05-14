package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.ResponseModel;

public class FinesseCalculatorResponseModel {

    String billingAccountId;
    String stateDate;
    String pricePlan;
    String pricePlanId;
    String region;
    String product;
    String productId;
    String srOpenDate;
    String srCloseDate;
    String srNumber;
    String rebateAmount;
    String faultyDays;
    String BIStatus;

    public String getBillingAccountId() {
        return billingAccountId;
    }

    public void setBillingAccountId(String billingAccountId) {
        this.billingAccountId = billingAccountId;
    }

    public String getStateDate() {
        return stateDate;
    }

    public void setStateDate(String stateDate) {
        this.stateDate = stateDate;
    }

    public String getPricePlan() {
        return pricePlan;
    }

    public void setPricePlan(String pricePlan) {
        this.pricePlan = pricePlan;
    }

    public String getPricePlanId() {
        return pricePlanId;
    }

    public void setPricePlanId(String pricePlanId) {
        this.pricePlanId = pricePlanId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSrOpenDate() {
        return srOpenDate;
    }

    public void setSrOpenDate(String srOpenDate) {
        this.srOpenDate = srOpenDate;
    }

    public String getSrCloseDate() {
        return srCloseDate;
    }

    public void setSrCloseDate(String srCloseDate) {
        this.srCloseDate = srCloseDate;
    }

    public String getSrNumber() {
        return srNumber;
    }

    public void setSrNumber(String srNumber) {
        this.srNumber = srNumber;
    }

    public String getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(String rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getFaultyDays() {
        return faultyDays;
    }

    public void setFaultyDays(String faultyDays) {
        this.faultyDays = faultyDays;
    }

    public String getBIStatus() {
        return BIStatus;
    }

    public void setBIStatus(String BIStatus) {
        this.BIStatus = BIStatus;
    }
}
