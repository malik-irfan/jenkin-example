package com.ef.clients.ptcl.UpgradedOneWindow.CRM.dbcp;

import org.apache.log4j.Logger;

import java.sql.DriverManager;

/**
 * Created by Zain on 16-Feb-18.
 */

public class DBPoolCreator {

    String poolName;
    Logger logger;

    public void startUp( String connectionUrl,String driver,String poolName,int loginTimeout,Logger logger,int maxActive,int minIdle,int maxIdle,Logger loggerAgent,long time1) {

        logger.info(time1+" Going to Create Database Pool.");
        loggerAgent.info(time1+" Going to Create Database Pool.");
        this.poolName = poolName;
        this.logger = logger;

        long endTime;
        long startTime;

        try {
            startTime = System.currentTimeMillis();

           // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName(driver);
            DriverManager.setLoginTimeout(loginTimeout);

            DBManager.setupDriver(connectionUrl,poolName,logger,maxActive,minIdle,maxIdle,loggerAgent,time1);

            endTime = System.currentTimeMillis();
            logger.info(time1+" Driver Initialization and DB Pool Setup took " + (endTime - startTime) / 1000 + " secs.");
            loggerAgent.info(time1+" Driver Initialization and DB Pool Setup took " + (endTime - startTime) / 1000 + " secs.");

        } catch (ClassNotFoundException e) {
            logger.error(time1+" Class Not Found Exception Occurred While Setting UP Database Pool: ", e);
            loggerAgent.error(time1+" Class Not Found Exception Occurred While Setting UP Database Pool: ", e);
        } catch (Exception ex) {
            logger.error(time1+" Exception Occurred While Setting UP Database Pool: ", ex);
            loggerAgent.error(time1+" Exception Occurred While Setting UP Database Pool: ", ex);
        }

        logger.info(time1+" Database Pool Creation Completed.");
        loggerAgent.info(time1+" Database Pool Creation Completed.");
    }

    public void statusReport() {
        logger.info("Starting function statusReport()");

        try {
            DBManager.printDriverStats(poolName,logger);
        } catch (Exception e) {
            logger.error("Caught exception: ", e);
        }

        logger.info("Finishing function statusReport()");
    }

    public void shutDown() {
        logger.info("Starting function shutDown()");

        try {
            DBManager.shutdownDriver(poolName,logger);
        } catch (Exception e) {
            logger.error("Caught exception: ", e);
        }

        logger.info("Finishing function shutDown()");
    }
}