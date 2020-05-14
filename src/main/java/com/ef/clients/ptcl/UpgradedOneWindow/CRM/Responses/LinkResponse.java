package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

public class LinkResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private String Link;


    public LinkResponse() {
    }

    public LinkResponse(long errorCode, String errorDetail, String billLink) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.Link = billLink;
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

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }
}
