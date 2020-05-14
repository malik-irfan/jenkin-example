package com.ef.clients.ptcl.UpgradedOneWindow.CRM.dbcp;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by Zain on 16-Feb-18.
 */
public class DBManager {

    public static void setupDriver(String connectURI,String poolName,Logger logger,int maxActive,int minIdle,int maxIdle,Logger loggerAgent,long time1) throws Exception {

        logger.debug(time1+" Going to Set up Database Driver at URI: " + connectURI);
        loggerAgent.debug(time1+" Going to Set up Database Driver at URI: " + connectURI);

        GenericObjectPool genericObjPool = new GenericObjectPool(null, 10,
                GenericObjectPool.WHEN_EXHAUSTED_BLOCK,
                3 * 1000);

        genericObjPool.setMaxActive(maxActive);
        genericObjPool.setMinIdle(minIdle);
        genericObjPool.setMaxIdle(maxIdle);
        genericObjPool.setNumTestsPerEvictionRun(5);
       // genericObjPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
        genericObjPool.setTimeBetweenEvictionRunsMillis(10 * 1000 * 60);

        ObjectPool connectionPool = genericObjPool;

        logger.info(time1+" Going to Initializing Connection Factory");
        loggerAgent.info(time1+" Going to Initializing Connection Factory");
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

        logger.info(time1+" Going to Load Pooling Driver Calss.");
        loggerAgent.info(time1+" Going to Load Pooling Driver Calss.");
        Class.forName("org.apache.commons.dbcp.PoolingDriver");

        logger.info(time1+" Going to Set Up Connection Pool "+poolName);
        loggerAgent.info(time1+" Going to Set Up Connection Pool "+poolName);
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.registerPool(poolName, connectionPool);

        logger.info(time1+" DB Driver and Connection Pool SMSBack-end has been setup");
        loggerAgent.info(time1+" DB Driver and Connection Pool SMSBack-end has been setup");
    }

    public static void printDriverStats(String poolName,Logger logger) throws Exception {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        ObjectPool connectionPool = driver.getConnectionPool(poolName);

        logger.info("MaxActive: " + ((GenericObjectPool) connectionPool).getMaxActive());
        logger.info("NumActive: " + connectionPool.getNumActive());
        logger.info("NumIdle: " + connectionPool.getNumIdle());
    }

    public static PoolStatistics getDriverStatus(String poolName,Logger logger) {

        ////logger.info("Going to get Database Driver Status. Driver Status is: ");

        PoolStatistics stats = new PoolStatistics();
        try {
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            GenericObjectPool connectionPool = (GenericObjectPool) driver.getConnectionPool(poolName);

            stats.maxActive = connectionPool.getMaxActive();
            stats.numActive = connectionPool.getNumActive();
            stats.numIdle = connectionPool.getNumIdle();

        } catch (SQLException e) {
            logger.error("Exception Occurred While Getting Driver Status: ", e);
        }

        return stats;
    }

    public static void shutdownDriver(String poolName,Logger logger) throws Exception {
        logger.info("Closing down DB Pool "+poolName);

        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.closePool(poolName);

        logger.info("DB Pool "+poolName+" has been Shutdown");
    }
}