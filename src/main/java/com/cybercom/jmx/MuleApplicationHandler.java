/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx;

import com.cybercom.jmx.proxy.MuleAllStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author scr
 */
@Service
public class MuleApplicationHandler {

    @Autowired
    private MuleServerList muleServerList;
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleApplicationHandler.class);

    public String getStatisticsAsXml() {
        //        ObjectName
        return null;
    }

    public String getStatisticsAsHtml(final int muleServerId, final String muleApplicationName) {
        MuleAllStatistics mule =
                muleServerList.getMuleServerWrappers().get(muleServerId).getMuleAllStatistics(muleApplicationName);
        String result = null;
        try {
            result = mule.printHtmlSummary();
        } catch (Exception ex) {
            LOGGER.error("Could not print HTML summary", ex);
        }
        return result;
    }


    public String getFlowStatistics() {
        String result = null;
        //TODO
        return result;
    }

    public ArrayList<String> getFlows(final int muleServerId, final String muleApplicationName) {
        //TODO lookup all MBeans with "type=Flow"
        /**
         * Example: Mule.jmxmanagement-1.0-SNAPSHOT:type=Flow,name="Echo test"
         * Domain: Mule.jmxmanagement-1.0-SNAPSHOT Flow name="Echo test"
         */
        ArrayList<String> list = new ArrayList<>();
        Set<ObjectName> setOfFlows =
                muleServerList.getMuleServerWrappers().get(muleServerId).getFlows(muleApplicationName);
        for (ObjectName objectName : setOfFlows) {
            String name = objectName.getKeyProperty("name");
            name = stripQuotes(name);
            list.add(name);
        }
        LOGGER.info("list of flows for mule app " + muleApplicationName + ": " + list);
        return list;


    }

    private String stripQuotes(final String str) {
        String string = str;
        if (string.startsWith("\"")) {
            string = string.substring(1, string.length());
        }
        if (string.endsWith("\"")) {
            string = str.substring(0, string.length() - 1);
        }

        return string;
    }
}
