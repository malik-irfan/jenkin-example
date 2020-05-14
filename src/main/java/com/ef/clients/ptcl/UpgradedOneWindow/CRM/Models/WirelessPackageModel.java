package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class WirelessPackageModel {

    private long PackageId;
    private String PackageName;

    public WirelessPackageModel() {
    }

    public WirelessPackageModel(long packageId, String packageName) {
        PackageId = packageId;
        PackageName = packageName;
    }

    public long getPackageId() {
        return PackageId;
    }

    public void setPackageId(long packageId) {
        PackageId = packageId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }
}
