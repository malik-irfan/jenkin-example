package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class SRHistoryModel {

    private String SUBTYPE;
    private String CLOSED;
    private String CREATEDON;
    private String SRNO;
    private String STATUS;
    private String SERVICEID;
    private String DESCRIPTION;
    private String ROOTCAUSEANALYSIS;
    private String ALTERNATEPHONENUMBER;
    private String ASSIGNEDTO;
    private String SOURCE;
    private String TYPE;
    private String SUBSTATUS;
    private String CREATEDBY;
    private String SRCATEGORY;
    private String LEADTIME;

    public SRHistoryModel() {
    }

    public SRHistoryModel(String SUBTYPE, String CLOSED, String CREATEDON, String SRNO, String STATUS, String SERVICEID, String DESCRIPTION, String ROOTCAUSEANALYSIS, String ALTERNATEPHONENUMBER, String ASSIGNEDTO, String SOURCE, String TYPE, String SUBSTATUS, String CREATEDBY, String SRCATEGORY, String LEADTIME) {
        this.SUBTYPE = SUBTYPE;
        this.CLOSED = CLOSED;
        this.CREATEDON = CREATEDON;
        this.SRNO = SRNO;
        this.STATUS = STATUS;
        this.SERVICEID = SERVICEID;
        this.DESCRIPTION = DESCRIPTION;
        this.ROOTCAUSEANALYSIS = ROOTCAUSEANALYSIS;
        this.ALTERNATEPHONENUMBER = ALTERNATEPHONENUMBER;
        this.ASSIGNEDTO = ASSIGNEDTO;
        this.SOURCE = SOURCE;
        this.TYPE = TYPE;
        this.SUBSTATUS = SUBSTATUS;
        this.CREATEDBY = CREATEDBY;
        this.SRCATEGORY = SRCATEGORY;
        this.LEADTIME = LEADTIME;
    }

    public String getSUBTYPE() {
        return SUBTYPE;
    }

    public void setSUBTYPE(String SUBTYPE) {
        this.SUBTYPE = SUBTYPE;
    }

    public String getCLOSED() {
        return CLOSED;
    }

    public void setCLOSED(String CLOSED) {
        this.CLOSED = CLOSED;
    }

    public String getCREATEDON() {
        return CREATEDON;
    }

    public void setCREATEDON(String CREATEDON) {
        this.CREATEDON = CREATEDON;
    }

    public String getSRNO() {
        return SRNO;
    }

    public void setSRNO(String SRNO) {
        this.SRNO = SRNO;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSERVICEID() {
        return SERVICEID;
    }

    public void setSERVICEID(String SERVICEID) {
        this.SERVICEID = SERVICEID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getROOTCAUSEANALYSIS() {
        return ROOTCAUSEANALYSIS;
    }

    public void setROOTCAUSEANALYSIS(String ROOTCAUSEANALYSIS) {
        this.ROOTCAUSEANALYSIS = ROOTCAUSEANALYSIS;
    }

    public String getALTERNATEPHONENUMBER() {
        return ALTERNATEPHONENUMBER;
    }

    public void setALTERNATEPHONENUMBER(String ALTERNATEPHONENUMBER) {
        this.ALTERNATEPHONENUMBER = ALTERNATEPHONENUMBER;
    }

    public String getASSIGNEDTO() {
        return ASSIGNEDTO;
    }

    public void setASSIGNEDTO(String ASSIGNEDTO) {
        this.ASSIGNEDTO = ASSIGNEDTO;
    }

    public String getSOURCE() {
        return SOURCE;
    }

    public void setSOURCE(String SOURCE) {
        this.SOURCE = SOURCE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSUBSTATUS() {
        return SUBSTATUS;
    }

    public void setSUBSTATUS(String SUBSTATUS) {
        this.SUBSTATUS = SUBSTATUS;
    }

    public String getCREATEDBY() {
        return CREATEDBY;
    }

    public void setCREATEDBY(String CREATEDBY) {
        this.CREATEDBY = CREATEDBY;
    }

    public String getSRCATEGORY() {
        return SRCATEGORY;
    }

    public void setSRCATEGORY(String SRCATEGORY) {
        this.SRCATEGORY = SRCATEGORY;
    }

    public String getLEADTIME() {
        return LEADTIME;
    }

    public void setLEADTIME(String LEADTIME) {
        this.LEADTIME = LEADTIME;
    }
}
