package com.ef.clients.ptcl.UpgradedOneWindow.CRM.dbcp;

public class PoolStatistics {

    public int maxActive = -1;
    public int numActive = -1;
    public int numIdle = -1;

    public PoolStatistics() {
    }

    @Override
    public String toString() {
        return "Max_Active: " + maxActive + " | NumActive: " + numActive + " | numIdle: " + numIdle;
    }
}