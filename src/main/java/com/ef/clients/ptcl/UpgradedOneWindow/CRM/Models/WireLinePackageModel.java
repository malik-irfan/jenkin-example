package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class WireLinePackageModel {

    private String PACKAGENAME;
    private String PRICEPLANID;

    public WireLinePackageModel() {
    }

    public WireLinePackageModel(String PACKAGENAME, String PRICEPLANID) {
        this.PACKAGENAME = PACKAGENAME;
        this.PRICEPLANID = PRICEPLANID;
    }

    public String getPACKAGENAME() {
        return PACKAGENAME;
    }

    public void setPACKAGENAME(String PACKAGENAME) {
        this.PACKAGENAME = PACKAGENAME;
    }

    public String getPRICEPLANID() {
        return PRICEPLANID;
    }

    public void setPRICEPLANID(String PRICEPLANID) {
        this.PRICEPLANID = PRICEPLANID;
    }
}
