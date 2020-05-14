package com.ef.clients.ptcl.UpgradedOneWindow.CRM.CustomerActivatedServices;

public class BroadBand {
    private String BBPACKAGENAME;
    private String BB_USERNAME;
    private String BB_DATARATE;

    public BroadBand() {
    }

    public BroadBand(String _package, String username, String BB_DATARATE) {
        this.BBPACKAGENAME = _package;
        this.BB_USERNAME = username;
        this.BB_DATARATE = BB_DATARATE;
    }

    public String get_package() {
        return BBPACKAGENAME;
    }

    public String getBBPACKAGENAME() {
        return BBPACKAGENAME;
    }

    public void setBBPACKAGENAME(String BBPACKAGENAME) {
        this.BBPACKAGENAME = BBPACKAGENAME;
    }

    public String getBB_USERNAME() {
        return BB_USERNAME;
    }

    public void setBB_USERNAME(String BB_USERNAME) {
        this.BB_USERNAME = BB_USERNAME;
    }

    public String getBB_DATARATE() {
        return BB_DATARATE;
    }

    public void setBB_DATARATE(String BB_DATARATE) {
        this.BB_DATARATE = BB_DATARATE;
    }
}
