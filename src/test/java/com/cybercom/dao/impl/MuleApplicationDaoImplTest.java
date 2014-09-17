package com.cybercom.dao.impl;

import com.cybercom.dao.objects.MuleApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 2:22 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class MuleApplicationDaoImplTest {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MuleApplicationDaoImplTest.class);
    @Autowired
    private MuleApplicationDaoImpl instance;
    private MuleApplication muleApplication;


    @Before
    public void setUp() throws Exception {
        muleApplication = new MuleApplication();
        muleApplication.setName("test");
        muleApplication.setMuleServerId(1);
    }

    @Test
    public void testCreateMuleApplication() throws Exception {
        instance.createMuleApplication(muleApplication);

    }

    @Test
    public void testGetMuleApplication() throws Exception {
        final int muleApplicationId = 1;
        MuleApplication result = instance.getMuleApplication(muleApplicationId);
        Assert.assertNotNull(result);
        Assert.assertEquals(muleApplicationId, result.getId());
    }

    @Test
    public void testGetMuleApplicationByServerId() throws Exception {
        final int muleServerId = 1;
        List<MuleApplication> result = instance.getMuleApplicationByServerId(muleServerId);
        Assert.assertNotNull(result);
        Assert.assertEquals(muleServerId, result.get(0).getMuleServerId());

    }

    @Test
    public void testUpdateMuleApplication() throws Exception {
        final int muleApplicationId = 1;
        muleApplication.setId(muleApplicationId);
        instance.updateMuleApplication(muleApplication);
        Assert.assertEquals("test", instance.getMuleApplication(muleApplicationId).getName());
    }

    @Test
    public void testRemoveMuleApplication() throws Exception {
        instance.removeMuleApplication(1);
        Assert.assertNull(instance.getMuleApplication(1));

    }

    @Test
    public void testApplicationExists() throws Exception {
        boolean result = instance.applicationExists("kidfjkdf", 3);
        Assert.assertFalse(result);

    }
}
