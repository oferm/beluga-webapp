package com.cybercom.dao.objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 6/13/13
 * Time: 9:28 AM
 */
public class MuleServerTest {
    private MuleServer instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleServerTest.class);

    @Before
    public void setUp() throws Exception {
        instance = new MuleServer();
        instance.setServerAddress("localhost");
    }

    @Test
    public void testBuildDeployUrl() throws Exception {
        String result = instance.buildDeployUrl();
        LOGGER.info("testBuildDeployUrl={}", result);
        Assert.assertNotNull(result);

    }

    @Test
    public void testBuildJmxUrl() throws Exception {
        String result = instance.buildJmxUrl();
        LOGGER.info("testBuildJmxUrl={}", result);
        Assert.assertNotNull(result);

    }
}
