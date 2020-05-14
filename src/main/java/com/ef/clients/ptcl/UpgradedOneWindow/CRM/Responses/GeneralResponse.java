package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

public class GeneralResponse {

    private long ErrorCode;
    private String ErrorDetail;

    public GeneralResponse() {
    }

    public GeneralResponse(long errorCode, String errorDetail) {
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
