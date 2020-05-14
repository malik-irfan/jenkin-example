package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.BucketResubscriptionWirelessModel;

import java.util.List;

public class BucketReSubscriptionHistoryWirelessResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<BucketResubscriptionWirelessModel> BucketReSubscriptionHistory;

    public BucketReSubscriptionHistoryWirelessResponse() {
    }

    public BucketReSubscriptionHistoryWirelessResponse(long errorCode, String errorDetail, List<BucketResubscriptionWirelessModel> bucketReSubscriptionHistory) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        BucketReSubscriptionHistory = bucketReSubscriptionHistory;
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

    public List<BucketResubscriptionWirelessModel> getBucketReSubscriptionHistory() {
        return BucketReSubscriptionHistory;
    }

    public void setBucketReSubscriptionHistory(List<BucketResubscriptionWirelessModel> bucketReSubscriptionHistory) {
        BucketReSubscriptionHistory = bucketReSubscriptionHistory;
    }
}
