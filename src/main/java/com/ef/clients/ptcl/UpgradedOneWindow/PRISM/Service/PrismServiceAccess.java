package com.ef.clients.ptcl.UpgradedOneWindow.PRISM.Service;


import com.ef.clients.ptcl.UpgradedOneWindow.PRISM.RestConsumer.ConsumerREST;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class PrismServiceAccess {

    private static final Logger logger = LogManager.getLogger(PrismServiceAccess.class);

    public String getCategoryList(Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_CATEGORY_LIST");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password;

        logger.info(time1+ " Going to request PRISM for Category list with parameters:  "+parameters);
        agentLogger.info(time1+ " Going to request PRISM for Category list with parameters:  "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }

    public String getSubCategoryList(String categoryId,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_SUB_CATEGORY_LIST");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password+"&category_id="+categoryId;

        logger.info(time1+ " Going to request PRISM for Sub Category list with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM for Sub Category list with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }


    public String getMessageTitleList(String subCategoryId,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_MESSAGE_TITLE_LISTING");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password+"&subcategory_id="+subCategoryId;

        logger.info(time1+ " Going to request PRISM for Message Title list with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM for Message Title list with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }

    public String getMessageList(String messageId,String languageType,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_MESSAGE_LISTING");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password+"&message_id="+messageId+"&language_type="+languageType;

        logger.info(time1+ " Going to request PRISM for Message list with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM for Message list with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }

    public String sendMessagesToCustomer(String messageId,String languageType,String mobile,String agentName,String agentId,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_SENDING_MESSAGES_TO_CUSTOMER");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password+"&message_id="+messageId
                +"&language_type="+languageType+"&msisdn="+mobile+"&agent_name="+agentName+"&agent_id="+agentId;

        logger.info(time1+ " Going to request PRISM Send Messages to Customer with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM Send Messages to Customer with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }


    public String searchMessage(String messageTitle,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_SEARCH_MESSAGE");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password+"&message_title="+messageTitle;

        logger.info(time1+ " Going to request PRISM to Search Message with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM to Search Message with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }

    public String blockUnBlockNumber(String pstn,String mobile,String email,String reason,
                                     String action,String agentName,String agentId,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_BLOCK_UNBLOCK_NUMBER");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String department = bundle.getString("DEPARTMENT");
        String tag = bundle.getString("TAGS");
        String parameters  = "process="+process+"&username="+userName+"&password="+password
                +"&pstn="+pstn+"&Msisdn="+mobile+"&email="+email+"&department="+department
                +"&tags="+tag+"&reason="+reason+"&action="+action+"&agent_id="+agentId+"&agent_name="+agentName;

        logger.info(time1+ " Going to request PRISM to Block/UnBlock Number with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM to Block/UnBlock Number with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }

    public String searchDNCRStatus(String mobile,Logger agentLogger,long time1){

        ResourceBundle bundle = ResourceBundle.getBundle("Prism");
        String userName = bundle.getString("PRISM_USERNAME");
        String password = bundle.getString("PRISM_PASSWORD");
        String process = bundle.getString("PROCESS_DNCR_STATUS_BLOCK_UNBLOCK");
        String endPoint = bundle.getString("PRISM_SERVICES_END_POINT");
        String parameters  = "process="+process+"&username="+userName+"&password="+password+"&msisdn="+mobile;

        logger.info(time1+ " Going to request PRISM to Search DNCR Status with parameters: "+parameters);
        agentLogger.info(time1+ " Going to request PRISM to Search DNCR Status with parameters: "+parameters);

        String PRISMResponse = ConsumerREST.prismConsumerByPost(endPoint,parameters,agentLogger,time1);


        if ( !PRISMResponse.equals("")){

            logger.info(time1+" PRISM response: "+PRISMResponse);
            agentLogger.info(time1+" PRISM response: "+PRISMResponse);
            return PRISMResponse;

        }else{

            logger.info(time1+" PRISM response: Empty");
            agentLogger.info(time1+" PRISM response: Empty");

            String customResponse;
            JSONObject json = new JSONObject();
            JSONArray array = new JSONArray();
            json.put("rescode", "5");
            json.put("message","PRISM Down");
            json.put("data",array);
            customResponse = json.toString();
            return customResponse;
        }

    }




}
