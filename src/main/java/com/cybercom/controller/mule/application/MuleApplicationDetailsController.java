/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.controller.mule.application;

import com.cybercom.dao.MuleServerDao;
import com.cybercom.jmx.MuleApplicationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author scr
 */
@Controller
public class MuleApplicationDetailsController {
    @Autowired
    private MuleApplicationHandler handler;
    @Autowired
    private MuleServerDao muleServerDao;

    /**
     * @param muleApplicationName The application name
     * @return The model and view
     */
    @RequestMapping("/mule/application/view/{muleApplicationName}")
    public ModelAndView handleRequest(@PathVariable("muleApplicationName") final String muleApplicationName) {

        final String fullMuleAppName = muleServerDao.getMuleServer(0).getPrefix() + muleApplicationName;

        ModelAndView model = new ModelAndView("muleApplicationDetails");
        model.addObject("applicationName", fullMuleAppName);
        model.addObject("muleStatistics", handler.getStatisticsAsHtml(0, fullMuleAppName));
        model.addObject("listOfFlows", handler.getFlows(0, fullMuleAppName));
        return model;
    }
}
