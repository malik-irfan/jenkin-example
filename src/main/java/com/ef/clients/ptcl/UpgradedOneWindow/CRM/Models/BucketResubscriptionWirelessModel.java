package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class BucketResubscriptionWirelessModel {

    private long MDN;
    private String PaymentMode;
    private String BucketName;
    private String SubscriptionDate;

    public BucketResubscriptionWirelessModel() {
    }

    public BucketResubscriptionWirelessModel(long MDN, String paymentMode, String bucketName, String subscriptionDate) {
        this.MDN = MDN;
        PaymentMode = paymentMode;
        BucketName = bucketName;
        SubscriptionDate = subscriptionDate;
    }

    public long getMDN() {
        return MDN;
    }

    public void setMDN(long MDN) {
        this.MDN = MDN;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getBucketName() {
        return BucketName;
    }

    public void setBucketName(String bucketName) {
        BucketName = bucketName;
    }

    public String getSubscriptionDate() {
        return SubscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        SubscriptionDate = subscriptionDate;
    }
}
