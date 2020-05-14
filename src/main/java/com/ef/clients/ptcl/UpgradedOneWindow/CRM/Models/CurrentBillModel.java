package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

public class CurrentBillModel {

    private long total_bill;
    private long ptcl_charges;
    private long DSL_over_usage;
    private String over_usage;
    private String generated_on;
    private String due_date;
    private long last_month_bill;

    public CurrentBillModel() {
    }

    public long getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(long total_bill) {
        this.total_bill = total_bill;
    }

    public long getPtcl_charges() {
        return ptcl_charges;
    }

    public void setPtcl_charges(long ptcl_charges) {
        this.ptcl_charges = ptcl_charges;
    }

    public long getDSL_over_usage() {
        return DSL_over_usage;
    }

    public void setDSL_over_usage(long DSL_over_usage) {
        this.DSL_over_usage = DSL_over_usage;
    }

    public String getOver_usage() {
        return over_usage;
    }

    public void setOver_usage(String over_usage) {
        this.over_usage = over_usage;
    }

    public String getGenerated_on() {
        return generated_on;
    }

    public void setGenerated_on(String generated_on) {
        this.generated_on = generated_on;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public long getLast_month_bill() {
        return last_month_bill;
    }

    public void setLast_month_bill(long last_month_bill) {
        this.last_month_bill = last_month_bill;
    }
}
