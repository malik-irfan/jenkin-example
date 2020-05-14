package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;


import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.NadraCustomerModel;

public class NadraServiceResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private NadraCustomerModel nadraCustomerModel;

    public NadraServiceResponse() {
    }

    public NadraServiceResponse(long errorCode, String errorMessage, NadraCustomerModel nadraCustomerModel) {
        ErrorCode = errorCode;
        ErrorDetail = errorMessage;
        this.nadraCustomerModel = nadraCustomerModel;
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

    public NadraCustomerModel getNadraCustomerModel() {
        return nadraCustomerModel;
    }

    public void setNadraCustomerModel(NadraCustomerModel nadraCustomerModel) {
        this.nadraCustomerModel = nadraCustomerModel;
    }
}
