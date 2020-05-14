package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.WirelessPackageModel;

import java.util.List;

public class PackageWirelessResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<WirelessPackageModel> Packages;

    public PackageWirelessResponse() {
    }

    public PackageWirelessResponse(long errorCode, String errorDetail, List<WirelessPackageModel> packages) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        Packages = packages;
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

    public List<WirelessPackageModel> getPackages() {
        return Packages;
    }

    public void setPackages(List<WirelessPackageModel> packages) {
        Packages = packages;
    }
}
