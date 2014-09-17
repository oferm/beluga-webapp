package com.cybercom.controller.server;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/21/13
 * Time: 1:17 PM
 */
public class ServerRow {
    private ApplicationName applicationName;
    private List<String> serverApplicationValues;

    public ServerRow(final ApplicationName applicationName) {
        this.applicationName = applicationName;
        this.serverApplicationValues = new ArrayList<>();
    }

    public ApplicationName getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(final ApplicationName applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ServerRow) {
            ServerRow serverRow = (ServerRow) object;
            if (serverRow.getApplicationName().equals(getApplicationName())) {
                return true;
            }
        }
        return false;
    }

    public List<String> getServerApplicationValues() {
        return serverApplicationValues;
    }

    public void setServerApplicationValues(final List<String> serverApplicationValues) {
        this.serverApplicationValues = serverApplicationValues;
    }

    public enum ApplicationName {
        START_TIME("Start time"),
        HOSTNAME("Hostname"),
        IP_ADDRESS("IP address"),
        JDK_VERSION("JDK version"),
        OS_VERSION("OS version"),
        MULE_VENDOR("Mule vendor"),
        MULE_VERSION("Mule version"),
        TOTAL_MEMORY("Total memory"),
        MAX_MEMORY("Max memory"),
        FREE_MEMORY("Free memory"),
        WRAPPER_VERSION("Wrapper version"),
        JAVA_PID("Java PID");

        private String applicationName;

        ApplicationName(final String applicationName) {
            this.applicationName = applicationName;
        }

        @Override
        public String toString() {
            return applicationName;
        }
    }

    @Override
    public String toString() {
        return "ServerRow{"
                + "applicationName=" + applicationName
                + ", serverApplicationValues=" + serverApplicationValues
                + "}\n";
    }
}
