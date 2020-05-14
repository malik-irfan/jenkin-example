package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class WirelessBucketModel {

    private long OptionalOfferID;
    private String OfferName;
    private long SubscriptionCharges;
    private String PaymentMode;

    public WirelessBucketModel() {
    }

    public WirelessBucketModel(long bucketId, String bucketName, long subscriptionCharges, String paymentMode) {
        OptionalOfferID = bucketId;
        OfferName = bucketName;
        SubscriptionCharges = subscriptionCharges;
        PaymentMode = paymentMode;
    }

    public long getOptionalOfferID() {
        return OptionalOfferID;
    }

    public void setOptionalOfferID(long optionalOfferID) {
        OptionalOfferID = optionalOfferID;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public long getSubscriptionCharges() {
        return SubscriptionCharges;
    }

    public void setSubscriptionCharges(long subscriptionCharges) {
        SubscriptionCharges = subscriptionCharges;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }
}
