package com.ef.clients.ptcl.UpgradedOneWindow.CRM.Controller;


import com.ef.clients.ptcl.UpgradedOneWindow.CustomLogs.EFLogger;
import com.ef.clients.ptcl.UpgradedOneWindow.CRM.CustomerActivatedServices.*;
import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Models.*;
import com.ef.clients.ptcl.UpgradedOneWindow.CRM.RequestModels.*;
import com.ef.clients.ptcl.UpgradedOneWindow.CRM.Responses.*;
import com.ef.clients.ptcl.UpgradedOneWindow.CRM.WSDLConsumer.*;
import com.ef.clients.ptcl.UpgradedOneWindow.CRM.dbcp.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.coyote.Response;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.xml.soap.SOAPMessage;


import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
  @RestController annotation tells Spring to render
  the resulting string directly back to the caller
 */
@RestController
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "*", allowedHeaders = "*")

    //@RequestMapping annotation provides “routing” information

@RequestMapping("/OneWindow")
public class OWController {

    private static final Logger logger = LogManager.getLogger(OWController.class);


    //<<====================================================================================================>>
                                                /* WIRELINE SERVICES */




    /*
        getCustomerInformation function will return specific customer detail
     */
    @RequestMapping(value="/GetCustomerInfo",method = RequestMethod.GET)
    public String getCustomerInformation(
            @RequestParam(value="ServiceIdentifier",defaultValue ="") String serviceIdentifier,
            @RequestParam(value="ServiceIdentifierType",defaultValue ="PSTN") String serviceIdentifierType,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ) {
        long time1 = System.currentTimeMillis();
        logger.info(time1+" GetCustomerInfo called with ServiceIdentifier:"+serviceIdentifier+" Type:"+serviceIdentifierType+" AgentId:"+agentId);
        String returnResponse=null;

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetCustomerInfo called with ServiceIdentifier:"+serviceIdentifier+" Type:"+serviceIdentifierType+" AgentId:"+agentId);

        List<PSTN> pstnList = new ArrayList<PSTN>();
        List<BroadBand> broadBandList = new ArrayList<BroadBand>();
        List<SmartTV> smartTVList = new ArrayList<SmartTV>();
        List<EVOCharji> evoCharjiList = new ArrayList<EVOCharji>();

        try {

            if (serviceIdentifier.equals("undefined") || StringUtils.isEmpty(serviceIdentifier) ||StringUtils.isEmpty(serviceIdentifierType)  ||serviceIdentifierType.equals("undefined") ){
                logger.info(time1+" Wrong Input Parameters");
                agentLogger.info(time1+" Wrong Input Parameters");

                CustomerInfoModel customerInfoModel = CustomerInfoModel.iniateEmptyObject();

                returnResponse = ConsumeWSDL.javaToJson(customerInfoModel);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }else{

                // CURRENTLY SEARCH CRITERIA IS (PSTN,CNIC,MDN). CHARACTER NOT ACCEPTABLE
                if(!serviceIdentifier.matches(("[0-9]+"))){
                    logger.info(time1+" ServiceIdentifier is not a valid number: "+serviceIdentifier);
                    agentLogger.info(time1+" ServiceIdentifier is not a valid number: "+serviceIdentifier);
                    CustomerInfoModel customerInfoModel = CustomerInfoModel.iniateEmptyObject();

                    returnResponse = ConsumeWSDL.javaToJson(customerInfoModel);

                    long time2 = System.currentTimeMillis();
                    long executionTime = time2 - time1;
                    logger.info(time1+" Total Execution time: "+executionTime+" ms");
                    agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                    logger.info(time1+" exit with  "+returnResponse);
                    agentLogger.info(time1+" exit with  "+returnResponse);
                    agentLogger.info("<<============================================================================>>");
                    return returnResponse;
                }




                // WSDL URL for Get Customer Info API
                ResourceBundle bundle = ResourceBundle.getBundle("Config");
                String userName = bundle.getString("PTCL_API_ACCESS_USER");
                String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
                String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
                String wsURL = bundle.getString("WSDL_URL_CUSTOMER_INFO");
                String SOAPAction = bundle.getString("SOAP_ACTION_CUSOMER_INFO");

                String xmlInput = SOAPRequestSample.getCustomerInfoRequest(userName,password,systemIP,serviceIdentifierType,serviceIdentifier);

                logger.info(time1+" Requesting CRM to get Customer Info with Following Params: UserName:"
                        + userName + " Password:" + password + " SystemIP:" + systemIP
                        +" ServiceIdentifier:"+ serviceIdentifier+" ServiceIdentifierType:"+serviceIdentifierType
                        +" AgentId:"+agentId);
                agentLogger.info(time1+" Requesting CRM to get Customer Info with Following Params: UserName:"
                        + userName + " Password:" + password + " SystemIP:" + systemIP
                        +" ServiceIdentifier:"+ serviceIdentifier+" ServiceIdentifierType:"+serviceIdentifierType
                        +" AgentId:"+agentId);


                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");


                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                JSONObject GetInfoFinResponse = ResponseJson.getJSONObject("GetInfoFinResponse");
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                int ReturnCode = GetInfoFinResponse.getInt("RetCode");
                String ReturnDescription = GetInfoFinResponse.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    CustomerInfoModel customerInfoModel = new CustomerInfoModel();
                    if (GetInfoFinResponse.has("CUSTACCOUNTID")){
                        customerInfoModel.setId(Long.parseLong(GetInfoFinResponse.get("CUSTACCOUNTID").toString()));
                    }else{
                        logger.info(time1+" Missing tag CUSTACCOUNTID");
                        agentLogger.info(time1+" Missing tag CUSTACCOUNTID");
                        // because it is long type so it automatically initialize with 0
                    }
                    if (GetInfoFinResponse.has("CUSTOMERID")){
                        customerInfoModel.setCustomerId(GetInfoFinResponse.get("CUSTOMERID").toString());
                    }else{
                        logger.info(time1+" Missing tag CUSTOMERID");
                        agentLogger.info(time1+" Missing tag CUSTOMERID");
                        // because it is long type so it automatically initialize with 0
                    }
                    if (GetInfoFinResponse.has("BILLACCOUNTID")){
                        customerInfoModel.setBillAccountId(GetInfoFinResponse.get("BILLACCOUNTID").toString());
                    }else{
                        logger.info(time1+" Missing tag BILLACCOUNTID");
                        agentLogger.info(time1+" Missing tag BILLACCOUNTID");
                        // because it is long type so it automatically initialize with 0
                    }
                    if (GetInfoFinResponse.has("CUSTOMERTYPE")){
                        customerInfoModel.setCustomer_type(GetInfoFinResponse.get("CUSTOMERTYPE").toString());
                    }else{
                        logger.info(time1+" Missing tag CUSTOMERTYPE");
                        agentLogger.info(time1+" Missing tag CUSTOMERTYPE");
                        customerInfoModel.setCustomer_type("");
                    }


                    //------------------------------------------------------------------------------------------
                    // Checks against registered number
                    logger.info(time1+" Checks against registered numbers");
                    agentLogger.info(time1+" Checks against registered numbers");
                    List<String> rNumbers = new ArrayList<String>();
                    JSONObject RegisteredNumbers = GetInfoFinResponse.getJSONObject("REGISTERDNUMBERS");
                    Object NumberObject = RegisteredNumbers.get("REGISTERDNUMBER");

                    if (checkObjectType(NumberObject).equals("JSONArray")){
                        JSONArray registeredNumberArray = (JSONArray) NumberObject;
                        //creating Gson instance to convert JSON array to Java array
                        Gson converter = new Gson();
                        Type type = new TypeToken<List<String>>(){}.getType();
                        rNumbers = converter.fromJson(String.valueOf(registeredNumberArray), type );
                    }else{
                        String regNumbers = RegisteredNumbers.get("REGISTERDNUMBER").toString();
                        List<String> myList = new ArrayList<String>(Arrays.asList(regNumbers));
                        rNumbers = myList;
                    }


                    //Object NumberObject = RegisteredNumbers.get("REGISTERDNUMBER");
                    // `instanceof` tells us whether the object can be cast to a specific type
                    /*String returnType = checkObjectType(NumberObject);
                    List<String> rNumbers = new ArrayList<String>();
                    if (returnType.equals("JSONArray"))
                    {
                        // it's an array
                        JSONArray registeredNumberArray = (JSONArray) NumberObject;
                        //creating Gson instance to convert JSON array to Java array
                        Gson converter = new Gson();
                        Type type = new TypeToken<List<String>>(){}.getType();
                        rNumbers = converter.fromJson(String.valueOf(registeredNumberArray), type );

                   *//* for (int i =0;i<registeredNumberArray.length();i++){
                        rNumbers.add(registeredNumberArray.getString(i));
                    }*//*
                        int len = registeredNumberArray.length();
                        logger.info("OWController-getCustomerInformation Registered Numbers: "+len);
                        logger.info("OWController-getCustomerInformation REGISTERDNUMBER is array: "+registeredNumberArray);

                    }else if (returnType.equals("JSONObject")){
                        logger.info("OWController-getCustomerInformation REGISTERDNUMBER is not in formate");

                    }else {
                        if (RegisteredNumbers.getString("REGISTERDNUMBER").equals("")){
                            logger.info("OWController-getCustomerInformation No registered number");
                        }else{
                            String registerNumber = RegisteredNumbers.getString("REGISTERDNUMBER");
                            logger.info("OWController-getCustomerInformation One registered number"+registerNumber);
                            rNumbers.add(registerNumber);
                        }
                    }*/
                    customerInfoModel.setRegistered_numbers(rNumbers);
                    //------------------------------------------------------------------------------------------
                    // Checks against black list number
                    logger.info(time1+" Checks against black list numbers");
                    agentLogger.info(time1+" Checks against black list numbers");
                    Object BlacklistObject = GetInfoFinResponse.get("BLACKLIST");
                    // `instanceof` tells us whether the object can be cast to a specific type
                    String blackListType = checkObjectType(BlacklistObject);
                    if (blackListType.equals("JSONArray"))
                    {
                        logger.info(time1+" blacklist type "+checkObjectType(BlacklistObject));
                        agentLogger.info(time1+" blacklist type "+checkObjectType(BlacklistObject));
                        JSONArray blackListArray = (JSONArray) BlacklistObject;
                        int len = blackListArray.length();
                        logger.info(time1+" Black List Number= "+len);
                        agentLogger.info(time1+" Black List Number= "+len);
                        logger.info(time1+" Blacklist is array: "+blackListArray);
                        agentLogger.info(time1+" Blacklist is array: "+blackListArray);
                        customerInfoModel.setBlacklisted_numbers(String.valueOf(len));
                        customerInfoModel.setBlacklisted(true);

                    }else if (blackListType.equals("JSONObject")){
                        logger.info(time1+" Blacklist is Json object");
                        agentLogger.info(time1+" Blacklist is Json object");
                    }else {
                        if (GetInfoFinResponse.getString("BLACKLIST").equals("")){
                            logger.info(time1+" No black listed number");
                            agentLogger.info(time1+" No black listed number");
                            customerInfoModel.setBlacklisted_numbers("0");
                            customerInfoModel.setBlacklisted(false);
                        }else{
                            logger.info(time1+" One blacklisted number: "+GetInfoFinResponse.getString("BLACKLIST"));
                            agentLogger.info(time1+" One blacklisted number: "+GetInfoFinResponse.getString("BLACKLIST"));
                            customerInfoModel.setBlacklisted_numbers("1");
                            customerInfoModel.setBlacklisted(true);
                        }
                    }
                    if (GetInfoFinResponse.get("OPENSRS").toString().equals("")){
                        customerInfoModel.setOpen_sr("0");
                    }else{
                        customerInfoModel.setOpen_sr(GetInfoFinResponse.get("OPENSRS").toString());
                    }


                    customerInfoModel.setFirst_name(GetInfoFinResponse.get("NAME").toString());
                    customerInfoModel.setCustomer_name_verification(GetInfoFinResponse.get("CUSTOMERNAMEVERIFIED").toString());
                    customerInfoModel.setLast_name("");
                    if (GetInfoFinResponse.has("SERVICEID")){
                        customerInfoModel.setService_id(GetInfoFinResponse.get("SERVICEID").toString());
                    }else{
                        logger.info(time1+" Missing tag SERVICEID which show either PSTN or MDN");
                        agentLogger.info(time1+" Missing tag SERVICEID which show either PSTN or MDN");
                        customerInfoModel.setService_id("");
                    }
                    customerInfoModel.setServices(GetInfoFinResponse.get("SERVICES").toString());
                    customerInfoModel.setService_status(GetInfoFinResponse.get("SERVICESTATUS").toString());
                    customerInfoModel.setAddress(GetInfoFinResponse.get("ADDRESS").toString());
                    customerInfoModel.setAddress_verification(GetInfoFinResponse.get("ADDRESSVERIFIED").toString());
                    //customerInfoModel.setAddress_verification("N");
                    //  customerInfoModel.setMobile_number(GetInfoFinResponse.getString("MOBILENO"));
                    customerInfoModel.setMobile_number(GetInfoFinResponse.get("MOBILENO").toString());
                    customerInfoModel.setMobile_no_verification(GetInfoFinResponse.get("MOBILEVERIFIED").toString());
                    //customerInfoModel.setMobile_no_verification("N");
                    customerInfoModel.setEmail_address(GetInfoFinResponse.get("EMAILADDRESS").toString());
                    customerInfoModel.setEmail_address_verification(GetInfoFinResponse.get("EMAILVERIFIED").toString());
                    //customerInfoModel.setEmail_address_verification("N");
                    customerInfoModel.setRegion(GetInfoFinResponse.get("REGION").toString());
                    customerInfoModel.setExchange(GetInfoFinResponse.get("EXCHANGE").toString());
                    customerInfoModel.setBm_name(GetInfoFinResponse.get("BMNAME").toString());
                    customerInfoModel.setConnection_catergory(GetInfoFinResponse.get("CONNECTIONCATEGORY").toString());
                    if (GetInfoFinResponse.has("CNIC")){
                        Object cnicObject = GetInfoFinResponse.get("CNIC");
                        String cnicType = checkObjectType(cnicObject);
                        if (cnicType.equals("Integer")){
                            customerInfoModel.setCnic(GetInfoFinResponse.get("CNIC").toString());
                        }else if (cnicType.equals("Long")){
                            customerInfoModel.setCnic(GetInfoFinResponse.get("CNIC").toString());
                        }else{
                            customerInfoModel.setCnic(GetInfoFinResponse.getString("CNIC"));
                        }
                    }else{
                        logger.info(time1+" Missing tag CNIC");
                        agentLogger.info(time1+" Missing tag CNIC");
                        customerInfoModel.setCnic("");
                    }
                    customerInfoModel.setCnic_verification(GetInfoFinResponse.get("CNICVERIFIED").toString());
                    //customerInfoModel.setCnic_verification("N");
                    customerInfoModel.setE_bill(GetInfoFinResponse.get("EBILL").toString());
                    customerInfoModel.setSms_alert_billing(GetInfoFinResponse.get("SMSALERTBILLING").toString());
                    customerInfoModel.setCustomer_segment(GetInfoFinResponse.get("CUSTOMERSAGMENT").toString());
                    customerInfoModel.setConnection_type(GetInfoFinResponse.get("CONNECTIONTYPE").toString());
                    customerInfoModel.setConnection_subtype(GetInfoFinResponse.get("SUBTYPE").toString());

                    //=======================================================================
                    // CHECK FOR PSTN
                    if (GetInfoFinResponse.has("PSTN")){
                        logger.info(time1+" PSTN is available");
                        agentLogger.info(time1+" PSTN is available");
                        Object pstnObject = GetInfoFinResponse.get("PSTN");
                        // `instanceof` tells us whether the object can be cast to a specific type

                        if (checkObjectType(pstnObject).equals("JSONArray")){
                            // it's an array
                            JSONArray PSTNArray = (JSONArray) pstnObject;
                            int len = PSTNArray.length();
                            logger.info(time1+" PSTN is list: "+PSTNArray);
                            agentLogger.info(time1+" PSTN is list: "+PSTNArray);
                            for (int i = 0; i<PSTNArray.length(); i++){
                                JSONObject pstnObj = PSTNArray.getJSONObject(i);
                                PSTN pstn = new PSTN();
                                pstn.setPSTNPACKAGENAME(pstnObj.getString("PSTNPACKAGENAME"));
                                pstn.setPSTNSERVICES(pstnObj.getString("PSTNSERVICES"));
                                pstnList.add(pstn);
                            }
                            customerInfoModel.setPstn(pstnList);

                        }else {
                            logger.info(time1+" PSTN is Json object");
                            agentLogger.info(time1+" PSTN is Json object");
                            JSONObject pstnObj = GetInfoFinResponse.getJSONObject("PSTN");
                            PSTN pstn = new PSTN();
                            pstn.setPSTNPACKAGENAME(pstnObj.get("PSTNPACKAGENAME").toString());
                            pstn.setPSTNSERVICES(pstnObj.get("PSTNSERVICES").toString());
                            pstnList.add(pstn);
                            customerInfoModel.setPstn(pstnList);

                        }

                    }else {
                        logger.info(time1+" PSTN not available");
                        agentLogger.info(time1+" PSTN not available");
                        PSTN pstn = new PSTN();
                        pstn.setPSTNPACKAGENAME("");
                        pstn.setPSTNSERVICES("");
                        pstnList.add(pstn);
                        customerInfoModel.setPstn(pstnList);
                    }

                    //=======================================================================
                    // CHECK FOR BROADBAND
                    if (GetInfoFinResponse.has("Broadband")){
                        logger.info(time1+" Broadband tag available");
                        agentLogger.info(time1+" Broadband tag available");
                        Object BBObject = GetInfoFinResponse.get("Broadband");
                        // `instanceof` tells us whether the object can be cast to a specific type
                        if (checkObjectType(BBObject).equals("JSONArray")){
                            // it's an array
                            JSONArray BBArray = (JSONArray) BBObject;
                            int len = BBArray.length();
                            logger.info(time1+" Broadband is list: "+BBArray);
                            agentLogger.info(time1+" Broadband is list: "+BBArray);
                            for (int i = 0; i<BBArray.length(); i++){
                                JSONObject BBObj = BBArray.getJSONObject(i);
                                BroadBand broadBand = new BroadBand();
                                broadBand.setBBPACKAGENAME(BBObj.get("BBPACKAGENAME").toString());
                                broadBand.setBB_DATARATE(BBObj.get("BB_DATARATE").toString());
                                Object BBUSERObject = BBObj.get("BB_USERNAME");

                                if (checkObjectType(BBUSERObject).equals("Integer")){
                                    broadBand.setBB_USERNAME(BBObj.get("BB_USERNAME").toString());
                                }else{
                                    broadBand.setBB_USERNAME(BBObj.get("BB_USERNAME").toString());
                                }
                                broadBandList.add(broadBand);
                            }
                            customerInfoModel.setBroadband(broadBandList);

                        }else {
                            logger.info(time1+" Broadband is Json object");
                            agentLogger.info(time1+" Broadband is Json object");
                            JSONObject BBObj = GetInfoFinResponse.getJSONObject("Broadband");
                            BroadBand broadBand = new BroadBand();
                            broadBand.setBBPACKAGENAME(BBObj.get("BBPACKAGENAME").toString());
                            broadBand.setBB_DATARATE(BBObj.get("BB_DATARATE").toString());
                            Object BBUSERObject = BBObj.get("BB_USERNAME");

                            if (checkObjectType(BBUSERObject).equals("Integer")){
                                broadBand.setBB_USERNAME(BBObj.get("BB_USERNAME").toString());
                            }else{
                                broadBand.setBB_USERNAME(BBObj.get("BB_USERNAME").toString());
                            }
                            broadBandList.add(broadBand);
                            customerInfoModel.setBroadband(broadBandList);
                        }

                    }else {
                        logger.info(time1+" Broadband not available");
                        agentLogger.info(time1+" Broadband not available");
                        BroadBand broadBand = new BroadBand();
                        broadBand.setBBPACKAGENAME("");
                        broadBand.setBB_DATARATE("");
                        broadBand.setBB_USERNAME("");

                        broadBandList.add(broadBand);
                        customerInfoModel.setBroadband(broadBandList);
                    }


                    //=======================================================================
                    // CHECK FOR SMART TV (IPTV)
                    String IPTV_username="",IPTV_count="";
                    if (GetInfoFinResponse.has("IPTV_USERNAME")){
                        logger.info(time1+" IPTV_USERNAME tag available");
                        agentLogger.info(time1+" IPTV_USERNAME tag available");
                        IPTV_username = GetInfoFinResponse.get("IPTV_USERNAME").toString();
                        logger.info(time1+" IPTV_USERNAME value:"+IPTV_username);
                        agentLogger.info(time1+" IPTV_USERNAME value:"+IPTV_username);
                    }else{
                        logger.info(time1+" IPTV_USERNAME tag not available");
                        agentLogger.info(time1+" IPTV_USERNAME tag not available");
                    }
                    if (GetInfoFinResponse.has("IPTV_COUNT")){
                        logger.info(time1+" IPTV_COUNT tag available");
                        agentLogger.info(time1+" IPTV_COUNT tag available");
                        String iptvCount = GetInfoFinResponse.get("IPTV_COUNT").toString();
                        if (iptvCount.equals("0")){

                        }else{
                            IPTV_count = iptvCount;
                        }
                        logger.info(time1+" IPTV_COUNT value:"+IPTV_count);
                        agentLogger.info(time1+" IPTV_COUNT value:"+IPTV_count);
                    }else{
                        logger.info(time1+" IPTV_COUNT not available");
                        agentLogger.info(time1+" IPTV_COUNT not available");
                    }
                    SmartTV smartTVObject = new SmartTV();
                    smartTVObject.setIPTV_father_UserName(IPTV_username);
                    smartTVObject.setNumberOf_IPTV(IPTV_count);
                    smartTVList.add(smartTVObject);
                    customerInfoModel.setSmart_tv(smartTVList);

                   //=======================================================================
                    // CHECK FOR EVO Charji
                    logger.info(time1+" Check against EVO Charji");
                    agentLogger.info(time1+" Check against EVO Charji");
                    String evoPackageName = "", evoPackageDes="";
                    if (GetInfoFinResponse.has("PACKAGENAME")){
                        logger.info(time1+" PACKAGENAME tag available");
                        agentLogger.info(time1+" PACKAGENAME tag available");
                        evoPackageName = GetInfoFinResponse.get("PACKAGENAME").toString();
                        logger.info(time1+" PACKAGENAME value:"+evoPackageName);
                        agentLogger.info(time1+" PACKAGENAME value:"+evoPackageName);
                    }else{
                        logger.info(time1+" PACKAGENAME not available");
                        agentLogger.info(time1+" PACKAGENAME not available");
                    }
                    if (GetInfoFinResponse.has("PACKAGEDESC")){
                        logger.info(time1+" PACKAGEDESC tag available");
                        agentLogger.info(time1+" PACKAGEDESC tag available");
                        evoPackageDes = GetInfoFinResponse.get("PACKAGEDESC").toString();
                        logger.info(time1+" PACKAGEDESC value:"+evoPackageDes);
                        agentLogger.info(time1+" PACKAGEDESC value:"+evoPackageDes);
                    }else{
                        logger.info(time1+" PACKAGEDESC tag not available");
                        agentLogger.info(time1+" PACKAGEDESC tag not available");
                    }
                    EVOCharji evoCharji = new EVOCharji();
                    evoCharji.setPACKAGENAME(evoPackageName);
                    evoCharji.setPACKAGEDESC(evoPackageDes);
                    evoCharjiList.add(evoCharji);

                    customerInfoModel.setEvo_charji(evoCharjiList);

                    returnResponse = ConsumeWSDL.javaToJson(customerInfoModel);

                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);
                    CustomerInfoModel customerInfoModel = CustomerInfoModel.iniateEmptyObject();
                    returnResponse = ConsumeWSDL.javaToJson(customerInfoModel);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }

        }catch (Exception e){
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            CustomerInfoModel customerInfoModel = CustomerInfoModel.iniateEmptyObject();
            returnResponse = ConsumeWSDL.javaToJson(customerInfoModel);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;
        }

    }



    /** getSRCategories function will return SR Category list */

    @RequestMapping(value="/GetSRCategories",method = RequestMethod.GET)
    public String getSRCategories(
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ){
        long time1 = System.currentTimeMillis();
        logger.info(time1+" GetSRCategories called with AgentId:"+agentId);
        String returnResponse = null;
        List<String> srCategoryList = new ArrayList<>();
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" GetSRCategories called with AgentId:"+agentId);

        ResourceBundle bundle = ResourceBundle.getBundle("Config");
        String userName = bundle.getString("PTCL_API_ACCESS_USER");
        String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
        String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
        String wsURL = bundle.getString("WSDL_URL_GET_SRCATEGORY");
        String SOAPAction = bundle.getString("SOAP_ACTION_GET_SRCATEGORY");
        String xmlInput = SOAPRequestSample.getSRCategoriesRequest(userName,password,systemIP);

        logger.info(time1+" Requesting CRM to get SR Categories");
        agentLogger.info(time1+" Requesting CRM to get SR Categories");
        try {
            String output = ConsumeWSDL.consumer(wsURL, xmlInput, "");
            // Convert xml string to json
            long ResponseTime = System.currentTimeMillis();
            long CRMResponseTime = ResponseTime - time1;
            logger.info(time1+" Received CRM response after " + CRMResponseTime + " ms");
            agentLogger.info(time1+" Received CRM response after " + CRMResponseTime + " ms");

            JSONObject ResponseJson = ConsumeWSDL.getJson(output);
            JSONObject SRCategory = ResponseJson.getJSONObject("SRCategory");

            logger.info(time1+" CRM response "+ResponseJson);
            agentLogger.info(time1+" CRM response "+ResponseJson);

            int ReturnCode = Integer.parseInt(SRCategory.get("RetCode").toString());
            String ReturnDescription = SRCategory.get("RetDesc").toString();
            //Check CRM return code
            if(ReturnCode == 0){
                logger.info(time1+" CRM response code: "+ReturnCode);
                agentLogger.info(time1+" CRM response code: "+ReturnCode);
                logger.info(time1+" CRM response detail: "+ReturnDescription);
                agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                Object srCAtegoryObject = SRCategory.get("SR_CATEGORY");
                if (checkObjectType(srCAtegoryObject).equals("JSONArray")){
                    logger.info(time1+" SR_CATEGORY type is "+checkObjectType(srCAtegoryObject));
                    agentLogger.info(time1+" SR_CATEGORY type is "+checkObjectType(srCAtegoryObject));

                    JSONArray SR_CATEGORYArray = SRCategory.getJSONArray("SR_CATEGORY");
                    for (int i=0;i<SR_CATEGORYArray.length();i++){
                        srCategoryList.add(SR_CATEGORYArray.get(i).toString());
                    }
                }else{
                    logger.info(time1+" SR_CATEGORY type is "+checkObjectType(srCAtegoryObject));
                    agentLogger.info(time1+" SR_CATEGORY type is "+checkObjectType(srCAtegoryObject));
                    srCategoryList.add(SRCategory.get("SR_CATEGORY").toString());
                }

                SRCategoryResponse returnOnject = new SRCategoryResponse();
                returnOnject.setErrorCode(0);
                returnOnject.setErrorDetail(ReturnDescription);
                returnOnject.setSRCategories(srCategoryList);
                returnResponse = ConsumeWSDL.javaToJson(returnOnject);

            }else{
                logger.info(time1+" CRM response code: "+ReturnCode);
                agentLogger.info(time1+" CRM response code: "+ReturnCode);
                logger.info(time1+" CRM response detail: "+ReturnDescription);
                agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                SRCategoryResponse returnOnject = new SRCategoryResponse();
                returnOnject.setErrorCode(ReturnCode);
                returnOnject.setErrorDetail(ReturnDescription);
                returnResponse = ConsumeWSDL.javaToJson(returnOnject);
            }

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with   "+returnResponse);
            agentLogger.info(time1+" exit with   "+returnResponse);
            return returnResponse;

        }catch (Exception e){
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            SRCategoryResponse returnOnject = new SRCategoryResponse();
            returnOnject.setErrorCode(5);
            returnOnject.setErrorDetail("Sorry I could not fetch SR Category");
            returnResponse = ConsumeWSDL.javaToJson(returnOnject);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }
    }



    /** getSRTypes function will return SRTYPE list against SR Category*/

    @RequestMapping(value="/GetSRTypes",method = RequestMethod.GET)
    public String getSRTypes(
            @RequestParam(value="SRCategory",defaultValue ="") String srCategory,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId

    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<String> srTypeList = new ArrayList<String>();
        logger.info(time1+" GetSRTypes called with SRCategory:"+srCategory+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" GetSRTypes called with SRCategory:"+srCategory+" AgentId:"+agentId);


        if (srCategory.equals("undefined") || StringUtils.isEmpty(srCategory)){
            logger.info(time1+" Wrong Input parameters SRCategory:"+srCategory);
            agentLogger.info(time1+" Wrong Input parameters SRCategory:"+srCategory);
            SRTypesResponse srTypesResponse = new SRTypesResponse();
            srTypesResponse.setErrorCode(4);
            srTypesResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(srTypesResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }else{


            // CONFIGURATION for GetSRTypes API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String wsURL = bundle.getString("WSDL_URL_GET_SRTYPE");
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_SRTYPE");
            String xmlInput = SOAPRequestSample.getSRTypeRequest(userName,password,systemIP,srCategory);

            logger.info(time1+" Requesting CRM to get SR Types with Following Params: " +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " SRCategory:"+srCategory );

            logger.info(time1+" Requesting CRM to get SR Types with Following Params: " +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " SRCategory:"+srCategory );
            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                JSONObject SRType = ResponseJson.getJSONObject("SRType");
                int ReturnCode = Integer.parseInt(SRType.get("RetCode").toString());
                String ReturnDescription = SRType.get("RetDesc").toString();
                //Check CRM return code
                if(ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);

                    Object srTypeObject = SRType.get("SR_TYPE");
                    if (checkObjectType(srTypeObject).equals("JSONArray")){

                        logger.info(time1+" SR_TYPE type is "+checkObjectType(srTypeObject));
                        logger.info(time1+" SR_TYPE type is "+checkObjectType(srTypeObject));

                        JSONArray SRTypeArray = SRType.getJSONArray("SR_TYPE");
                        for (int i=0;i<SRTypeArray.length();i++){
                            srTypeList.add(SRTypeArray.get(i).toString());

                        }
                    }else{
                        logger.info(time1+" SR_TYPE type is "+checkObjectType(srTypeObject));
                        agentLogger.info(time1+" SR_TYPE type is "+checkObjectType(srTypeObject));
                        srTypeList.add(SRType.get("SR_TYPE").toString());
                    }

                    SRTypesResponse srTypesResponse = new SRTypesResponse();
                    srTypesResponse.setErrorCode(0);
                    srTypesResponse.setErrorDetail(ReturnDescription);
                    srTypesResponse.setSRTypes(srTypeList);

                    returnResponse = ConsumeWSDL.javaToJson(srTypesResponse);

                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);
                    SRTypesResponse srTypesResponse = new SRTypesResponse();
                    srTypesResponse.setErrorCode(ReturnCode);
                    srTypesResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(srTypesResponse);
                }
                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                SRTypesResponse srTypesResponse = new SRTypesResponse();
                srTypesResponse.setErrorCode(5);
                srTypesResponse.setErrorDetail("Sorry I could not get SR Types");
                returnResponse = ConsumeWSDL.javaToJson(srTypesResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }
        }
    }



    /** getComplaintCodes function will return list of SR SUB Type and SR Code*/

    @RequestMapping(value="/GetComplaintCodes",method = RequestMethod.GET)
    public String getComplaintCodes(
            @RequestParam(value="SRCategory",defaultValue ="") String srCategory,
            @RequestParam(value="SRType",defaultValue ="") String srType,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId

    )
    {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<SRModel> SRObjectList = new ArrayList<SRModel>();
        logger.info(time1+" GetComplaintCode called with SRCategory:"+srCategory+" SRType:"+srType+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" GetComplaintCode called with SRCategory:"+srCategory+" SRType:"+srType+" AgentId:"+agentId);


        if (srCategory.equals("undefined") || StringUtils.isEmpty(srCategory) || srType.equals("undefined") || StringUtils.isEmpty(srType)){
            logger.info(time1+" Wrong input parameters");
            agentLogger.info(time1+" Wrong input parameters");

            ComplaintCodesResponse complaintCodesResponse = new ComplaintCodesResponse();
            complaintCodesResponse.setErrorCode(4);
            complaintCodesResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(complaintCodesResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;

        }else{
            // CONFIGURATION for Get Complaint Code API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String wsURL = bundle.getString("WSDL_URL_COMPLAINT_CODE");
            String SOAPAction = bundle.getString("SOAP_ACTION_COMPLAINT_CODE");

            String xmlInput = SOAPRequestSample.complaintCodeRequest(userName,password,systemIP,srCategory,srType);

            logger.info(time1+" Request CRM to get Complaint Code with Following Params: " +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    +" SRCategory:" + srCategory + " SRType:" + srType+" AgentId:"+agentId);

            agentLogger.info(time1+" Request CRM to get Complaint Code with Following Params: " +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    +" SRCategory:" + srCategory + " SRType:" + srType+" AgentId:"+agentId);



            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response  "+ResponseJson);
                agentLogger.info(time1+" CRM response  "+ResponseJson);

                JSONObject ComplaintCodes = ResponseJson.getJSONObject("ComplaintCodes");
                int ReturnCode = Integer.parseInt(ComplaintCodes.get("RetCode").toString());
                String ReturnDescription = ComplaintCodes.get("RetDesc").toString();
                //Check CRM return code
                if(ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);
                    if (ComplaintCodes.has("ComplaintCode")){
                        Object complaintCodeObject = ComplaintCodes.get("ComplaintCode");
                        if (checkObjectType(complaintCodeObject).equals("JSONArray")){

                            logger.info(time1+" ComplaintCode type is "+checkObjectType(complaintCodeObject));
                            agentLogger.info(time1+" ComplaintCode type is "+checkObjectType(complaintCodeObject));
                            JSONArray ComplaintCodeArray = ComplaintCodes.getJSONArray("ComplaintCode");
                            for (int i=0;i<ComplaintCodeArray.length();i++){
                                JSONObject codeObject = ComplaintCodeArray.getJSONObject(i);
                                SRModel srModel = new SRModel();
                                srModel.setSRSUBTYPE(codeObject.get("SRSUBTYPE").toString());
                                srModel.setSRTYPECODE(codeObject.get("SRTYPECODE").toString());
                                SRObjectList.add(srModel);
                            }

                            ComplaintCodesResponse responseObject = new ComplaintCodesResponse();
                            responseObject.setErrorCode(0);
                            responseObject.setErrorDetail(ReturnDescription);
                            responseObject.setComplaintCode(SRObjectList);
                            returnResponse = ConsumeWSDL.javaToJson(responseObject);

                        }else if (checkObjectType(complaintCodeObject).equals("JSONObject")){

                            logger.info(time1+" ComplaintCode type is "+checkObjectType(complaintCodeObject));
                            agentLogger.info(time1+" ComplaintCode type is "+checkObjectType(complaintCodeObject));

                            JSONObject ComplaintCode =  ComplaintCodes.getJSONObject("ComplaintCode");
                            SRModel srModel = new SRModel();
                            srModel.setSRSUBTYPE(ComplaintCode.get("SRSUBTYPE").toString());
                            srModel.setSRTYPECODE(ComplaintCode.get("SRTYPECODE").toString());
                            SRObjectList.add(srModel);

                            ComplaintCodesResponse responseObject = new ComplaintCodesResponse();
                            responseObject.setErrorCode(0);
                            responseObject.setErrorDetail(ReturnDescription);
                            responseObject.setComplaintCode(SRObjectList);
                            returnResponse = ConsumeWSDL.javaToJson(responseObject);

                        }else{
                            logger.info(time1+" ComplaintCode type is "+checkObjectType(complaintCodeObject));
                            agentLogger.info(time1+" ComplaintCode type is "+checkObjectType(complaintCodeObject));
                            ComplaintCodesResponse responseObject = new ComplaintCodesResponse();
                            responseObject.setErrorCode(1);
                            responseObject.setErrorDetail("No Data Available");
                            returnResponse = ConsumeWSDL.javaToJson(responseObject);
                        }

                    }else{
                        logger.info(time1+" Missing tag ComplaintCode");
                        agentLogger.info(time1+" Missing tag ComplaintCode");
                        ComplaintCodesResponse responseObject = new ComplaintCodesResponse();
                        responseObject.setErrorCode(2);
                        responseObject.setErrorDetail("Missing tag in response");
                        returnResponse = ConsumeWSDL.javaToJson(responseObject);

                    }

                   /* JSONArray ComplaintCodeArray = ComplaintCodes.getJSONArray("ComplaintCode");
                    for (int i = 0; i < ComplaintCodeArray.length(); ++i) {
                        SRModel srModel = new SRModel();
                        JSONObject ComplaintObject = ComplaintCodeArray.getJSONObject(i);
                        srModel.setSRTYPE(ComplaintObject.getString("SRTYPE"));
                        srModel.setSRSUBTYPE(ComplaintObject.getString("SRSUBTYPE"));
                        srModel.setSRTYPECODE(ComplaintObject.getString("SRTYPECODE"));
                        SRObjectList.add(srModel);
                    }
                    ComplaintCodesResponse complaintCodesResponse = new ComplaintCodesResponse();
                    complaintCodesResponse.setErrorCode(0);
                    complaintCodesResponse.setErrorDetail(ReturnDescription);
                    complaintCodesResponse.setSrModels(SRObjectList);
                    // JAVA TO JSON
                    returnResponse = ConsumeWSDL.javaToJson(complaintCodesResponse);*/
                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    ComplaintCodesResponse responseObject = new ComplaintCodesResponse();
                    responseObject.setErrorCode(ReturnCode);
                    responseObject.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(responseObject);

                   /* logger.info("OWController-getComplaintCodes Response Code from CRM: "+ReturnCode);
                    logger.info("OWController-getComplaintCodes Response Description from CRM: "+ReturnDescription);
                    ComplaintCodesResponse complaintCodesResponse = new ComplaintCodesResponse();
                    complaintCodesResponse.setErrorCode(ReturnCode);
                    complaintCodesResponse.setErrorDetail(ReturnDescription);
                    // JAVA TO JSON
                    returnResponse = ConsumeWSDL.javaToJson(complaintCodesResponse);*/
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                ComplaintCodesResponse complaintCodesResponse = new ComplaintCodesResponse();
                complaintCodesResponse.setErrorCode(5);
                complaintCodesResponse.setErrorDetail("Exception");
                returnResponse = ConsumeWSDL.javaToJson(complaintCodesResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }

        }



    }


    /*
       getSRHistory function will return SR History against service identifier
       between start and end date
    */
    @RequestMapping(value="/GetSRHistory",method = RequestMethod.GET)
    public String getSRHistory(
            @RequestParam(value="ServiceIdentifier",defaultValue ="") String serviceIdentifier,
            @RequestParam(value="ServiceIdentifierType",defaultValue ="") String serviceIdentifierType,
            @RequestParam(value="StartDate",defaultValue ="") String startDate,
            @RequestParam(value="EndDate",defaultValue ="") String endDate,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ) {

        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<SRHistoryModel> srHistoryModelList = new ArrayList<SRHistoryModel>();
        logger.info(time1+" GetSRHistory called with ServiceId:"+serviceIdentifier + " ServiceType:"+serviceIdentifierType
                +" StartDate:"+startDate+" EndDate:"+endDate+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" GetSRHistory called with ServiceId:"+serviceIdentifier + " ServiceType:"+serviceIdentifierType
                +" StartDate:"+startDate+" EndDate:"+endDate+" AgentId:"+agentId);



        if (serviceIdentifier.equals("undefined") || StringUtils.isEmpty(serviceIdentifier) || serviceIdentifierType.equals("undefined") || StringUtils.isEmpty(serviceIdentifierType)
        || startDate.equals("undefined") || StringUtils.isEmpty(startDate) || endDate.equals("undefined") || StringUtils.isEmpty(endDate)){
            logger.info(time1+" Wrong input parameters");
            agentLogger.info(time1+" Wrong input parameters");
            SRHistoryResponse srHistoryResponse = new SRHistoryResponse();
            srHistoryResponse.setErrorCode(4);
            srHistoryResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(srHistoryResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }else{
            // CONFIGURATION for GetSRHistory API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            //int monthsToSubtract = Integer.parseInt(bundle.getString("SR_HISTORY_LAST_MONTH"));
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String wsURL = bundle.getString("WSDL_URL_GET_SRHISTORY");
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_SRHISTORY");

            // TO SHOW LAST ONE MONTH SR HISTORY
       /* Calendar c = Calendar.getInstance();
        String endDate = c.get(Calendar.DATE)+"/"+(c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, -monthsToSubtract);
        String startDate = c.get(Calendar.DATE)+"/"+(c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.YEAR);
        logger.info("OWController-getSRHistory Going to fetch SR History from Date:"+startDate+"   to Date:"+endDate);*/

            String xmlInput =  SOAPRequestSample.getSRHistoryRequest(userName,password,systemIP,serviceIdentifier,serviceIdentifierType,startDate,endDate);
            logger.info(time1+" Request CRM to get SR History with Following Params: "
                    +" UserName:"+userName+" Password:"+password+" SystemIP:" +systemIP
                    +" ServiceIdentifier: "+ serviceIdentifier+" ServiceIdentifierType:"+serviceIdentifierType
                    +" StartDate:"+startDate+" EndDate:"+endDate+" AgentId:"+agentId);

            agentLogger.info(time1+" Request CRM to get SR History with Following Params: "
                    +" UserName:"+userName+" Password:"+password+" SystemIP:" +systemIP
                    +" ServiceIdentifier: "+ serviceIdentifier+" ServiceIdentifierType:"+serviceIdentifierType
                    +" StartDate:"+startDate+" EndDate:"+endDate+" AgentId:"+agentId);

            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
               // System.out.println(ResponseJson);
                logger.info(time1+" CRM response: "+ResponseJson);
                agentLogger.info(time1+" CRM response: "+ResponseJson);
                JSONObject SRHistory = ResponseJson.getJSONObject("SRHistory");
                int ReturnCode = SRHistory.getInt("RetCode");
                String ReturnDescription = SRHistory.getString("RetDesc");

                //Check CRM return code
                if (ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);
                    //-------------------------------------------------------------
                    Object SRObject = SRHistory.get("SR");
                    if (checkObjectType(SRObject).equals("JSONArray"))
                    {
                        logger.info(time1+" SR type is "+checkObjectType(SRObject));
                        agentLogger.info(time1+" SR type is "+checkObjectType(SRObject));
                        JSONArray srHistoryArray = (JSONArray) SRObject;
                        for (int i =0; i< srHistoryArray.length(); i++){
                            SRHistoryModel srHistoryModel = new SRHistoryModel();
                            JSONObject object = srHistoryArray.getJSONObject(i);
                            srHistoryModel.setSUBTYPE(object.get("SUBTYPE").toString());
                            srHistoryModel.setCLOSED(object.get("CLOSED").toString());
                            srHistoryModel.setCREATEDON(object.get("CREATEDON").toString());
                            srHistoryModel.setSRNO(object.get("SRNO").toString());
                            srHistoryModel.setSTATUS(object.get("STATUS").toString());
                            srHistoryModel.setSERVICEID(object.get("SERVICEID").toString());
                            srHistoryModel.setDESCRIPTION(object.get("DESCRIPTION").toString());
                            srHistoryModel.setROOTCAUSEANALYSIS(object.get("ROOTCAUSEANALYSIS").toString());
                            srHistoryModel.setALTERNATEPHONENUMBER(object.get("ALTERNATEPHONENUMBER").toString());
                            srHistoryModel.setASSIGNEDTO(object.get("ASSIGNEDTO").toString());
                            srHistoryModel.setSOURCE(object.get("SOURCE").toString());
                            srHistoryModel.setTYPE(object.get("TYPE").toString());
                            srHistoryModel.setSUBSTATUS(object.get("SUBSTATUS").toString());
                            srHistoryModel.setCREATEDBY(object.get("CREATEDBY").toString());
                            srHistoryModel.setSRCATEGORY(object.get("SRCATEGORY").toString());
                            srHistoryModel.setLEADTIME(object.get("LEADTIME").toString());
                            srHistoryModelList.add(srHistoryModel);
                        }

                        int len = srHistoryArray.length();
                        logger.info(time1+" Number of SR`s= "+len);
                        agentLogger.info(time1+" Number of SR`s= "+len);

                    }else if (checkObjectType(SRObject).equals("JSONObject")){
                        logger.info(time1+" SR type is "+checkObjectType(SRObject));
                        agentLogger.info(time1+" SR type is "+checkObjectType(SRObject));

                        SRHistoryModel srHistoryModel = new SRHistoryModel();
                        JSONObject object = (JSONObject) SRObject;
                        srHistoryModel.setSUBTYPE(object.get("SUBTYPE").toString());
                        srHistoryModel.setCLOSED(object.get("CLOSED").toString());
                        srHistoryModel.setCREATEDON(object.get("CREATEDON").toString());
                        srHistoryModel.setSRNO(object.get("SRNO").toString());
                        srHistoryModel.setSTATUS(object.get("STATUS").toString());
                        srHistoryModel.setSERVICEID(object.get("SERVICEID").toString());
                        srHistoryModel.setDESCRIPTION(object.get("DESCRIPTION").toString());
                        srHistoryModel.setROOTCAUSEANALYSIS(object.get("ROOTCAUSEANALYSIS").toString());
                        srHistoryModel.setALTERNATEPHONENUMBER(object.get("ALTERNATEPHONENUMBER").toString());
                        srHistoryModel.setASSIGNEDTO(object.get("ASSIGNEDTO").toString());
                        srHistoryModel.setSOURCE(object.get("SOURCE").toString());
                        srHistoryModel.setTYPE(object.get("TYPE").toString());
                        srHistoryModel.setSUBSTATUS(object.get("SUBSTATUS").toString());
                        srHistoryModel.setCREATEDBY(object.get("CREATEDBY").toString());
                        srHistoryModel.setSRCATEGORY(object.get("SRCATEGORY").toString());
                        srHistoryModel.setLEADTIME(object.get("LEADTIME").toString());
                        srHistoryModelList.add(srHistoryModel);


                        logger.info(time1+" Number of SR`s=1");
                        agentLogger.info(time1+" Number of SR`s=1");

                    }else {
                        if (SRHistory.getString("SR").equals("")){
                            logger.info(time1+" No SR in history");
                            agentLogger.info(time1+" No SR in history");
                        }else{
                            logger.info(time1+" Some Unexpected value in SR");
                            agentLogger.info(time1+" Some Unexpected value in SR");
                        }
                    }

                    SRHistoryResponse srHistoryResponse = new SRHistoryResponse();
                    srHistoryResponse.setErrorCode(ReturnCode);
                    srHistoryResponse.setErrorDetail(ReturnDescription);
                    srHistoryResponse.setSRHistory(srHistoryModelList);
                    returnResponse = ConsumeWSDL.javaToJson(srHistoryResponse);

                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);
                    //--------------------------------------------------------------
                    SRHistoryResponse srHistoryResponse = new SRHistoryResponse();
                    srHistoryResponse.setErrorCode(ReturnCode);
                    srHistoryResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(srHistoryResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                SRHistoryResponse srHistoryResponse = new SRHistoryResponse();
                srHistoryResponse.setErrorCode(5);
                srHistoryResponse.setErrorDetail("Sorry could not fetch SR History");
                returnResponse = ConsumeWSDL.javaToJson(srHistoryResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }
        }
    }


    @RequestMapping(value="/CreateSR",method = RequestMethod.POST)
    public String createSR(
            @RequestParam(value="AltNumber",defaultValue ="") String altNumber,
            @RequestParam(value="ServiceID",defaultValue ="") String serviceId,
            @RequestParam(value="SRCode",defaultValue ="") String srCode,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="Description",required = false,defaultValue ="") String description,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {

       final long time1 = System.currentTimeMillis();
        String returnResponse = "";
        logger.info(time1+" CreateSR called with ServiceID:"+serviceId+" AltNumber:"+altNumber+" SRCode:"+srCode
        +"Email:"+email+" Description:"+description+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" CreateSR called with ALT_Number:"+altNumber+" ServiceID:"+serviceId+" SRCode:"+srCode
                +"Email:"+email+" Description:"+description+" AgentId:"+agentId);


        if (altNumber.equals("undefined") || StringUtils.isEmpty(altNumber) ||
                serviceId.equals("undefined") || StringUtils.isEmpty(serviceId) ||
                srCode.equals("undefined") || StringUtils.isEmpty(srCode) ||
                email.equals("undefined") || StringUtils.isEmpty(email))
        {
            logger.info(time1+" Wrong input parameters");
            agentLogger.info(time1+" Wrong input parameters");
            CreateSRResponse createSRResponse = new CreateSRResponse();
            createSRResponse.setErrorCode("4");
            createSRResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(createSRResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }else{
            // CONFIGURATION for GetSRHistory API
            /* Remove special character from Description */
            description = removeSpecialCharacter(description);
            logger.info(time1+" Description after removing special characters: "+description);
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String source  = bundle.getString("PTCL_API_ACCESS_SOURCE");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_CREATE_SR");
            String SOAPAction = bundle.getString("SOAP_ACTION_CREATE_SR");
            String xmlInput = SOAPRequestSample.createSRRequest(userName,password,systemIP,altNumber,serviceId,srCode,email,source,
                    description,channel,agentId);

            logger.info(time1+" Requesting CRM to Create SR with Params: UserName: "
                    +userName+" Password:"+password+" SystemIP:"+systemIP
                    +" AlternateNumber:"+altNumber+" ServiceId:"+serviceId+" SRCode:"+srCode+" Email:"+email
                    +" Source:"+source+" Description:"+description+" Channel:"+channel+" AgentId:"+agentId);

            agentLogger.info(time1+" Requesting CRM to Create SR with Params: UserName: "
                    +userName+" Password:"+password+" SystemIP:"+systemIP
                    +" AlternateNumber:"+altNumber+" ServiceId:"+serviceId+" SRCode:"+srCode+" Email:"+email
                    +" Source:"+source+" Description:"+description+" Channel:"+channel+" AgentId:"+agentId);

            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);
                JSONObject PTCL_SR_REGISTRATION = ResponseJson.getJSONObject("ns:PTCL_spcSR_spcRegistration_spcCustom_Output");
               // int Count = PTCL_SR_REGISTRATION.get("ns:Count").toString();
                String ErrorCode = PTCL_SR_REGISTRATION.get("ns:Error_spcCode").toString();
                String ResponseMessage = PTCL_SR_REGISTRATION.get("ns:Response_spcMessage").toString();
                String SRNumber = PTCL_SR_REGISTRATION.get("ns:SR_spcNumber").toString();
                if (ErrorCode.equals("0")){

                    logger.info(time1+" CRM response code: "+ErrorCode);
                    agentLogger.info(time1+" CRM response code: "+ErrorCode);
                    logger.info(time1+" CRM response detail: "+ResponseMessage);
                    agentLogger.info(time1+" CRM response detail: "+ResponseMessage);
                    CreateSRResponse createSRResponse = new CreateSRResponse();
                    createSRResponse.setErrorCode("0");
                    createSRResponse.setErrorDetail(ResponseMessage);
                    createSRResponse.setSRNumber(SRNumber);
                    returnResponse = ConsumeWSDL.javaToJson(createSRResponse);
                }else {
                    logger.info(time1+" CRM response code: "+ErrorCode);
                    agentLogger.info(time1+" CRM response code: "+ErrorCode);
                    logger.info(time1+" CRM response detail: "+ResponseMessage);
                    agentLogger.info(time1+" CRM response detail: "+ResponseMessage);
                    CreateSRResponse createSRResponse = new CreateSRResponse();
                    if(ErrorCode.equals("")){
                        createSRResponse.setErrorCode("0");
                        createSRResponse.setSRNumber(SRNumber);
                    }else{
                        createSRResponse.setErrorCode(ErrorCode);
                    }
                    createSRResponse.setErrorDetail(ResponseMessage);
                    returnResponse = ConsumeWSDL.javaToJson(createSRResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                CreateSRResponse createSRResponse = new CreateSRResponse();
                createSRResponse.setErrorCode("5");
                createSRResponse.setErrorDetail("Sorry I could not Create Your SR");
                returnResponse = ConsumeWSDL.javaToJson(createSRResponse);
                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }

        }
    }


    @RequestMapping(value="/GetProductWireLine",method = RequestMethod.GET)
    public String getProductWireLine(
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {

        final long time1 = System.currentTimeMillis();
        List<WireLineProductModel> wireLineProductModelList = new ArrayList<WireLineProductModel>();
        String returnResponse = null;
        logger.info(time1+" GetProductWireline called with AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" GetProductWireline called with AgentId:"+agentId);


        // CONFIGURATION for Get WireLine Product list API
        ResourceBundle bundle = ResourceBundle.getBundle("Config");
        String userName = bundle.getString("PTCL_API_ACCESS_USER");
        String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
        String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
        String wsURL = bundle.getString("WSDL_URL_WIRELINE_PRODUCTS");
        String xmlInput = SOAPRequestSample.getProductWirelineRequest(userName,password,systemIP);
        String SOAPAction = bundle.getString("SOAP_ACTION_WIRELINE_PRODUCT");

        logger.info(time1+" Requesting CRM to get Wireline Product with Following Params:" +
                " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP);

        agentLogger.info(time1+" Requesting CRM to get Wireline Product with Following Params:" +
                " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP);

        try {
            String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
            // Convert xml string to json
            long ResponseTime = System.currentTimeMillis();
            long CRMResponseTime = ResponseTime - time1;
            logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
            agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

            JSONObject ResponseJson = ConsumeWSDL.getJson(output);
            logger.info(time1+" CRM response "+ResponseJson);
            agentLogger.info(time1+" CRM response "+ResponseJson);
            JSONObject ProductWireLine = ResponseJson.getJSONObject("ProductWireLine");
            int ReturnCode = ProductWireLine.getInt("RetCode");
            String ReturnDescription = ProductWireLine.getString("RetDesc");
            //Check CRM return code
            if(ReturnCode == 0){
                logger.info(time1+" CRM response code: "+ReturnCode);
                agentLogger.info(time1+" CRM response code: "+ReturnCode);
                logger.info(time1+" CRM response detail: "+ReturnDescription);
                agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                JSONArray PRODUCTNAME = ProductWireLine.getJSONArray("PRODUCTNAME");
                for (int i = 0; i < PRODUCTNAME.length(); ++i) {
                    WireLineProductModel wireLineProductModel = new WireLineProductModel();
                    wireLineProductModel.setPRODUCTNAME(PRODUCTNAME.getString(i));
                    wireLineProductModelList.add(wireLineProductModel);
                }
                returnResponse = ConsumeWSDL.javaToJson(wireLineProductModelList);
            }else{
                logger.info(time1+" CRM response code: "+ReturnCode);
                agentLogger.info(time1+" CRM response code: "+ReturnCode);
                logger.info(time1+" CRM response detail: "+ReturnDescription);
                agentLogger.info(time1+" CRM response detail: "+ReturnDescription);
                returnResponse = ReturnDescription;
            }

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }catch (Exception e){
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            return returnResponse;
        }

    }




    @RequestMapping(value="/GetPackageWireLine",method = RequestMethod.GET)
    public String getPackageWireLine(
            @RequestParam(value="Product",defaultValue ="") String product,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<WireLinePackageModel> wireLinePackageModelList = new ArrayList<WireLinePackageModel>();
        List<WireLinePackageModel> wireLinePackageModelList2 = new ArrayList<WireLinePackageModel>();

        logger.info(time1+" GetPackageWireline called with Product:"+product+" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" GetPackageWireline called with Product:"+product+" AgentId:"+agentId);


        try {

            if (product.equals("undefined") || StringUtils.isEmpty(product) )
            {
                logger.info(time1+" Wrong input parameters");
                agentLogger.info(time1+" Wrong input parameters");
                PackagesWirelineResponse packagesWirelineResponse = new PackagesWirelineResponse();
                packagesWirelineResponse.setErrorCode(4);
                packagesWirelineResponse.setErrorDetail("Wrong input parameters");
                returnResponse = ConsumeWSDL.javaToJson(packagesWirelineResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }else{

                // CONFIGURATION URL for Get WireLine Package list API
                ResourceBundle bundle = ResourceBundle.getBundle("Config");
                String userName = bundle.getString("PTCL_API_ACCESS_USER");
                String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
                String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
                String wsURL = bundle.getString("WSDL_URL_WIRELINE_PACKAGES");
                String xmlInput = SOAPRequestSample.getPackageWirelineRequest(userName,password,systemIP,product);
                String SOAPAction = bundle.getString("SOAP_ACTION_WIRELINE_PACKAGES");

                logger.info(time1+" Requesting CRM to get WireLine Package List with Following Params: " +
                        " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP+" Product:"+product);

                agentLogger.info(time1+" Requesting CRM to get WireLine Package List with Following Params: " +
                        " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP+" Product:"+product);


                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);
                JSONObject ProductWireLine = ResponseJson.getJSONObject("PackagelistWireLine");
                int ReturnCode = ProductWireLine.getInt("RetCode");
                String ReturnDescription = ProductWireLine.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    JSONArray PACKAGENAME = ProductWireLine.getJSONArray("PACKAGENAME");
                    JSONArray PRICEPLANS = ProductWireLine.getJSONArray("PRICEPLAN");
                    for (int i = 0; i<PACKAGENAME.length(); ++i){
                        WireLinePackageModel wireLinePackageModel = new WireLinePackageModel();
                        wireLinePackageModel.setPACKAGENAME(PACKAGENAME.get(i).toString());
                        wireLinePackageModel.setPRICEPLANID(PRICEPLANS.get(i).toString());
                        wireLinePackageModelList.add(wireLinePackageModel);
                    }
                /*for (int i = 0; i < PACKAGENAME.length(); ++i) {
                    WireLinePackageModel wireLinePackageModel = new WireLinePackageModel();
                    wireLinePackageModel.setPACKAGENAME(PACKAGENAME.getString(i));
                    wireLinePackageModelList.add(wireLinePackageModel);
                }
               JSONArray PRICEPLANS = ProductWireLine.getJSONArray("PRICEPLAN");
               for (int i = 0; i < PRICEPLANS.length(); ++i) {
                   WireLinePackageModel wireLinePackageModel = new WireLinePackageModel();
                   wireLinePackageModel.setPRICEPLANID(PRICEPLANS.getString(i));
                   wireLinePackageModelList2.add(wireLinePackageModel);
               }*/

                    PackagesWirelineResponse packagesWirelineResponse = new PackagesWirelineResponse();
                    packagesWirelineResponse.setErrorCode(0);
                    packagesWirelineResponse.setErrorDetail(ReturnDescription);
                    packagesWirelineResponse.setPACKAGES(wireLinePackageModelList);
               /*packagesWirelineResponse.setPACKAGENAMES(wireLinePackageModelList);
               packagesWirelineResponse.setPRICEPLANIDS(wireLinePackageModelList2);*/

                    returnResponse = ConsumeWSDL.javaToJson(packagesWirelineResponse);
                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    PackagesWirelineResponse packagesWirelineResponse = new PackagesWirelineResponse();
                    packagesWirelineResponse.setErrorCode(ReturnCode);
                    packagesWirelineResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packagesWirelineResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }

        }catch (Exception e){
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            PackagesWirelineResponse packagesWirelineResponse = new PackagesWirelineResponse();
            packagesWirelineResponse.setErrorCode(5);
            packagesWirelineResponse.setErrorDetail("Sorry I could not get packages");
            returnResponse = ConsumeWSDL.javaToJson(packagesWirelineResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }

    }



    @RequestMapping(value="/GetDupBill",method = RequestMethod.GET)
    public String getDuplicateBill(
            @RequestParam(value="SubscriberNo",defaultValue ="") String subscriberNo,
            @RequestParam(value="SubscriberId",defaultValue ="",required = false) String subscriberId,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {

       final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        String SUBSCRIBER_NO = null;
        String SUBSCRIBER_ID = "";
        logger.info(time1+" GetDupBill called with SubscriberNo:"+subscriberNo+" SubscriberId:"+subscriberId+" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" GetDupBill called with SubscriberNo:"+subscriberNo+" SubscriberId:"+subscriberId+" AgentId:"+agentId);

        try {
            if (subscriberNo.equals("undefined") || StringUtils.isEmpty(subscriberNo) ){
                logger.info(time1+" Wrong Input Parameters");
                agentLogger.info(time1+" Wrong Input Parameters");

                CurrentBillModel currentBillModel = new CurrentBillModel();
                currentBillModel.setDue_date("");
                currentBillModel.setGenerated_on("");
                currentBillModel.setOver_usage("");
                returnResponse = ConsumeWSDL.javaToJson(currentBillModel);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }else{
                if (subscriberId.equals("undefined") || StringUtils.isEmpty(subscriberId) || subscriberId.equals("0")){
                    logger.info(time1+" Requested to get bill info against MDN:"+subscriberNo);
                    agentLogger.info(time1+" Requested to get bill info against MDN:"+subscriberNo);
                    SUBSCRIBER_NO = subscriberNo;

                }else{
                    logger.info(time1+" Requested to get bill info against PSTN:"+subscriberNo);
                    agentLogger.info(time1+" Requested to get bill info against PSTN:"+subscriberNo);
                    SUBSCRIBER_NO = subscriberNo.substring(3);
                    logger.info(time1+" PSTN after removing 3 digit area code:"+SUBSCRIBER_NO);
                    agentLogger.info(time1+" PSTN after removing 3 digit area code:"+SUBSCRIBER_NO);
                    SUBSCRIBER_ID = subscriberId;

                }

                // CONFIGURATION for GET DUP BILL API
                ResourceBundle bundle = ResourceBundle.getBundle("Config");
                String userName = bundle.getString("PTCL_API_ACCESS_USER");
                String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
                String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
                String wsURL = bundle.getString("WSDL_URL_GET_DUP_BILL");
                String SOAPAction = bundle.getString("SOAP_ACTION_GET_DUP_BILL");

                String xmlInput = SOAPRequestSample.getDupBill(userName,password,systemIP,SUBSCRIBER_NO,SUBSCRIBER_ID);

                logger.info(time1+" Requesting CRM to get billing detail with parameters" +
                        " UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                        + " SubscriberNo:"+ SUBSCRIBER_NO+" SubscriberId:"+SUBSCRIBER_ID+" AgentId:"+agentId);

                agentLogger.info(time1+" Requesting CRM to get billing detail with parameters" +
                        " UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                        + " SubscriberNo:"+ SUBSCRIBER_NO+" SubscriberId:"+SUBSCRIBER_ID+" AgentId:"+agentId);


                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject DupBill = ResponseJson.getJSONObject("DupBill");
                int ReturnCode = DupBill.getInt("RetCode");
                String ReturnDescription = DupBill.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    CurrentBillModel currentBillModel = new CurrentBillModel();
                    currentBillModel.setTotal_bill(Long.parseLong(DupBill.get("DUETOTAL").toString()));
                    currentBillModel.setPtcl_charges(Long.parseLong(DupBill.get("DUETOTAL").toString()));
                    currentBillModel.setDue_date(DupBill.get("DUE_DATE").toString());
                    currentBillModel.setDSL_over_usage(Long.parseLong(DupBill.get("FIXED_BB_EXTRA_USAGE_CHRG").toString()));
                    currentBillModel.setGenerated_on("");
                    currentBillModel.setLast_month_bill(Long.parseLong(DupBill.get("BILLED_AMOUNT6").toString()));
                    currentBillModel.setOver_usage(DupBill.get("BBUSAGEFIELD").toString());
                    returnResponse = ConsumeWSDL.javaToJson(currentBillModel);

                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    CurrentBillModel currentBillModel = new CurrentBillModel();
                    currentBillModel.setDue_date("");
                    currentBillModel.setGenerated_on("");
                    currentBillModel.setOver_usage("");
                    returnResponse = ConsumeWSDL.javaToJson(currentBillModel);

                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }

        }catch (Exception e){
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            CurrentBillModel currentBillModel = new CurrentBillModel();
            currentBillModel.setDue_date("");
            currentBillModel.setGenerated_on("");
            currentBillModel.setOver_usage("");
            returnResponse = ConsumeWSDL.javaToJson(currentBillModel);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }


    }



    @RequestMapping(value="/GetDupBillLink",method = RequestMethod.GET)
    public String getDuplicateBillLink(
            @RequestParam(value="SubscriberNo",defaultValue ="") String subscriberNo,
            @RequestParam(value="AccountId",defaultValue ="") String accountId,
            @RequestParam(value="BillType",defaultValue ="") String billType,
            @RequestParam(value="AgentId",defaultValue ="") String agentId
    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" GetDupBillLink called with SubscriberNo:"+subscriberNo+" AccountId:"+accountId+" BillType:"+billType+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" GetDupBillLink called with SubscriberNo:"+subscriberNo+" AccountId:"+accountId+" BillType:"+billType+" AgentId:"+agentId);

        if (subscriberNo.equals("undefined") || StringUtils.isEmpty(subscriberNo) ||
                accountId.equals("undefined") || StringUtils.isEmpty(accountId) ||
                billType.equals("undefined") || StringUtils.isEmpty(billType))
        {
           logger.info(time1+" Wrong input parameters");
           agentLogger.info(time1+" Wrong input parameters");
            LinkResponse  linkResponse = new LinkResponse();
            linkResponse.setErrorCode(4);
            linkResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(linkResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }else{
            logger.info(time1+" PSTN before removing area code:"+subscriberNo);
            agentLogger.info(time1+" PSTN before removing area code:"+subscriberNo);
            String pstnWithOutAreaCode = subscriberNo.substring(3);
            logger.info(time1+"PSTN after removing area code:"+pstnWithOutAreaCode);
            agentLogger.info(time1+"PSTN after removing area code:"+pstnWithOutAreaCode);
            // CONFIGURATION for GET DUP BILL API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String requesterIP = bundle.getString("REQUESTER_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_GET_DUP_BILL_LINK");
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_DUP_BILL_LINK");

            String xmlInput = SOAPRequestSample.duplicateBillLinkRequest(userName,password,systemIP,requesterIP,channel,
                    accountId,pstnWithOutAreaCode,billType);

            logger.info(time1+" Requesting CRM to get Duplicate Bill link with Params: UserName:"
                    +userName+" Password:"+password+" SystemIP:"+systemIP
                    +" SubscriberNo:"+ pstnWithOutAreaCode+" AccountId:"+accountId
                    +" BillType:"+ billType+" Channel:"+channel+" AgentId:"+agentId);

            agentLogger.info(time1+" Requesting CRM to get Duplicate Bill link with Params: UserName:"
                    +userName+" Password:"+password+" SystemIP:"+systemIP
                    +" SubscriberNo:"+ pstnWithOutAreaCode+" AccountId:"+accountId
                    +" BillType:"+ billType+" Channel:"+channel+" AgentId:"+agentId);


            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                Object dupBillLinkObj = ResponseJson.get("DupBillLink");

                if (checkObjectType(dupBillLinkObj).equals("String")){

                    logger.info(time1+" CRM Response: Empty link ");
                    agentLogger.info(time1+" CRM Response: Empty link ");
                    LinkResponse  linkResponse = new LinkResponse();
                    linkResponse.setErrorCode(1);
                    linkResponse.setErrorDetail("No Bill found against "+subscriberNo);
                    returnResponse = ConsumeWSDL.javaToJson(linkResponse);
                }else{

                    JSONObject DupBill = ResponseJson.getJSONObject("DupBillLink");
                    int ReturnCode = Integer.parseInt(DupBill.get("RetCode").toString());
                    String ReturnDescription = DupBill.get("RetDesc").toString();

                    if(ReturnCode == 0)
                    {
                        logger.info(time1+" CRM response code: "+ReturnCode);
                        agentLogger.info(time1+" CRM response code: "+ReturnCode);
                        logger.info(time1+" CRM response detail: "+ReturnDescription);
                        agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                        String BillLink = DupBill.get("DupBillLink").toString();

                        LinkResponse  linkResponse = new LinkResponse();
                        linkResponse.setErrorCode(0);
                        linkResponse.setErrorDetail(ReturnDescription);
                        linkResponse.setLink(BillLink);
                        returnResponse = ConsumeWSDL.javaToJson(linkResponse);
                    }else{
                        logger.info(time1+" CRM response code: "+ReturnCode);
                        agentLogger.info(time1+" CRM response code: "+ReturnCode);
                        logger.info(time1+" CRM response detail: "+ReturnDescription);
                        agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                        LinkResponse  linkResponse = new LinkResponse();
                        linkResponse.setErrorCode(ReturnCode);
                        linkResponse.setErrorDetail(ReturnDescription);
                        returnResponse = ConsumeWSDL.javaToJson(linkResponse);
                    }
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                LinkResponse  linkResponse = new LinkResponse();
                linkResponse.setErrorCode(5);
                linkResponse.setErrorDetail("Sorry I could not get bill link");
                returnResponse = ConsumeWSDL.javaToJson(linkResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }

        }


    }


    @RequestMapping(value = "/SendBill",method = RequestMethod.POST)
    public String sendBill(
            @RequestParam(value = "SubscriberNo",defaultValue = "") String subscriberNo,
            @RequestParam(value = "BillingAccountId",defaultValue = "") String billingAccountId,
            @RequestParam(value = "Email",defaultValue = "") String email,
            @RequestParam(value = "BillType",defaultValue = "") String billType,
            @RequestParam(value = "AgentId",defaultValue = "",required = false) String agentId

    ){
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" SendBill called with SubscriberNo:"+subscriberNo+" BillingAccountID:"+billingAccountId
                +" Email:"+email+" BillType:"+billType+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" SendBill called with SubscriberNo:"+subscriberNo+" BillingAccountID:"+billingAccountId
                +" Email:"+email+" BillType:"+billType+" AgentId:"+agentId);

        if (subscriberNo.equals("undefined") || StringUtils.isEmpty(subscriberNo) ||
                billingAccountId.equals("undefined") || StringUtils.isEmpty(billingAccountId) ||
                email.equals("undefined") || StringUtils.isEmpty(email) ||
                billType.equals("undefined") || StringUtils.isEmpty(billType))
        {
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");
            SendBillResponse sendBillResponse = new SendBillResponse();
            sendBillResponse.setErrorCode(4);
            sendBillResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(sendBillResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }else{
            try {
                // CONFIGURATION for SEND BILL API
                ResourceBundle bundle = ResourceBundle.getBundle("Config");
                String emailSubject = bundle.getString("EMAIL_SUBJECT_BILL_LINK");
                String emailBody = bundle.getString("EMAIL_BODY_BILL_LINK");

                logger.info(time1+" Going to get bill link with params SubscriberNo:"+subscriberNo+" BillingAccountId:"+billingAccountId
                        +" BillType:"+billType+" AgentId:"+agentId);

                agentLogger.info(time1+" Going to get bill link with params SubscriberNo:"+subscriberNo+" BillingAccountId:"+billingAccountId
                        +" BillType:"+billType+" AgentId:"+agentId);

                String BillLinkResponse = getDuplicateBillLink(subscriberNo, billingAccountId,billType,agentId);
                JSONObject BillLinkJson = new JSONObject(BillLinkResponse);
                int BillLinkErrorCode = Integer.parseInt(BillLinkJson.get("ErrorCode").toString());
                String BillLinkErrorDetail = BillLinkJson.getString("ErrorDetail");

                if (BillLinkErrorCode == 0) {
                    logger.info(time1+" Get bill link response code:"+BillLinkErrorCode);
                    agentLogger.info(time1+" Get bill link response code:"+BillLinkErrorCode);
                    logger.info(time1+" Get bill link response detail:"+BillLinkErrorDetail);
                    agentLogger.info(time1+" Get bill link response detail:"+BillLinkErrorDetail);
                    String BillLink = BillLinkJson.getString("Link");
                    logger.info(time1+" Received bill link: "+BillLink);
                    agentLogger.info(time1+" Received bill link: "+BillLink);

                    logger.info(time1+" Going to send bill link to customer email: "+email);
                    agentLogger.info(time1+" Going to send bill link to customer email: "+email);
                    String customBody = emailBody+"  " +BillLink;
                    String emailResponse =  sendEmail(email,emailSubject,customBody,agentId);

                    if (emailResponse.equals("sent")){
                        logger.info(time1+" Successfully sent bill to customer email: "+email);
                        agentLogger.info(time1+" Successfully sent bill to customer email: "+email);
                        SendBillResponse sendBillResponse = new SendBillResponse();
                        sendBillResponse.setErrorCode(0);
                        sendBillResponse.setErrorDetail("Sent Successfully");
                        returnResponse = ConsumeWSDL.javaToJson(sendBillResponse);
                    }else{
                        logger.info(time1+" Bill link found but couldn`t send email to: "+email);
                        agentLogger.info(time1+" Bill link found but couldn`t send email to: "+email);
                        SendBillResponse sendBillResponse = new SendBillResponse();
                        sendBillResponse.setErrorCode(1);
                        sendBillResponse.setErrorDetail("Sorry I could not send bill");
                        returnResponse = ConsumeWSDL.javaToJson(sendBillResponse);
                    }

                } else {
                    logger.info(time1+" Get bill link response code:"+BillLinkErrorCode);
                    agentLogger.info(time1+" Get bill link response code:"+BillLinkErrorCode);
                    logger.info(time1+" Get bill link response detail:"+BillLinkErrorDetail);
                    agentLogger.info(time1+" Get bill link response detail:"+BillLinkErrorDetail);

                    SendBillResponse sendBillResponse = new SendBillResponse();
                    sendBillResponse.setErrorCode(1);
                    //sendBillResponse.setErrorDetail(BillLinkErrorDetail);
                    sendBillResponse.setErrorDetail("No Bill found against:"+subscriberNo);
                    returnResponse = ConsumeWSDL.javaToJson(sendBillResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.info(time1+" Exception:"+e);
                agentLogger.info(time1+" Exception:"+e);
                SendBillResponse sendBillResponse = new SendBillResponse();
                sendBillResponse.setErrorCode(5);
                sendBillResponse.setErrorDetail("Sorry I could not send bill");
                returnResponse = ConsumeWSDL.javaToJson(sendBillResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }


        }

    }

    @RequestMapping(value = "/GetEmailVerificationLink",method = RequestMethod.POST)
    public String getEmailVerificationLink(
            @RequestParam(value="PSTN",defaultValue = "") String pstn,
            @RequestParam(value="CustomerAccountID",defaultValue ="") String accountId,
            @RequestParam(value="BillingAccount",defaultValue = "") String billingAccountId,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="AgentId",defaultValue = "",required = false) String agentId
    ){
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" GetEmailVerificationLink called with PSTN:"+pstn+" CustomerAccountId:"+accountId+" BillingAccountId:"+billingAccountId+" Email:"+email+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" GetEmailVerificationLink called with PSTN:"+pstn+" CustomerAccountId:"+accountId+" BillingAccountId:"+billingAccountId+" Email:"+email+" AgentId:"+agentId);

        if (pstn.equals("undefined") || StringUtils.isEmpty(pstn) ||
                billingAccountId.equals("undefined") || StringUtils.isEmpty(billingAccountId) ||
                email.equals("undefined") || StringUtils.isEmpty(email) ||
                accountId.equals("undefined") || StringUtils.isEmpty(accountId))
        {
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");
            LinkResponse linkResponse = new LinkResponse();
            linkResponse.setErrorCode(4);
            linkResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(linkResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;

        }else{

            // CONFIGURATION for GET Email verificaiton link API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");

            String wsURL = bundle.getString("WSDL_URL_GET_EMAIL_LINK");
            String xmlInput = SOAPRequestSample.getEmailVerificationLinkRequest(userName,password,systemIP,pstn,accountId,billingAccountId,channel,email,agentId);
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_EMAIL_LINK");

            logger.info(time1+" Requesting CRM get Email verification link with Following Params " +
                    "UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " PSTN:"+ pstn+ " CustomerAccountId:"+ accountId+ " BillAccountId:"+ billingAccountId
                    + " Channel:"+channel+" Email:"+email+" AgentId:"+agentId);

            agentLogger.info(time1+" Requesting CRM get Email verification link with Following Params " +
                    "UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " PSTN:"+ pstn+ " CustomerAccountId:"+ accountId+ " BillAccountId:"+ billingAccountId
                    + " Channel:"+channel+" Email:"+email+" AgentId:"+agentId);

            try {


                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject EmailURL = ResponseJson.getJSONObject("EmailURL");
                int ReturnCode = EmailURL.getInt("RetCode");
                String ReturnDescription = EmailURL.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    String emailLink = EmailURL.getString("URL");
                    LinkResponse linkResponse = new LinkResponse();
                    linkResponse.setErrorCode(0);
                    linkResponse.setErrorDetail(ReturnDescription);
                    linkResponse.setLink(emailLink);
                    returnResponse = ConsumeWSDL.javaToJson(linkResponse);

                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    LinkResponse linkResponse = new LinkResponse();
                    linkResponse.setErrorCode(ReturnCode);
                    linkResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(linkResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                LinkResponse linkResponse = new LinkResponse();
                linkResponse.setErrorCode(5);
                linkResponse.setErrorDetail("Sorry I could not get email verification link");
                returnResponse = ConsumeWSDL.javaToJson(linkResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }


        }


    }


    @RequestMapping(value = "/SendEmailVerification",method = RequestMethod.POST)
    public String sendEmailVerification(
            @RequestParam(value="PSTN",defaultValue = "") String pstn,
            @RequestParam(value="CustomerAccountID",defaultValue ="") String accountId,
            @RequestParam(value="BillingAccount",defaultValue = "") String billingAccountId,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="AgentId",defaultValue = "",required = false) String agentId
    ){
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" SendEmailVerification called with PSTN:"+pstn+" CustomerAccountId:"+accountId+" BillingAccountId:"+billingAccountId+" Email:"+email+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" SendEmailVerification called with PSTN:"+pstn+" CustomerAccountId:"+accountId+" BillingAccountId:"+billingAccountId+" Email:"+email+" AgentId:"+agentId);

        if (pstn.equals("undefined") || StringUtils.isEmpty(pstn) ||
                billingAccountId.equals("undefined") || StringUtils.isEmpty(billingAccountId) ||
                email.equals("undefined") || StringUtils.isEmpty(email) ||
                accountId.equals("undefined") || StringUtils.isEmpty(accountId))
        {
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");
            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setErrorCode(4);
            generalResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(generalResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;

        }else{

            try {

                ResourceBundle bundle = ResourceBundle.getBundle("Config");
                String emailSubject  = bundle.getString("EMAIL_SUBJECT_EMAIL_VERIFICATION");
                String emailBody = bundle.getString("EMAIL_BODY_EMAIL_VERIFICATION");

                logger.info(time1+" Going to call GetEmailVerificationLink with params:"
                        + " PSTN:" + pstn +" CustomerAccountId:"+accountId+" BillingAccountId:" + billingAccountId
                        + " Email:" + email +" AgentId:"+agentId);

                agentLogger.info(time1+" Going to call GetEmailVerificationLink with params:"
                        + " PSTN:" + pstn +" CustomerAccountId:"+accountId+" BillingAccountId:" + billingAccountId
                        + " Email:" + email +" AgentId:"+agentId);

                String LinkResponse = getEmailVerificationLink(pstn,accountId,billingAccountId,email,agentId);
                JSONObject LinkJson = new JSONObject(LinkResponse);
                int LinkErrorCode = LinkJson.getInt("ErrorCode");
                String LinkErrorDetail = LinkJson.getString("ErrorDetail");

                logger.info(time1+" Received response from getEmailVerificationLink: " + LinkErrorDetail);
                agentLogger.info(time1+" Received response from getEmailVerificationLink: " + LinkErrorDetail);

                if (LinkErrorCode == 0) {

                    String Link = LinkJson.getString("Link");
                    logger.info(time1+" Email verification link: "+Link);
                    agentLogger.info(time1+" Email verification link: "+Link);
                    logger.info(time1+" Going to send link on email: "+email);
                    agentLogger.info(time1+" Going to send link on email: "+email);

                    String customBody = emailBody+"  " +Link;
                    String emailResponse = sendEmail(email,emailSubject,customBody,agentId) ;
                    if (emailResponse.equals("sent")){
                        logger.info(time1+" Email sent Successfully on email: "+email);
                        agentLogger.info(time1+" Email sent Successfully on email: "+email);
                        GeneralResponse generalResponse = new GeneralResponse();
                        generalResponse.setErrorCode(0);
                        generalResponse.setErrorDetail("Email verification link successfully sent");
                        returnResponse = ConsumeWSDL.javaToJson(generalResponse);
                    }else{
                        logger.info(time1+" Email verification link could not send");
                        agentLogger.info(time1+" Email verification link could not send");
                        GeneralResponse generalResponse = new GeneralResponse();
                        generalResponse.setErrorCode(1);
                        generalResponse.setErrorDetail("Failed while sending email verification link");
                        returnResponse = ConsumeWSDL.javaToJson(generalResponse);
                    }

                } else {
                    logger.info(time1+" Failure from getEmailVerificationLink: " + LinkErrorDetail);
                    agentLogger.info(time1+" Failure from getEmailVerificationLink: " + LinkErrorDetail);

                    GeneralResponse generalResponse = new GeneralResponse();
                    generalResponse.setErrorCode(1);
                    generalResponse.setErrorDetail(LinkErrorDetail);
                    returnResponse = ConsumeWSDL.javaToJson(generalResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.info(time1+" Exception:"+e);
                GeneralResponse generalResponse = new GeneralResponse();
                generalResponse.setErrorCode(5);
                generalResponse.setErrorDetail("Sorry I could not send email verification link");
                returnResponse = ConsumeWSDL.javaToJson(generalResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return  returnResponse;
            }
        }


    }





    @RequestMapping(value="/UpdateCNICNumber",method = RequestMethod.POST)
    public String updateCNICNumber(
            @RequestParam(value="PSTN",defaultValue = "") String pstn,
            @RequestParam(value="CNIC",defaultValue = "") String cnic,
            @RequestParam(value="CustomerAccountID",defaultValue ="") String accountId,
            @RequestParam(value="BillingAccount",defaultValue = "") String billingAccountId,
            @RequestParam(value="CustomerName",defaultValue ="") String customerName,
            @RequestParam(value="FatherName",defaultValue ="",required = false) String fatherName,
            @RequestParam(value="NadraPresentAddress",defaultValue ="") String nPresentAddr,
            @RequestParam(value="NadraParmanentAddress",defaultValue ="") String nPermanentAddr,
            @RequestParam(value="NadraTransactionID",required = false,defaultValue ="") String nTransactionId,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;

        logger.info(time1+" UpdateCNICNumber called with PSTN:"+pstn+" CNIC:"+cnic
                +" CustomerAccountId:"+accountId+" BillingAccountId:"+billingAccountId
                +" CustomerName:"+customerName+" FatherName:"+fatherName
                +" PresentAddress:"+nPresentAddr+" ParmanentAddress:"+nPermanentAddr
                +" TransactionId:"+nTransactionId+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" UpdateCNICNumber called with PSTN:"+pstn+" CNIC:"+cnic
                +" CustomerAccountId:"+accountId+" BillingAccountId:"+billingAccountId
                +" CustomerName:"+customerName+" FatherName:"+fatherName
                +" PresentAddress:"+nPresentAddr+" ParmanentAddress:"+nPermanentAddr
                +" TransactionId:"+nTransactionId+" AgentId:"+agentId);


        if (pstn.equals("undefined") || StringUtils.isEmpty(pstn) ||
                cnic.equals("undefined") || StringUtils.isEmpty(cnic) ||
                accountId.equals("undefined") || StringUtils.isEmpty(accountId) ||
                billingAccountId.equals("undefined") || StringUtils.isEmpty(billingAccountId) ||
                customerName.equals("undefined") || StringUtils.isEmpty(customerName) ||
                nPresentAddr.equals("undefined") || StringUtils.isEmpty(nPresentAddr) ||
                nPermanentAddr.equals("undefined") || StringUtils.isEmpty(nPermanentAddr)
        ){

            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");
            UpdateResponse updateResponse = new UpdateResponse();
            updateResponse.setErrorCode(4);
            updateResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(updateResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;

        }else{

            // WSDL URL for Update CNIC Number API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_UPDATE_CNIC");

            String xmlInput = SOAPRequestSample.updateCNICNumberRequest(userName,password,systemIP,channel,cnic,pstn,
                    accountId,billingAccountId,agentId,customerName,fatherName,nPresentAddr,nPermanentAddr,nTransactionId);
            String SOAPAction = bundle.getString("SOAP_ACTION_UPDATE_CNIC");

            logger.info(time1+" Requesting CRM to upadte customer CNIC with Following Params" +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " CNIC:"+ cnic +" PSTN:"+ pstn + " AccountID:"+accountId+" AgentId:"+agentId
                    + " CustomerName:"+customerName+" FatherName:"+fatherName
                    + " NadraPresentAddress:"+nPresentAddr+" NadraPermanentAddress:"+nPermanentAddr
                    + " NadraTransactionID:"+nTransactionId+" Channel:"+channel);

            agentLogger.info(time1+" Requesting CRM to upadte customer CNIC with Following Params" +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " CNIC:"+ cnic +" PSTN:"+ pstn + " AccountID:"+accountId+" AgentId:"+agentId
                    + " CustomerName:"+customerName+" FatherName:"+fatherName
                    + " NadraPresentAddress:"+nPresentAddr+" NadraPermanentAddress:"+nPermanentAddr
                    + " NadraTransactionID:"+nTransactionId+" Channel:"+channel);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject UpdateCNICNumber = ResponseJson.getJSONObject("UpdateCNICNumber");
                int ReturnCode = UpdateCNICNumber.getInt("RetCode");
                String ReturnDescription = UpdateCNICNumber.getString("RetDesc");
                if (ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(0);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }else {
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(ReturnCode);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                UpdateResponse updateResponse = new UpdateResponse();
                updateResponse.setErrorCode(5);
                updateResponse.setErrorDetail("Sorry I could not update CNIC");
                returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }

        }

    }


    @RequestMapping(value="/updateCustomer",method = RequestMethod.POST)
    public String updateCustomer(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,
            @RequestParam(value="CustomerAccountId",defaultValue = "") String customerAccountId,
            @RequestParam(value="BillingAccountId",defaultValue = "") String billingAccountId,
            @RequestParam(value="CustomerName",defaultValue ="") String customerName,
            @RequestParam(value="PresentAddress",defaultValue ="") String presentAddress,
            @RequestParam(value="PermanentAddress",defaultValue ="") String permanentAddress,
            @RequestParam(value="CNIC",defaultValue ="") String cnic,
            @RequestParam(value="Mobile",defaultValue ="") String mobile,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="NadraTransactionID",required = false,defaultValue ="") String nTransactionId,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId

    ){
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" UpdateCustomer called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +" BillingAccountId:"+billingAccountId
                +" CustomerName:"+customerName+" PresentAddress:"+presentAddress+" PermanentAddress:"+permanentAddress
                +" CNIC:"+cnic+" Mobile:"+mobile+" Email:"+email
                +" NadraTransactionID:"+nTransactionId+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" UpdateCustomer called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +" BillingAccountId:"+billingAccountId
                +" CustomerName:"+customerName+" PresentAddress:"+presentAddress+" PermanentAddress:"+permanentAddress
                +" CNIC:"+cnic+" Mobile:"+mobile+" Email:"+email
                +" NadraTransactionID:"+nTransactionId+" AgentId:"+agentId);


        try {

            if (
                    pstn.equals("undefined") || StringUtils.isEmpty(pstn)
                    || customerAccountId.equals("undefined") || StringUtils.isEmpty(customerAccountId)
                    || billingAccountId.equals("undefined") || StringUtils.isEmpty(billingAccountId)
                    || customerName.equals("undefined") || StringUtils.isEmpty(customerName)
                    || presentAddress.equals("undefined") || StringUtils.isEmpty(presentAddress)
                    || permanentAddress.equals("undefined") || StringUtils.isEmpty(permanentAddress)
                    || cnic.equals("undefined") || StringUtils.isEmpty(cnic)
                    || mobile.equals("undefined") || StringUtils.isEmpty(mobile)
                    || email.equals("undefined") || StringUtils.isEmpty(email)
            ){
                logger.info(time1+" Wrong Input Parameters");
                agentLogger.info(time1+" Wrong Input Parameters");
                UpdateResponse updateResponse = new UpdateResponse();
                updateResponse.setErrorCode(4);
                updateResponse.setErrorDetail("Wrong Input Parameters");
                returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }else{
                logger.info(time1+" Sending Request to Update Customer with Following Params:"
                        +" PSTN:"+pstn+" CustomerAccountId:"+customerAccountId+" BillingAccountId:"+billingAccountId
                        +" CustomerName:"+customerName+" PresentAddress:"+presentAddress+" PermanentAddress:"+permanentAddress );
                agentLogger.info(time1+" Sending Request to Update Customer with Following Params:"
                        +" PSTN:"+pstn+" CustomerAccountId:"+customerAccountId+" BillingAccountId:"+billingAccountId
                        +" CustomerName:"+customerName+" PresentAddress:"+presentAddress+" PermanentAddress:"+permanentAddress );

                logger.info(time1+" CNIC:"+cnic+" Mobile:"+mobile+" Email:"+email
                        +" NadraTransactionID:"+nTransactionId+" AgentId:"+agentId);
                agentLogger.info(time1+" CNIC:"+cnic+" Mobile:"+mobile+" Email:"+email
                        +" NadraTransactionID:"+nTransactionId+" AgentId:"+agentId);


                String updateMobileResponse = updateMobileNumber(pstn, customerAccountId, billingAccountId, mobile, agentId);
                JSONObject updateMobileJson = new JSONObject(updateMobileResponse);
                int updateMobileErrorCode = updateMobileJson.getInt("ErrorCode");
                String updateMobileErrorDetail = updateMobileJson.getString("ErrorDetail");

                /*String updateEmailResponse = updateEmailId(pstn, customerAccountId, billingAccountId, email, agentId);
            JSONObject updateEmailJson = new JSONObject(updateEmailResponse);
            int updateEmailErrorCode = updateEmailJson.getInt("ErrorCode");
            String updateEmailErrorDetail = updateEmailJson.getString("ErrorDetail");*/

                /*String updateCNICResponse = updateCNICNumber(pstn, cnic, customerAccountId, billingAccountId, customerName, "", presentAddress, permanentAddress, nTransactionId, agentId);
                JSONObject updateCNICJson = new JSONObject(updateCNICResponse);
                int updateCNICErrorCode = updateCNICJson.getInt("ErrorCode");
                String updateCNICErrorDetail = updateCNICJson.getString("ErrorDetail");*/

                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;

                logger.info(time1+" Resonses from updation API`s after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Resonses from updation API`s after "+CRMResponseTime+" ms");
                logger.info(time1+" Update Mobile API Response: " + updateMobileErrorDetail);
                agentLogger.info(time1+" Update Mobile API Response: " + updateMobileErrorDetail);
                //logger.info("OWController-updateCustomer Update Email API Response: " + updateEmailErrorDetail);
                /*logger.info(time1+" Update CNIC API Response: " + updateCNICErrorDetail);
                agentLogger.info(time1+" Update CNIC API Response: " + updateCNICErrorDetail);*/

                //if (updateMobileErrorCode == 0 && updateCNICErrorCode == 0) {
                if (updateMobileErrorCode == 0) {

                    logger.info(time1+" Customer updated successfully");
                    agentLogger.info(time1+" Customer updated successfully");

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(0);
                    updateResponse.setErrorDetail("Customer Updated Successfully");
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                } else {
                    logger.info(time1+" Failed to update customer");
                    agentLogger.info(time1+" Failed to update customer");

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(1);
                    updateResponse.setErrorDetail("Failed to update customer");
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }

                //String updateAddressResponse = updateCustomerAddress(pstn,customerAccountId,billingAccountId)
        /*UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse();
        updateCustomerResponse.setErrorCode(0);
        updateCustomerResponse.setErrorDetail("Success");
        returnResponse =  ConsumeWSDL.javaToJson(updateCustomerResponse);*/

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }

        }catch (Exception e){
            logger.info(time1+" Exception: "+e);
            agentLogger.info(time1+" Exception: "+e);
            UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse();
            updateCustomerResponse.setErrorCode(5);
            updateCustomerResponse.setErrorDetail("Sorry could not update customer info");
            returnResponse =  ConsumeWSDL.javaToJson(updateCustomerResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }
    }


    @RequestMapping(value="/UpdateCustomerAddress",method = RequestMethod.POST)
    public String updateCustomerAddress(
            @RequestParam(value="PSTN",defaultValue = "") String pstn,
            @RequestParam(value="CustomerAccountId",defaultValue = "") String customerAccountId,
            @RequestParam(value="BillingAccountId",defaultValue = "") String billingAccountID,
            @RequestParam(value="AddressHouseFlate",defaultValue = "") String addressHouseFlate,
            @RequestParam(value="AddressStoreyFloor",defaultValue ="", required = false) String addressStoreyFloor,
            @RequestParam(value="AddressStreetMohalla",defaultValue ="") String addressStreetMohalla,
            @RequestParam(value="AddressSectorArea",defaultValue ="") String addressSectorArea,
            @RequestParam(value="AddressCity",defaultValue ="") String addressCity,
            @RequestParam(value="AddressNearestLandMark",defaultValue ="", required = false) String addressNearestLandMark,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId,
            @RequestParam(value="AddressType",defaultValue ="Billing") String addressType
    ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" UpdateCustomerAddress called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +" BillingAccountId:"+billingAccountID
                +" AddressHouseFlate:"+addressHouseFlate+" AddressStoreyFloor:"+addressStoreyFloor+" AddressStreetMohalla:"+addressStreetMohalla
                +" AddressSectorArea:"+addressSectorArea+" AddressCity:"+addressCity+" NearestLandMark:"+addressNearestLandMark
                +" AddressType:"+addressType+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }

        agentLogger.info(time1+" UpdateCustomerAddress called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +" BillingAccountId:"+billingAccountID
                +" AddressHouseFlate:"+addressHouseFlate+" AddressStoreyFloor:"+addressStoreyFloor+" AddressStreetMohalla:"+addressStreetMohalla
                +" AddressSectorArea:"+addressSectorArea+" AddressCity:"+addressCity+" NearestLandMark:"+addressNearestLandMark
                +" AddressType:"+addressType+" AgentId:"+agentId);


        if (
                pstn.equals("undefined") || StringUtils.isEmpty(pstn)
                || customerAccountId.equals("undefined") || StringUtils.isEmpty(customerAccountId)
                || billingAccountID.equals("undefined") || StringUtils.isEmpty(billingAccountID)
                || addressHouseFlate.equals("undefined") || StringUtils.isEmpty(addressHouseFlate)
               // || addressStoreyFloor.equals("undefined") || StringUtils.isEmpty(addressStoreyFloor)
                || addressStreetMohalla.equals("undefined") || StringUtils.isEmpty(addressStreetMohalla)
                || addressSectorArea.equals("undefined") || StringUtils.isEmpty(addressSectorArea)
                || addressCity.equals("undefined") || StringUtils.isEmpty(addressCity)
               // || addressNearestLandMark.equals("undefined") || StringUtils.isEmpty(addressNearestLandMark)
        ){
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");

            UpdateResponse updateResponse = new UpdateResponse();
            updateResponse.setErrorCode(4);
            updateResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(updateResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            return returnResponse;
        }else{
            // CONFIGURATION for Update Customer Address API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_UPDATE_CUSTOMER_ADDRESS");
            String SOAPAction = bundle.getString("SOAP_ACTION_UPDATE_CUSTOMER_ADDRESS");

            String xmlInput  = SOAPRequestSample.updateCustomerAddressRequest(userName,password,systemIP,pstn,customerAccountId,billingAccountID,
                    channel,addressHouseFlate,addressStoreyFloor,addressStreetMohalla,addressSectorArea,addressCity,addressNearestLandMark,
                    agentId,addressType);


            logger.info(time1+" Requesting CRM to update customer address with Following Params:"
                    +" UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" CustAccountID:"+customerAccountId+" BillAccountID:"+billingAccountID+" Channel:"+channel);
            logger.info(time1+" Address_HouseFlat:"+addressHouseFlate+" Address_StoreyFloor:"+addressStoreyFloor
                    +" Address_StreetMohalla:"+addressStreetMohalla+ " Address_SectorArea:"+addressSectorArea+" Address_City:"+addressCity
                    +" Address_NearestLandMark:"+addressNearestLandMark+" AddressType:"+addressType+" AgentId: "+agentId);

            agentLogger.info(time1+" Requesting CRM to update customer address with Following Params:"
                    +" UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" CustAccountID:"+customerAccountId+" BillAccountID:"+billingAccountID+" Channel:"+channel);
            agentLogger.info(time1+" Address_HouseFlat:"+addressHouseFlate+" Address_StoreyFloor:"+addressStoreyFloor
                    +" Address_StreetMohalla:"+addressStreetMohalla+ " Address_SectorArea:"+addressSectorArea+" Address_City:"+addressCity
                    +" Address_NearestLandMark:"+addressNearestLandMark+" AddressType:"+addressType+" AgentId: "+agentId);


            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject UpdateCNICNumber = ResponseJson.getJSONObject("UpdateCustomerAddress");
                int ReturnCode = UpdateCNICNumber.getInt("RetCode");
                String ReturnDescription = UpdateCNICNumber.getString("RetDesc");
                if (ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(0);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }else {
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(ReturnCode);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                UpdateResponse updateResponse = new UpdateResponse();
                updateResponse.setErrorCode(5);
                updateResponse.setErrorDetail("Sorry could not update the address");
                returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                return returnResponse;
            }

        }









    }



    @RequestMapping(value="/UpdateMobileNumber",method = RequestMethod.POST)
    public String updateMobileNumber(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,
            @RequestParam(value="CustomerAccountId",defaultValue = "") String customerAccountId,
            @RequestParam(value="BillingAccountId",defaultValue = "") String billingAccountID,
            @RequestParam(value="Mobile",defaultValue ="") String mobile,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;

        logger.info(time1+" UpdateMobileNumber called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +"BillingAccountId:"+billingAccountID+" Mobile:"+mobile+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" UpdateMobileNumber called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +"BillingAccountId:"+billingAccountID+" Mobile:"+mobile+" AgentId:"+agentId);

        if (
                pstn.equals("undefined") || StringUtils.isEmpty(pstn)
                        || customerAccountId.equals("undefined") || StringUtils.isEmpty(customerAccountId)
                        || billingAccountID.equals("undefined") || StringUtils.isEmpty(billingAccountID)
                        || mobile.equals("undefined") || StringUtils.isEmpty(mobile)
        ){
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");

            UpdateResponse updateResponse = new UpdateResponse();
            updateResponse.setErrorCode(4);
            updateResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(updateResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;
        }else{

            // WSDL URL for Update Customer Mobile API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_UPDATE_CUSTOMER_MOBILE");

            String xmlInput = SOAPRequestSample.updateMobileNumberRequest(userName,password,systemIP,pstn,customerAccountId,billingAccountID,channel,
                    mobile,agentId);
            String SOAPAction = bundle.getString("SOAP_ACTION_UPDATE_CUSTOMER_MOBILE");




            logger.info(time1+" Requesting CRM to update customer mobile with Following Params "
                    +" UserName:" + userName + " Password:" + password + " SystemIP: " + systemIP
                    + " CustAccountID:"+ customerAccountId +" BillAccountID:"+ billingAccountID
                    +  " PSTN:"+pstn+" Mobile:"+mobile +" AgentId:"+agentId+" Channel:"+channel);

            agentLogger.info(time1+" Requesting CRM to update customer mobile with Following Params "
                    +" UserName:" + userName + " Password:" + password + " SystemIP: " + systemIP
                    + " CustAccountID:"+ customerAccountId +" BillAccountID:"+ billingAccountID
                    +  " PSTN:"+pstn+" Mobile:"+mobile +" AgentId:"+agentId+" Channel:"+channel);

            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject UpdateCNICNumber = ResponseJson.getJSONObject("UpdateMobileNumber");
                int ReturnCode = UpdateCNICNumber.getInt("RetCode");
                String ReturnDescription = UpdateCNICNumber.getString("RetDesc");
                if (ReturnCode == 0){
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(0);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                }else {

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(ReturnCode);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                UpdateResponse updateResponse = new UpdateResponse();
                updateResponse.setErrorCode(1);
                updateResponse.setErrorDetail("Exception");
                returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }

        }

    }


    @RequestMapping(value="/UpdateEmailId",method = RequestMethod.POST)
    public String updateEmailId(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,
            @RequestParam(value="CustomerAccountId",defaultValue = "") String customerAccountId,
            @RequestParam(value="BillingAccountId",defaultValue = "") String billingAccountID,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;

        logger.info(time1+" UpdateEmailId called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +" BillingAccountId:"+billingAccountID+" Email:"+email+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" UpdateEmailId called with PSTN:"+pstn+" CustomerAccountId:"+customerAccountId
                +" BillingAccountId:"+billingAccountID+" Email:"+email+" AgentId:"+agentId);

        if (
                pstn.equals("undefined") || StringUtils.isEmpty(pstn)
                        || customerAccountId.equals("undefined") || StringUtils.isEmpty(customerAccountId)
                        || billingAccountID.equals("undefined") || StringUtils.isEmpty(billingAccountID)
                        || email.equals("undefined") || StringUtils.isEmpty(email)
        ){
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");

            UpdateResponse updateResponse = new UpdateResponse();
            updateResponse.setErrorCode(4);
            updateResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(updateResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;
        }else{

            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_UPDATE_CUSTOMER_EMAIL");
            String xmlInput = SOAPRequestSample.updateEmailIdRequest(userName,password,systemIP,pstn,customerAccountId,
                    billingAccountID,channel,email,agentId);
            String SOAPAction = bundle.getString("SOAP_ACTION_UPDATE_CUSTOMER_EMAIL");

            logger.info(time1+" Requesting CRM to update customer email with Following Params" +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " PSTN:"+ pstn + " CustAccountID:"+customerAccountId+" BillAccountID:"+billingAccountID
                    + " Channel:"+ channel + " EmailID:"+email+" AgentId:"+agentId);

            agentLogger.info(time1+" Requesting CRM to update customer email with Following Params" +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " PSTN:"+ pstn + " CustAccountID:"+customerAccountId+" BillAccountID:"+billingAccountID
                    + " Channel:"+ channel + " EmailID:"+email+" AgentId:"+agentId);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject UpdateCNICNumber = ResponseJson.getJSONObject("UpdateCustomerEmailId");
                int ReturnCode = UpdateCNICNumber.getInt("RetCode");
                String ReturnDescription = UpdateCNICNumber.getString("RetDesc");

                if (ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(0);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                }else {

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    UpdateResponse updateResponse = new UpdateResponse();
                    updateResponse.setErrorCode(ReturnCode);
                    updateResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(updateResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){

                logger.error(time1+" Exception: "+e);
                UpdateResponse updateResponse = new UpdateResponse();
                updateResponse.setErrorCode(5);
                updateResponse.setErrorDetail("Sorry I could not update email");
                returnResponse = ConsumeWSDL.javaToJson(updateResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }

    }


    @RequestMapping(value="/NadraService",method = RequestMethod.GET)
    public String nadraService(
            @RequestParam(value="CNIC",defaultValue ="") String cnic,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;

        logger.info(time1+" NadraService called with CNIC:"+cnic+" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" NadraService called with CNIC:"+cnic+" AgentId:"+agentId);
        if (
                cnic.equals("undefined") || StringUtils.isEmpty(cnic)
        ){
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");

            NadraServiceResponse nadraServiceResponse = new NadraServiceResponse();
            nadraServiceResponse.setErrorCode(4);
            nadraServiceResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(nadraServiceResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            try {

                ResourceBundle bundle = ResourceBundle.getBundle("Config");
                String wsURL = bundle.getString("WSDL_URL_NADRA_SERVICE");
                String SOAPAction = bundle.getString("SOAP_ACTION_NADRA_SERVICE");
                String xmlInput =  SOAPRequestSample.nadraServiceRequest(cnic);

                logger.info(time1+" Requesting Nadra Service with Params CNIC:"+cnic);
                agentLogger.info(time1+" Requesting Nadra Service with Params CNIC:"+cnic);

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received Nadra response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received Nadra response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" Nadra response "+ResponseJson);
                agentLogger.info(time1+" Nadra response "+ResponseJson);

                JSONObject NadraResponse = ResponseJson.getJSONObject("NS1:NadraResponse");
                int ReturnCode = Integer.parseInt(NadraResponse.get("NS1:returnCode").toString());
                String ReturnDescription = NadraResponse.get("NS1:returnMessage").toString();

                if(ReturnCode == 40100000){

                    logger.info(time1+" Nadra response code: "+ReturnCode);
                    agentLogger.info(time1+" Nadra response code: "+ReturnCode);
                    logger.info(time1+" Nadra response detail: "+ReturnDescription);
                    agentLogger.info(time1+" Nadra response detail: "+ReturnDescription);

                    NadraCustomerModel nadraCustomerModel = new NadraCustomerModel();
                    nadraCustomerModel.setCustomerName(NadraResponse.get("NS1:customerName").toString());
                    nadraCustomerModel.setFatherName(NadraResponse.get("NS1:fatherName").toString());
                    nadraCustomerModel.setPermanentAddress(NadraResponse.get("NS1:permanentAddress").toString());
                    nadraCustomerModel.setPresentAddress(NadraResponse.get("NS1:presentAddress").toString());
                    nadraCustomerModel.setTransactionId(Long.parseLong(NadraResponse.get("NS1:transactionId").toString()));
                    nadraCustomerModel.setMotherName(NadraResponse.get("NS1:motherName").toString());
                    nadraCustomerModel.setPlaceOfBirth(NadraResponse.get("NS1:placeOfBirth").toString());
                    nadraCustomerModel.setReligion(NadraResponse.get("NS1:religion").toString());

                    NadraServiceResponse nadraServiceResponse = new NadraServiceResponse();
                    nadraServiceResponse.setErrorCode(0);
                    nadraServiceResponse.setErrorDetail(ReturnDescription);
                    nadraServiceResponse.setNadraCustomerModel(nadraCustomerModel);
                    returnResponse = ConsumeWSDL.javaToJson(nadraServiceResponse);

                }else{

                    logger.info(time1+" Nadra response code: "+ReturnCode);
                    agentLogger.info(time1+" Nadra response code: "+ReturnCode);
                    logger.info(time1+" Nadra response detail: "+ReturnDescription);
                    agentLogger.info(time1+" Nadra response detail: "+ReturnDescription);

                    NadraServiceResponse nadraServiceResponse = new NadraServiceResponse();
                    nadraServiceResponse.setErrorCode(ReturnCode);
                    nadraServiceResponse.setErrorDetail("CNIC not exist in Nadra");
                    returnResponse = ConsumeWSDL.javaToJson(nadraServiceResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                NadraServiceResponse nadraServiceResponse = new NadraServiceResponse();
                nadraServiceResponse.setErrorCode(5);
                nadraServiceResponse.setErrorDetail("Sorry I could not get Nadra detail");
                returnResponse = ConsumeWSDL.javaToJson(nadraServiceResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }

    }


    @RequestMapping(value="/NewOrder",method = RequestMethod.POST)
    public String newOrder(
            @RequestParam(value="AlternateNumber",defaultValue = "") String altNumber,
            @RequestParam(value="ServiceId",defaultValue = "",required = false) String serviceId,
            @RequestParam(value="AccountId",defaultValue ="") String customerAccountId,
            @RequestParam(value="Description",required = false,defaultValue = "") String description,
            @RequestParam(value="ProductId",defaultValue ="") String productId,
            @RequestParam(value="PackageId",defaultValue ="") String packageId,
            @RequestParam(value="PricePlanId",defaultValue = "") String pricePlanId,
            @RequestParam(value="ItemQuantity",defaultValue ="") String itemQuantity,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId

    ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" NewOrder called with ALTNumber:"+altNumber+" ServiceId:"+serviceId
                +" AccountId:"+customerAccountId+" Description:"+description
                +" Input Parameters Product:"+productId+" Package:"+packageId
                +" PricePlanID:"+pricePlanId+" Quantity:"+itemQuantity+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" NewOrder called with ALTNumber:"+altNumber+" ServiceId:"+serviceId
                +" AccountId:"+customerAccountId+" Description:"+description
                +" Input Parameters Product:"+productId+" Package:"+packageId
                +" PricePlanID:"+pricePlanId+" Quantity:"+itemQuantity+" AgentId:"+agentId);


        if (altNumber.equals("undefined") || StringUtils.isEmpty(altNumber) ||
                customerAccountId.equals("undefined") || StringUtils.isEmpty(customerAccountId) ||
                productId.equals("undefined") || StringUtils.isEmpty(productId) ||
                packageId.equals("undefined") || StringUtils.isEmpty(packageId) ||
                pricePlanId.equals("undefined") || StringUtils.isEmpty(pricePlanId) ||
                itemQuantity.equals("undefined") || StringUtils.isEmpty(itemQuantity))
        {
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");
            NewOrderResponse newOrderResponse = new NewOrderResponse();
            newOrderResponse.setErrorCode(4);
            newOrderResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(newOrderResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{


            // CONFIGURATION for NEW ORDER API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String srCategory = bundle.getString("SR_CATEGORY_FOR_NEW_ORDER");
            String type = bundle.getString("TYPE_FOR_NEW_ORDER");
            String subType = bundle.getString("SUB_TYPE_FOR_NEW_ORDER");
            //String referralId =  "v.SabaZulfiqar";// bundle.getString("REFERRALID_FOR_NEW_ORDER");
            String referralId = agentId;
            String sourceName = bundle.getString("SOURCE_NAME_FOR_NEW_ORDER");
            String wsURL = bundle.getString("WSDL_URL_NEW_ORDER");
            String SOAPAction = bundle.getString("SOAP_ACTION_NEW_ORDER");

            String xmlInput = SOAPRequestSample.newOrderRequest(userName,password,systemIP,altNumber,srCategory,serviceId,
                    type,customerAccountId,subType,referralId,sourceName,description,productId,packageId,pricePlanId,itemQuantity);

            logger.info(time1+" Requesting CRM for New Order with params UserName:"
                    +userName+" Password:"+password+" SystemIP:"+systemIP
                    +" CustomerAccountID:"+customerAccountId+" ALT_Number:"+altNumber+" SRCategory:"+srCategory);
            logger.info(time1+" ServiceId:"+serviceId+" Type:"+type+" Referral_spcId:"+referralId+" Source:"+sourceName
                    +" Product:"+productId+" Package:"+packageId+" PricePlanID:"+pricePlanId+" Quantity:"+itemQuantity);

            agentLogger.info(time1+" Requesting CRM for New Order with params UserName:"
                    +userName+" Password:"+password+" SystemIP:"+systemIP
                    +" CustomerAccountID:"+customerAccountId+" ALT_Number:"+altNumber+" SRCategory:"+srCategory);
            agentLogger.info(time1+" ServiceId:"+serviceId+" Type:"+type+" Referral_spcId:"+referralId+" Source:"+sourceName
                    +" Product:"+productId+" Package:"+packageId+" PricePlanID:"+pricePlanId+" Quantity:"+itemQuantity);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject PTCL_OUTPUT = ResponseJson.getJSONObject("ns:MyPTCL_Output");
                String ReturnCode = PTCL_OUTPUT.get("ns:Error_spcCode").toString();
                String ReturnDescription = PTCL_OUTPUT.get("ns:Error_spcMessage").toString();

                if (ReturnCode.equals("")){

                    logger.info(time1+" CRM response code:'It return empty for success'");
                    agentLogger.info(time1+" CRM response code:'It return empty for success'");
                    logger.info(time1+" CRM response detail: Success");
                    agentLogger.info(time1+" CRM response detail: Success");

                    String OrderId = PTCL_OUTPUT.get("ns:OrderId").toString();
                    String SRNumber = PTCL_OUTPUT.get("ns:SRNumber").toString();
                    NewOrderModel newOrderModel = new NewOrderModel();
                    newOrderModel.setOrderId(OrderId);
                    newOrderModel.setSRNumber(SRNumber);

                    NewOrderResponse newOrderResponse = new NewOrderResponse();
                    newOrderResponse.setErrorCode(0);
                    newOrderResponse.setErrorDetail("Order placed successfully");
                    newOrderResponse.setNewOrder(newOrderModel);
                    returnResponse = ConsumeWSDL.javaToJson(newOrderResponse);
                }else{
                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    NewOrderResponse newOrderResponse = new NewOrderResponse();
                    newOrderResponse.setErrorCode(Long.parseLong(ReturnCode));
                    newOrderResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(newOrderResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                NewOrderResponse newOrderResponse = new NewOrderResponse();
                newOrderResponse.setErrorCode(5);
                newOrderResponse.setErrorDetail("Sorry I could not create New Order");
                returnResponse = ConsumeWSDL.javaToJson(newOrderResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }

        }
    }


    @RequestMapping(value="/CreateCustomer",method = RequestMethod.POST)
    public String createCustomer(
            @RequestParam(value="FirstName",defaultValue = "") String firstName,
            @RequestParam(value="LastName",defaultValue = "") String lastName,
            @RequestParam(value="MiddleName",defaultValue = "",required = false) String middleName,
            @RequestParam(value="CustomerName",defaultValue = "",required = false) String customerName,
            @RequestParam(value="Salutation",defaultValue ="") String salutation,

            @RequestParam(value="CNICNumber",defaultValue = "") String cnicNumber,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="Mobile",defaultValue ="") String mobileNumber,
            @RequestParam(value="PreferredContactMethod",defaultValue = "") String preferredContactMethod,

            @RequestParam(value="HouseFlatNumber",defaultValue ="") String houseFlatNumber,
            @RequestParam(value="HouseFlatNumberService",defaultValue ="") String houseFlatNoService,
            @RequestParam(value="HouseFlatNumberBilling",defaultValue = "") String houseFlatNumberBilling,

            @RequestParam(value="StoreyFloor",defaultValue ="",required = false) String storeyFloor,
            @RequestParam(value="StoreyFloorBilling",defaultValue ="",required = false) String storeyFloorBilling,
            @RequestParam(value="StoreyFloorService",defaultValue = "", required = false) String storeyFloorService,

            @RequestParam(value="StreetMohalla",defaultValue ="") String streetMohalla,
            @RequestParam(value="StreetMohallaBilling",defaultValue ="") String streetMohallaBilling,
            @RequestParam(value="StreetMohallaService",defaultValue = "") String streetMohallaService,

            @RequestParam(value="SectorAreaHousingSociety",defaultValue ="") String sectorAreaHousingSociety,
            @RequestParam(value="SectorAreaHousingSocietyBilling",defaultValue ="") String sectorAreaHousingSocietyBilling,
            @RequestParam(value="SectorAreaHousingSocietyService",defaultValue ="") String sectorAreaHousingSocietyService,

            @RequestParam(value="City",defaultValue ="") String city,
            @RequestParam(value="CityBilling",defaultValue = "") String cityBilling,
            @RequestParam(value="CityService",defaultValue = "") String cityService,

            @RequestParam(value="NearestLandMark",defaultValue ="",required = false) String nearestLandMark,
            @RequestParam(value="NearestLandMarkService",defaultValue ="",required = false) String nearestLandMarkService,
            @RequestParam(value="NearestLandMarkBilling",defaultValue ="",required = false) String nearestLandMarkBilling,

            @RequestParam(value="NadraTransactionId",defaultValue ="",required = false) String nadraTransactionId,
            @RequestParam(value="NadraPresentAddress",defaultValue ="",required = false) String nadraPresentAddress,
            @RequestParam(value="NadraPermanentAddress",defaultValue ="",required = false) String nadraPermanentAddress,

            @RequestParam(value="AgentId",defaultValue = "",required = false) String agentId
    ) {
        String returnResponse = null;
        long time1 = System.currentTimeMillis();
        logger.info(time1+" CreateCustomer called with FirstName:"+firstName+" LastName:"+lastName
                +" MiddleName:"+middleName+" CustomerName:"+customerName+" Salutation:"+salutation);
        logger.info(time1+" CNIC:"+cnicNumber+" Mobile:"+mobileNumber+" Email:"+email+" PreferredContactMethod:"+preferredContactMethod);
        logger.info(time1+" HouseFlateNumber:"+houseFlatNumber+" HouseFlateNumberService:"+houseFlatNoService+" HouseFlateNumberBilling:"+houseFlatNumberBilling);
        logger.info(time1+" StoreyFloor:"+storeyFloor+" StoreyFloorBilling:"+storeyFloorBilling+" StoreyFloorService:"+storeyFloorService);
        logger.info(time1+" StreetMohalla:"+streetMohalla+" StreetMohallaBilling:"+streetMohallaBilling+" StreetMohallaService:"+streetMohallaService);
        logger.info(time1+" SectorAreaHousingSociety:"+sectorAreaHousingSociety+" SectorAreaHousingSocietyBilling:"+sectorAreaHousingSocietyBilling+" SectorAreaHousingSocietyService:"+sectorAreaHousingSocietyService);
        logger.info(time1+" City:"+city+" CityBilling:"+cityBilling+" CityService:"+cityService);
        logger.info(time1+" NearestLandMark:"+nearestLandMark+" NearestLandMarkBilling:"+nearestLandMarkBilling+" NearestLandMarkService:"+nearestLandMarkService);
        logger.info(time1+" NadraTransactionID:"+nadraTransactionId+" NadraPermanentAddress:"+nadraPresentAddress+" NadraPermanentAddress:"+nadraPermanentAddress);
        logger.info(time1+" AgentId:"+ agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" CreateCustomer called with FirstName:"+firstName+" LastName:"+lastName
                +" MiddleName:"+middleName+" CustomerName:"+customerName+" Salutation:"+salutation);
        agentLogger.info(time1+" CNIC:"+cnicNumber+" Mobile:"+mobileNumber+" Email:"+email+" PreferredContactMethod:"+preferredContactMethod);
        agentLogger.info(time1+" HouseFlateNumber:"+houseFlatNumber+" HouseFlateNumberService:"+houseFlatNoService+" HouseFlateNumberBilling:"+houseFlatNumberBilling);
        agentLogger.info(time1+" StoreyFloor:"+storeyFloor+" StoreyFloorBilling:"+storeyFloorBilling+" StoreyFloorService:"+storeyFloorService);
        agentLogger.info(time1+" StreetMohalla:"+streetMohalla+" StreetMohallaBilling:"+streetMohallaBilling+" StreetMohallaService:"+streetMohallaService);
        agentLogger.info(time1+" SectorAreaHousingSociety:"+sectorAreaHousingSociety+" SectorAreaHousingSocietyBilling:"+sectorAreaHousingSocietyBilling+" SectorAreaHousingSocietyService:"+sectorAreaHousingSocietyService);
        agentLogger.info(time1+" City:"+city+" CityBilling:"+cityBilling+" CityService:"+cityService);
        agentLogger.info(time1+" NearestLandMark:"+nearestLandMark+" NearestLandMarkBilling:"+nearestLandMarkBilling+" NearestLandMarkService:"+nearestLandMarkService);
        agentLogger.info(time1+" NadraTransactionID:"+nadraTransactionId+" NadraPermanentAddress:"+nadraPresentAddress+" NadraPermanentAddress:"+nadraPermanentAddress);
        agentLogger.info(time1+" AgentId:"+ agentId);


        if (
                firstName.equals("undefined") || StringUtils.isEmpty(firstName) ||
                lastName.equals("undefined") || StringUtils.isEmpty(lastName) ||
              //  customerName.equals("undefined") || StringUtils.isEmpty(customerName) ||
                salutation.equals("undefined") || StringUtils.isEmpty(salutation) ||

                cnicNumber.equals("undefined") || StringUtils.isEmpty(cnicNumber) ||
                email.equals("undefined") || StringUtils.isEmpty(email) ||
                mobileNumber.equals("undefined") || StringUtils.isEmpty(mobileNumber) ||
                preferredContactMethod.equals("undefined") || StringUtils.isEmpty(preferredContactMethod) ||

                houseFlatNumber.equals("undefined") || StringUtils.isEmpty(houseFlatNumber) ||
                houseFlatNoService.equals("undefined") || StringUtils.isEmpty(houseFlatNoService) ||
                houseFlatNumberBilling.equals("undefined") || StringUtils.isEmpty(houseFlatNumberBilling) ||

                streetMohalla.equals("undefined") || StringUtils.isEmpty(streetMohalla) ||
                streetMohallaService.equals("undefined") || StringUtils.isEmpty(streetMohallaService) ||
                streetMohallaBilling.equals("undefined") || StringUtils.isEmpty(streetMohallaBilling) ||

                sectorAreaHousingSociety.equals("undefined") || StringUtils.isEmpty(sectorAreaHousingSociety) ||
                sectorAreaHousingSocietyService.equals("undefined") || StringUtils.isEmpty(sectorAreaHousingSocietyService) ||
                sectorAreaHousingSocietyBilling.equals("undefined") || StringUtils.isEmpty(sectorAreaHousingSocietyBilling) ||

                city.equals("undefined") || StringUtils.isEmpty(city) ||
                cityBilling.equals("undefined") || StringUtils.isEmpty(cityBilling) ||
                cityService.equals("undefined") || StringUtils.isEmpty(cityService)

               // nadraPresentAddress.equals("undefined") || StringUtils.isEmpty(nadraPresentAddress) ||
               // nadraPermanentAddress.equals("undefined") || StringUtils.isEmpty(nadraPermanentAddress)
        )
        {
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");
            NewOrderResponse newOrderResponse = new NewOrderResponse();
            newOrderResponse.setErrorCode(4);
            newOrderResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(newOrderResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{


            CreateNewCustomerResponse createNewCustomerResponse = new CreateNewCustomerResponse();
            createNewCustomerResponse.setErrorCode(0);
            createNewCustomerResponse.setErrorDetail("success");
            createNewCustomerResponse.setAccountNumber("4532532");
            createNewCustomerResponse.setBillingContact("57575754");
            returnResponse = ConsumeWSDL.javaToJson(createNewCustomerResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;




            // WSDL URL for CREATE CUSTOMER API
            /*ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String wsURL = bundle.getString("WSDL_URL_CREATE_CUSTOMER");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String accountNameBilling = bundle.getString("ACCOUNT_NAME_BILLING");
            String accountNameService = bundle.getString("ACCOUNT_NAME_SERVICE");

            String xmlInput = SOAPRequestSample.createCustomerRequest(userName,password,systemIP,channel,nearestLandMarkService,
                    firstName,storeyFloorBilling,storeyFloor,nearestLandMark,lastName,accountNameBilling,preferredContactMethod,
                    mobileNumber,houseFlatNoService,cityBilling,city,accountNameService,storeyFloorService,salutation,
                    cityService,streetMohallaBilling,sectorAreaHousingSociety,sectorAreaHousingSocietyBilling,
                    streetMohallaService,sectorAreaHousingSocietyService,houseFlatNumberBilling,houseFlatNumber,
                    nearestLandMarkBilling,middleName,streetMohalla,cnicNumber,email,customerName,nadraPresentAddress,
                    nadraPermanentAddress,nadraTransactionId,agentId);


            String SOAPAction = bundle.getString("SOAP_ACTION_CREATE_CUSTOMER");

            logger.info(time1+" Requesting CRM to create New Customer with params: UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP);
            logger.info(time1+" FirstName:"+firstName+" LastName:"+lastName+" MiddleName:"+middleName+" CustomerName:"+customerName+" Salutation:"+salutation);
            logger.info(time1+" CNIC:"+cnicNumber+" Mobile:"+mobileNumber+" Email:"+email+" PreferredContactMethod:"+preferredContactMethod);
            logger.info(time1+" AccountNameBilling:"+accountNameBilling+" AccountNameService:"+accountNameService);
            logger.info(time1+" HouseFlateNumber:"+houseFlatNumber+" HouseFlateNumberService:"+houseFlatNoService+" HouseFlateNumberBilling:"+houseFlatNumberBilling);
            logger.info(time1+" StoreyFloor:"+storeyFloor+" StoreyFloorBilling:"+storeyFloorBilling+" StoreyFloorService:"+storeyFloorService);
            logger.info(time1+" StreetMohalla:"+streetMohalla+" StreetMohallaBilling:"+streetMohallaBilling+" StreetMohallaService:"+streetMohallaService);
            logger.info(time1+" SectorAreaHousingSociety:"+sectorAreaHousingSociety+" SectorAreaHousingSocietyBilling:"+sectorAreaHousingSocietyBilling+" SectorAreaHousingSocietyService:"+sectorAreaHousingSocietyService);
            logger.info(time1+" City:"+city+" CityBilling:"+cityBilling+" CityService:"+cityService);
            logger.info(time1+" NearestLandMark:"+nearestLandMark+" NearestLandMarkBilling:"+nearestLandMarkBilling+" NearestLandMarkService:"+nearestLandMarkService);
            logger.info(time1+" NadraTransactionID:"+nadraTransactionId+" NadraPermanentAddress:"+nadraPresentAddress+" NadraPermanentAddress:"+nadraPermanentAddress);
            logger.info(time1+" AgentId:"+ agentId);
            //==================================>>
            //  Agent base logging
            agentLogger.info(time1+" Requesting CRM to create New Customer with params: UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP);
            agentLogger.info(time1+" FirstName:"+firstName+" LastName:"+lastName+" MiddleName:"+middleName+" CustomerName:"+customerName+" Salutation:"+salutation);
            agentLogger.info(time1+" CNIC:"+cnicNumber+" Mobile:"+mobileNumber+" Email:"+email+" PreferredContactMethod:"+preferredContactMethod);
            agentLogger.info(time1+" AccountNameBilling:"+accountNameBilling+" AccountNameService:"+accountNameService);
            agentLogger.info(time1+" HouseFlateNumber:"+houseFlatNumber+" HouseFlateNumberService:"+houseFlatNoService+" HouseFlateNumberBilling:"+houseFlatNumberBilling);
            agentLogger.info(time1+" StoreyFloor:"+storeyFloor+" StoreyFloorBilling:"+storeyFloorBilling+" StoreyFloorService:"+storeyFloorService);
            agentLogger.info(time1+" StreetMohalla:"+streetMohalla+" StreetMohallaBilling:"+streetMohallaBilling+" StreetMohallaService:"+streetMohallaService);
            agentLogger.info(time1+" SectorAreaHousingSociety:"+sectorAreaHousingSociety+" SectorAreaHousingSocietyBilling:"+sectorAreaHousingSocietyBilling+" SectorAreaHousingSocietyService:"+sectorAreaHousingSocietyService);
            agentLogger.info(time1+" City:"+city+" CityBilling:"+cityBilling+" CityService:"+cityService);
            agentLogger.info(time1+" NearestLandMark:"+nearestLandMark+" NearestLandMarkBilling:"+nearestLandMarkBilling+" NearestLandMarkService:"+nearestLandMarkService);
            agentLogger.info(time1+" NadraTransactionID:"+nadraTransactionId+" NadraPermanentAddress:"+nadraPresentAddress+" NadraPermanentAddress:"+nadraPermanentAddress);
            agentLogger.info(time1+" AgentId:"+ agentId);

            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                if (ResponseJson.has("PTCL2_spcCreate_spcIndividual_spcAccount_Output")){
                    JSONObject serviceNotConnected = ResponseJson.getJSONObject("PTCL2_spcCreate_spcIndividual_spcAccount_Output");
                    int ReturnCode = serviceNotConnected.getInt("RetCode");
                    String ReturnDescription = serviceNotConnected.getString("RetDesc");

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    CreateNewCustomerResponse createNewCustomerResponse = new CreateNewCustomerResponse();
                    createNewCustomerResponse.setErrorCode(ReturnCode);
                    createNewCustomerResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(createNewCustomerResponse);

                }else{
                    //logger.info(time1+" else case running");

                    JSONObject CreateAccountOutput = ResponseJson.getJSONObject("ns:PTCL2_spcCreate_spcIndividual_spcAccount_Output");
                    int ReturnCode = CreateAccountOutput.getInt("ns:Error_spcCode");
                    String ReturnDescription = CreateAccountOutput.getString("ns:Error_spcMessage");
                    if (ReturnCode == 0){

                        String accountNumber = CreateAccountOutput.getString("ns:Account_spcNumber");
                        String BillingContact = CreateAccountOutput.getString("ns:Primary_spcBilling_spcContact");

                        logger.info(time1+" CRM response code: "+ReturnCode);
                        agentLogger.info(time1+" CRM response code: "+ReturnCode);
                        logger.info(time1+" CRM response detail: "+ReturnDescription);
                        agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                        logger.info(time1+" Customer Account Number:"+accountNumber+"  Billing Contact:"+BillingContact);
                        agentLogger.info(time1+" Customer Account Number:"+accountNumber+"  Billing Contact:"+BillingContact);

                        CreateNewCustomerResponse createNewCustomerResponse = new CreateNewCustomerResponse();
                        createNewCustomerResponse.setErrorCode(0);
                        createNewCustomerResponse.setErrorDetail(ReturnDescription);
                        createNewCustomerResponse.setAccountNumber(accountNumber);
                        createNewCustomerResponse.setBillingContact(BillingContact);
                        returnResponse = ConsumeWSDL.javaToJson(createNewCustomerResponse);
                    }else {

                        logger.info(time1+" CRM response code: "+ReturnCode);
                        agentLogger.info(time1+" CRM response code: "+ReturnCode);
                        logger.info(time1+" CRM response detail: "+ReturnDescription);
                        agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                        CreateNewCustomerResponse createNewCustomerResponse = new CreateNewCustomerResponse();
                        createNewCustomerResponse.setErrorCode(ReturnCode);
                        createNewCustomerResponse.setErrorDetail(ReturnDescription);
                        returnResponse = ConsumeWSDL.javaToJson(createNewCustomerResponse);
                    }

                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.info(time1+" Exception: "+e);
                CreateNewCustomerResponse createNewCustomerResponse = new CreateNewCustomerResponse();
                createNewCustomerResponse.setErrorCode(5);
                createNewCustomerResponse.setErrorDetail("Sorry I could not create new customer due to some abnormality");
                returnResponse = ConsumeWSDL.javaToJson(createNewCustomerResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }*/
        }






    }




    //<<====================================================================================================>>
                                            /*  WIRELESS SERVICES */


    @RequestMapping(value="/GetBucketListWireless",method = RequestMethod.GET)
    public String getBucketListWireless(
            @RequestParam(value = "MDN",defaultValue = "") String mdn,
            @RequestParam(value = "AgentId",required = false,defaultValue = "") String agentId
    ) {
        final long time1 = System.currentTimeMillis();

        String returnResponse = null;
        List<WirelessBucketModel> wirelessBucketModelList = new ArrayList<WirelessBucketModel>();
        logger.info(time1+" Get BucketListWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" Get BucketListWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");

            BucketListResponse bucketListResponse = new BucketListResponse();
            bucketListResponse.setErrorCode(4);
            bucketListResponse.setErrorDetail("Wrong Input parameters");
            bucketListResponse.setBuckets(wirelessBucketModelList);
            returnResponse = ConsumeWSDL.javaToJson(bucketListResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            // CON for get bucket list wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_GET_BUCKETLIST_WIRELESS");
            String xmlInput = SOAPRequestSample.getBucketListWirelessRequest(userName,password,systemIP,mdn,agentId,channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_BUCKETLIST_WIRELESS");

            logger.info(time1+" Requesting CRM to get Bucket list wireless with Following Params" +
                    " UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" MDN:"+mdn+" AgentId:"+agentId+" Channel:" +channel);

            agentLogger.info(time1+" Requesting CRM to get Bucket list wireless with Following Params" +
                    " UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" MDN:"+mdn+" AgentId:"+agentId+" Channel:" +channel);

            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response: "+ResponseJson);
                agentLogger.info(time1+" CRM response: "+ResponseJson);

                JSONObject BucketListParams = ResponseJson.getJSONObject("BucketListParams");
                int ReturnCode = BucketListParams.getInt("RetCode");
                String ReturnDescription = BucketListParams.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);


                    Object bucketObject = BucketListParams.get("BucketList");

                    if (checkObjectType(bucketObject).equals("JSONArray")){
                        logger.info(time1+" BucketList is type of: "+checkObjectType(bucketObject));
                        agentLogger.info(time1+" BucketList is type of: "+checkObjectType(bucketObject));
                        JSONArray bucketListArray = (JSONArray) bucketObject;

                        for (int i = 0; i < bucketListArray.length(); ++i) {
                            WirelessBucketModel object = new WirelessBucketModel();
                            JSONObject BucketObject = bucketListArray.getJSONObject(i);
                            object.setOptionalOfferID(BucketObject.getLong("OptionalOfferID"));
                            object.setOfferName(BucketObject.getString("OfferName"));
                            object.setPaymentMode(BucketObject.getString("PaymentMode"));
                            object.setSubscriptionCharges(BucketObject.getLong("SubscriptionCharges"));
                            wirelessBucketModelList.add(object);
                        }

                    }else{
                        logger.info(time1+" BucketList is type of: "+checkObjectType(bucketObject));
                        agentLogger.info(time1+" BucketList is type of: "+checkObjectType(bucketObject));

                        JSONObject bucket = (JSONObject) bucketObject;
                        WirelessBucketModel object = new WirelessBucketModel();
                        object.setOptionalOfferID(bucket.getLong("OptionalOfferID"));
                        object.setOfferName(bucket.getString("OfferName"));
                        object.setPaymentMode(bucket.getString("PaymentMode"));
                        object.setSubscriptionCharges(bucket.getLong("SubscriptionCharges"));
                        wirelessBucketModelList.add(object);

                    }



                    //JSONArray BucketList = BucketListParams.getJSONArray("BucketList");


                    BucketListResponse bucketListResponse = new BucketListResponse();
                    bucketListResponse.setErrorCode(0);
                    bucketListResponse.setErrorDetail(ReturnDescription);
                    bucketListResponse.setBuckets(wirelessBucketModelList);
                    returnResponse = ConsumeWSDL.javaToJson(bucketListResponse);

                }else{

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    BucketListResponse bucketListResponse = new BucketListResponse();
                    bucketListResponse.setErrorCode(ReturnCode);
                    bucketListResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(bucketListResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

                // Need to return response
            }catch (Exception e){

                logger.error(time1+" Exception: "+e);
                BucketListResponse bucketListResponse = new BucketListResponse();
                bucketListResponse.setErrorCode(5);
                bucketListResponse.setErrorDetail("Sorry I could not get bucket list");
                returnResponse = ConsumeWSDL.javaToJson(bucketListResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }






    }


    @RequestMapping(value="/BucketSubscriptionWireless",method = RequestMethod.POST)
    public String bucketSubscriptionWireless(
            @RequestParam(value="MDN",defaultValue = "") String mdn,
            @RequestParam(value="OptionalOfferId",defaultValue = "") String optionalOfferId,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
            ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" BucketSubscriptionWireless called with MDN:"
                +mdn+" OptionalOfferId:"+optionalOfferId+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" BucketSubscriptionWireless called with MDN:"
                +mdn+" OptionalOfferId:"+optionalOfferId+" AgentId:"+agentId);

        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn) ||
                optionalOfferId.equals("undefined") || StringUtils.isEmpty(optionalOfferId)
        ){
            logger.info(time1+" Wrong Input parameters");
            agentLogger.info(time1+" Wrong Input parameters");

            BucketSubscriptionResponse bucketSubscriptionResponse = new BucketSubscriptionResponse();
            bucketSubscriptionResponse.setErrorCode(4);
            bucketSubscriptionResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(bucketSubscriptionResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            // CONFIGURATION for Bucket Subscription Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String userId = bundle.getString("PTCL_API_ACCESS_CRM_USER_ID");
            String wsURL = bundle.getString("WSDL_URL_BUCKET_SUBSCRIPTION_WIRELESS");
            String xmlInput = SOAPRequestSample.bucketSubscriptionWirelessRequest(userName,password,systemIP,mdn,
                    userId,optionalOfferId,agentId,channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_BUCKET_SUBSCRIPTION_WIRELESS");

            logger.info(time1+" Requesting CRM to Bucket Subscription with Following Params" +
                    " UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" MDN:"+mdn+" UserID:"+userId+" OptionalOfferID:"+optionalOfferId
                    +" AgentId:"+agentId+" Channel:"+channel);

            agentLogger.info(time1+" Requesting CRM to Bucket Subscription with Following Params" +
                    " UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" MDN:"+mdn+" UserID:"+userId+" OptionalOfferID:"+optionalOfferId
                    +" AgentId:"+agentId+" Channel:"+channel);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject WirelessBucketSubscription = ResponseJson.getJSONObject("WirelessBucketSubscription");
                int ReturnCode = WirelessBucketSubscription.getInt("RetCode");
                String ReturnDescription = WirelessBucketSubscription.getString("RetDesc");

                if (ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    BucketSubscriptionResponse bucketSubscriptionResponse = new BucketSubscriptionResponse();
                    bucketSubscriptionResponse.setErrorCode(0);
                    bucketSubscriptionResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(bucketSubscriptionResponse);

                }else {

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    BucketSubscriptionResponse bucketSubscriptionResponse = new BucketSubscriptionResponse();
                    bucketSubscriptionResponse.setErrorCode(ReturnCode);
                    bucketSubscriptionResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(bucketSubscriptionResponse);

                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e) {

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                BucketSubscriptionResponse bucketSubscriptionResponse = new BucketSubscriptionResponse();
                bucketSubscriptionResponse.setErrorCode(5);
                bucketSubscriptionResponse.setErrorDetail("Sorry I could not Subscribe the bucket");
                returnResponse = ConsumeWSDL.javaToJson(bucketSubscriptionResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }

    }


    @RequestMapping(value="/BucketReSubscriptionHistoryWireless",method = RequestMethod.GET)
    public String bucketReSubscriptionHistoryWireless(
            @RequestParam(value="MDN",defaultValue = "") String mdn,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<BucketResubscriptionWirelessModel> bucketResubscriptionWirelessModelList = new ArrayList<BucketResubscriptionWirelessModel>();
        logger.info(time1+" BucketResubscriptionHistoryWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" BucketResubscriptionHistoryWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            BucketReSubscriptionHistoryWirelessResponse responseObject = new BucketReSubscriptionHistoryWirelessResponse();
            responseObject.setErrorCode(4);
            responseObject.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(responseObject);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;
        }else{

            // CONFIGURATION for Bucket ReSubscription History Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_BUCKET_RESUBSCRIPTION_HISTORY_WIRELESS");
            String SOAPAction = bundle.getString("SOAP_ACTION_BUCKET_RESUBSCRIPTION_HISTORY_WIRELESS");
            String xmlInput = SOAPRequestSample.bucketReSubscriptionHistoryRequest(userName,password,systemIP,mdn,agentId,channel);

            logger.info(time1+" Requesting CRM to get Bucket ReSubscription History with Following Params: " +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:"+ mdn+" AgentId:"+agentId+" Channel:"+channel);

            agentLogger.info(time1+" Requesting CRM to get Bucket ReSubscription History with Following Params: " +
                    " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:"+ mdn+" AgentId:"+agentId+" Channel:"+channel);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject WireBuckResubParams = ResponseJson.getJSONObject("WireBuckResubParams");
                int ReturnCode = Integer.parseInt(WireBuckResubParams.get("RetCode").toString());
                String ReturnDescription = WireBuckResubParams.get("RetDesc").toString();
                if (ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    Object bucketResubHistoryObject = WireBuckResubParams.get("BucketResubscriptionHistory");
                    if (checkObjectType(bucketResubHistoryObject).equals("JSONArray")){

                        logger.info(time1+" BucketResubscriptionHistory type is "+checkObjectType(bucketResubHistoryObject));
                        agentLogger.info(time1+" BucketResubscriptionHistory type is "+checkObjectType(bucketResubHistoryObject));

                        JSONArray BucketResubscriptionHistory = WireBuckResubParams.getJSONArray("BucketResubscriptionHistory");
                        for (int i = 0; i < BucketResubscriptionHistory.length(); ++i) {

                            BucketResubscriptionWirelessModel object = new BucketResubscriptionWirelessModel();
                            JSONObject bucketResubObject = BucketResubscriptionHistory.getJSONObject(i);
                            object.setBucketName(bucketResubObject.get("BucketName").toString());
                            object.setMDN(Long.parseLong(bucketResubObject.get("MDN").toString()));
                            object.setPaymentMode(bucketResubObject.get("PaymentMode").toString());
                            object.setSubscriptionDate(bucketResubObject.get("SubscriptionDate").toString());
                            bucketResubscriptionWirelessModelList.add(object);
                        }
                    }else{
                        logger.info("BucketResubscriptionHistory type is "+checkObjectType(bucketResubHistoryObject));
                        agentLogger.info("BucketResubscriptionHistory type is "+checkObjectType(bucketResubHistoryObject));

                        BucketResubscriptionWirelessModel object = new BucketResubscriptionWirelessModel();
                        JSONObject bucketResubObject = WireBuckResubParams.getJSONObject("BucketResubscriptionHistory");
                        object.setBucketName(bucketResubObject.get("BucketName").toString());
                        object.setMDN(Long.parseLong(bucketResubObject.get("MDN").toString()));
                        object.setPaymentMode(bucketResubObject.get("PaymentMode").toString());
                        object.setSubscriptionDate(bucketResubObject.get("SubscriptionDate").toString());
                        bucketResubscriptionWirelessModelList.add(object);
                    }

                    BucketReSubscriptionHistoryWirelessResponse responseObject = new BucketReSubscriptionHistoryWirelessResponse();
                    responseObject.setErrorCode(0);
                    responseObject.setErrorDetail(ReturnDescription);
                    responseObject.setBucketReSubscriptionHistory(bucketResubscriptionWirelessModelList);
                    returnResponse = ConsumeWSDL.javaToJson(responseObject);

                }else {

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    BucketReSubscriptionHistoryWirelessResponse responseObject = new BucketReSubscriptionHistoryWirelessResponse();
                    responseObject.setErrorCode(ReturnCode);
                    responseObject.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(responseObject);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e) {

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                BucketReSubscriptionHistoryWirelessResponse responseObject = new BucketReSubscriptionHistoryWirelessResponse();
                responseObject.setErrorCode(5);
                responseObject.setErrorDetail("Sorry I could not get bucket resubscription history");
                returnResponse = ConsumeWSDL.javaToJson(responseObject);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }

        }

    }

    @RequestMapping(value="/GetPackageListWireless",method = RequestMethod.GET)
    public String GetPackageListWireless(
            @RequestParam(value="MDN",defaultValue = "") String mdn,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<WirelessPackageModel> wirelessPackageModelList = new ArrayList<WirelessPackageModel>();
        logger.info(time1+" GetPackageListWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetPackageListWireless called with MDN:"+mdn+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            PackageWirelessResponse packageWirelessResponse = new PackageWirelessResponse();
            packageWirelessResponse.setErrorCode(4);
            packageWirelessResponse.setErrorDetail("Wrong input parameters");
            packageWirelessResponse.setPackages(wirelessPackageModelList);
            returnResponse = ConsumeWSDL.javaToJson(packageWirelessResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            // CONFIGURATION for get bucket list wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_GET_PACKAGE_LIST_WIRELESS");
            String xmlInput = SOAPRequestSample.getPackageListWirelessReqest(userName,password,systemIP,mdn,agentId,channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_PACKAGE_LIST_WIRELESS");

            logger.info(time1+" Requesting CRM to get Wireless Package list with Following Params"
                    +" UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP+ " MDN:" + mdn);

            agentLogger.info(time1+" Requesting CRM to get Wireless Package list with Following Params"
                    +" UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP+ " MDN:" + mdn);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);

                JSONObject PkgListParams = ResponseJson.getJSONObject("PkgListParams");
                int ReturnCode = PkgListParams.getInt("RetCode");
                String ReturnDescription = PkgListParams.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    JSONArray PackageList = PkgListParams.getJSONArray("PackageList");

                    for (int i = 0; i < PackageList.length(); ++i) {

                        WirelessPackageModel object = new WirelessPackageModel();
                        JSONObject PackageObject = PackageList.getJSONObject(i);
                        object.setPackageId(PackageObject.getLong("PackageId"));
                        object.setPackageName(PackageObject.getString("PackageName"));
                        wirelessPackageModelList.add(object);
                    }

                    PackageWirelessResponse packageWirelessResponse = new PackageWirelessResponse();
                    packageWirelessResponse.setErrorCode(0);
                    packageWirelessResponse.setErrorDetail(ReturnDescription);
                    packageWirelessResponse.setPackages(wirelessPackageModelList);
                    returnResponse = ConsumeWSDL.javaToJson(packageWirelessResponse);

                }else{

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    PackageWirelessResponse packageWirelessResponse = new PackageWirelessResponse();
                    packageWirelessResponse.setErrorCode(ReturnCode);
                    packageWirelessResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packageWirelessResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                PackageWirelessResponse packageWirelessResponse = new PackageWirelessResponse();
                packageWirelessResponse.setErrorCode(5);
                packageWirelessResponse.setErrorDetail("Sorry I could not get Packages");
                returnResponse = ConsumeWSDL.javaToJson(packageWirelessResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }

        }






    }

    @RequestMapping(value="/GetPackageHistoryWireless",method = RequestMethod.GET)
    public String getPackageHistoryWireless(
            @RequestParam(value="MDN",defaultValue = "") String mdn,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ) {
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<WirelessPackageHistoryModel> wirelessPackageHistoryModelList = new ArrayList<WirelessPackageHistoryModel>();
        logger.info(time1+" GetPackageHistoryWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetPackageHistoryWireless called with MDN:"+mdn+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            PackageHistoryWirelessResponse packageHistoryWirelessResponse = new PackageHistoryWirelessResponse();
            packageHistoryWirelessResponse.setErrorCode(4);
            packageHistoryWirelessResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(packageHistoryWirelessResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            // CONFIGURATION for Get Package History Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_GET_PACKAGE_HISTORY_WIRELESS");
            String xmlInput = SOAPRequestSample.getPackageHistoryWireless(userName,password,systemIP,mdn,agentId,channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_PACKAGE_HISTORY_WIRELESS");

            logger.info(time1+" Requesting CRM to get Package History Wireless with Following Params"
                    +" UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP+" MDN:"+mdn);

            agentLogger.info(time1+" Requesting CRM to get Package History Wireless with Following Params"
                    +" UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP+" MDN:"+mdn);
            try {
                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");
                agentLogger.info(time1+" Received CRM response after "+CRMResponseTime+" ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1+" CRM response "+ResponseJson);
                agentLogger.info(time1+" CRM response "+ResponseJson);


                JSONObject PackageHistoryParams = ResponseJson.getJSONObject("PackageHistoryParams");
                int ReturnCode = PackageHistoryParams.getInt("RetCode");
                String ReturnDescription = PackageHistoryParams.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    Object packageHistoryObject = PackageHistoryParams.get("PackageHistory");
                    if (checkObjectType(packageHistoryObject).equals("JSONArray")){
                        JSONArray PackageHistory = PackageHistoryParams.getJSONArray("PackageHistory");

                        for (int i = 0; i < PackageHistory.length(); ++i) {
                            WirelessPackageHistoryModel object = new WirelessPackageHistoryModel();
                            JSONObject PackageObject = PackageHistory.getJSONObject(i);
                            object.setMDN(Long.parseLong(PackageObject.get("MDN").toString()));
                            object.setModificationDate(PackageObject.get("ModificationDate").toString());
                            object.setNewPackage(PackageObject.get("NewPackage").toString());
                            object.setOldPackage(PackageObject.get("OldPackage").toString());
                            wirelessPackageHistoryModelList.add(object);
                        }
                    }else{

                        JSONObject PackageHistory = PackageHistoryParams.getJSONObject("PackageHistory");
                        WirelessPackageHistoryModel object = new WirelessPackageHistoryModel();
                        object.setMDN(Long.parseLong(PackageHistory.get("MDN").toString()));
                        object.setModificationDate(PackageHistory.get("ModificationDate").toString());
                        object.setNewPackage(PackageHistory.get("NewPackage").toString());
                        object.setOldPackage(PackageHistory.get("OldPackage").toString());
                        wirelessPackageHistoryModelList.add(object);
                    }


                    PackageHistoryWirelessResponse packageHistoryWirelessResponse = new PackageHistoryWirelessResponse();
                    packageHistoryWirelessResponse.setErrorCode(0);
                    packageHistoryWirelessResponse.setErrorDetail(ReturnDescription);
                    packageHistoryWirelessResponse.setWirelessPackageHistory(wirelessPackageHistoryModelList);
                    returnResponse = ConsumeWSDL.javaToJson(packageHistoryWirelessResponse);

                }else{

                    logger.info(time1+" CRM response code: "+ReturnCode);
                    agentLogger.info(time1+" CRM response code: "+ReturnCode);
                    logger.info(time1+" CRM response detail: "+ReturnDescription);
                    agentLogger.info(time1+" CRM response detail: "+ReturnDescription);

                    PackageHistoryWirelessResponse packageHistoryWirelessResponse = new PackageHistoryWirelessResponse();
                    packageHistoryWirelessResponse.setErrorCode(ReturnCode);
                    packageHistoryWirelessResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packageHistoryWirelessResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            } catch (Exception e) {
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                PackageHistoryWirelessResponse packageHistoryWirelessResponse = new PackageHistoryWirelessResponse();
                packageHistoryWirelessResponse.setErrorCode(5);
                packageHistoryWirelessResponse.setErrorDetail("Sorry I could not get Package history");
                returnResponse = ConsumeWSDL.javaToJson(packageHistoryWirelessResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }














    }

    @RequestMapping(value="/GetPackageReSubscriptionHistoryWireless",method = RequestMethod.GET)
    public String getPackageReSubscriptionHistoryWireless(
            @RequestParam(value="MDN",defaultValue = "") String mdn,
            @RequestParam(value="AgentId",required = false,defaultValue = "") String agentId
    ) {

        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<WirelessPackageReSubHistoryModel> wirelessPackageReSubHistoryModelList = new ArrayList<WirelessPackageReSubHistoryModel>();
        logger.info(time1+" GetPackageReSubscriptionHistoryWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetPackageReSubscriptionHistoryWireless called with MDN:"+mdn+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            PackageReSubscriptionHistoryWirelessResponse responseObject = new PackageReSubscriptionHistoryWirelessResponse();
            responseObject.setErrorCode(4);
            responseObject.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(responseObject);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else {

            // CONFIGURATION for Get Package Resubscription History Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_GET_PACKAGE_RESUBSCRIPTION_HISTORY_WIRELESS");
            String xmlInput = SOAPRequestSample.packageReSubscriptionHistoryRequest(userName, password, systemIP, mdn, agentId, channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_PACKAGE_RESUBSCRIPTION_HISTORY_WIRELESS");

            logger.info(time1 + " Requesting CRM to get Package Re-Subscription History Wireless with Following Params"
                    + " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:" + mdn + " AgentID:" + agentId + " Channel:" + channel);

            agentLogger.info(time1 + " Requesting CRM to get Package Re-Subscription History Wireless with Following Params"
                    + " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:" + mdn + " AgentID:" + agentId + " Channel:" + channel);

            try {
                String output = ConsumeWSDL.consumer(wsURL, xmlInput, SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");
                agentLogger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1 + " CRM response " + ResponseJson);
                agentLogger.info(time1 + " CRM response " + ResponseJson);

                JSONObject WirePackResubParams = ResponseJson.getJSONObject("WirePackResubParams");
                int ReturnCode = WirePackResubParams.getInt("RetCode");
                String ReturnDescription = WirePackResubParams.getString("RetDesc");
                //Check CRM return code
                if (ReturnCode == 0) {

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    Object packageReSubHistoryObject = WirePackResubParams.get("PackageResubscriptionHistory");
                    if (checkObjectType(packageReSubHistoryObject).equals("JSONArray")) {

                        logger.info(time1 + " PackageResubscriptionHistory type is " + checkObjectType(packageReSubHistoryObject));
                        agentLogger.info(time1 + " PackageResubscriptionHistory type is " + checkObjectType(packageReSubHistoryObject));

                        JSONArray PackageResubscriptionHistory = WirePackResubParams.getJSONArray("PackageResubscriptionHistory");
                        for (int i = 0; i < PackageResubscriptionHistory.length(); ++i) {
                            WirelessPackageReSubHistoryModel object = new WirelessPackageReSubHistoryModel();
                            JSONObject PackageResubHistoryObject = PackageResubscriptionHistory.getJSONObject(i);
                            object.setMDN(Long.parseLong(PackageResubHistoryObject.get("MDN").toString()));
                            object.setPackageName(PackageResubHistoryObject.get("PackageName").toString());
                            object.setPaymentMode(PackageResubHistoryObject.get("PaymentMode").toString());
                            object.setSubscriptionDate(PackageResubHistoryObject.get("SubscriptionDate").toString());
                            wirelessPackageReSubHistoryModelList.add(object);
                        }
                    } else {
                        logger.info(time1 + " PackageResubscriptionHistory type is " + checkObjectType(packageReSubHistoryObject));
                        agentLogger.info(time1 + " PackageResubscriptionHistory type is " + checkObjectType(packageReSubHistoryObject));

                        JSONObject PackageResubHistoryObject = WirePackResubParams.getJSONObject("PackageResubscriptionHistory");
                        WirelessPackageReSubHistoryModel object = new WirelessPackageReSubHistoryModel();
                        object.setMDN(Long.parseLong(PackageResubHistoryObject.get("MDN").toString()));
                        object.setPackageName(PackageResubHistoryObject.get("PackageName").toString());
                        object.setPaymentMode(PackageResubHistoryObject.get("PaymentMode").toString());
                        object.setSubscriptionDate(PackageResubHistoryObject.get("SubscriptionDate").toString());
                        wirelessPackageReSubHistoryModelList.add(object);

                    }

                    PackageReSubscriptionHistoryWirelessResponse responseObject = new PackageReSubscriptionHistoryWirelessResponse();
                    responseObject.setErrorCode(0);
                    responseObject.setErrorDetail(ReturnDescription);
                    responseObject.setWirelessPackageReSubHistory(wirelessPackageReSubHistoryModelList);
                    returnResponse = ConsumeWSDL.javaToJson(responseObject);

                } else {

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    PackageReSubscriptionHistoryWirelessResponse responseObject = new PackageReSubscriptionHistoryWirelessResponse();
                    responseObject.setErrorCode(ReturnCode);
                    responseObject.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(responseObject);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1 + " Total Execution time: " + executionTime + " ms");
                agentLogger.info(time1 + " Total Execution time: " + executionTime + " ms");
                logger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            } catch (Exception e) {

                logger.error(time1 + " Exception: " + e);
                agentLogger.error(time1 + " Exception: " + e);
                PackageReSubscriptionHistoryWirelessResponse responseObject = new PackageReSubscriptionHistoryWirelessResponse();
                responseObject.setErrorCode(5);
                responseObject.setErrorDetail("Sorry I could not get Package ReSubscription history");
                returnResponse = ConsumeWSDL.javaToJson(responseObject);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1 + " Total Execution time: " + executionTime + " ms");
                agentLogger.info(time1 + " Total Execution time: " + executionTime + " ms");
                logger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }
    }

    @RequestMapping(value="/PackageChangeWireless",method = RequestMethod.POST)
    public String packageChangeWireless(
           @RequestParam(value="MDN",defaultValue ="") String mdn,
           @RequestParam(value="NewPackageId",defaultValue ="") String newPackageId,
           @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
    ) {

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" PackageChangeWireless called with MDN:"+mdn+" NewPackageId:"+newPackageId+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" PackageChangeWireless called with MDN:"+mdn+" NewPackageId:"+newPackageId+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn) ||
                newPackageId.equals("undefined") || StringUtils.isEmpty(newPackageId)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            PackageChangeWirelessResponse packageChangeWirelessResponse = new PackageChangeWirelessResponse();
            packageChangeWirelessResponse.setErrorCode(4);
            packageChangeWirelessResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(packageChangeWirelessResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else {

            // CONFIGURATION for Package Change Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String user_id = bundle.getString("PTCL_ACCESS_USER");
            String wsURL = bundle.getString("WSDL_URL_PACKAGE_CHANGE_WIRELESS");
            String xmlInput = SOAPRequestSample.packageChangeWirelessRequest(userName,password,systemIP,mdn,user_id,
                    newPackageId,agentId,channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_PACKAGE_CHANGE_WIRELESS");

            logger.info(time1+" Requesting CRM to Wireless Package Change with Following Params"
                    +" UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" MDN:"+mdn+" NewPackageID:"+newPackageId+" AgentID:"+agentId
                    +" Channel:"+channel+" UserID:"+user_id);

            agentLogger.info(time1+" Requesting CRM to Wireless Package Change with Following Params"
                    +" UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    +" MDN:"+mdn+" NewPackageID:"+newPackageId+" AgentID:"+agentId
                    +" Channel:"+channel+" UserID:"+user_id);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;

                logger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");
                agentLogger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1 + " CRM response " + ResponseJson);
                agentLogger.info(time1 + " CRM response " + ResponseJson);

                JSONObject GetInfoWirelessPackResubHistory = ResponseJson.getJSONObject("WirelessPackageChange");
                int ReturnCode = GetInfoWirelessPackResubHistory.getInt("RetCode");
                String ReturnDescription = GetInfoWirelessPackResubHistory.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    PackageChangeWirelessResponse packageChangeWirelessResponse = new PackageChangeWirelessResponse();
                    packageChangeWirelessResponse.setErrorCode(0);
                    packageChangeWirelessResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packageChangeWirelessResponse);

                }else{

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    PackageChangeWirelessResponse packageChangeWirelessResponse = new PackageChangeWirelessResponse();
                    packageChangeWirelessResponse.setErrorCode(ReturnCode);
                    packageChangeWirelessResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packageChangeWirelessResponse);

                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                PackageChangeWirelessResponse packageChangeWirelessResponse = new PackageChangeWirelessResponse();
                packageChangeWirelessResponse.setErrorCode(5);
                packageChangeWirelessResponse.setErrorDetail("Sorry I could not change the package");
                returnResponse = ConsumeWSDL.javaToJson(packageChangeWirelessResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }












    }


    @RequestMapping(value="/PackageReSubscriptionWireless",method = RequestMethod.POST)
    public String packageReSubscriptionWireless(
            @RequestParam(value="MDN",defaultValue ="") String mdn,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
    ) {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" PackageReSubscriptionWireless called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" PackageReSubscriptionWireless called with MDN:"+mdn+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            PackageResubscriptionResponse packageResubscriptionResponse = new PackageResubscriptionResponse();
            packageResubscriptionResponse.setErrorCode(4);
            packageResubscriptionResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(packageResubscriptionResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else {
            // CONFIGURATION for Package Resubscription Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String user_id = bundle.getString("PTCL_ACCESS_USER");
            String wsURL = bundle.getString("WSDL_URL_PACKAGE_RESUBSCRIPTION_WIRELESS");
            String xmlInput = SOAPRequestSample.packageResubscriptionWirelessRequest(userName, password, systemIP, mdn,
                    user_id, agentId, channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_PACKAGE_RESUBSCRIPTION_WIRELESS");

            logger.info(time1 + " Requesting CRM to Package Resubscription Wireless with Following Params"
                    + " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:" + mdn + " UserID:" + user_id + " AgentID:" + agentId + " Channel:" + channel);

            agentLogger.info(time1 + " Requesting CRM to Package Resubscription Wireless with Following Params"
                    + " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:" + mdn + " UserID:" + user_id + " AgentID:" + agentId + " Channel:" + channel);

            try {
                String output = ConsumeWSDL.consumer(wsURL, xmlInput, SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;
                logger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");
                agentLogger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");

                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1 + " CRM response " + ResponseJson);
                agentLogger.info(time1 + " CRM response " + ResponseJson);

                JSONObject WirelessPackageResubscription = ResponseJson.getJSONObject("WirelessPackageResubscription");
                int ReturnCode = WirelessPackageResubscription.getInt("RetCode");
                String ReturnDesc = WirelessPackageResubscription.getString("RetDesc");
                String ReturnDescription = WirelessPackageResubscription.getString("Description");
                //Check CRM return code
                if (ReturnCode == 0) {

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    PackageResubscriptionResponse packageResubscriptionResponse = new PackageResubscriptionResponse();
                    packageResubscriptionResponse.setErrorCode(0);
                    packageResubscriptionResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packageResubscriptionResponse);

                } else {

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    PackageResubscriptionResponse packageResubscriptionResponse = new PackageResubscriptionResponse();
                    packageResubscriptionResponse.setErrorCode(ReturnCode);
                    packageResubscriptionResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(packageResubscriptionResponse);

                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1 + " Total Execution time: " + executionTime + " ms");
                agentLogger.info(time1 + " Total Execution time: " + executionTime + " ms");
                logger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            } catch (Exception e) {

                logger.error(time1 + " Exception: " + e);
                agentLogger.error(time1 + " Exception: " + e);
                PackageResubscriptionResponse packageResubscriptionResponse = new PackageResubscriptionResponse();
                packageResubscriptionResponse.setErrorCode(5);
                packageResubscriptionResponse.setErrorDetail("Sorry I could not Package Resubscribe");
                returnResponse = ConsumeWSDL.javaToJson(packageResubscriptionResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1 + " Total Execution time: " + executionTime + " ms");
                agentLogger.info(time1 + " Total Execution time: " + executionTime + " ms");
                logger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }

        }

    }

    @RequestMapping(value="/GetBalanceHistoryWireless",method = RequestMethod.GET)
    public String getBalanceHistoryWireless(
            @RequestParam(value="MDN",defaultValue ="") String mdn,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
            ) {

        String returnResponse = null;
        long time1 = System.currentTimeMillis();
        List<BalanceHistoryWirelessModel> balanceHistoryWirelessModelList = new ArrayList<BalanceHistoryWirelessModel>();
        logger.info(time1+" GetBalanceHistory called with MDN:"+mdn+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetBalanceHistory called with MDN:"+mdn+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            BalanceHistoryWirelessResponse balanceHistoryWirelessResponse = new BalanceHistoryWirelessResponse();
            balanceHistoryWirelessResponse.setErrorCode(4);
            balanceHistoryWirelessResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(balanceHistoryWirelessResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else {

            // CONFIGURATION for get balance history wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_GET_BALANCE_HISTORY_WIRELESS");
            String xmlInput = SOAPRequestSample.getBalanceHistoryWireless(userName,password,systemIP,mdn,agentId,channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_GET_BALANCE_HISTORY_WIRELESS");

            logger.info(time1+" Requesting CRM to get balance history with following Params"
                    +" UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    + " MDN:" + mdn+ " AgentID:" + agentId+ " Channel:" + channel);

            agentLogger.info(time1+" Requesting CRM to get balance history with following Params"
                    +" UserName:"+userName+" Password:"+password+" SystemIP:"+systemIP
                    + " MDN:" + mdn+ " AgentID:" + agentId+ " Channel:" + channel);

            try {

                String output =  ConsumeWSDL.consumer(wsURL,xmlInput,SOAPAction);
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;

                logger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");
                agentLogger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1 + " CRM response " + ResponseJson);
                agentLogger.info(time1 + " CRM response " + ResponseJson);

                JSONObject BalanceHistoryParams = ResponseJson.getJSONObject("BalanceHistoryParams");
                int ReturnCode = BalanceHistoryParams.getInt("RetCode");
                String ReturnDescription = BalanceHistoryParams.getString("RetDesc");
                //Check CRM return code
                if(ReturnCode == 0){

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    Object balanceHistoryObject = BalanceHistoryParams.get("BalanceHistory");
                    if (checkObjectType(balanceHistoryObject).equals("JSONArray")){

                        JSONArray BalanceHistory = BalanceHistoryParams.getJSONArray("BalanceHistory");

                        for (int i = 0; i < BalanceHistory.length(); ++i) {
                            BalanceHistoryWirelessModel object = new BalanceHistoryWirelessModel();
                            JSONObject BalanceHistoryObject = BalanceHistory.getJSONObject(i);
                            object.setAmount(BalanceHistoryObject.getLong("Amount"));
                            object.setLoadingDate(BalanceHistoryObject.getString("LoadingDate"));
                            object.setMDN(BalanceHistoryObject.getLong("MDN"));
                            object.setChannel(BalanceHistoryObject.getString("Channel"));
                            balanceHistoryWirelessModelList.add(object);
                        }
                    }else{

                        JSONObject BalanceHistory = BalanceHistoryParams.getJSONObject("BalanceHistory");
                        BalanceHistoryWirelessModel object = new BalanceHistoryWirelessModel();
                        object.setAmount(BalanceHistory.getLong("Amount"));
                        object.setLoadingDate(BalanceHistory.getString("LoadingDate"));
                        object.setMDN(BalanceHistory.getLong("MDN"));
                        object.setChannel(BalanceHistory.getString("Channel"));
                        balanceHistoryWirelessModelList.add(object);
                    }



                    BalanceHistoryWirelessResponse balanceHistoryWirelessResponse = new BalanceHistoryWirelessResponse();
                    balanceHistoryWirelessResponse.setErrorCode(0);
                    balanceHistoryWirelessResponse.setErrorDetail(ReturnDescription);
                    balanceHistoryWirelessResponse.setBalanceHistory(balanceHistoryWirelessModelList);
                    returnResponse = ConsumeWSDL.javaToJson(balanceHistoryWirelessResponse);

                }else{

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    BalanceHistoryWirelessResponse balanceHistoryWirelessResponse = new BalanceHistoryWirelessResponse();
                    balanceHistoryWirelessResponse.setErrorCode(ReturnCode);
                    balanceHistoryWirelessResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(balanceHistoryWirelessResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }catch (Exception e){
                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                BalanceHistoryWirelessResponse balanceHistoryWirelessResponse = new BalanceHistoryWirelessResponse();
                balanceHistoryWirelessResponse.setErrorCode(5);
                balanceHistoryWirelessResponse.setErrorDetail("Sorry I could not get balance history");
                returnResponse = ConsumeWSDL.javaToJson(balanceHistoryWirelessResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }

    }



    @RequestMapping(value="/EbillActivation",method = RequestMethod.POST)
    public String ebillActivation(
            @RequestParam(value="MDN",defaultValue ="") String mdn,
            @RequestParam(value="Email",defaultValue ="") String email,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId) {

        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" EBillActivation called with MDN:"+mdn+" Email:"+email+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" EBillActivation called with MDN:"+mdn+" Email:"+email+" AgentId:"+agentId);
        if (
                mdn.equals("undefined") || StringUtils.isEmpty(mdn) ||
                email.equals("undefined") || StringUtils.isEmpty(email)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            EBillActivationResponse eBillActivationResponse = new EBillActivationResponse();
            eBillActivationResponse.setErrorCode(4);
            eBillActivationResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(eBillActivationResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else {

            // CONFIGURATION for E-Bill Activation Wireless API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_API_ACCESS_USER");
            String password = bundle.getString("PTCL_API_ACCESS_PASSWORD");
            String systemIP = bundle.getString("PTCL_API_ACCESS_SYSTEM_IP");
            String channel = bundle.getString("PTCL_API_ACCESS_CHANEL");
            String wsURL = bundle.getString("WSDL_URL_EBILL_ACTIVATION_WIRELESS");
            String xmlInput = SOAPRequestSample.ebillActivationRequest(userName, password, systemIP, mdn, email, agentId, channel);
            String SOAPAction = bundle.getString("SOAP_ACTION_EBILL_ACTIVATION_WIRELESS");

            logger.info(time1 + " Requesting CRM  to E-Bill Activation Wireless with Following Params"
                    + " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:" + mdn + " Email:" + email + " AgentID:" + agentId + " Channel:" + channel);

            agentLogger.info(time1 + " Requesting CRM  to E-Bill Activation Wireless with Following Params"
                    + " UserName:" + userName + " Password:" + password + " SystemIP:" + systemIP
                    + " MDN:" + mdn + " Email:" + email + " AgentID:" + agentId + " Channel:" + channel);

            try {
                String output = ConsumeWSDL.consumer(wsURL, xmlInput, SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;

                logger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");
                agentLogger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1 + " CRM response " + ResponseJson);
                agentLogger.info(time1 + " CRM response " + ResponseJson);

                JSONObject EbillActivationWireless = ResponseJson.getJSONObject("EbillActivationWireless");
                int ReturnCode = EbillActivationWireless.getInt("RetCode");
                String ReturnDescription = EbillActivationWireless.getString("RetDesc");
                //Check CRM return code
                if (ReturnCode == 0) {

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    EBillActivationResponse eBillActivationResponse = new EBillActivationResponse();
                    eBillActivationResponse.setErrorCode(0);
                    eBillActivationResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(eBillActivationResponse);

                } else {

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + ReturnDescription);
                    agentLogger.info(time1 + " CRM response detail: " + ReturnDescription);

                    EBillActivationResponse eBillActivationResponse = new EBillActivationResponse();
                    eBillActivationResponse.setErrorCode(ReturnCode);
                    eBillActivationResponse.setErrorDetail(ReturnDescription);
                    returnResponse = ConsumeWSDL.javaToJson(eBillActivationResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1 + " Total Execution time: " + executionTime + " ms");
                agentLogger.info(time1 + " Total Execution time: " + executionTime + " ms");
                logger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            } catch (Exception e) {

                logger.error(time1 + " Exception: " + e);
                agentLogger.error(time1 + " Exception: " + e);
                EBillActivationResponse eBillActivationResponse = new EBillActivationResponse();
                eBillActivationResponse.setErrorCode(5);
                eBillActivationResponse.setErrorDetail("Sorry I could not proceed with EBill Activation");
                returnResponse = ConsumeWSDL.javaToJson(eBillActivationResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1 + " Total Execution time: " + executionTime + " ms");
                agentLogger.info(time1 + " Total Execution time: " + executionTime + " ms");
                logger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info(time1 + " exit with  " + returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }

    }


    @RequestMapping(value="/SendSMS",method = RequestMethod.POST)
    public String sendSMS(
            @RequestParam(value = "Number",defaultValue = "") String number,
            @RequestParam(value = "MSG",defaultValue = "") String msg,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
    ){
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" SendSMS called with Number:"+number+"  MSG:"+msg+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" SendSMS called with Number:"+number+"  MSG:"+msg+" AgentId:"+agentId);
        if (
                number.equals("undefined") || StringUtils.isEmpty(number) ||
                        msg.equals("undefined") || StringUtils.isEmpty(msg)
        ){
            logger.info("Wrong input parameters");
            agentLogger.info("Wrong input parameters");

            GeneralResponse generalResponse = new GeneralResponse();
            generalResponse.setErrorCode(4);
            generalResponse.setErrorDetail("Wrong input parameters");
            returnResponse = ConsumeWSDL.javaToJson(generalResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else {

            // CONFIGURATION for Send SMS API
            ResourceBundle bundle = ResourceBundle.getBundle("Config");
            String userName = bundle.getString("PTCL_SMS_USER");
            String password = bundle.getString("PTCL_SMS_PASS");
            String wsURL = bundle.getString("WSDL_URL_SMS");
            String xmlInput = SOAPRequestSample.sendSMSRequest(userName,password,number,msg);
            // Because no need to provide soap action so its empty
            String SOAPAction = "";

            logger.info(time1+" Requesting CRM  to Send SMS with Following Params"
                    +" UserName:"+userName+" Password:"+password
                    + " SendTo:" + number+" MSG:" + msg);

            agentLogger.info(time1+" Requesting CRM  to Send SMS with Following Params"
                    +" UserName:"+userName+" Password:"+password
                    + " SendTo:" + number+" MSG:" + msg);

            try {

                String output = ConsumeWSDL.consumer(wsURL, xmlInput, SOAPAction);
                // Convert xml string to json
                long ResponseTime = System.currentTimeMillis();
                long CRMResponseTime = ResponseTime - time1;

                logger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");
                agentLogger.info(time1 + " Received CRM response after " + CRMResponseTime + " ms");

                // Convert xml string to json
                JSONObject ResponseJson = ConsumeWSDL.getJson(output);
                logger.info(time1 + " CRM response " + ResponseJson);
                agentLogger.info(time1 + " CRM response " + ResponseJson);

                JSONObject SendSmsResponse = ResponseJson.getJSONObject("SendSmsResponse");
                JSONObject SendSmsResult = SendSmsResponse.getJSONObject("SendSmsResult");
                JSONObject ack = SendSmsResult.getJSONObject("ack");


                int ReturnCode = ack.getInt("ReturnCode");
                String Message = ack.getString("Message");
                //Check CRM return code
                if(ReturnCode == 1){

                    String MessageDescription = ack.getString("MessageDescription");
                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + Message);
                    agentLogger.info(time1 + " CRM response detail: " + Message);
                    logger.info(time1 + " CRM MSG detail: " + MessageDescription);
                    agentLogger.info(time1 + " CRM MSG detail: " + MessageDescription);

                    GeneralResponse generalResponse = new GeneralResponse();
                    generalResponse.setErrorCode(0);
                    generalResponse.setErrorDetail(MessageDescription);
                    returnResponse = ConsumeWSDL.javaToJson(generalResponse);

                }else{

                    logger.info(time1 + " CRM response code: " + ReturnCode);
                    agentLogger.info(time1 + " CRM response code: " + ReturnCode);
                    logger.info(time1 + " CRM response detail: " + Message);
                    agentLogger.info(time1 + " CRM response detail: " + Message);

                    GeneralResponse generalResponse = new GeneralResponse();
                    generalResponse.setErrorCode(ReturnCode);
                    generalResponse.setErrorDetail(Message);
                    returnResponse = ConsumeWSDL.javaToJson(generalResponse);
                }

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;


            }catch (Exception e){

                logger.info(time1+" Exception: "+e);
                agentLogger.info(time1+" Exception: "+e);
                GeneralResponse generalResponse = new GeneralResponse();
                generalResponse.setErrorCode(5);
                generalResponse.setErrorDetail("Sorry I could not send SMS");
                returnResponse = ConsumeWSDL.javaToJson(generalResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }











    }



    @RequestMapping(value = "/GetMttrCategory",method = RequestMethod.GET)
    public String getMttrCategory(
            @RequestParam(value = "Number",defaultValue = "") String number,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
    ){

        String returnResponse=null;
        long time1 = System.currentTimeMillis();
        logger.info(time1+" GetMTTRCategory called with Number:"+number+" AgentId:"+agentId);


        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetMTTRCategory called with Number:"+number+" AgentId:"+agentId);

        try{

            if (number.equals("undefined") || StringUtils.isEmpty(number)){

                logger.info(time1+" Wrong Input Parameters");
                agentLogger.info(time1+" Wrong Input Parameters");

                MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                mttrCategoryResponse.setErrorCode(4);
                mttrCategoryResponse.setErrorDetail("Wrong Input parameters");
                returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            }else{


              /*  try (OutputStream output = new FileOutputStream("path/to/config.properties")) {

                    Properties prop = new Properties();

                    // set the properties value
                    prop.setProperty("db.url", "localhost");
                    prop.setProperty("db.user", "mkyong");
                    prop.setProperty("db.password", "password");

                    // save properties to project root folder
                    prop.store(output, null);

                    System.out.println(prop);

                } catch (IOException io) {
                    io.printStackTrace();
                }

                Properties propp = new Properties();
                String propFile = "application.properties";


                InputStream inputStream = getClass()
                        .getClassLoader().getResourceAsStream("Config.properties");

                if (inputStream!= null){
                    propp.load(inputStream);
                }

                String uu = propp.getProperty("MTTR_SERVER_IP");
                String uu2 = propp.getProperty("MTTR_UP_IP");
                System.out.println(uu);
                System.out.println(uu2);
                PropertiesConfiguration config = new PropertiesConfiguration("Config.properties");

                config.setProperty("MTTR_UP_IP", "10.11");
                config.save();
                String uu3 = propp.getProperty("MTTR_UP_IP");
                System.out.println(uu3);*/


                ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");
                String maxActive=resourceBundle.getString("maxActive");
                String maxIdle=resourceBundle.getString("maxIdle");
                String minIdle=resourceBundle.getString("minIdle");
                String loginTimeOut=resourceBundle.getString("loginTimeOut");
                String serverIP = resourceBundle.getString("MTTR_SERVER_IP");
                String username = resourceBundle.getString("MTTR_USERNAME");
                String password = resourceBundle.getString("MTTR_PASSWORD");
                String database = resourceBundle.getString("MTTR_DATABASE");

                //LIKE operator require a string as left-hand value, conversion from float to decimal

                //String Query = "SELECT CAST(Prefix AS INT) as Prefix,Region,EXCHANGE_ID,EXCHANGE_NM,Zone,ExchangeName FROM PTCLMTTR where("+number+" like CONCAT('%', convert(decimal(20), Prefix), '%') )";
                //String Query = "select * from MTTRExchange where ("+number+" like CONCAT('%',Prefix, '%')  )";
                // CONCAT is introduce in SQL 2012 and on 239 its less then 2012
                String Query = "select * from MTTRExchange where ("+number+" like ('%'+Prefix+'%') )";
                String PoolName=resourceBundle.getString("MTTR_PoolName");

                logger.info(time1+" Going to fetch MTTR Category from Dump with following params "
                        +" UserName:"+username+" password:"+password+" ServerIP:"+serverIP+" DB:"+database
                        +" MaxActive:"+maxActive+" MaxIdle:"+maxIdle+" MinIdle:"+minIdle
                        +" LoginTimeOut:"+loginTimeOut+" PoolName:"+PoolName);

                agentLogger.info(time1+" Going to fetch MTTR Category from Dump with following params "
                        +" UserName:"+username+" password:"+password+" ServerIP:"+serverIP+" DB:"+database
                        +" MaxActive:"+maxActive+" MaxIdle:"+maxIdle+" MinIdle:"+minIdle
                        +" LoginTimeOut:"+loginTimeOut+" PoolName:"+PoolName);


                //fetchMTTRData();


                String urlSQLServer = "jdbc:sqlserver://"+serverIP+";Databasename="+database+
                        ";user="+username+";password="+password;
                String driverSQLServer = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

                Statement statement = null;
                Connection dbConnection = null;
                DBConnectionManager connectionManager=null;
                MttrData mttrDataObject = null;
                ResultSet rs=null;

                try {
                    logger.info(time1+" Going to get DB Connection Manager");
                    agentLogger.info(time1+" Going to get DB Connection Manager");
                    connectionManager = DBConnectionManager.getDBConnectionManager(urlSQLServer, driverSQLServer, PoolName,
                            Integer.parseInt(loginTimeOut), Logger.getLogger(OWController.class.getName()),
                            Integer.parseInt(maxActive), Integer.parseInt(minIdle), Integer.parseInt(maxIdle),Logger.getLogger(agentId),time1);
                    if (connectionManager != null) {

                        logger.info(time1+" Successfully get Connection Manager");
                        agentLogger.info(time1+" Successfully get Connection Manager");
                        logger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                        agentLogger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                        dbConnection=connectionManager.getConnection(PoolName,Logger.getLogger(agentId),time1);

                        logger.info(time1+" Connection Build successfully");
                        agentLogger.info(time1+" Connection Build successfully");
                        statement = dbConnection.createStatement();

                        logger.info(time1+" Going to execute query: ");
                        agentLogger.info(time1+" Going to execute query");
                        logger.info(time1+" Query: "+Query);
                        agentLogger.info(time1+" Query: "+Query);
                        // execute select SQL stetement
                        rs = statement.executeQuery(Query);

                        if(rs.equals(null))
                        {
                            logger.info(time1+" MTTR Category not found against number:"+number);
                            agentLogger.info(time1+" MTTR Category not found against number:"+number);
                            MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                            mttrCategoryResponse.setErrorCode(1);
                            mttrCategoryResponse.setErrorDetail("MTTR Category not available against "+number);
                            returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);

                        }else {
                            int i=0;
                            while (rs.next()) {
                                if (i==0) {
                                    mttrDataObject = new MttrData();
                                    mttrDataObject.setPrefix(rs.getString("Prefix"));
                                    mttrDataObject.setRegion(rs.getString("Region"));
                                    mttrDataObject.setEXCHANGE_ID(rs.getString("EXCHANGE_ID"));
                                    mttrDataObject.setEXCHANGE_NM(rs.getString("EXCHANGE_NM"));
                                    mttrDataObject.setZone(rs.getString("Zone"));
                                    mttrDataObject.setEXCHANGE_NAME(rs.getString("ExchangeName"));

                                }else{
                                    logger.info(time1+" Multiple MTTR Category records found against number:"+number);
                                    logger.info(time1+" There should be only one record for MTTR Category against one number, need to check this issue");
                                    agentLogger.info(time1+" Multiple MTTR Category records found against number:"+number);
                                    agentLogger.info(time1+" There should be only one record for MTTR Category against one number, need to check this issue");
                                }
                            }

                            MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                            mttrCategoryResponse.setErrorCode(0);
                            mttrCategoryResponse.setErrorDetail("Successfully get MTTR Category");
                            mttrCategoryResponse.setMttrCategory(mttrDataObject);
                            returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);
                        }
                    }else{
                        logger.info(time1+" Erro while getting DB Connection Manager");
                        logger.info(time1+" Found null in DB Connection Manager");
                        agentLogger.info(time1+" Erro while getting DB Connection Manager");
                        agentLogger.info(time1+" Found null in DB Connection Manager");
                        MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                        mttrCategoryResponse.setErrorCode(1);
                        mttrCategoryResponse.setErrorDetail("Sorry I could not get MTTR Category");
                        returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);
                    }

                } catch (SQLException e) {

                    logger.error(time1+" Exception: "+e);
                    agentLogger.error(time1+" Exception: "+e);
                    MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                    mttrCategoryResponse.setErrorCode(5);
                    mttrCategoryResponse.setErrorDetail("Sorry could not get MTTR Category");
                    returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);

                }finally {
                    if (rs != null) {
                        try {
                            rs.close();
                            logger.info(time1+" Result set closed....... " + null);
                            agentLogger.info(time1+" Result set closed....... " + null);
                        } catch (Exception e) {
                            logger.error(time1+" Exception found in resultSet closing: "+e);
                            agentLogger.error(time1+" Exception found in resultSet closing: "+e);
                        }
                    }
                    if (statement != null) {
                        try {
                            statement.close();
                            logger.info(time1+" statement  closed....... " + null);
                            agentLogger.info(time1+" statement  closed....... " + null);
                        } catch (Exception e) {
                            logger.error(time1+" Exception found in statement closing: ",e);
                            agentLogger.error(time1+" Exception found in statement closing: ",e);
                        }
                    }
                    connectionManager.closeConnection(dbConnection,PoolName,Logger.getLogger(agentId),time1);
                }


                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }

        } catch (Exception e) {
            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
            mttrCategoryResponse.setErrorCode(5);
            mttrCategoryResponse.setErrorDetail("Sorry I could not get MTTR Category");
            returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;
        }


    }


    @RequestMapping(value = "/GetMttrComplaints",method = RequestMethod.GET)
    public String getMttrComplaints(
            @RequestParam(value = "Exchange") String Exchange,
            @RequestParam(value = "Product") String Product,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
    )
    {
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        List<String> mttrComplaints = new ArrayList<>();
        logger.info(time1+" GetMTTRComplaints called with Exchange:"+Exchange+" Product:"+Product+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetMTTRComplaints called with Exchange:"+Exchange+" Product:"+Product+" AgentId:"+agentId);

        if (
                Exchange.equals("undefined") || StringUtils.isEmpty(Exchange) ||
                Product.equals("undefined") || StringUtils.isEmpty(Product)
        ){

            logger.info(time1+" Wrong Input Parameters");
            agentLogger.info(time1+" Wrong Input Parameters");

            MTTRComplaintsResponse mttrComplaintsResponse = new MTTRComplaintsResponse();
            mttrComplaintsResponse.setErrorCode(4);
            mttrComplaintsResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(mttrComplaintsResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            try{

                ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");
                String maxActive=resourceBundle.getString("maxActive");
                String maxIdle=resourceBundle.getString("maxIdle");
                String minIdle=resourceBundle.getString("minIdle");
                String loginTimeOut=resourceBundle.getString("loginTimeOut");
                String serverIP = resourceBundle.getString("MTTR_SERVER_IP");
                String username = resourceBundle.getString("MTTR_USERNAME");
                String password = resourceBundle.getString("MTTR_PASSWORD");
                String database = resourceBundle.getString("MTTR_DATABASE");

                String Query = "select DISTINCT SR_Sub_Type from MTTRProductComplaint where (Exchange ='"+Exchange+"' AND Product_CEM = '"+Product+"')";
                String PoolName=resourceBundle.getString("MTTR_PoolName");

                logger.info(time1+" Going to fetch MTTR Complaints from Dump with following params "
                        + " Exchange:"+Exchange+" Product:"+Product
                        +" UserName:"+username+" password:"+password+" ServerIP:"+serverIP+" DB:"+database
                        +" MaxActive:"+maxActive+" MaxIdle:"+maxIdle+" MinIdle:"+minIdle
                        +" LoginTimeOut:"+loginTimeOut+" PoolName:"+PoolName);

                agentLogger.info(time1+" Going to fetch MTTR Complaints from Dump with following params "
                        + " Exchange:"+Exchange+" Product:"+Product
                        +" UserName:"+username+" password:"+password+" ServerIP:"+serverIP+" DB:"+database
                        +" MaxActive:"+maxActive+" MaxIdle:"+maxIdle+" MinIdle:"+minIdle
                        +" LoginTimeOut:"+loginTimeOut+" PoolName:"+PoolName);

                String urlSQLServer = "jdbc:sqlserver://"+serverIP+";Databasename="+database+
                        ";user="+username+";password="+password;
                String driverSQLServer = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

                Statement statement = null;
                Connection dbConnection = null;
                DBConnectionManager connectionManager=null;
                ResultSet resultSet=null;

                try {
                    logger.info(time1+" Going to get DB Connection Manager");
                    agentLogger.info(time1+" Going to get DB Connection Manager");
                    connectionManager = DBConnectionManager.getDBConnectionManager(urlSQLServer, driverSQLServer, PoolName,
                            Integer.parseInt(loginTimeOut), Logger.getLogger(OWController.class.getName()),
                            Integer.parseInt(maxActive), Integer.parseInt(minIdle), Integer.parseInt(maxIdle),Logger.getLogger(agentId),time1);
                    if (connectionManager != null)
                    {

                        logger.info(time1+" Successfully get Connection Manager");
                        agentLogger.info(time1+" Successfully get Connection Manager");
                        logger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                        agentLogger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                        dbConnection=connectionManager.getConnection(PoolName,Logger.getLogger(agentId),time1);
                        logger.info(time1+" Connection Build successfully");
                        agentLogger.info(time1+" Connection Build successfully");

                        statement = dbConnection.createStatement();

                        logger.info(time1+" Going to execute query: ");
                        agentLogger.info(time1+" Going to execute query");
                        logger.info(time1+" Query: "+Query);
                        agentLogger.info(time1+" Query: "+Query);

                        // execute select SQL stetement
                        resultSet = statement.executeQuery(Query);

                        if(resultSet.equals(null))
                        {

                            logger.info(time1+" MTTR Complaints not found against Exchange:"+Exchange+" Product:"+Product);
                            agentLogger.info(time1+" MTTR Complaints not found against Exchange:"+Exchange+" Product:"+Product);

                            MTTRComplaintsResponse mttrComplaintsResponse = new MTTRComplaintsResponse();
                            mttrComplaintsResponse.setErrorCode(1);
                            mttrComplaintsResponse.setErrorDetail("MTTR Complaints not available against Exchange:"+Exchange+" Product:"+Product);
                            returnResponse = ConsumeWSDL.javaToJson(mttrComplaintsResponse);

                        }else {

                            logger.debug(time1+" MTTR Complaints list found");
                            while (resultSet.next()) {
                                mttrComplaints.add(resultSet.getString("SR_Sub_Type"));
                            }
                            MTTRComplaintsResponse mttrComplaintsResponse = new MTTRComplaintsResponse();
                            mttrComplaintsResponse.setErrorCode(0);
                            mttrComplaintsResponse.setErrorDetail("Success");
                            mttrComplaintsResponse.setMrrtComplaints(mttrComplaints);
                            returnResponse = ConsumeWSDL.javaToJson(mttrComplaintsResponse);
                        }


                    }else{

                        logger.info(time1+" Erro while getting DB Connection Manager");
                        logger.info(time1+" Found null in DB Connection Manager");
                        agentLogger.info(time1+" Erro while getting DB Connection Manager");
                        agentLogger.info(time1+" Found null in DB Connection Manager");

                        MTTRComplaintsResponse mttrComplaintsResponse = new MTTRComplaintsResponse();
                        mttrComplaintsResponse.setErrorCode(1);
                        mttrComplaintsResponse.setErrorDetail("Sorry I could not get MTTR Complaints");
                        returnResponse = ConsumeWSDL.javaToJson(mttrComplaintsResponse);
                    }

                } catch (SQLException e) {

                    logger.error(time1+" Exception: "+e);
                    agentLogger.error(time1+" Exception: "+e);

                    MTTRComplaintsResponse mttrComplaintsResponse = new MTTRComplaintsResponse();
                    mttrComplaintsResponse.setErrorCode(5);
                    mttrComplaintsResponse.setErrorDetail("Sorry I could not get MTTR Complaints");
                    returnResponse = ConsumeWSDL.javaToJson(mttrComplaintsResponse);

                }finally {
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                            logger.info(time1+" Result set closed....... " + null);
                            agentLogger.info(time1+" Result set closed....... " + null);
                        } catch (Exception e) {
                            logger.error(time1+" Exception found in resultSet closing: "+e);
                            agentLogger.error(time1+" Exception found in resultSet closing: "+e);
                        }

                    }
                    if (statement != null) {
                        try {
                            statement.close();
                            logger.info(time1+" statement  closed....... " + null);
                            agentLogger.info(time1+" statement  closed....... " + null);
                        } catch (Exception e) {
                            logger.error(time1+" Exception found in statement closing: ",e);
                            agentLogger.error(time1+" Exception found in statement closing: ",e);
                        }

                    }
                    connectionManager.closeConnection(dbConnection,PoolName,Logger.getLogger(agentId),time1);
                }


                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;

            } catch (Exception e) {

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);
                MTTRComplaintsResponse mttrComplaintsResponse = new MTTRComplaintsResponse();
                mttrComplaintsResponse.setErrorCode(5);
                mttrComplaintsResponse.setErrorDetail("Sorry I could not get MTTR Complaints");
                returnResponse = ConsumeWSDL.javaToJson(mttrComplaintsResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }

        }



    }


    @RequestMapping(value = "/GetMttrTime",method = RequestMethod.GET)
    public String getMttrTime(
            @RequestParam(value = "Exchange") String ExchangeID,
            @RequestParam(value = "Product") String Product,
            @RequestParam(value = "Complaint") String Complaint,
            @RequestParam(value="AgentId",required = false,defaultValue ="") String agentId
    ){
        long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" GetMTTRTime called with Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" GetMTTRTime called with Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint+" AgentId:"+agentId);

        if (
                ExchangeID.equals("undefined") || StringUtils.isEmpty(ExchangeID) ||
                Product.equals("undefined") || StringUtils.isEmpty(Product) ||
                Complaint.equals("undefined") || StringUtils.isEmpty(Complaint)
        ){

            logger.info(time1+" Wrong Input Parameters");
            agentLogger.info(time1+" Wrong Input Parameters");

            MTTRTimeResponse mttrTimeResponse = new MTTRTimeResponse();
            mttrTimeResponse.setErrorCode(4);
            mttrTimeResponse.setErrorDetail("Wrong Input parameters");
            returnResponse = ConsumeWSDL.javaToJson(mttrTimeResponse);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+returnResponse);
            agentLogger.info(time1+" exit with  "+returnResponse);
            agentLogger.info("<<============================================================================>>");
            return returnResponse;

        }else{

            try{

                ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");
                String maxActive=resourceBundle.getString("maxActive");
                String maxIdle=resourceBundle.getString("maxIdle");
                String minIdle=resourceBundle.getString("minIdle");
                String loginTimeOut=resourceBundle.getString("loginTimeOut");

                String serverIP = resourceBundle.getString("MTTR_SERVER_IP");
                String username = resourceBundle.getString("MTTR_USERNAME");
                String password = resourceBundle.getString("MTTR_PASSWORD");
                String database = resourceBundle.getString("MTTR_DATABASE");


                String Query = "select MTTR from MTTRProductComplaint where (Exchange ='"+ExchangeID+"' AND Product_CEM = '"+Product+"' AND SR_Sub_Type ='"+Complaint+"')";
                String PoolName=resourceBundle.getString("MTTR_PoolName");

                logger.info(time1+" Going to fetch MTTR Time from Dump with following params "
                        + " Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint
                        +" UserName:"+username+" password:"+password+" ServerIP:"+serverIP+" DB:"+database
                        +" MaxActive:"+maxActive+" MaxIdle:"+maxIdle+" MinIdle:"+minIdle
                        +" LoginTimeOut:"+loginTimeOut+" PoolName:"+PoolName);

                agentLogger.info(time1+" Going to fetch MTTR Time from Dump with following params "
                        + " Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint
                        +" UserName:"+username+" password:"+password+" ServerIP:"+serverIP+" DB:"+database
                        +" MaxActive:"+maxActive+" MaxIdle:"+maxIdle+" MinIdle:"+minIdle
                        +" LoginTimeOut:"+loginTimeOut+" PoolName:"+PoolName);



                String urlSQLServer = "jdbc:sqlserver://"+serverIP+";Databasename="+database+
                        ";user="+username+";password="+password;
                String driverSQLServer = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

                Statement statement = null;
                Connection dbConnection = null;
                DBConnectionManager connectionManager=null;
                ResultSet resultSet=null;

                try {
                    logger.info(time1+" Going to get DB Connection Manager");
                    agentLogger.info(time1+" Going to get DB Connection Manager");
                    connectionManager = DBConnectionManager.getDBConnectionManager(urlSQLServer, driverSQLServer, PoolName,
                            Integer.parseInt(loginTimeOut), Logger.getLogger(OWController.class.getName()),
                            Integer.parseInt(maxActive), Integer.parseInt(minIdle), Integer.parseInt(maxIdle),Logger.getLogger(agentId),time1);
                    if (connectionManager != null) {

                        logger.info(time1+" Successfully get Connection Manager");
                        agentLogger.info(time1+" Successfully get Connection Manager");
                        logger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                        agentLogger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                        dbConnection=connectionManager.getConnection(PoolName,Logger.getLogger(agentId),time1);
                        logger.info(time1+" Connection Build successfully");
                        agentLogger.info(time1+" Connection Build successfully");

                        statement = dbConnection.createStatement();

                        logger.info(time1+" Going to execute query: ");
                        agentLogger.info(time1+" Going to execute query");
                        logger.info(time1+" Query: "+Query);
                        agentLogger.info(time1+" Query: "+Query);

                        // execute select SQL stetement
                        resultSet = statement.executeQuery(Query);

                        if(resultSet.equals(null))
                        {
                            logger.info(time1+" MTTR Time not found against Exchange:"+ExchangeID+" Product:"+Product +" Complaint:"+Complaint);
                            agentLogger.info(time1+" MTTR Time not found against Exchange:"+ExchangeID+" Product:"+Product +" Complaint:"+Complaint);

                            MTTRTimeResponse mttrTimeResponse = new MTTRTimeResponse();
                            mttrTimeResponse.setErrorCode(1);
                            mttrTimeResponse.setErrorDetail("MTTR Time not available against Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint);
                            returnResponse = ConsumeWSDL.javaToJson(mttrTimeResponse);

                        }
                        else {
                            String MTTR = null;
                            int i =0;
                            while (resultSet.next()) {
                                if (i==0){
                                    MTTR = resultSet.getString("MTTR");
                                }else{
                                    logger.info(time1+" Multiple MTTR Time records found against Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint);
                                    logger.info(time1+" There should be only one record for MTTR Time against one number, need to check this issue");
                                    agentLogger.info(time1+" Multiple MTTR Time records found against Exchange:"+ExchangeID+" Product:"+Product+" Complaint:"+Complaint);
                                    agentLogger.info(time1+" There should be only one record for MTTR Time against one number, need to check this issue");
                                }

                            }

                            MTTRTimeResponse mttrTimeResponse = new MTTRTimeResponse();
                            mttrTimeResponse.setErrorCode(0);
                            mttrTimeResponse.setErrorDetail("Success");
                            mttrTimeResponse.setMTTR(MTTR);
                            returnResponse = ConsumeWSDL.javaToJson(mttrTimeResponse);

                        }

                    }else{
                        logger.info(time1+" Erro while getting DB Connection Manager");
                        logger.info(time1+" Found null in DB Connection Manager");
                        agentLogger.info(time1+" Erro while getting DB Connection Manager");
                        agentLogger.info(time1+" Found null in DB Connection Manager");

                        MTTRTimeResponse mttrTimeResponse = new MTTRTimeResponse();
                        mttrTimeResponse.setErrorCode(1);
                        mttrTimeResponse.setErrorDetail("Sorry I could not get MTTR Time");
                        returnResponse = ConsumeWSDL.javaToJson(mttrTimeResponse);
                    }


                } catch (SQLException e) {

                    logger.error(time1+" Exception: "+e);
                    agentLogger.error(time1+" Exception: "+e);

                    MTTRTimeResponse mttrTimeResponse = new MTTRTimeResponse();
                    mttrTimeResponse.setErrorCode(5);
                    mttrTimeResponse.setErrorDetail("Sorry I could not get MTTR Time");
                    returnResponse = ConsumeWSDL.javaToJson(mttrTimeResponse);

                }finally {

                    if (resultSet != null) {
                        try {
                            resultSet.close();
                            logger.info(time1+" Result set closed....... " + null);
                            agentLogger.info(time1+" Result set closed....... " + null);
                        } catch (Exception e) {
                            logger.error(time1+" Exception found in resultSet closing: "+e);
                            agentLogger.error(time1+" Exception found in resultSet closing: "+e);
                        }

                    }
                    if (statement != null) {
                        try {
                            statement.close();
                            logger.info(time1+" statement  closed....... " + null);
                            agentLogger.info(time1+" statement  closed....... " + null);
                        } catch (Exception e) {
                            logger.error(time1+" Exception found in statement closing: ",e);
                            agentLogger.error(time1+" Exception found in statement closing: ",e);
                        }

                    }
                    connectionManager.closeConnection(dbConnection,PoolName,Logger.getLogger(agentId),time1);
                }


                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;



            } catch (Exception e) {

                logger.error(time1+" Exception: "+e);
                agentLogger.error(time1+" Exception: "+e);

                MTTRTimeResponse mttrTimeResponse = new MTTRTimeResponse();
                mttrTimeResponse.setErrorCode(5);
                mttrTimeResponse.setErrorDetail("Sorry I could not get MTTR Time");
                returnResponse = ConsumeWSDL.javaToJson(mttrTimeResponse);

                long time2 = System.currentTimeMillis();
                long executionTime = time2 - time1;
                logger.info(time1+" Total Execution time: "+executionTime+" ms");
                agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
                logger.info(time1+" exit with  "+returnResponse);
                agentLogger.info(time1+" exit with  "+returnResponse);
                agentLogger.info("<<============================================================================>>");
                return returnResponse;
            }
        }




    }

    public String fetchMTTRData(
            String urlSQLServer,String driverSQLServer,String PoolName,String loginTimeOut,
            String maxActive,String minIdle,String maxIdle,String agentId,long time1,
            Logger agentLogger,String Query){
        String number = null;
        String returnResponse = null;
        Statement statement = null;
        Connection dbConnection = null;
        DBConnectionManager connectionManager=null;
        MttrData mttrDataObject = null;
        ResultSet rs=null;

        try {
            logger.info(time1+" Going to get DB Connection Manager");
            agentLogger.info(time1+" Going to get DB Connection Manager");
            connectionManager = DBConnectionManager.getDBConnectionManager(urlSQLServer, driverSQLServer, PoolName,
                    Integer.parseInt(loginTimeOut), Logger.getLogger(OWController.class.getName()),
                    Integer.parseInt(maxActive), Integer.parseInt(minIdle), Integer.parseInt(maxIdle),Logger.getLogger(agentId),time1);
            if (connectionManager != null) {

                logger.info(time1+" Successfully get Connection Manager");
                agentLogger.info(time1+" Successfully get Connection Manager");
                logger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                agentLogger.info(time1+" Proceeding to get DB Connection from Connection Pool Name: "+PoolName);
                dbConnection=connectionManager.getConnection(PoolName,Logger.getLogger(agentId),time1);

                logger.info(time1+" Connection Build successfully");
                agentLogger.info(time1+" Connection Build successfully");
                statement = dbConnection.createStatement();

                logger.info(time1+" Going to execute query: ");
                agentLogger.info(time1+" Going to execute query");
                logger.info(time1+" Query: "+Query);
                agentLogger.info(time1+" Query: "+Query);
                // execute select SQL stetement
                rs = statement.executeQuery(Query);

                if(rs.equals(null))
                {
                    logger.info(time1+" MTTR Category not found against number:"+number);
                    agentLogger.info(time1+" MTTR Category not found against number:"+number);
                    MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                    mttrCategoryResponse.setErrorCode(1);
                    mttrCategoryResponse.setErrorDetail("MTTR Category not available against "+number);
                    returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);

                }else {
                    int i=0;
                    while (rs.next()) {
                        if (i==0) {
                            mttrDataObject = new MttrData();
                            mttrDataObject.setPrefix(rs.getString("Prefix"));
                            mttrDataObject.setRegion(rs.getString("Region"));
                            mttrDataObject.setEXCHANGE_ID(rs.getString("EXCHANGE_ID"));
                            mttrDataObject.setEXCHANGE_NM(rs.getString("EXCHANGE_NM"));
                            mttrDataObject.setZone(rs.getString("Zone"));
                            mttrDataObject.setEXCHANGE_NAME(rs.getString("ExchangeName"));

                        }else{
                            logger.info(time1+" Multiple MTTR Category records found against number:"+number);
                            logger.info(time1+" There should be only one record for MTTR Category against one number, need to check this issue");
                            agentLogger.info(time1+" Multiple MTTR Category records found against number:"+number);
                            agentLogger.info(time1+" There should be only one record for MTTR Category against one number, need to check this issue");
                        }
                    }

                    MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                    mttrCategoryResponse.setErrorCode(0);
                    mttrCategoryResponse.setErrorDetail("Successfully get MTTR Category");
                    mttrCategoryResponse.setMttrCategory(mttrDataObject);
                    returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);
                }
            }else{
                logger.info(time1+" Erro while getting DB Connection Manager");
                logger.info(time1+" Found null in DB Connection Manager");
                agentLogger.info(time1+" Erro while getting DB Connection Manager");
                agentLogger.info(time1+" Found null in DB Connection Manager");
                MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
                mttrCategoryResponse.setErrorCode(1);
                mttrCategoryResponse.setErrorDetail("Sorry I could not get MTTR Category");
                returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);
            }

        } catch (SQLException e) {

            logger.error(time1+" Exception: "+e);
            agentLogger.error(time1+" Exception: "+e);
            MTTRCategoryResponse mttrCategoryResponse = new MTTRCategoryResponse();
            mttrCategoryResponse.setErrorCode(5);
            mttrCategoryResponse.setErrorDetail("Sorry could not get MTTR Category");
            returnResponse = ConsumeWSDL.javaToJson(mttrCategoryResponse);

        }finally {
            if (rs != null) {
                try {
                    rs.close();
                    logger.info(time1+" Result set closed....... " + null);
                    agentLogger.info(time1+" Result set closed....... " + null);
                } catch (Exception e) {
                    logger.error(time1+" Exception found in resultSet closing: "+e);
                    agentLogger.error(time1+" Exception found in resultSet closing: "+e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                    logger.info(time1+" statement  closed....... " + null);
                    agentLogger.info(time1+" statement  closed....... " + null);
                } catch (Exception e) {
                    logger.error(time1+" Exception found in statement closing: ",e);
                    agentLogger.error(time1+" Exception found in statement closing: ",e);
                }
            }
            connectionManager.closeConnection(dbConnection,PoolName,Logger.getLogger(agentId),time1);
        }


        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }



  /*  @RequestMapping(value = "/SendEmail",method = RequestMethod.POST)
    public String sendEmail(
            @RequestParam(value = "EmailTo") String emailTo,
            @RequestParam(value = "BillLink") String link
    ){

        logger.error("OWController-sendEmail Called");
        logger.error("OWController-sendEmail Customer Email: "+emailTo+" and link: "+link);

        // READ CONFIGURATION FOR HOST,USER,PASS,TITLE
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");
        String smtpHostServer = resourceBundle.getString("SMTP_HOST_SERVER"); // SMTP HOST
        final String fromEmail = resourceBundle.getString("EXCHANGE_EMAIL"); //requires valid gmail id
        final String password = resourceBundle.getString("EXCHANGE_PASS"); // correct password for gmail id
        String fromTitle = resourceBundle.getString("EXCHANGE_TITLE");//This will show like where email come from, just like title
        String emailSubject = resourceBundle.getString("EMAIL_SUBJECT");
        String bodyText = resourceBundle.getString("EMAIL_BODY");

       *//* TWO TYPES OF AUTHENTICATION TLS AND SSL

      If the SMTP server is not running on default port (25), then you will
      also need to set mail.smtp.port property*//*

        logger.info("OWController-sendEmail SSL EMAIL Configuration...");
        Properties props = new Properties();

        props.put("mail.smtp.host", smtpHostServer);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");

        // WE NEED TO JUST SEND EAMIL SO NO NEED TO PROVIDE CREDENTIALS
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return super.getPasswordAuthentication();
            }
        };




       *//* Authenticator authenticator = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                //We were getting Email Authentication Failure, So using username
                //http://stackoverflow.com/questions/6610572/javax-mail-authenticationfailedexception-failed-to-connect-no-password-specifi
                return new PasswordAuthentication(fromEmail, password);
                //return new PasswordAuthentication(email, password);
            }
        };*//*

        // Get the default Session object.
        Session session = Session.getInstance(props,authenticator);
        logger.info("OWController-sendEmail Session created!");

            logger.info("OWController-sendEmail Going to send simple email, detail: Sesstion: "+session+"  To: "+emailTo+"  From: "
                    +fromEmail+"  Subject: "+emailSubject+"  Body: "+bodyText);

            String customBody = "<html>" +
                    "<body>" +
                    "<h3>"+bodyText+"</h3>" +
                    "<a href=" +link+">Bill link</a>" +
                    "</body>" +
                    "</html>";


            String response = sendSimpleEmail(session,emailTo,fromEmail,fromTitle, emailSubject,customBody);
            if (response.equals("sent")){
                logger.info("OWController-sendEmail Email sent Successfully to: "+emailTo);
            }else{
                logger.info("OWController-sendEmail Email not send to: "+emailTo);
            }

            return  response;

    }


    public String sendSimpleEmail(Session session, String toEmail,String fromEmail,String fromTitle, String subject, String body){
        String responseStatus = "no sent";
        try
        {

            logger.info("OWController-sendSimpleEmail session value is : "+session);
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, fromTitle));
            msg.setReplyTo(InternetAddress.parse(fromEmail));

            // msg.setReplyTo(InternetAddress.parse("m.irfanawan77@gmail.com", false));

            msg.setSubject(subject, "UTF-8");
            msg.setContent(body, "text/html");

            //msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            logger.info("OWController-sendSimpleEmail Message is ready to send: ");

            // logger.info("transport authentication");
           // Transport transport;

                //transport = session.getTransport("smtp");
              //  transport.connect("10.254.170.207", "care@ptcl.net.pk", "Rtgh@1234");
               // logger.info("its success");
               // transport.send(msg);
               // transport.close();

            //Authentication success

            Transport.send(msg);

            logger.info("OWController-sendSimpleEmail EMail Sent Successfully!!");
            responseStatus = "sent";
            return  responseStatus;
        } catch (Exception e) {
            logger.error("OWController-sendSimpleEmail Exception found at sendSimpleEmail method: "+e.toString());
            // e.printStackTrace();
            return responseStatus;
        }
    }

    @RequestMapping(value = "/SendTestingEmail",method = RequestMethod.POST)
    public String sendTestingEmail(
            @RequestParam(value = "EmailTo") String emailTo
    ){

        logger.info("OWController-sendTestingEmail called");
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "10.254.170.207");
            props.put("mail.smtp.socketFactory.port", "25");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "25");
           *//* Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return super.getPasswordAuthentication();
                }
            };*//*

            Authenticator authenticator = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                //We were getting Email Authentication Failure, So using username
                //http://stackoverflow.com/questions/6610572/javax-mail-authenticationfailedexception-failed-to-connect-no-password-specifi
                return new PasswordAuthentication("care@ptcl.net.pk", "Rtgh@1234");
                //return new PasswordAuthentication(email, password);
            }
        };
            Session session = Session.getDefaultInstance(props,authenticator);
           *//* Transport transport = session.getTransport("smtp");
            transport.connect("10.254.170.207", "care@ptcl.net.pk", "Rtgh@1234");
            logger.info("its success");
            transport.close();
*//*


            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("care@ptcl.net.pk", "PTCL Email title"));
            msg.setReplyTo(InternetAddress.parse("care@ptcl.net.pk"));
            msg.setSubject("PTCL Email Testing", "UTF-8");
            msg.setContent("Hello, this is testing email", "text/html");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false));
            logger.info("OWController-sendTestingEmail Message is ready to send: ");

            // logger.info("transport authentication");
            // Transport transport;

            //transport = session.getTransport("smtp");
            //  transport.connect("10.254.170.207", "care@ptcl.net.pk", "Rtgh@1234");
            // logger.info("its success");
            // transport.send(msg);
            // transport.close();

            //Authentication success

            Transport.send(msg);

            logger.info("OWController-sendTestingEmail EMail Sent Successfully!!");
            return  "sent";




        } catch (NoSuchProviderException var9) {
            logger.info("  sendTestingEmailNoSuchProviderException " + var9.getMessage());
            var9.printStackTrace();
            return "exception";
        } catch (MessagingException var10) {
            logger.info("sendTestingEmail  MessagingException " + var10.getMessage());
            var10.printStackTrace();
            return "exception";
        } catch (UnsupportedEncodingException e) {
            logger.info("sendTestingEmail  MessagingException " + e.getMessage());
            e.printStackTrace();
            return "exception";
        }
    }*/

    @RequestMapping(value = "/SendEmail",method = RequestMethod.POST)
    public String sendEmail(
            @RequestParam(value = "EmailTo") String emailTo,
            @RequestParam(value = "EmailSubject") String emailSubject,
            @RequestParam(value = "EmailBody") String emailBody,
            @RequestParam(value = "AgentId") String agentId

    ){
        long time1 = System.currentTimeMillis();

        String response = "not sent";

        logger.info(time1+" SendEmail called with EmailTo:"+emailTo+" EmailSubject:"+emailSubject+" EmailBody:"+emailBody+" AgentId:"+agentId);

        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info("<<============================================================================>>");
        agentLogger.info(time1+" SendEmail called with EmailTo:"+emailTo+" EmailSubject:"+emailSubject+" EmailBody:"+emailBody+" AgentId:"+agentId);



        // Recipient's email ID needs to be mentioned.
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");

        // Sender's email ID needs to be mentioned
        String from = resourceBundle.getString("EXCHANGE_EMAIL");

        // Exchange server needs to be mentioned
        String host = resourceBundle.getString("SMTP_HOST_SERVER");

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(emailTo));

            // Set Subject: header field
            message.setSubject(emailSubject);

            // Now set the actual message
            message.setText(emailBody);

            logger.info(time1+" Message is ready to send");
            agentLogger.info(time1+" Message is ready to send");
            logger.info(time1+" Going to send email, detail: Host:"+host+" From:"+from+" To:"+emailTo+" Subject:"+emailSubject+" Body:"+emailBody);
            agentLogger.info(time1+" Going to send email, detail: Host:"+host+" From:"+from+" To:"+emailTo+" Subject:"+emailSubject+" Body:"+emailBody);


            // Send message
            Transport.send(message);
            logger.info(time1+" Email sent successfully to:"+emailTo);
            agentLogger.info(time1+" Email sent successfully to:"+emailTo);

            response = "sent";
            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+response);
            agentLogger.info(time1+" exit with  "+response);
            agentLogger.info("<<============================================================================>>");
            return response;

        }catch (MessagingException mex) {

            logger.info(time1+" MessagingException while sending email to:"+emailTo);
            agentLogger.info(time1+" MessagingException while sending email to:"+emailTo);
            logger.info(time1+" Exception: "+mex);
            agentLogger.info(time1+" Exception: "+mex);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+response);
            agentLogger.info(time1+" exit with  "+response);
            agentLogger.info("<<============================================================================>>");
            return response;

        }catch (Exception e){

            logger.info(time1+" MessagingException while sending email to:"+emailTo);
            agentLogger.info(time1+" MessagingException while sending email to:"+emailTo);
            logger.info(time1+" Exception: "+e);
            agentLogger.info(time1+" Exception: "+e);

            long time2 = System.currentTimeMillis();
            long executionTime = time2 - time1;
            logger.info(time1+" Total Execution time: "+executionTime+" ms");
            agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
            logger.info(time1+" exit with  "+response);
            agentLogger.info(time1+" exit with  "+response);
            agentLogger.info("<<============================================================================>>");
            return response;
        }
    }










  /*  @RequestMapping(
            value = {"/sendEmail"},
            method = {RequestMethod.POST}
    )
    public String sendEmail(@RequestParam("to") String emailTo, @RequestParam("bodyText") String bodyText, @RequestParam(value = "subject",defaultValue = "One Window Email Subject",required = false) String emailSubject, @RequestParam(value = "attachment",required = false) MultipartFile file) {
        try {
            logger.debug("sendEmail method called");
            logger.info("Data in File: " + file);
            String fileName = null;
            String fileExtension = null;
            long time_1 = System.currentTimeMillis();
            if (emailTo != null) {
                ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");
                String smtpHostServer = resourceBundle.getString("SMTP_HOST_SERVER");
                String fromEmail = resourceBundle.getString("EXCHANGE_EMAIL");
                String password = resourceBundle.getString("EXCHANGE_PASS");
                String fromTitle = resourceBundle.getString("EXCHANGE_TITLE");
                logger.info("SSL EMAIL Configuration...");
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "25");
                Authenticator authenticator = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return super.getPasswordAuthentication();
                    }
                };
                Session session = Session.getInstance(props, authenticator);
                logger.info("Session created!");
                String Result = null;
                String response;
                if (file == null) {
                    logger.info("No attachment found for this email");
                    logger.info("Going to send simple email, detail: Sesstion: " + session + "  To: " + emailTo + "  From: " + fromEmail + "  Subject: " + emailSubject + "  Body: " + bodyText);
                    response = this.sendSimpleEmail(session, "muhammad.irfan@expertflow.com", "m.irfanawan77@gmail.com", fromTitle, emailSubject, bodyText);
                    Result = null;
                    if (response.equals("sent")) {
                        Result = "Email has been sent Successfully to: " + emailTo;
                    } else {
                        Result = "Email not sent to: " + emailTo;
                    }
                } else {
                    fileName = file.getOriginalFilename().split("\\.")[0];
                    fileExtension = file.getOriginalFilename().split("\\.")[1];
                    logger.info("File name is: " + fileName + "  File extension :  " + fileExtension);
                    logger.info("Attachment found for this email");
                    logger.info("Going to send email with attachment, detail: Sesstion: " + session + "  To: " + emailTo + "  From: " + fromEmail + "  Subject: " + emailSubject + "  Body: " + bodyText + "  Attachment: " + fileName + "." + fileExtension);
                    response = this.sendAttachmentEmail(session, emailTo, fromEmail, fromTitle, emailSubject, bodyText, file, fileName, fileExtension);
                    this.deleteAttachmentFromServer(fileName, fileExtension);
                    if (response.equals("sent")) {
                        Result = "Email has been sent Successfully to: " + emailTo;
                    } else {
                        Result = "Email not sent to: " + emailTo;
                    }
                }

                ObjectMapper mapper = new ObjectMapper();
                String ResultInJason = mapper.writeValueAsString(Result);
                logger.debug("Returning response: " + ResultInJason);
                long time_2 = System.currentTimeMillis();
                long time = time_2 - time_1;
                logger.debug("Total Execution Time is: " + time);
                return ResultInJason;
            } else {
                logger.error("ERROR: Email address for recipient not found");
                return "ERROR:  Email address for recipient not found";
            }
        } catch (Exception var24) {
            logger.error("Error occurred while sending email:", var24);
            return "Error occured while sending email: " + var24.getMessage();
        }
    }
*/

    public String checkObjectType(Object object){

        if (object instanceof JSONArray)
        {
            return "JSONArray";
        }else if (object instanceof JSONObject){
            return "JSONObject";
        }else if (object instanceof Integer){
            return "Integer";
        }else if (object instanceof Long){
            return "Long";
        }else if (object instanceof String){
            return "String";
        }else  {
            return "Not JSON";
        }
    }

    public String removeSpecialCharacter(String your_string){
        StringBuilder actualString = new StringBuilder(your_string);
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        Matcher m = regex.matcher(actualString);
        while (m.find()) {
            actualString.setCharAt(m.start(),' ');
        }
        return String.valueOf(actualString);
    }

}
