package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class NewOrderModel {

    private String SRNumber;
    private String OrderId;


    public NewOrderModel() {
    }

    public NewOrderModel(String SRNumber, String orderId) {
        this.SRNumber = SRNumber;
        OrderId = orderId;
    }

    public String getSRNumber() {
        return SRNumber;
    }

    public void setSRNumber(String SRNumber) {
        this.SRNumber = SRNumber;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
