package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.Service;


import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Controller.OWController;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.RequestModel.SOAPRequestModel;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.ResponseModel.FinesseCalculatorResponseModel;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.ResponseModel.RebateResponse;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.SMS.SMS;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.WSDLConsume.WSDLConsumer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RebateService {

    private static final Logger logger = LogManager.getLogger(RebateService.class);

    public static String loadRebate(String pstn,String product,String productId,String srOpenDate,
            String srCloseDate,String region,String pricePlanId,String pricePlan,String stateDate,
            String agentId,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Rebate");
        String userName = bundle.getString("REBATE_ACCESS_USER");
        String password = bundle.getString("REBATE_ACCESS_PASSWORD");
        String systemIP = bundle.getString("REBATE_ACCESS_SYSTEM_IP");
        String chanel = bundle.getString("REBATE_ACCESS_CHANEL");
        String wsURL = bundle.getString("WSDL_URL_LOAD_REBATE");
        String SOAPAction = bundle.getString("SOAP_ACTION_LOAD_REBATE");
        String xmlInput = SOAPRequestModel.getTestRequest(userName,password,systemIP,pstn,product,productId,
                srOpenDate,srCloseDate,region,pricePlanId,pricePlan,stateDate,agentId,chanel);

        long time = System.currentTimeMillis();
        logger.info(time1+" Requesting CRM to load Rebate");
        agentLogger.info(time1+" Requesting CRM to load Rebate");
        String customResponse = null;

        RebateResponse response = new RebateResponse();
        try {

            String output = WSDLConsumer.consumer(wsURL, xmlInput, "");

            long ResponseTime = System.currentTimeMillis();
            long CRMResponseTime = ResponseTime - time;
            logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
            agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

            logger.info(time1+" CRM response "+output);
            agentLogger.info(time1+" CRM response "+output);

            JSONObject ResponseJson = WSDLConsumer.getJson(output);
            JSONObject Rebate = ResponseJson.getJSONObject("LoadRebate");
            int ReturnCode = Integer.parseInt(Rebate.get("RetCode").toString());

            if (ReturnCode == 0){
                response.setErrorCode(ReturnCode);
                response.setErrorDetail(Rebate.get("Description").toString());
            }else{
                response.setErrorCode(ReturnCode);
                response.setErrorDetail(Rebate.get("RetDesc").toString());
            }
        } catch (IOException e) {
            response.setErrorCode(5);
            response.setErrorDetail("Something wrong...");
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
        }

        customResponse = WSDLConsumer.javaToJson(response);

        return customResponse;

    }

    public static String finesseCalculator(String pstn,String agentId,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Rebate");
        String userName = bundle.getString("REBATE_ACCESS_USER");
        String password = bundle.getString("REBATE_ACCESS_PASSWORD");
        String systemIP = bundle.getString("REBATE_ACCESS_SYSTEM_IP");
        String chanel = bundle.getString("REBATE_ACCESS_CHANEL");
        String wsURL = bundle.getString("WSDL_URL_CALCULATOR");
        String SOAPAction = bundle.getString("SOAP_ACTION_CALCULATOR");
        String xmlInput = SOAPRequestModel.finesseCalculator(userName,password,systemIP,pstn,agentId,chanel);

        long time = System.currentTimeMillis();
        logger.info(time1+" Requesting CRM for Finesse Calculator");
        agentLogger.info(time1+" Requesting CRM for Finesse Calculator");
        String customResponse = null;

        RebateResponse response = new RebateResponse();
        try {

            String output = WSDLConsumer.consumer(wsURL, xmlInput, "");

            long ResponseTime = System.currentTimeMillis();
            long CRMResponseTime = ResponseTime - time;
            logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
            agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

            logger.info(time1+" CRM response "+output);
            agentLogger.info(time1+" CRM response "+output);

            JSONObject ResponseJson = WSDLConsumer.getJson(output);
            JSONObject FinesseCal = ResponseJson.getJSONObject("FinesseCalculator");
            if (FinesseCal.has("Rebate")){
                Object RebateObj = FinesseCal.get("Rebate");
                OWController owController = new OWController();
                List<FinesseCalculatorResponseModel> rebateList = new ArrayList<>();
                if (owController.checkObjectType(RebateObj).equals("JSONArray")){
                    JSONArray rebateArray = FinesseCal.getJSONArray("Rebate");

                    for (int i =0;i< rebateArray.length(); i++){

                        FinesseCalculatorResponseModel responseModel = new FinesseCalculatorResponseModel();
                        JSONObject rebateObject = rebateArray.getJSONObject(i);

                        responseModel.setBillingAccountId(rebateObject.get("BillingAccountId").toString());
                        responseModel.setStateDate(rebateObject.get("StateDate").toString());
                        responseModel.setPricePlan(rebateObject.get("PricePlan").toString());
                        responseModel.setPricePlanId(rebateObject.get("PricePlanId").toString());
                        responseModel.setRegion(rebateObject.get("Region").toString());
                        responseModel.setProductId(rebateObject.get("Prodcutid").toString());
                        responseModel.setProduct(rebateObject.get("Product").toString());
                        responseModel.setSrOpenDate(rebateObject.get("SROpendate").toString());
                        responseModel.setSrCloseDate(rebateObject.get("SRClosedate").toString());
                        responseModel.setSrNumber(rebateObject.get("SRnumber").toString());
                        responseModel.setRebateAmount(rebateObject.get("RebateAmount").toString());
                        responseModel.setFaultyDays(rebateObject.get("FaultyDays").toString());
                        responseModel.setBIStatus(rebateObject.get("BIStatus").toString());

                        rebateList.add(responseModel);
                    }
                }else{
                    JSONObject rebateJson = FinesseCal.getJSONObject("Rebate");
                    FinesseCalculatorResponseModel responseModel = new FinesseCalculatorResponseModel();
                    responseModel.setBillingAccountId(rebateJson.get("BillingAccountId").toString());
                    responseModel.setStateDate(rebateJson.get("StateDate").toString());
                    responseModel.setPricePlan(rebateJson.get("PricePlan").toString());
                    responseModel.setPricePlanId(rebateJson.get("PricePlanId").toString());
                    responseModel.setRegion(rebateJson.get("Region").toString());
                    responseModel.setProductId(rebateJson.get("Prodcutid").toString());
                    responseModel.setProduct(rebateJson.get("Product").toString());
                    responseModel.setSrOpenDate(rebateJson.get("SROpendate").toString());
                    responseModel.setSrCloseDate(rebateJson.get("SRClosedate").toString());
                    responseModel.setSrNumber(rebateJson.get("SRnumber").toString());
                    responseModel.setRebateAmount(rebateJson.get("RebateAmount").toString());
                    responseModel.setFaultyDays(rebateJson.get("FaultyDays").toString());
                    responseModel.setBIStatus(rebateJson.get("BIStatus").toString());

                    rebateList.add(responseModel);
                }
                response.setErrorCode(0);
                response.setErrorDetail("Success");
                response.setFinesseCalculator(rebateList);

            }else{
                // Check error
                if (FinesseCal.has("RetCode")){
                    int ReturnCode = Integer.parseInt(FinesseCal.get("RetCode").toString());
                    logger.info(time1+" CRM return code: "+ReturnCode+" Desc: "+FinesseCal.get("RetDesc").toString());
                    response.setErrorCode(ReturnCode);
                    response.setErrorDetail(FinesseCal.get("RetDesc").toString());

                }
            }


/*
            if (ReturnCode == 0){
                FinesseCalculatorResponseModel responseModel = new FinesseCalculatorResponseModel();
                response.setErrorCode(ReturnCode);
                response.setErrorDetail(Rebate.get("RetDesc").toString());

                responseModel.setBillingAccountId(Rebate.get("BillingAccountId").toString());
                responseModel.setStateDate(Rebate.get("StateDate").toString());
                responseModel.setPricePlan(Rebate.get("PricePlan").toString());
                responseModel.setPricePlanId(Rebate.get("PricePlanId").toString());
                responseModel.setRegion(Rebate.get("Region").toString());
                responseModel.setProductId(Rebate.get("Prodcutid").toString());
                responseModel.setProduct(Rebate.get("Product").toString());
                responseModel.setSrOpenDate(Rebate.get("SROpendate").toString());
                responseModel.setSrCloseDate(Rebate.get("SRClosedate").toString());
                responseModel.setSrNumber(Rebate.get("SRnumber").toString());
                responseModel.setRebateAmount(Rebate.get("RebateAmount").toString());
                responseModel.setFaultyDays(Rebate.get("FaultyDays").toString());
                responseModel.setBIStatus(Rebate.get("BIStatus").toString());

                response.setFinesseCalculator(responseModel);

            }else{
                response.setErrorCode(ReturnCode);
                response.setErrorDetail(Rebate.get("RetDesc").toString());
            }
            */
        } catch (IOException e) {
            response.setErrorCode(5);
            response.setErrorDetail("Something wrong...");
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
        }

        customResponse = WSDLConsumer.javaToJson(response);

        return customResponse;

    }

    public static String sendRebateSms(String pstn,String areaCode,String mobile,String accountId
            ,String amount,String customerName,String complaintNo,String complaintType,String agentId,Logger agentLogger,long time){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("SEND_REBATE_SMS_PROCESS");

        JSONObject parent = new JSONObject();
        parent.put("process",process);
        parent.put("username",userName);
        parent.put("password",password);
        parent.put("pstn",pstn);
        parent.put("area_code",areaCode);
        parent.put("msisdn",mobile);
        parent.put("amount",amount);
        parent.put("account_id",accountId);
        parent.put("complaint_no",complaintNo);
        parent.put("complaint_type",complaintType);
        parent.put("customer_name",customerName);

        logger.info(time+" Calling API to Send Rebate SMS with Params:  "+parent.toString());
        agentLogger.info(time+" Calling API to Send Rebate SMS with Params:  "+parent.toString());
        String resp=null;
        resp = SMS.SEND_REBATE_SMS(endPoint,parent,agentLogger,time);
        if (resp != null && ! resp.equals("")){
            return resp;
        }else{
            JSONObject response = new JSONObject();
            response.put("rescode",5);
            response.put("message","Something wrong...");
            resp = WSDLConsumer.javaToJson(response);
        }
        return resp;
    }

    public static String sendEBillSms(String pstn,String areaCode,String mobile,String accountId,String agentId,Logger agentLogger,long time){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("SEND_EBILL_SMS_PROCESS");

        JSONObject parent = new JSONObject();
        parent.put("process",process);
        parent.put("username",userName);
        parent.put("password",password);
        parent.put("pstn",pstn);
        parent.put("area_code",areaCode);
        parent.put("msisdn",mobile);
        parent.put("account_id",accountId);

        logger.info(time+" Calling API to Send E-Bill Link SMS with Params:  "+parent.toString());
        agentLogger.info(time+" Calling API to Send E-Bill Link SMS with Params:  "+parent.toString());
        String resp=null;
        resp = SMS.SEND_REBATE_SMS(endPoint,parent,agentLogger,time);
        if (resp != null && ! resp.equals("")){
            return resp;
        }else{
            JSONObject response = new JSONObject();
            response.put("rescode",5);
            response.put("message","Something wrong...");
            resp = WSDLConsumer.javaToJson(response);
        }
        return resp;
    }
}
