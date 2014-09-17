package com.cybercom.jmx;

import com.cybercom.controller.mule.overview.ApplicationStatus;
import com.cybercom.controller.server.ServerRow;
import com.cybercom.dao.MuleApplicationDao;
import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleApplication;
import com.cybercom.dao.objects.MuleServer;
import com.cybercom.jmx.proxy.MuleAllStatistics;
import com.cybercom.jmx.proxy.MuleContext;
import com.cybercom.jmx.proxy.MuleWrapper;
import com.cybercom.util.MuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectName;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MuleServerWrapper {
    private int muleServerId;
    private MuleServerDao muleServerDao;
    private MuleApplicationDao muleApplicationDao;
    private JmxConnector jmxConnector;
    public static final Logger LOGGER = LoggerFactory.getLogger(MuleServerWrapper.class);

    MuleServerWrapper(final MuleServerDao muleServerDao, final MuleApplicationDao muleApplicationDao,
                      final int muleServerId) {
        this.muleServerDao = muleServerDao;
        this.muleApplicationDao = muleApplicationDao;
        this.muleServerId = muleServerId;
        final String muleUrl = getJmxUrl();
        this.jmxConnector = new JmxConnector(muleUrl);
    }

    private String getJmxUrl() {
        final MuleServer muleServer = muleServerDao.getMuleServer(muleServerId);
        return muleServer.buildJmxUrl();
    }

    /**
     * Creates and returns an MBean Proxy for the Mule Context
     *
     * @return The Mule Context
     */
    protected MuleContext getMuleContext() {
        final String objectName = "Mule.default:name=MuleContext";
        return (MuleContext) jmxConnector.createMBeanProxy(objectName, MuleContext.class);
    }

    /**
     * Creates and returns an MBean Proxy for the Mule Wrapper
     *
     * @return The Mule Wrapper
     */
    protected MuleWrapper getMuleWrapper() {
        final String objectName = "Mule:name=WrapperManager";
        return (MuleWrapper) jmxConnector.createMBeanProxy(objectName, MuleWrapper.class);
    }

    public void buildServerStatistics(final List<ServerRow> serverRows) {
        statisticsFromContext(serverRows);
        statisticsFromWrapper(serverRows);

    }

    private void statisticsFromWrapper(final List<ServerRow> serverRows) {
        MuleWrapper muleWrapper = getMuleWrapper();
        try {
            doUpsert(serverRows, ServerRow.ApplicationName.WRAPPER_VERSION, String.valueOf(muleWrapper.getVersion()));
            doUpsert(serverRows, ServerRow.ApplicationName.JAVA_PID, String.valueOf(muleWrapper.getJavaPID()));
        } catch (IOException e) {
            LOGGER.error("Could not get wrapper version or java pid", e);
        }
    }

    private void doUpsert(final List<ServerRow> serverRows, final ServerRow.ApplicationName applicationName,
                          final String value) {
        int index = serverRows.indexOf(new ServerRow(applicationName));

        if (index > -1) {
            //exists
            serverRows.get(index).getServerApplicationValues().add(value);
        } else {
            final ServerRow serverRow = new ServerRow(applicationName);
            serverRow.getServerApplicationValues().add(value);
            serverRows.add(serverRow);
        }

    }

    private void statisticsFromContext(final List<ServerRow> serverRows) {
        MuleContext muleContext = getMuleContext();

        doUpsert(serverRows, ServerRow.ApplicationName.START_TIME, String.valueOf(muleContext.getStartTime()));
        doUpsert(serverRows, ServerRow.ApplicationName.HOSTNAME, muleContext.getHostname());
        doUpsert(serverRows, ServerRow.ApplicationName.IP_ADDRESS, muleContext.getHostIp());
        doUpsert(serverRows, ServerRow.ApplicationName.JDK_VERSION, muleContext.getJdkVersion());
        doUpsert(serverRows, ServerRow.ApplicationName.OS_VERSION, muleContext.getOsVersion());
        doUpsert(serverRows, ServerRow.ApplicationName.MULE_VENDOR, muleContext.getVendor());
        doUpsert(serverRows, ServerRow.ApplicationName.MULE_VERSION, muleContext.getVersion());
        doUpsert(serverRows, ServerRow.ApplicationName.TOTAL_MEMORY, formatMemoryMb(muleContext.getTotalMemory()));
        doUpsert(serverRows, ServerRow.ApplicationName.MAX_MEMORY, formatMemoryMb(muleContext.getMaxMemory()));
        doUpsert(serverRows, ServerRow.ApplicationName.FREE_MEMORY, formatMemoryMb(muleContext.getFreeMemory()));
    }


    /**
     * Formats the memory used and appends Mb at the end
     *
     * @param memory The memory in bytes
     * @return The memory in Megabytes
     */
    private String formatMemoryMb(final long memory) {
        BigDecimal memoryBytes = new BigDecimal(memory);
        final BigDecimal divider = new BigDecimal(1000 * 1000);
        final BigDecimal memoryMb = memoryBytes.divide(divider);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(memoryMb) + " Mb";
    }

    /**
     * Compares the retrieveRunningMuleApps() and the retrieveAllMuleApps()
     * method
     *
     * @return The difference between the lists
     */
    public List<MuleApplication> getMissingMuleApps() {
        List<MuleApplication> currentMuleApps = retrieveRunningMuleApps();
        List<MuleApplication> wantedMuleApps = retrieveAllMuleApps();
        wantedMuleApps.removeAll(currentMuleApps);

        return wantedMuleApps;
    }

    /**
     * Searches the database after Mule apps that should be running
     *
     * @return The list of the Mule applications that should be running
     */
    protected List<MuleApplication> retrieveAllMuleApps() {
        return muleApplicationDao.getMuleApplicationByServerId(muleServerId);
    }

    public MuleAllStatistics getMuleAllStatistics(final String muleApplicationName) {
        final String type = "Statistics";
        final String name = "AllStatistics";
        return (MuleAllStatistics) jmxConnector.createMBeanProxy(
                getObjectName(muleApplicationName, type, name), MuleAllStatistics.class);
    }

    /**
     * @param muleApplicationName The Mule application name
     * @param type                The Type
     * @param name                The name
     * @return The name of the object
     */
    private String getObjectName(final String muleApplicationName, final String type, final String name) {
        String result = muleApplicationName + ":type=" + type + ",name=" + name;
        return result;
    }

    public String getMulePrefix() {
        return muleServerDao.getMuleServer(muleServerId).getPrefix();
    }

    public List<String> getMuleApplicationNames() {
        List<String> list = new ArrayList<>();
        for (ObjectName objectName : getMuleObjectNames()) {
            list.add(objectName.getCanonicalName());
        }
        return list;
    }

    protected Set<ObjectName> getMuleObjectNames() {
        String query = "Mule" + ObjectName.WILDCARD;
        return jmxConnector.getObjectNames(query);
    }

    /**
     * Produces a list of the running mule apps, without the mule prefix
     *
     * @return A list of the running mule apps, without the mule prefix
     */
    public List<MuleApplication> retrieveRunningMuleApps() {
        List<String> allDomains = jmxConnector.retrieveDomains();
        List<MuleApplication> result = new ArrayList<>();
        final String mulePrefix = getMulePrefix();

        if (allDomains != null) {
            for (String domain : allDomains) {
                if (domain.startsWith(mulePrefix)) {
                    //strip prefix
                    result.add(new MuleApplication(stripPrefix(domain)));
                }
            }
        }
        return result;
    }

    private String stripPrefix(final String domain) {
        MuleUtils muleUtils = new MuleUtils(muleServerDao);
        return muleUtils.stripPrefix(muleServerId, domain);
    }

    public List<MuleApplication> getListOfRunningMuleApps() {
        List<String> allDomains = jmxConnector.retrieveDomains();
        List<MuleApplication> result = new ArrayList<>();
        final String mulePrefix = getMulePrefix();

        if (allDomains != null) {
            for (String domain : allDomains) {
                if (domain.startsWith(mulePrefix)) {
                    final String applicationName = stripPrefix(domain);
                    MuleApplication muleApplication = new MuleApplication();
                    muleApplication.setMuleServerId(getMuleServerId());
                    muleApplication.setName(applicationName);
                    result.add(muleApplication);
//                    final boolean appExists = muleApplicationDao.applicationExists(applicationName, muleServerId);
                }
            }
        }
        return result;
    }


    /**
     * Gets the Mule Flows
     *
     * @param appName The Mule application name
     * @return The set of ObjectNames representing the Mule flows
     */
    public Set<ObjectName> getFlows(final String appName) {
        String query = appName + ":type=Flow,name=*";
        Set<ObjectName> theSet = jmxConnector.getObjectNames(query);
        return theSet;
    }

    /**
     * Produces a list of mule apps that are running but not monitored, without the mule prefix
     *
     * @return A list of mule apps, without the mule prefix
     */
    public List<MuleApplication> getOtherMuleApps() {
        List<MuleApplication> runningMuleApps = retrieveRunningMuleApps();
        List<MuleApplication> registeredMuleApps = getMuleApps();
        registeredMuleApps.removeAll(runningMuleApps);
        return registeredMuleApps;
    }

    private List<MuleApplication> getMuleApps() {
        return muleApplicationDao.getMuleApplicationByServerId(muleServerId);
    }

    public void restartServer() {
        try {
            getMuleWrapper().restart();
        } catch (IOException e) {
            LOGGER.error("Could not restart server", e);
        }
    }

    public int getMuleServerId() {
        return muleServerId;
    }

    @Override
    public String toString() {
        return muleServerDao.getMuleServer(muleServerId).getName();

    }

    /**
     * Calculates the application status on this mule server
     *
     * @param applicationNameWithPrefix The complete application name
     * @return The application status
     */
    public ApplicationStatus.Status buildApplicationStatus(final String applicationNameWithPrefix) {
        final ApplicationStatus.Status result;
        List<String> listOfDomains = jmxConnector.retrieveDomains();
        if (listOfDomains.contains(applicationNameWithPrefix)) {
            if (muleApplicationDao.applicationExists(stripPrefix(applicationNameWithPrefix), muleServerId)) {
                LOGGER.info("Setting RUNNING for application={} on serverid={}",
                        applicationNameWithPrefix, muleServerId);
                result = ApplicationStatus.Status.RUNNING;
            } else {
                LOGGER.info("Setting RUNNING_NOT_IN_DATABASE for application={} on serverid={}",
                        applicationNameWithPrefix, muleServerId);
                result = ApplicationStatus.Status.RUNNING_NOT_IN_DATABASE;
            }
        } else {
            LOGGER.info("Setting NOT_RUNNING for application={} on serverid={}",
                    applicationNameWithPrefix, muleServerId);
            result = ApplicationStatus.Status.NOT_RUNNING;
        }

        return result;
    }
}
