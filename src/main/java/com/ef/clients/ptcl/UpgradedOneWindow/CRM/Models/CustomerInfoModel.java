package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models;

import com.ef.clients.ptcl.UpgradedOneWindow.CRM.CustomerActivatedServices.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoModel {
    private long id; // this is CUSTACCOUNTID
    private String customerId;
    //private long billAccountId;
    private String billAccountId;
    private String service_id;
    private String customer_type;
    private List<String> registered_numbers;
    private boolean blacklisted;
    private String blacklisted_numbers;
    private String open_sr;
    private String first_name;
    private String customer_name_verification;
    private String last_name;
    private String services;
    private String service_status;
    private String address;
    private String address_verification;
    private String mobile_number;
    private String mobile_no_verification;
    private String email_address;
    private String email_address_verification;
    private String region;
    private String exchange;
    private String bm_name;
    private String connection_catergory;
    private String cnic;
    private String cnic_verification;
    private String e_bill;
    private String sms_alert_billing;
    private String customer_segment;
    private String connection_type;
    private String connection_subtype;
    private List<PSTN> pstn;
    private List<BroadBand> broadband;
    private List<SmartTV> smart_tv;
    private List<EVOCharji> evo_charji;

    public CustomerInfoModel() {
    }

    public CustomerInfoModel(long id,String customerId,String billAccountId, String customer_type, List<String> registered_numbers, boolean blacklisted, String blacklisted_numbers, String open_sr, String first_name, String customer_name_verification, String last_name, String service_id, String services, String service_status, String address, String address_verification, String mobile_number, String mobile_no_verification, String email_address, String email_address_verification, String region, String exchange, String bm_name, String connection_catergory, String cnic, String cnic_verification, String e_bill, String sms_alert_billing, String customer_segment, String connection_type, String connection_subtype, List<PSTN> pstn, List<BroadBand> broadband, List<SmartTV> smart_tv, List<EVOCharji> evo_charji) {
        this.id = id;
        this.billAccountId = billAccountId;
        this.customerId = customerId;
        this.customer_type = customer_type;
        this.registered_numbers = registered_numbers;
        this.blacklisted = blacklisted;
        this.blacklisted_numbers = blacklisted_numbers;
        this.open_sr = open_sr;
        this.first_name = first_name;
        this.customer_name_verification = customer_name_verification;
        this.last_name = last_name;
        this.service_id = service_id;
        this.services = services;
        this.service_status = service_status;
        this.address = address;
        this.address_verification = address_verification;
        this.mobile_number = mobile_number;
        this.mobile_no_verification = mobile_no_verification;
        this.email_address = email_address;
        this.email_address_verification = email_address_verification;
        this.region = region;
        this.exchange = exchange;
        this.bm_name = bm_name;
        this.connection_catergory = connection_catergory;
        this.cnic = cnic;
        this.cnic_verification = cnic_verification;
        this.e_bill = e_bill;
        this.sms_alert_billing = sms_alert_billing;
        this.customer_segment = customer_segment;
        this.connection_type = connection_type;
        this.connection_subtype = connection_subtype;
        this.pstn = pstn;
        this.broadband = broadband;
        this.smart_tv = smart_tv;
        this.evo_charji = evo_charji;
    }


    public long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBillAccountId() {
        return billAccountId;
    }

    public void setBillAccountId(String billAccountId) {
        this.billAccountId = billAccountId;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public List<String> getRegistered_numbers() {
        return registered_numbers;
    }

    public void setRegistered_numbers(List<String> registered_numbers) {
        this.registered_numbers = registered_numbers;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public String getBlacklisted_numbers() {
        return blacklisted_numbers;
    }

    public void setBlacklisted_numbers(String blacklisted_numbers) {
        this.blacklisted_numbers = blacklisted_numbers;
    }

    public String getOpen_sr() {
        return open_sr;
    }

    public void setOpen_sr(String open_sr) {
        this.open_sr = open_sr;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getCustomer_name_verification() {
        return customer_name_verification;
    }

    public void setCustomer_name_verification(String customer_name_verification) {
        this.customer_name_verification = customer_name_verification;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_verification() {
        return address_verification;
    }

    public void setAddress_verification(String address_verification) {
        this.address_verification = address_verification;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getMobile_no_verification() {
        return mobile_no_verification;
    }

    public void setMobile_no_verification(String mobile_no_verification) {
        this.mobile_no_verification = mobile_no_verification;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getEmail_address_verification() {
        return email_address_verification;
    }

    public void setEmail_address_verification(String email_address_verification) {
        this.email_address_verification = email_address_verification;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getBm_name() {
        return bm_name;
    }

    public void setBm_name(String bm_name) {
        this.bm_name = bm_name;
    }

    public String getConnection_catergory() {
        return connection_catergory;
    }

    public void setConnection_catergory(String connection_catergory) {
        this.connection_catergory = connection_catergory;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCnic_verification() {
        return cnic_verification;
    }

    public void setCnic_verification(String cnic_verification) {
        this.cnic_verification = cnic_verification;
    }

    public String getE_bill() {
        return e_bill;
    }

    public void setE_bill(String e_bill) {
        this.e_bill = e_bill;
    }

    public String getSms_alert_billing() {
        return sms_alert_billing;
    }

    public void setSms_alert_billing(String sms_alert_billing) {
        this.sms_alert_billing = sms_alert_billing;
    }

    public String getCustomer_segment() {
        return customer_segment;
    }

    public void setCustomer_segment(String customer_segment) {
        this.customer_segment = customer_segment;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getConnection_subtype() {
        return connection_subtype;
    }

    public void setConnection_subtype(String connection_subtype) {
        this.connection_subtype = connection_subtype;
    }

    public List<PSTN> getPstn() {
        return pstn;
    }

    public void setPstn(List<PSTN> pstn) {
        this.pstn = pstn;
    }

    public List<BroadBand> getBroadband() {
        return broadband;
    }

    public void setBroadband(List<BroadBand> broadband) {
        this.broadband = broadband;
    }

    public List<SmartTV> getSmart_tv() {
        return smart_tv;
    }

    public void setSmart_tv(List<SmartTV> smart_tv) {
        this.smart_tv = smart_tv;
    }

    public List<EVOCharji> getEvo_charji() {
        return evo_charji;
    }

    public void setEvo_charji(List<EVOCharji> evo_charji) {
        this.evo_charji = evo_charji;
    }

    public static CustomerInfoModel iniateEmptyObject(){

        List<PSTN> pstnList = new ArrayList<PSTN>();
        List<BroadBand> broadBandList = new ArrayList<BroadBand>();
        List<SmartTV> smartTVList = new ArrayList<SmartTV>();
        List<EVOCharji> evoCharjiList = new ArrayList<EVOCharji>();


        CustomerInfoModel customerInfoModel = new CustomerInfoModel();
        customerInfoModel.setCustomer_type("");
        List<String> rNumbers = null;
        customerInfoModel.setRegistered_numbers(rNumbers);
        customerInfoModel.setBlacklisted(false);
        customerInfoModel.setBlacklisted_numbers("");
        customerInfoModel.setFirst_name("");
        customerInfoModel.setCustomer_name_verification("");
        customerInfoModel.setLast_name("");
        customerInfoModel.setService_id("");
        customerInfoModel.setServices("");
        customerInfoModel.setService_status("");
        customerInfoModel.setAddress("");
        customerInfoModel.setAddress_verification("");
        customerInfoModel.setMobile_number("");
        customerInfoModel.setMobile_no_verification("");
        customerInfoModel.setEmail_address("");
        customerInfoModel.setEmail_address_verification("");
        customerInfoModel.setRegion("");
        customerInfoModel.setExchange("");
        customerInfoModel.setBm_name("");
        customerInfoModel.setCnic_verification("");
        customerInfoModel.setConnection_catergory("");
        customerInfoModel.setE_bill("");
        customerInfoModel.setSms_alert_billing("");
        customerInfoModel.setCustomer_segment("");
        customerInfoModel.setConnection_type("");
        customerInfoModel.setConnection_subtype("");

        PSTN pstn = new PSTN();
        pstn.setPSTNPACKAGENAME("");
        pstn.setPSTNSERVICES("");
        pstnList.add(pstn);

        BroadBand broadBand = new BroadBand();
        broadBand.setBBPACKAGENAME("");
        broadBand.setBB_DATARATE("");
        broadBand.setBB_USERNAME("");
        broadBandList.add(broadBand);

        SmartTV smartTV = new SmartTV();
        smartTV.setIPTV_father_UserName("");
        smartTV.setNumberOf_IPTV("");
        smartTVList.add(smartTV);

        EVOCharji evoCharji = new EVOCharji();
        evoCharji.setPACKAGEDESC("");
        evoCharji.setPACKAGENAME("");
        evoCharjiList.add(evoCharji);


        customerInfoModel.setPstn(pstnList);
        customerInfoModel.setBroadband(broadBandList);
        customerInfoModel.setSmart_tv(smartTVList);
        customerInfoModel.setEvo_charji(evoCharjiList);

        return customerInfoModel;
    }
}
