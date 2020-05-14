package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class MttrData {
    private String Prefix;
    private String Region;
    private String EXCHANGE_ID;
    private String EXCHANGE_NM;
    private String Zone;
    private String EXCHANGE_NAME;

    public MttrData(){}

    public MttrData(String prefix, String region, String EXCHANGE_ID, String EXCHANGE_NM, String zone, String EXCHANGE_NAME) {
        Prefix = prefix;
        Region = region;
        this.EXCHANGE_ID = EXCHANGE_ID;
        this.EXCHANGE_NM = EXCHANGE_NM;
        Zone = zone;
        this.EXCHANGE_NAME = EXCHANGE_NAME;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getEXCHANGE_ID() {
        return EXCHANGE_ID;
    }

    public void setEXCHANGE_ID(String EXCHANGE_ID) {
        this.EXCHANGE_ID = EXCHANGE_ID;
    }

    public String getEXCHANGE_NM() {
        return EXCHANGE_NM;
    }

    public void setEXCHANGE_NM(String EXCHANGE_NM) {
        this.EXCHANGE_NM = EXCHANGE_NM;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getEXCHANGE_NAME() {
        return EXCHANGE_NAME;
    }

    public void setEXCHANGE_NAME(String EXCHANGE_NAME) {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
    }
}
