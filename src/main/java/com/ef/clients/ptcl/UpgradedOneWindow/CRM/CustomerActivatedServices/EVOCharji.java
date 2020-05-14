package com.ef.clients.ptcl.UpgradedOneWindow.CRM.CustomerActivatedServices;

public class EVOCharji {

    private String PACKAGENAME;
    private String PACKAGEDESC;

    public EVOCharji() {
    }

    public EVOCharji(String PACKAGENAME, String SERVICES) {
        this.PACKAGENAME = PACKAGENAME;
        this.PACKAGEDESC = SERVICES;
    }

    public String getPACKAGENAME() {
        return PACKAGENAME;
    }

    public void setPACKAGENAME(String PACKAGENAME) {
        this.PACKAGENAME = PACKAGENAME;
    }

    public String getPACKAGEDESC() {
        return PACKAGEDESC;
    }

    public void setPACKAGEDESC(String PACKAGEDESC) {
        this.PACKAGEDESC = PACKAGEDESC;
    }
}
