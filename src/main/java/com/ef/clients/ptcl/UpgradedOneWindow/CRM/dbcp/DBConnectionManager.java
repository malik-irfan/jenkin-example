package com.ef.clients.ptcl.UpgradedOneWindow.CRM.dbcp;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Zain on 16-Feb-18.
 */


public class DBConnectionManager {

    private static DBConnectionManager dbConnManagerOneWindow = null;
    private static DBConnectionManager dbConnManagerMTTR = null;
    String connectionUrl,connectionDriver,connectionPoolName;
    Logger logger;


    public static synchronized DBConnectionManager getDBConnectionManager(String connectionUrl,String connectionDriver,String poolName, int loginTimeOut,Logger logger,int maxActive,int minIdle,int maxIdle,Logger loggerAgent,long time1) {


        logger.info(time1+" Going to Initialize DBConnectionManager's Singleton Instance");
        loggerAgent.info(time1+" Going to Initialize DBConnectionManager's Singleton Instance");
        switch(poolName) {
            case "One-Window":{
                if (dbConnManagerOneWindow == null) {
                    logger.info(time1+" Database ConnectionManager's Singleton Instance NOT Found. Going to Create New Instance.");
                    loggerAgent.info(time1+" Database ConnectionManager's Singleton Instance NOT Found. Going to Create New Instance.");
                    dbConnManagerOneWindow = new DBConnectionManager(connectionUrl,connectionDriver,poolName,loginTimeOut,logger,maxActive,minIdle,maxIdle,loggerAgent,time1);
                    return dbConnManagerOneWindow;
                }
                return dbConnManagerOneWindow;

            }
            case "MTTR": {
                if (dbConnManagerMTTR == null) {
                    logger.info(time1+" Database ConnectionManager's Singleton Instance NOT Found. Going to Create New Instance.");
                    loggerAgent.info(time1+" Database ConnectionManager's Singleton Instance NOT Found. Going to Create New Instance.");
                    dbConnManagerMTTR = new DBConnectionManager(connectionUrl,connectionDriver,poolName,loginTimeOut,logger,maxActive,minIdle,maxIdle,loggerAgent,time1);
                    return dbConnManagerMTTR;
                }
                return dbConnManagerMTTR;

            }

        }


        logger.info(time1+" DBConnectionManager's Singleton Instance Initialization Completed.");
        loggerAgent.info(time1+" DBConnectionManager's Singleton Instance Initialization Completed.");
        return null;
    }

    private DBConnectionManager(String connectionUrl,String connectionDriver,String poolName, int loginTimeOut,Logger logger,int maxActive,int minIdle,int maxIdle,Logger loggerAgent,long time1) {
        this.connectionDriver = connectionDriver;
        this.connectionUrl = connectionUrl;
        this.connectionPoolName = poolName;
        this.logger = logger;
        DBPoolCreator pool = new DBPoolCreator();
        pool.startUp(connectionUrl,connectionDriver,poolName,loginTimeOut,logger,maxActive,minIdle,maxIdle,loggerAgent,time1);
    }

    public Connection getConnection(String connectionPoolName,Logger loggerAgent,long time1) throws SQLException {

        logger.info(time1+" Going to Get Connection from Database Connection Manager.");
        loggerAgent.info(time1+" Going to Get Connection from Database Connection Manager.");

        long endTime;
        long startTime;
        startTime = System.currentTimeMillis();

        //logger.info("Getting Connection from SMSBack-end Connection Pool ...");
        Connection connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:"+connectionPoolName);

        endTime = System.currentTimeMillis();
        logger.info(time1+" Connection obtained from "+connectionPoolName+" Pool ...");
        loggerAgent.info(time1+" Connection obtained from "+connectionPoolName+" Pool ...");
        logger.info(time1+" Conneciton Acquired in " + (endTime - startTime) / 1000 + " secs.");
        loggerAgent.info(time1+" Conneciton Acquired in " + (endTime - startTime) / 1000 + " secs.");
        logger.info(time1+" Driver status after getting connection:"+DBManager.getDriverStatus(connectionPoolName,logger));
        loggerAgent.info(time1+" Driver status after getting connection:"+DBManager.getDriverStatus(connectionPoolName,logger));
      return connection;
    }

    public void closeConnection(Connection conn,String function,Logger loggerAgent,long time1) {

        if (conn != null) {
            try {
               // PoolableConnection.close();
                logger.info(time1+" Going to close connection in method "+function);
                loggerAgent.info(time1+" Going to close connection in method "+function);
                conn.close();
                logger.info(time1+" Driver status after closing connection in "+function+" method.." + DBManager.getDriverStatus(connectionPoolName,logger));
                loggerAgent.info(time1+" Driver status after closing connection in "+function+" method.." + DBManager.getDriverStatus(connectionPoolName,logger));
            } catch (SQLException ex) {
                logger.error(time1+" Error closing connection in "+function+" method: " + ex.getMessage());
                loggerAgent.error(time1+" Error closing connection in "+function+" method: " + ex.getMessage());
            }
            conn = null;
        }
    }

}