package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

import java.util.List;

public class MTTRComplaintsResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<String> mrrtComplaints;

    public MTTRComplaintsResponse() {
    }

    public MTTRComplaintsResponse(long errorCode, String errorDetail, List<String> mrrtComplaints) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.mrrtComplaints = mrrtComplaints;
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

    public List<String> getMrrtComplaints() {
        return mrrtComplaints;
    }

    public void setMrrtComplaints(List<String> mrrtComplaints) {
        this.mrrtComplaints = mrrtComplaints;
    }
}
