package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class WirelessPackageHistoryModel {

    private long MDN;
    private String ModificationDate;
    private String OldPackage;
    private String NewPackage;

    public WirelessPackageHistoryModel() {
    }

    public WirelessPackageHistoryModel(long MDN, String modificationDate, String oldPackage, String newPackage) {
        this.MDN = MDN;
        ModificationDate = modificationDate;
        OldPackage = oldPackage;
        NewPackage = newPackage;
    }

    public long getMDN() {
        return MDN;
    }

    public void setMDN(long MDN) {
        this.MDN = MDN;
    }

    public String getModificationDate() {
        return ModificationDate;
    }

    public void setModificationDate(String modificationDate) {
        ModificationDate = modificationDate;
    }

    public String getOldPackage() {
        return OldPackage;
    }

    public void setOldPackage(String oldPackage) {
        OldPackage = oldPackage;
    }

    public String getNewPackage() {
        return NewPackage;
    }

    public void setNewPackage(String newPackage) {
        NewPackage = newPackage;
    }
}
