package com.ef.clients.ptcl.UpgradedOneWindow.CRM.CustomerActivatedServices;

public class SmartTV {

    private String IPTV_father_UserName;
    private String NumberOf_IPTV;

    public SmartTV() {
    }


    public SmartTV(String IPTV_father_UserName, String numberOf_IPTV) {
        this.IPTV_father_UserName = IPTV_father_UserName;
        NumberOf_IPTV = numberOf_IPTV;
    }

    public String getIPTV_father_UserName() {
        return IPTV_father_UserName;
    }

    public void setIPTV_father_UserName(String IPTV_father_UserName) {
        this.IPTV_father_UserName = IPTV_father_UserName;
    }

    public String getNumberOf_IPTV() {
        return NumberOf_IPTV;
    }

    public void setNumberOf_IPTV(String numberOf_IPTV) {
        NumberOf_IPTV = numberOf_IPTV;
    }
}
