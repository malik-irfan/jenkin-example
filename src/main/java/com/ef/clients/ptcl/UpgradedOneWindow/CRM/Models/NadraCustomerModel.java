package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class NadraCustomerModel {

    private String customerName;
    private String fatherName;
    private String presentAddress;
    private String permanentAddress;
    private String motherName;
    private String placeOfBirth;
    private String religion;
    private long transactionId;

    public NadraCustomerModel() {
    }

    public NadraCustomerModel(String customerName, String fatherName, String presentAddress, String permanentAddress, String motherName, String placeOfBirth, String religion, long transactionId) {
        this.customerName = customerName;
        this.fatherName = fatherName;
        this.presentAddress = presentAddress;
        this.permanentAddress = permanentAddress;
        this.motherName = motherName;
        this.placeOfBirth = placeOfBirth;
        this.religion = religion;
        this.transactionId = transactionId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
}
