package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

public class MTTRTimeResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private String MTTR;

    public MTTRTimeResponse() {
    }

    public MTTRTimeResponse(long errorCode, String errorDetail, String MTTR) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.MTTR = MTTR;
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

    public String getMTTR() {
        return MTTR;
    }

    public void setMTTR(String MTTR) {
        this.MTTR = MTTR;
    }
}
