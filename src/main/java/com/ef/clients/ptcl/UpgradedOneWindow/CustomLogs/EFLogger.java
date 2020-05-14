package com.ef.clients.ptcl.UpgradedOneWindow.CustomLogs;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.util.ResourceBundle;

public class EFLogger {
    private static String AGENT_LOGS_FILE_SIZE="2KB";
    private static int AGENT_LOGS_MAX_FILES = 10;
    private static String AGENT_LOGS_PATH="C:\\\\EF\\\\Logs\\\\UpgradedOneWindow\\\\Agents\\\\";
    private static Level AGENT_LOGS_LEVEL= Level.DEBUG;


    public static Appender getAgentFileLogAppender(String agentID) {
        ResourceBundle bundle = ResourceBundle.getBundle("Config");
        String agentLogsFileSize = bundle.getString("AGENT_LOG_FILE_SIZE");
        String agentLogsMaxFiles = bundle.getString("AGENT_MAX_FILES");
        String agentFileLocation = bundle.getString("AGENT_LOG_FILE_LOCATION");
        RollingFileAppender appender = new RollingFileAppender();
        appender.setMaxFileSize(agentLogsFileSize);
        appender.setMaxBackupIndex(Integer.parseInt(agentLogsMaxFiles));
        appender.setName(agentID);
        appender.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1} %L %M_%d{yyyyMMdd}%m%n"));
        //appender.setLayout(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss,SSS} | %-5p | %m |%M Line:%L| %t%n"));
        agentFileLocation = agentFileLocation+"\\"+agentID+"\\";
        appender.setFile(agentFileLocation +"logs.log");
        appender.setAppend(true);
        appender.setThreshold(AGENT_LOGS_LEVEL);
        appender.activateOptions();
        return appender;
    }
}
