package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

public class EBillActivationResponse {

    private long ErrorCode;
    private String ErrorDetail;

    public EBillActivationResponse() {
    }

    public EBillActivationResponse(long errorCode, String errorDetail) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
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
}
