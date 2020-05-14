package com.ef.clients.ptcl.UpgradedOneWindow.CRM.WSDLConsumer;


import com.ef.clients.ptcl.UpgradedOneWindow.CRM.RequestModels.ByPassSSL;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.ws.WebServiceException;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class ConsumeWSDL {

    /**
     * Dummy class implementing X509TrustManager to trust all certificates
     */
    private static class TrustAllCertificates implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * Dummy class implementing HostnameVerifier to trust all host names
     */
    private static class TrustAllHosts implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static SOAPMessage sendSoapRequest(String endpointUrl, String send) {





        try {


           // SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
           // SOAPConnection connection = sfc.createConnection();
            InputStream is = new ByteArrayInputStream(send.getBytes());
            SOAPMessage request = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(new MimeHeaders(), is);
            /*SOAPMessage request = MessageFactory.newInstance().createMessage(null, is);
            request.removeAllAttachments();

            SOAPPart part = request.getSOAPPart();
            part.detachNode();
            SOAPEnvelope env = part.getEnvelope();
            env.detachNode();
            SOAPBody body = env.getBody();
            body.detachNode();
            SOAPHeader head = env.getHeader();
            head.detachNode();

            request.writeTo(System.out);*/
            request.writeTo(System.out);



            final boolean isHttps = endpointUrl.toLowerCase().startsWith("https");
            HttpsURLConnection httpsConnection = null;
            // Open HTTPS connection
            if (isHttps) {
                // Create SSL context and trust all certificates
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] trustAll
                        = new TrustManager[] {new TrustAllCertificates()};
                sslContext.init(null, trustAll, new java.security.SecureRandom());
                // Set trust all certificates context to HttpsURLConnection
                HttpsURLConnection
                        .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                // Open HTTPS connection
                URL url = new URL(endpointUrl);
                httpsConnection = (HttpsURLConnection) url.openConnection();
                // Trust all hosts
                httpsConnection.setHostnameVerifier(new TrustAllHosts());
                httpsConnection.setRequestProperty("SOAPAction", "http://siebel.com/CustomUI");
                // Connect
                httpsConnection.connect();
            }
            // Send HTTP SOAP request and get response
            SOAPConnection soapConnection
                    = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage response = soapConnection.call(request, endpointUrl);

            response.writeTo(System.out);
            //System.out.println(response.getContentDescription());
            // Close connection
            soapConnection.close();
            // Close HTTPS connection
            if (isHttps) {
                httpsConnection.disconnect();
            }
            return response;
        } catch (SOAPException | IOException
                | NoSuchAlgorithmException | KeyManagementException ex) {
            // Do Something
        }
        return null;
    }





















    public static String consumer2(String wsURL, String xmlInput, String SOAPAction)throws MalformedURLException,
            IOException,NoSuchAlgorithmException,KeyManagementException{




            final boolean isHttps = wsURL.toLowerCase().startsWith("https");
            HttpsURLConnection httpsConnection = null;
            // Open HTTPS connection
            if (isHttps) {

                // Create SSL context

                // and trust all certificates
                SSLContext sslContext = null;
                    sslContext = SSLContext.getInstance("SSL");

                TrustManager[] trustAll
                        = new TrustManager[] {new TrustAllCertificates()};
                sslContext.init(null, trustAll, new java.security.SecureRandom());
                // Set trust all certificates context to HttpsURLConnection
                HttpsURLConnection
                        .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                // Open HTTPS connection
                URL url = new URL(wsURL);
                httpsConnection = (HttpsURLConnection) url.openConnection();
                // Trust all hosts
                httpsConnection.setHostnameVerifier(new TrustAllHosts());
                httpsConnection.setRequestMethod("GET");
                httpsConnection.setRequestProperty("SOAPAction", SOAPAction);
                httpsConnection.setDoOutput(true);

                // Connect
               // httpsConnection.connect();
            }

            String responseString = "";
            String outputString = "";



            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
            bout.write(buffer);
            byte[] b = bout.toByteArray();

        OutputStream out = httpsConnection.getOutputStream();
         out.write(b);
         out.close();



       // httpsConnection.setDoInput(true);
        BufferedReader br;
        StringBuilder sb;

        if (200 <= httpsConnection.getResponseCode() && httpsConnection.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(httpsConnection.getErrorStream()));
        }


       // br = new BufferedReader(new InputStreamReader((httpsConnection.getInputStream())));
        sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        System.out.println( sb.toString() );

        //OutputStream out = httpsConnection.getOutputStream();
//Write the content of the request to the outputstream of the HTTP Connection.
           // out.write(b);
           // out.close();
//Ready with sending the request.

//Read the response.
       /* if(httpsConnection.getResponseCode() == 200){
            System.out.println("Response code is 200");
        }else{
            System.out.println("Response code is "+httpsConnection.getResponseCode());
             System.out.println(httpsConnection.getErrorStream());
        }
            InputStreamReader isr =
                    new InputStreamReader(httpsConnection.getInputStream());
            BufferedReader in = new BufferedReader(isr);*/

//Write the SOAP message response to a String.
           /* while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }*/
            // System.out.println("response: "+outputString);
            //Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
            // Document document = parseXmlFile(outputString);
            //String formattedSOAPResponse = formatXML(outputString);
            // System.out.println(formattedSOAPResponse);
            return outputString;
           /* // Send HTTP SOAP request and get response
            SOAPConnection soapConnection
                    = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage response = soapConnection.call((SOAPMessage) xmlInput, wsURL);
            // Close connection
            soapConnection.close();
            // Close HTTPS connection
            if (isHttps) {
                httpsConnection.disconnect();
            }
            return response;*/



    }
















    public static String consumer(String wsURL, String xmlInput, String SOAPAction) throws MalformedURLException,
            IOException{

        //System.out.println("wsURL:"+wsURL);
       // System.out.println("SOAP Action:"+SOAPAction);
        //Code to make a webservice HTTP request
        String responseString = "";
        String outputString = "";

        URL url = new URL(wsURL);
        URLConnection connection = url.openConnection();
        ByPassSSL.disableSslVerification();

        HttpURLConnection httpConn = (HttpURLConnection)connection;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
       // System.out.println("after out stream");

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
        String formattedSOAPResponse = formatXML(outputString);
       // System.out.println(formattedSOAPResponse);
        return outputString;

    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //format the XML in your String
    public static String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public static String javaToJson(Object object){

        Gson gson = new Gson();
          return gson.toJson(object);


    }

    public static JSONObject quickParse(Object obj) throws IllegalArgumentException, IllegalAccessException, JSONException {
        JSONObject object = new JSONObject();

        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getDeclaredAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof SerializedName){
                    SerializedName myAnnotation = (SerializedName) annotation;
                    String name = myAnnotation.value();
                    Object value = field.get(obj);

                    if(value == null)
                        value = new String("NaN");

                    object.put(name, value);
                }
            }
        }

        return object;
    }



}