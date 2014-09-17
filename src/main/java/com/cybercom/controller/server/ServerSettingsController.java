package com.cybercom.controller.server;

import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleServer;
import com.cybercom.dao.objects.Property;
import com.cybercom.jmx.MuleServerList;
import com.cybercom.util.MuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 6/4/13
 * Time: 9:58 AM
 */
@Controller
public class ServerSettingsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSettingsController.class);
    @Autowired
    private MuleServerDao muleServerDao;
    @Autowired
    private MuleServerList muleServerList;

    @RequestMapping("/mule/server/settings/view/{muleServerId}")
    public ModelAndView handleRequest(@PathVariable("muleServerId") final int muleServerId) {
        final ModelAndView modelAndView = new ModelAndView("serverSetting");
//        modelAndView.addObject("serverRows", buildServerStatistics());
        modelAndView.addObject("serverName", muleServerDao.getAllMuleServers().get(muleServerId).getName());
        modelAndView.addObject("muleServerId", muleServerId);
        modelAndView.addObject("muleServerProperties", buildServerProperties(muleServerId));
        modelAndView.addObject("muleServers", muleServerDao.getAllMuleServers());
        return modelAndView;
    }

    @RequestMapping("/mule/server/settings/overview")
    public ModelAndView overview() {
        final ModelAndView modelAndView = new ModelAndView("serverSettingsOverview");
        modelAndView.addObject("muleServers", muleServerDao.getAllMuleServers());
        return modelAndView;
    }

    @RequestMapping(value = "/mule/server/add", method = RequestMethod.POST)
    public ResponseEntity<String> handleAdd(final MuleServer muleServer, final BindingResult bindingResult) {
        LOGGER.info("Attempting to create muleServer={}", muleServer);
        muleServerDao.createMuleServer(muleServer);
        LOGGER.info("Inserted muleServer={}", muleServer);
        muleServerList.populateServers();
        LOGGER.info("Populated mule servers muleServer={}", muleServer);
        return MuleUtils.jsonResponse(muleServer, HttpStatus.OK);
    }

    private Map<String, String> buildServerProperties(final int muleServerId) {
        Map<String, String> result = new LinkedHashMap<>();
        MuleServer muleServer = getMuleServer(muleServerId);
        result.put("id", String.valueOf(muleServer.getId()));
        result.put("name", String.valueOf(muleServer.getName()));
        result.put("serverAddress", String.valueOf(muleServer.getServerAddress()));
        result.put("prefix", String.valueOf(muleServer.getPrefix()));
        return result;
    }

    private MuleServer getMuleServer(final int muleServerId) {
        return muleServerDao.getMuleServer(muleServerId);
    }

    @RequestMapping(value = "/mule/server/settings/edit", method = RequestMethod.POST)
    public ResponseEntity<String> handleEdit(final Property property, final BindingResult bindingResult) {
        LOGGER.info("Request to edit property={}", property);
        MuleServer muleServer = buildCompleteMuleServer(property);
        LOGGER.info("Attempting to update muleServer={}", muleServer);
        muleServerDao.updateMuleServer(muleServer);

        return MuleUtils.jsonResponse(muleServer, HttpStatus.OK);
    }

    /**
     * Builds a complete Mule server.
     *
     * @param property This Mule server has a correct id and one of its attributes are not null
     * @return A mule server with a correct id with none of its attributes null
     */
    private MuleServer buildCompleteMuleServer(final Property property) {
        MuleServer muleServer = getMuleServer(property.getId());

        switch (property.getName()) {
            case "id":
                //Don't set id
                break;
            case "name":
                muleServer.setName(property.getValue());
                break;
            case "serverAddress":
                muleServer.setServerAddress(property.getValue());
                break;
            case "prefix":
                muleServer.setPrefix(property.getValue());
                break;
            default:
                LOGGER.warn("this should never happen");
                break;
        }
        return muleServer;
    }
}
