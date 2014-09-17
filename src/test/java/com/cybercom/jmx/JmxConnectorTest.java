/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx;

import com.cybercom.dao.MuleServerDao;
import com.cybercom.jmx.proxy.MuleAllStatistics;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class JmxConnectorTest extends TestCase {

    @Autowired
    private MuleServerDao muleServerDao;
    private JmxConnector instance;


    @Before
    public void setUp() throws Exception {
        final String jmxUrl = muleServerDao.getMuleServer(0).buildJmxUrl();
        instance = new JmxConnector(jmxUrl);
    }

    @Test
    public void testRetrieveDomains() {
        try {
            List<String> domains = instance.retrieveDomains();
            assertNotNull("retrieveDomains() should not return null", domains);
        } catch (Exception e) {
            fail("Exception caught: " + e);
        }
    }

    @Test
    public void testGetObjectNamesByDomain() {
        String domain = "Mule";
        ArrayList<String> list = instance.getObjectNamesByDomain(domain);
        assertNotNull("getMbeanByDomain() returns null", list);
    }

    @Test
    public void testGetMbeanInfo() {
        String name = "Mule:name=WrapperManager";
        MBeanInfo mbeanInfo = instance.getMbeanInfo(name);
        assertNotNull("getMbeanInfo should not be null", mbeanInfo);
    }

    @Test
    public void testGetAttributes() {
        String name = "Mule:name=WrapperManager";
        MBeanInfo mbeanInfo = instance.getMbeanInfo(name);
        assertNotNull("getMbeanInfo should not be null", mbeanInfo);

        //specific for this test below
        MBeanAttributeInfo[] attributes = instance.getAttributes(mbeanInfo);
        for (MBeanAttributeInfo attr : attributes) {
//            System.out.println("attr = " + attr);
        }
    }

    @Test
    public void testGetMuleInstance() {
        String name = "Mule.default:type=Statistics,name=AllStatistics";
        MuleAllStatistics mule = (MuleAllStatistics) instance.createMBeanProxy(name, MuleAllStatistics.class);
        assertNotNull(mule);
    }

    @Test
    public void testGetObjectNames() {
        String appName = "Mule.jmxmanagement-1.0-SNAPSHOT";
        String query = appName + ":type=Flow,name=*";
        Set<ObjectName> theSet = instance.getObjectNames(query);
        for (ObjectName objectName : theSet) {
            System.out.println("objectName name: " + objectName.getKeyProperty("name"));

        }
        assertNotNull(theSet);
    }
}
