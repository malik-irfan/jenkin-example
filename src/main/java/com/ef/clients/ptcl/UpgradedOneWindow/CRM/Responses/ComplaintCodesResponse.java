package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.SRModel;

import java.util.List;


public class ComplaintCodesResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<SRModel> ComplaintCode;

    public ComplaintCodesResponse() {
    }

    public ComplaintCodesResponse(long errorCode, String errorDetail, List<SRModel> complaintCode) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.ComplaintCode = complaintCode;
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

    public List<SRModel> getComplaintCode() {
        return ComplaintCode;
    }

    public void setComplaintCode(List<SRModel> complaintCode) {
        ComplaintCode = complaintCode;
    }
}
