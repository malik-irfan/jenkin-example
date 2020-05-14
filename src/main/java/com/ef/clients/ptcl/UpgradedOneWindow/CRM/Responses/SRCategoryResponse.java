package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

import java.util.List;

public class SRCategoryResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<String> SRCategories;

    public SRCategoryResponse() {
    }

    public SRCategoryResponse(long errorCode, String errorDetail, List<String> SRCategories) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.SRCategories = SRCategories;
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

    public List<String> getSRCategories() {
        return SRCategories;
    }

    public void setSRCategories(List<String> SRCategories) {
        this.SRCategories = SRCategories;
    }
}
