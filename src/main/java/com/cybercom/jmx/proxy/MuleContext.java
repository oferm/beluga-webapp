package com.cybercom.jmx.proxy;

import java.util.Date;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/20/13
 * Time: 3:30 PM
 */
public interface MuleContext {
    long getFreeMemory();

    String getHostIp();

    String getHostname();

    String getJdkVersion();

    long getMaxMemory();

    String getOsVersion();

    Date getStartTime();

    long getTotalMemory();

    String getVendor();

    String getVersion();


}
