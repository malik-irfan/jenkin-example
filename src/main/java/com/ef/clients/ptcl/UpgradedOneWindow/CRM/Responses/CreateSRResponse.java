package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

public class CreateSRResponse {

   // private long ErrorCode;
    private String ErrorCode;
    private String ErrorDetail;
    private String SRNumber;

    public CreateSRResponse() {
    }

    public CreateSRResponse(String errorCode, String errorDetail, String SRNumber) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.SRNumber = SRNumber;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorDetail() {
        return ErrorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        ErrorDetail = errorDetail;
    }

    public String getSRNumber() {
        return SRNumber;
    }

    public void setSRNumber(String SRNumber) {
        this.SRNumber = SRNumber;
    }
}
