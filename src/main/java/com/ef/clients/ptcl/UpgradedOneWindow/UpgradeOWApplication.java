package com.ef.clients.ptcl.UpgradedOneWindow;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UpgradeOWApplication {

    private static final Logger logger = LogManager.getLogger(UpgradeOWApplication.class);

    public static void main(String[] args) {
        //logger.debug("Upgraded OneWindow main Method called");
        SpringApplication.run(UpgradeOWApplication.class, args);

    }
}
