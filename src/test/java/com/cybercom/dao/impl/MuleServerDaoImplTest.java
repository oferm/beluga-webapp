package com.cybercom.dao.impl;

import com.cybercom.dao.objects.MuleServer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/30/13
 * Time: 12:57 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class MuleServerDaoImplTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MuleServerDaoImplTest.class);
    @Autowired
    private MuleServerDaoImpl instance;
    private MuleServer server;

    @Before
    public void setUp() throws Exception {
        server = new MuleServer();
        server.setName("test");
        server.setPrefix("Mule.");
        server.setServerAddress("jmxAddress");

    }

    @Test
    public void testCreateMuleServer() throws Exception {
        instance.createMuleServer(server);
    }

    @Test
    public void testGetMuleServerById() throws Exception {
        final int id = 1;
        MuleServer result = instance.getMuleServer(id);
        Assert.assertEquals(id, result.getId());
    }

    @Test
    public void testGetMuleServerByName() throws Exception {
        final String muleServerName = "Local server";
        MuleServer result = instance.getMuleServer(muleServerName);
        Assert.assertEquals(muleServerName, result.getName());
    }

    @Test
    public void testUpdateMuleServer() throws Exception {
        instance.updateMuleServer(server);

    }
}
