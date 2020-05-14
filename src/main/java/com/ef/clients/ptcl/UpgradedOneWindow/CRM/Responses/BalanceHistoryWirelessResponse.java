package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.BalanceHistoryWirelessModel;

import java.util.List;

public class BalanceHistoryWirelessResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<BalanceHistoryWirelessModel> BalanceHistory;

    public BalanceHistoryWirelessResponse() {
    }

    public BalanceHistoryWirelessResponse(long errorCode, String errorDetail, List<BalanceHistoryWirelessModel> balanceHistory) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.BalanceHistory = balanceHistory;
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

    public List<BalanceHistoryWirelessModel> getBalanceHistory() {
        return BalanceHistory;
    }

    public void setBalanceHistory(List<BalanceHistoryWirelessModel> balanceHistory) {
        BalanceHistory = balanceHistory;
    }
}
