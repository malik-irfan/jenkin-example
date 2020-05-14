package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.WirelessPackageReSubHistoryModel;

import java.util.List;

public class PackageReSubscriptionHistoryWirelessResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<WirelessPackageReSubHistoryModel> WirelessPackageReSubHistory;

    public PackageReSubscriptionHistoryWirelessResponse() {
    }

    public PackageReSubscriptionHistoryWirelessResponse(long errorCode, String errorDetail, List<WirelessPackageReSubHistoryModel> wirelessPackageReSubHistory) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        WirelessPackageReSubHistory = wirelessPackageReSubHistory;
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

    public List<WirelessPackageReSubHistoryModel> getWirelessPackageReSubHistory() {
        return WirelessPackageReSubHistory;
    }

    public void setWirelessPackageReSubHistory(List<WirelessPackageReSubHistoryModel> wirelessPackageReSubHistory) {
        WirelessPackageReSubHistory = wirelessPackageReSubHistory;
    }
}

