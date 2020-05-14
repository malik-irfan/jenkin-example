package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class WirelessPackageReSubHistoryModel {

    private long MDN;
    private String PaymentMode;
    private String PackageName;
    private String SubscriptionDate;

    public WirelessPackageReSubHistoryModel() {
    }

    public WirelessPackageReSubHistoryModel(long MDN, String paymentMode, String packageName, String subscriptionDate) {
        this.MDN = MDN;
        PaymentMode = paymentMode;
        PackageName = packageName;
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

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getSubscriptionDate() {
        return SubscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        SubscriptionDate = subscriptionDate;
    }
}
