package com.cybercom.controller.server;

import com.cybercom.jmx.MuleServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ServerController {
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    @Autowired
    private MuleServerList muleServerList;

    @RequestMapping("/mule/server")
    public ModelAndView handleRequest() {
        final ModelAndView modelAndView = new ModelAndView("server");
        modelAndView.addObject("serverRows", buildServerStatistics());
        modelAndView.addObject("serverNames", buildServerNames());
        modelAndView.addObject("serverIds", buildServerIds());
        return modelAndView;
    }

    private List<Integer> buildServerIds() {
        return muleServerList.getServerIds();
    }

    private List<String> buildServerNames() {
        return muleServerList.getServerNames();
    }

    private List<ServerRow> buildServerStatistics() {
        //TODO fix
        final List<ServerRow> serverRows = muleServerList.buildServerStatistics();
        return serverRows;
    }

    @RequestMapping("/mule/server/restart/{muleServerNbr}.html")
    public ModelAndView handleRestart(@PathVariable("muleServerNbr") final int muleServerNbr) {
        String status = "fail";
        try {
            muleServerList.getMuleServerWrappers().get(muleServerNbr).restartServer();
            status = "success";
        } catch (Exception e) {
            LOGGER.error("Could not restart server {}", muleServerNbr, e);
        }
        final ModelAndView modelAndView = new ModelAndView("serverRestarted");
        modelAndView.addObject("serverNbr", muleServerNbr);
        modelAndView.addObject("status", status);

        return modelAndView;
    }
}
