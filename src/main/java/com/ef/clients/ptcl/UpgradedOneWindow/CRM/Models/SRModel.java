package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class SRModel {

    private String SRSUBTYPE;
    private String SRTYPECODE;

    public SRModel() {
    }

    public SRModel(String SRSUBTYPE, String SRTYPECODE) {

        this.SRSUBTYPE = SRSUBTYPE;
        this.SRTYPECODE = SRTYPECODE;
    }



    public String getSRSUBTYPE() {
        return SRSUBTYPE;
    }

    public void setSRSUBTYPE(String SRSUBTYPE) {
        this.SRSUBTYPE = SRSUBTYPE;
    }

    public String getSRTYPECODE() {
        return SRTYPECODE;
    }

    public void setSRTYPECODE(String SRTYPECODE) {
        this.SRTYPECODE = SRTYPECODE;
    }
}
