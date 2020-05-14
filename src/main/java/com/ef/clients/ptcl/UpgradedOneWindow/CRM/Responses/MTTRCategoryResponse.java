package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;


import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.MttrData;

public class MTTRCategoryResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private MttrData mttrCategory;

    public MTTRCategoryResponse() {
    }

    public MTTRCategoryResponse(long errorCode, String errorDetail, MttrData mttrCategory) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.mttrCategory = mttrCategory;
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

    public MttrData getMttrCategory() {
        return mttrCategory;
    }

    public void setMttrCategory(MttrData mttrCategory) {
        this.mttrCategory = mttrCategory;
    }
}
