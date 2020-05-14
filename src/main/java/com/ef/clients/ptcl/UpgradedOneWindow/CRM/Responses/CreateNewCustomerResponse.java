package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses;

public class CreateNewCustomerResponse {

    private long ErrorCode;
    private String ErrorDetail;
    private String accountNumber;
    private String billingContact;

    public CreateNewCustomerResponse() {
    }

    public CreateNewCustomerResponse(long errorCode, String errorDetail, String accountNumber, String billingContact) {
        ErrorCode = errorCode;
        ErrorDetail = errorDetail;
        this.accountNumber = accountNumber;
        this.billingContact = billingContact;
    }

    public long getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(long errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorDetail() {
        return ErrorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        ErrorDetail = errorDetail;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBillingContact() {
        return billingContact;
    }

    public void setBillingContact(String billingContact) {
        this.billingContact = billingContact;
    }
}
