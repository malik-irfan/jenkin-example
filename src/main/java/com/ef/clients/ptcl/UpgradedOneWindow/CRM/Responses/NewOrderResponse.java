package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;


import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.NewOrderModel;

public class NewOrderResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private NewOrderModel NewOrder;

    public NewOrderResponse() {
    }

    public NewOrderResponse(long errorCode, String errorDetail, NewOrderModel newOrder) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        NewOrder = newOrder;
    }

    public long getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(long errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorDetail() {
        return ErrorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        ErrorDetail = errorDetail;
    }

    public NewOrderModel getNewOrder() {
        return NewOrder;
    }

    public void setNewOrder(NewOrderModel newOrder) {
        NewOrder = newOrder;
    }
}
