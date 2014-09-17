/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMX;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

//@Service
public class JmxConnector {
    public static final Logger LOGGER = LoggerFactory.getLogger(JmxConnector.class);
    private final String jmxUrl;
    private static final String ENDPOINT_PROPERTY_NAME = "mule.server.url";
    private MBeanServerConnection serverConnection;
    private boolean connected;

    public JmxConnector(final String jmxUrl) {
        this.jmxUrl = jmxUrl;
        setupConnections();
    }

    /**
     * Initiates the server connections
     */
    private void setupConnections() {
        connected = false;
        this.serverConnection = doConnect(jmxUrl);
    }

    /**
     * Initiates a JMX connection to the Mule server
     *
     * @param muleServerUrl The url to the mule server.
     * @return The MBean server connection object
     */
    private MBeanServerConnection doConnect(final String muleServerUrl) {
        JMXServiceURL url = null;
        JMXConnector jmxConnector = null;
        MBeanServerConnection mBeanServerConnection = null;
        LOGGER.debug("Creating an RMI connector");

        try {
            url = new JMXServiceURL(muleServerUrl);
            jmxConnector = JMXConnectorFactory.connect(url);
            mBeanServerConnection = jmxConnector.getMBeanServerConnection();
            connected = true;
        } catch (Exception ex) {
            LOGGER.error("Could not create MBean Server Connection:", ex);
            connected = false;
        }

        return mBeanServerConnection;

    }

    protected Set<ObjectName> getObjectNames(final String query) {
        checkConnection();
        Set<ObjectName> queryNames = null;
        try {
            LOGGER.debug("Entering method getMbeans");
            queryNames = serverConnection.queryNames(new ObjectName(query), null);
        } catch (Exception e) {
            LOGGER.error("Failed to create ObjectName set: ", e);
            connected = false;
        }
        return queryNames;
    }

    protected List<String> retrieveDomains() {
        checkConnection();
        ArrayList<String> list = new ArrayList<>();
        String[] domains;

        try {
            domains = serverConnection.getDomains();
        } catch (Exception e) {
            LOGGER.error("Could not get list of domains: " + e);
            connected = false;
            return null;
        }
        Collections.addAll(list, domains);
        return list;
    }

    /**
     * Creates an MBean proxy
     *
     * @param objectName The objectName
     * @param theClass   The MBean proxy class.
     * @return The MBean proxy
     */
    protected <T> T createMBeanProxy(final String objectName, final Class<T> theClass) {
        checkConnection();
        Object result = null;
        try {
            ObjectName objName = new ObjectName(objectName);
            result = JMX.newMBeanProxy(serverConnection, objName, theClass);
        } catch (Exception e) {
            LOGGER.error("Could not create Mule instance: ", e);
            connected = false;
        }
        return theClass.cast(result);
    }


    /**
     * Checks if we are connected. If not, connect.
     */
    private void checkConnection() {
        if (!connected) {
            setupConnections();
        }

    }

    public ArrayList<String> getObjectNamesByDomain(final String domain) {
        checkConnection();
        String query = domain + ":*";
        LOGGER.debug("Query: " + query);
        Set<ObjectName> objectNames = getObjectNames(query);
        ArrayList<String> list = new ArrayList<>();

        for (ObjectName objectName : objectNames) {
            list.add(objectName.getCanonicalName());
        }
        return list;
    }

    protected MBeanInfo getMbeanInfo(final String name) {
        checkConnection();
        MBeanInfo mbeanInfo = null;
        try {
            ObjectName objectName = new ObjectName(name);
            mbeanInfo = serverConnection.getMBeanInfo(objectName);
        } catch (Exception e) {
            LOGGER.error("Could not get MBean Info for bean with name: " + name + ". Error: " + e);
            connected = false;
        }
        return mbeanInfo;
    }

    protected MBeanAttributeInfo[] getAttributes(final MBeanInfo mbeanInfo) {
        checkConnection();
        return mbeanInfo.getAttributes();
    }
}
