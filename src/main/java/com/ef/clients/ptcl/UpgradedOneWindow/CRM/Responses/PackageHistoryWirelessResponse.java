package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.WirelessPackageHistoryModel;

import java.util.List;

public class PackageHistoryWirelessResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<WirelessPackageHistoryModel> wirelessPackageHistory;

    public PackageHistoryWirelessResponse() {
    }

    public PackageHistoryWirelessResponse(long errorCode, String errorDetail, List<WirelessPackageHistoryModel> wirelessPackageHistory) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.wirelessPackageHistory = wirelessPackageHistory;
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

    public List<WirelessPackageHistoryModel> getWirelessPackageHistory() {
        return wirelessPackageHistory;
    }

    public void setWirelessPackageHistory(List<WirelessPackageHistoryModel> wirelessPackageHistory) {
        this.wirelessPackageHistory = wirelessPackageHistory;
    }
}
