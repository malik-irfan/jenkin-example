package com.ef.clients.ptcl.UpgradedOneWindow.PRISM.RestConsumer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConsumerREST {

    private static final Logger logger = LogManager.getLogger(ConsumerREST.class);

    public static String prismConsumerByPost(String targetURL, String input, Logger agentLogger, long time1){

        String responseString = "";
        String outputString = "";

        HttpURLConnection connection = null;
        try {


            byte[] postData       = input.getBytes( StandardCharsets.UTF_8 );

            // instantiate the URL object with the target URL of the resource to
            // request
            URL url = new URL(targetURL);

            // instantiate the HttpURLConnection with the URL object - A new
            // connection is opened every time by calling the openConnection
            // method of the protocol handler for this URL.
            // ------  This is the point where the connection is opened.

            connection = (HttpURLConnection) url
                    .openConnection();
            // set connection output to true
            connection.setDoOutput(true);
            // instead of a GET, we're going to send using method="POST"
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type", "application/json");

            // instantiate OutputStreamWriter using the output stream, returned
            // from getOutputStream, that writes to this connection.
            // ------- This is the point where you'll know if the connection was
            // successfully established. If an I/O error occurs while creating
            // the output stream, you'll see an IOException.
            OutputStream os = connection.getOutputStream();
            //OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            // write data to the connection. This is data that you are sending
            // to the server
            // ------ Sending the data is conducted here. We established the
            // connection with getOutputStream

            os.write(postData);
            //os.write("process=LIST_CATEGORY");





            // Closes this output stream and releases any system resources
            // associated with this stream. At this point, we've sent all the
            // data. Only the outputStream is closed at this point, not the
            // actual connection

            os.flush();
            //writer.close();

            // if there is a response code AND that response code is 200 OK, do
            // stuff in the first if block
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // OK
                logger.info(time1+" PRISM response code:"+200);
                agentLogger.info(time1+" PRISM response code:"+200);

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (connection.getInputStream())));

                while ((responseString = br.readLine()) != null) {
                    outputString = outputString + responseString;
                }

                // otherwise, if any other status code is returned, or no status
                // code is returned, do stuff in the else block
            } else {
                // Server returned HTTP error code.
                logger.info(time1+" Failed : HTTP error code:"+connection.getResponseCode());
                agentLogger.info(time1+" Failed : HTTP error code:"+connection.getResponseCode());
                //throw new RuntimeException("Failed : HTTP error code : "+ connection.getResponseCode());
            }

            connection.disconnect();


        }catch(MalformedURLException e){
            logger.info(time1+" Exception: "+e);
            agentLogger.info(time1+" Exception: "+e);

            //...
        }catch(IOException e){
            logger.info(time1+" Exception: "+e);
            agentLogger.info(time1+" Exception: "+e);

            //...
        }catch (RuntimeException e){
            logger.info(time1+" Exception: "+e);
            agentLogger.info(time1+" Exception: "+e);
        }finally {
            logger.info(time1+" finally: Going to close connection resource");
            agentLogger.info(time1+" finally: Going to close connection resource");

            if (connection != null){
                try{
                    connection.disconnect();
                    logger.info(time1+" Connection closed successfully");
                    agentLogger.info(time1+" Connection closed successfully");
                }catch (Exception e){
                    logger.info(time1+" Exception while trying to close Connection:"+e);
                    agentLogger.info(time1+" Exception while trying to close Connection:"+e);

                }
                connection = null;
            }else{
                logger.info(time1+" Connection already closed");
                agentLogger.info(time1+" Connection already closed");
            }
        }

        logger.info(time1+" returning response: "+outputString);
        agentLogger.info(time1+" returning response: "+outputString);
        return outputString;
    }


}
