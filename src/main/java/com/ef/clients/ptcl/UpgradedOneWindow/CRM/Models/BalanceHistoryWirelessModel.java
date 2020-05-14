package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class BalanceHistoryWirelessModel {

    private long MDN;
    private long Amount;
    private String LoadingDate;
    private String Channel;

    public BalanceHistoryWirelessModel() {
    }

    public BalanceHistoryWirelessModel(long MDN, long amount, String loadingDate, String channel) {
        this.MDN = MDN;
        Amount = amount;
        LoadingDate = loadingDate;
        Channel = channel;
    }

    public long getMDN() {
        return MDN;
    }

    public void setMDN(long MDN) {
        this.MDN = MDN;
    }

    public long getAmount() {
        return Amount;
    }

    public void setAmount(long amount) {
        Amount = amount;
    }

    public String getLoadingDate() {
        return LoadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        LoadingDate = loadingDate;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }
}
