package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import java.util.List;

public class SRTypesResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<String> SRTypes;

    public SRTypesResponse() {
    }

    public SRTypesResponse(long errorCode, String errorDetail, List<String> srTypeModels) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.SRTypes = srTypeModels;
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

    public List<String> getSRTypes() {
        return SRTypes;
    }

    public void setSRTypes(List<String> SRTypes) {
        this.SRTypes = SRTypes;
    }
}
