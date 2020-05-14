package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;



import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.WirelessBucketModel;

import java.util.List;

public class BucketListResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private List<WirelessBucketModel> Buckets;

    public BucketListResponse() {
    }

    public BucketListResponse(long errorCode, String errorDetail, List<WirelessBucketModel> buckets) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        Buckets = buckets;
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

    public List<WirelessBucketModel> getBuckets() {
        return Buckets;
    }

    public void setBuckets(List<WirelessBucketModel> buckets) {
        Buckets = buckets;
    }
}
