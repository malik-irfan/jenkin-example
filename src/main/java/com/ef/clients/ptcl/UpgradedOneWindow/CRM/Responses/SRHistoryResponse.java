package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.SRHistoryModel;

import java.util.List;

public class SRHistoryResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<SRHistoryModel> SRHistory;

    public SRHistoryResponse() {
    }

    public SRHistoryResponse(long errorCode, String errorDetail, List<SRHistoryModel> SRHistory) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.SRHistory = SRHistory;
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

    public List<SRHistoryModel> getSRHistory() {
        return SRHistory;
    }

    public void setSRHistory(List<SRHistoryModel> SRHistory) {
        this.SRHistory = SRHistory;
    }
}
