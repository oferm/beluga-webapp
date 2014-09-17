package com.cybercom.controller.mule.overview;

import com.cybercom.dao.MuleApplicationDao;
import com.cybercom.dao.objects.MuleApplication;
import com.cybercom.jmx.MuleServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OverviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverviewController.class);
    @Autowired
    private MuleServerList muleServerList;
    @Autowired
    private MuleApplicationDao muleApplicationDao;


    @RequestMapping("/overview")
    public ModelAndView handleRequestInternal() {
        ModelAndView model = new ModelAndView("overview");

        model.addObject("muleServerList", muleServerList.getMuleServerWrappers());
        model.addObject("muleExistingApps", buildBody());
        model.addObject("muleMissingApps", muleServerList.getMissingMuleApps());
        model.addObject("lastChecked", new Date().toString());

        return model;
    }


    private List<ApplicationStatus> buildBody() {
        List<ApplicationStatus> applicationStatuses = new ArrayList<>();
        List<MuleApplication> runningApplications = muleServerList.getAllRunningApplications();
        for (MuleApplication runningApplication : runningApplications) {
            applicationStatuses.add(muleServerList.getAppStatus(runningApplication));
        }

        return applicationStatuses;
    }
}
