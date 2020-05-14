package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.SMS;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;

public class SMS {
    private static final Logger logger = LogManager.getLogger(SMS.class);

    public static String SEND_REBATE_SMS(String url, JSONObject paramsObj, Logger agentLogger, long time){
        String response = "";

        logger.debug(time+" Entered into SEND_REBATE_SMS method().");
        agentLogger.debug(time+" Entered into SEND_REBATE_SMS method().");

        HttpURLConnection httpClient = null;
        try{

            logger.debug(time+" Connecting to PRISM Rebate SMS...");
            agentLogger.debug(time+" Connecting to PRISM Rebate SMS...");

            httpClient = (HttpURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("POST");
            httpClient.setReadTimeout(10*1000);
            httpClient.setDoOutput(true);
            httpClient.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(httpClient.getOutputStream());
            wr.write(paramsObj.toString());
            wr.flush();

            int HttpResult = httpClient.getResponseCode();
            logger.debug(time+" PRISM Rebate SMS Status: "+HttpResult);
            agentLogger.debug(time+" PRISM Rebate SMS Status: "+HttpResult);
            BufferedReader br = new BufferedReader(new InputStreamReader((httpClient.getInputStream())));

            String responseString;
            while ((responseString = br.readLine()) != null) {
                response = response + responseString;
            }


        }catch (MissingResourceException e){
            logger.error(time+" Exception occurred into SEND_REBATE_SMS method(): "+e.getMessage());
            agentLogger.error(time+" Exception occurred into SEND_REBATE_SMS method(): "+e.getMessage());
        }catch (MalformedURLException e) {
            logger.error(time+" Exception occurred into SEND_REBATE_SMS method(): "+e.getMessage());
            agentLogger.error(time+" Exception occurred into SEND_REBATE_SMS method(): "+e.getMessage());
        } catch (IOException e) {
            logger.error(time+" Exception occurred into SEND_REBATE_SMS method(): "+e.getMessage());
            agentLogger.error(time+" Exception occurred into SEND_REBATE_SMS method(): "+e.getMessage());
        }finally {
            logger.debug(time+" Closing resources...");
            agentLogger.debug(time+" Closing resources...");
            if (httpClient != null){
                try{
                    httpClient.disconnect();
                    logger.debug(time+" Connection closed successfully!");
                    agentLogger.debug(time+" Connection closed successfully!");
                }catch (Exception e){
                    logger.error(time+" While closing connection: "+e.getMessage());
                    agentLogger.error(time+" While closing connection: "+e.getMessage());
                }
                httpClient = null;
            }else{
                logger.debug(time+" Connection already closed!");
                agentLogger.debug(time+" Connection already closed!");
            }
        }
        logger.debug(time+" Exit from SEND_REBATE_SMS method().");
        agentLogger.debug(time+" Exit from SEND_REBATE_SMS method().");
        return response;

    }
}
