package com.cybercom.controller.mule.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/16/13
 * Time: 1:36 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class MuleApplicationPropertiesControllerTest {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MuleApplicationPropertiesController instance;

    @Test
    public void testAddApplication() throws Exception {
        instance.addApplication("mule.test", 0);
    }
}
