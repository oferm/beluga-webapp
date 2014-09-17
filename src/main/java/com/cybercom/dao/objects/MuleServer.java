package com.cybercom.dao.objects;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 10:29 AM
 */
public class MuleServer {
    private int id;
    private String name;
    private String serverAddress;
    private String prefix;

    private static final String REST_BASE_URL = "/beluga";
    private static final String DEPLOY_URL_SUFFIX = "/deploy";
    private static final String UNDEPLOY_URL_SUFFIX = "/undeploy";
    private static final String DEPLOY_URL_PORT = "8082";
    private static final String JMX_PREFIX = "service:jmx:rmi:///jndi/rmi://";
    private static final String JMX_SUFFIX = "/server";
    private static final String JMX_PORT = "1098";

    public MuleServer() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(final String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "MuleServer{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", serverAddress='" + serverAddress + '\''
                + ", prefix='" + prefix + '\''
                + '}';
    }

    public String buildDeployUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(serverAddress);
        builder.append(":");
        builder.append(DEPLOY_URL_PORT);
        builder.append(REST_BASE_URL);
        builder.append(DEPLOY_URL_SUFFIX);
        return builder.toString();
    }

    public String buildJmxUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(JMX_PREFIX);
        builder.append(serverAddress);
        builder.append(":");
        builder.append(JMX_PORT);
        builder.append(JMX_SUFFIX);
        return builder.toString();

    }


    public String buildUndeployUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(serverAddress);
        builder.append(":");
        builder.append(DEPLOY_URL_PORT);
        builder.append(REST_BASE_URL);
        builder.append(UNDEPLOY_URL_SUFFIX);
        return builder.toString();
    }
}
