package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.WSDLConsume;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WSDLConsumer {


    public static String consumer(String wsURL, String xmlInput, String SOAPAction) throws MalformedURLException,
            IOException {

        String responseString = "";
        String outputString = "";

        URL url = new URL(wsURL);
        URLConnection connection = url.openConnection();
        //ByPassSSL.disableSslVerification();

        HttpURLConnection httpConn = (HttpURLConnection)connection;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();


        byte[] buffer = new byte[xmlInput.length()];
        buffer = xmlInput.getBytes();
        bout.write(buffer);
        byte[] b = bout.toByteArray();

        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        if (SOAPAction.equals("")){
            //System.out.println("action is empty");
        }else{

            httpConn.setRequestProperty("SOAPAction", SOAPAction);
        }

        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);



        OutputStream out = httpConn.getOutputStream();
        //Write the content of the request to the outputstream of the HTTP Connection.
        out.write(b);
        out.close();
        //Ready with sending the request.

        //Read the response.
        InputStreamReader isr =
                new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        //Write the SOAP message response to a String.
        while ((responseString = in.readLine()) != null) {
            outputString = outputString + responseString;
        }
        // System.out.println("response: "+outputString);
        //Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
        // Document document = parseXmlFile(outputString);
        //String formattedSOAPResponse = formatXML(outputString);
        // System.out.println(formattedSOAPResponse);
        return outputString;

    }


    public static String javaToJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    //return json object . Remove soapenv:Envelope -> soapenv:Body  -> then required object
    public static JSONObject getJson(String xmlInput){
        // Convert string to Json
        JSONObject SoapBody = null;
        JSONObject soapDatainJsonObject = XML.toJSONObject(xmlInput);
        if(soapDatainJsonObject.has("soapenv:Envelope")){
            JSONObject SoapEnvelope = soapDatainJsonObject.getJSONObject("soapenv:Envelope");
            SoapBody =  SoapEnvelope.getJSONObject("soapenv:Body");
        }else if (soapDatainJsonObject.has("soap:Envelope")){
            JSONObject SoapEnvelope = soapDatainJsonObject.getJSONObject("soap:Envelope");
            SoapBody = SoapEnvelope.getJSONObject("soap:Body");
        }else if(soapDatainJsonObject.has("SOAP-ENV:Envelope")){
            JSONObject SoapEnvelope = soapDatainJsonObject.getJSONObject("SOAP-ENV:Envelope");
            SoapBody =  SoapEnvelope.getJSONObject("SOAP-ENV:Body");
        }

        return SoapBody;
    }
}
