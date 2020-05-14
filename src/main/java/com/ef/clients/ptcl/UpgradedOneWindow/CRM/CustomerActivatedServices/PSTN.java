package com.ef.clients.ptcl.UpgradedOneWindow.CRM.CustomerActivatedServices;

public class PSTN {

    private String PSTNPACKAGENAME;
    private String PSTNSERVICES;

    public PSTN() {
    }

    public PSTN(String PSTNPACKAGENAME, String PSTNSERVICES) {
        this.PSTNPACKAGENAME = PSTNPACKAGENAME;
        this.PSTNSERVICES = PSTNSERVICES;
    }

    public String getPSTNPACKAGENAME() {
        return PSTNPACKAGENAME;
    }

    public void setPSTNPACKAGENAME(String PSTNPACKAGENAME) {
        this.PSTNPACKAGENAME = PSTNPACKAGENAME;
    }

    public String getPSTNSERVICES() {
        return PSTNSERVICES;
    }

    public void setPSTNSERVICES(String PSTNSERVICES) {
        this.PSTNSERVICES = PSTNSERVICES;
    }
}
