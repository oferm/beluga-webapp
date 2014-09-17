package com.cybercom.jmx;

import com.cybercom.controller.mule.overview.ApplicationStatus;
import com.cybercom.controller.server.ServerRow;
import com.cybercom.dao.MuleApplicationDao;
import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleApplication;
import com.cybercom.dao.objects.MuleServer;
import com.cybercom.util.MuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/31/13
 * Time: 10:10 AM
 */
@Component
public class MuleServerList {
    private List<MuleServerWrapper> muleServerWrappers;
    private MuleServerDao muleServerDao;
    private MuleApplicationDao muleApplicationDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleServerList.class);

    @Autowired
    public MuleServerList(final MuleServerDao muleServerDao, final MuleApplicationDao muleApplicationDao) {
        this.muleServerDao = muleServerDao;
        this.muleApplicationDao = muleApplicationDao;

        populateServers();
    }

    public void populateServers() {
        muleServerWrappers = new LinkedList<>();
        List<MuleServer> muleServers = muleServerDao.getAllMuleServers();
        for (MuleServer muleServer : muleServers) {
            muleServerWrappers.add(new MuleServerWrapper(muleServerDao, muleApplicationDao, muleServer.getId()));
        }
    }

    public List<MuleServerWrapper> getMuleServerWrappers() {
        populateServers();
        return muleServerWrappers;
    }

    /**
     * Returns a list of all running Mule Application names
     *
     * @return A list of all running Mule Application names
     */
    public List<MuleApplication> getAllRunningApplications() {
        final List<MuleApplication> result = new ArrayList<>();
        for (MuleServerWrapper muleServerWrapper : getMuleServerWrappers()) {
            doRunningMuleApps(result, muleServerWrapper);
        }
        return result;
    }

    private void doRunningMuleApps(final List<MuleApplication> result, final MuleServerWrapper muleServerWrapper) {
        List<MuleApplication> runningMuleApps = muleServerWrapper.getListOfRunningMuleApps();
        for (MuleApplication runningMuleApp : runningMuleApps) {
            if (!result.contains(runningMuleApp)) {
                result.add(runningMuleApp);
            }
        }
    }

    /**
     * Sets the application's status on all the available Mule servers.
     *
     * @param muleApplication The Mule Application
     * @return The ApplicationStatus for the applicationName
     */
    public ApplicationStatus getAppStatus(final MuleApplication muleApplication) {
        final ApplicationStatus result = new ApplicationStatus(muleApplication);
        for (MuleServerWrapper muleServerWrapper : getMuleServerWrappers()) {
            final int muleServerId = muleServerWrapper.getMuleServerId();
            final String applicationNameWithPrefix =
                    muleServerDao.getMuleServer(muleServerId).getPrefix() + muleApplication.getName();
            result.getListOfServerStatus().add(muleServerWrapper.buildApplicationStatus(applicationNameWithPrefix));
        }
        return result;
    }

    public List<ApplicationStatus> getMissingMuleApps() {
        List<ApplicationStatus> result = new ArrayList<>();
        MuleUtils muleUtils = new MuleUtils(muleServerDao);
        for (MuleServerWrapper muleServerWrapper : getMuleServerWrappers()) {
            List<MuleApplication> missingMuleApps = muleServerWrapper.getMissingMuleApps();
            for (MuleApplication missingMuleApp : missingMuleApps) {
                final String applicationName = muleUtils.stripPrefix(
                        muleServerWrapper.getMuleServerId(), missingMuleApp.getName());
                final ApplicationStatus muleApp = new ApplicationStatus(new MuleApplication(applicationName));
                muleApp.getListOfServerStatus().add(ApplicationStatus.Status.NOT_RUNNING);
                result.add(muleApp);
            }
        }
        return result;
    }

    public List<ServerRow> buildServerStatistics() {
        final List<ServerRow> result = new ArrayList<>();
        for (MuleServerWrapper muleServerWrapper : getMuleServerWrappers()) {
            muleServerWrapper.buildServerStatistics(result);
        }
        return result;
    }

    public List<String> getServerNames() {

        final List<String> serverNames = new ArrayList<>();
        for (MuleServerWrapper muleServerWrapper : getMuleServerWrappers()) {
            serverNames.add(muleServerDao.getMuleServer(muleServerWrapper.getMuleServerId()).getName());
        }
        return serverNames;
    }

    public List<Integer> getServerIds() {
        final List<Integer> result = new ArrayList<>();
        for (MuleServerWrapper muleServerWrapper : getMuleServerWrappers()) {
            result.add(muleServerWrapper.getMuleServerId());
        }
        return result;
    }
}
