/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx.proxy;

import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import java.io.IOException;

/**
 * @author scr
 */
public interface MuleFlow {

    long getAsyncEventsReceived() throws IOException, InstanceNotFoundException;

    long getAverageProcessingTime() throws IOException, InstanceNotFoundException;

    long getExecutionErrors() throws IOException, InstanceNotFoundException;

    long getFatalErrors() throws IOException, InstanceNotFoundException;

    long getMaxProcessingTime() throws IOException, InstanceNotFoundException;

    long getMinProcessingTime() throws IOException, InstanceNotFoundException;

    String getName() throws IOException, InstanceNotFoundException;

    long getProcessedEvents() throws IOException, InstanceNotFoundException;

    ObjectName getStatistics() throws IOException, InstanceNotFoundException;

    long getSyncEventsReceived() throws IOException, InstanceNotFoundException;

    long getTotalEventsReceived() throws IOException, InstanceNotFoundException;

    long getTotalProcessingTime() throws IOException, InstanceNotFoundException;

    String getType() throws IOException, InstanceNotFoundException;

    void clearStatistics() throws IOException, InstanceNotFoundException;

}
