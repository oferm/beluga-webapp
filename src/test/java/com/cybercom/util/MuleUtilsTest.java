package com.cybercom.util;

import com.cybercom.dao.MuleServerDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 6/3/13
 * Time: 11:07 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/PropertiesUtilTest-context.xml")
public class MuleUtilsTest {
    @Autowired
    private MuleServerDao muleServerDao;
    private MuleUtils instance;

    @Before
    public void setUp() throws Exception {
        this.instance = new MuleUtils(muleServerDao);

    }

    @Test
    public void testStripPrefix() throws Exception {
        String expResult = "default";
        String result = instance.stripPrefix(0, "Mule.default");
        assertEquals(expResult, result);
    }
}
