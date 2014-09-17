/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.jmx;

import com.cybercom.dao.MuleApplicationDao;
import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleApplication;
import com.cybercom.jmx.proxy.MuleContext;
import com.cybercom.jmx.proxy.MuleWrapper;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.management.ObjectName;
import java.util.List;
import java.util.Set;

/**
 * @author scr
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class MuleServerWrapperTest extends TestCase {

    @Autowired
    private MuleServerDao muleServerDao;
    @Autowired
    private MuleApplicationDao muleApplicationDao;
    private MuleServerWrapper instance;

    @Override
    public void setUp() throws Exception {
        instance = new MuleServerWrapper(muleServerDao, muleApplicationDao, 1);
    }

    @Test
    public void testGetMuleObjectNames() {
        Set<ObjectName> setOfObjectNames = instance.getMuleObjectNames();
        assertNotNull("Set of Object Names should not be null", setOfObjectNames);
    }

    @Test
    public void testGetMuleApplicationNames() {
        List<String> list = instance.getMuleApplicationNames();
        assertNotNull("muleApplicationNames should not be null", list);
    }

    @Test
    public void testRetrieveRuningMuleApps() {
        List<MuleApplication> list = instance.retrieveRunningMuleApps();
        assertNotNull("retrieveRunningMuleApps is null", list);
    }

    @Test
    public void testGetMissingMuleApp() {
        List<MuleApplication> list = instance.retrieveRunningMuleApps();
        assertNotNull("retrieveRunningMuleApps is null", list);
        String mulePrefix = instance.getMulePrefix();
        assertNotNull("mulePrefix is null.", mulePrefix);
        List<MuleApplication> missingMuleApps = instance.getMissingMuleApps();
        assertTrue("getMissingMuleApp should not be null", missingMuleApps.size() > 0);
        System.out.println("missingMuleApps = " + missingMuleApps);
        assertEquals("Wrong value", "TalentumESB", missingMuleApps.get(0));
    }

    @Test
    public void testGetMuleContext() throws Exception {
        MuleContext muleContext = instance.getMuleContext();
        long result = muleContext.getTotalMemory();
        System.out.println("result = " + result);
        assertTrue(result > 0);
    }

    @Test
    public void testGetMuleWrapper() throws Exception {
        MuleWrapper muleWrapper = instance.getMuleWrapper();
        int result = muleWrapper.getJavaPID();
        System.out.println("result = " + result);
        assertTrue(result > 0);

        muleWrapper.restart();
    }

}
