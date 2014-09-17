/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx.proxy;

import javax.management.InstanceNotFoundException;
import java.io.IOException;

/**
 * @author scr
 */
public interface MuleAllStatistics {
    String printCSVSummary() throws IOException, InstanceNotFoundException;

    String printHtmlSummary() throws IOException, InstanceNotFoundException;

    String printXmlSummary() throws IOException, InstanceNotFoundException;
}
