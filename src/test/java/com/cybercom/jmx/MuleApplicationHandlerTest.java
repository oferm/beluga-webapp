/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * @author scr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class MuleApplicationHandlerTest extends TestCase {
    @Autowired
    private MuleApplicationHandler handler;
    private String MULE_APP_NAME = "Mule.default";

    @Test
    public void testGetStatisticsAsHtml() {
        String html = handler.getStatisticsAsHtml(0, MULE_APP_NAME);
        assertNotNull(html);
//        System.out.println("html = " + html);
    }

    @Test
    public void testGetFlows() {
        String appName = "Mule.jmxmanagement-1.0-SNAPSHOT";
        ArrayList<String> list = handler.getFlows(0, appName);
        assertFalse("List should not be empty", list.isEmpty());
    }
}
