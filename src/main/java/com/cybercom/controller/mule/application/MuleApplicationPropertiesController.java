package com.cybercom.controller.mule.application;

import com.cybercom.dao.MuleApplicationDao;
import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleApplication;
import com.cybercom.dao.objects.MuleServer;
import com.cybercom.util.MuleUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
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

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 6/5/13
 * Time: 1:46 PM
 */
@Controller
public class MuleApplicationPropertiesController {
    public static final Logger LOGGER = LoggerFactory.getLogger(MuleApplicationPropertiesController.class);
    @Autowired
    private MuleApplicationDao muleApplicationDao;
    @Autowired
    private MuleServerDao muleServerDao;


    @RequestMapping("/mule/application/properties/view")
    public ModelAndView view() {
        final ModelAndView modelAndView = new ModelAndView("muleApplicationPropertiesView");
        modelAndView.addObject("muleApplications", muleApplicationDao.getMuleApplications());
        return modelAndView;
    }

    @RequestMapping(value = "/mule/application/properties/delete/{applicationName}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteProperty(@PathVariable final String applicationName) {
        ResponseEntity<String> result = null;
        LOGGER.info("Deleting all mule applications with name={}", applicationName);

        muleApplicationDao.removeMuleApplication(applicationName);

        for (MuleServer server : muleServerDao.getAllMuleServers()) {
            String url = server.buildUndeployUrl();
            url += "/" + applicationName;
            WebResource webResource = Client.create().resource(url);
            try {
                webResource.delete();
            } catch (Exception e) {
                LOGGER.info("Could not delete mule application {} from server id={}", applicationName, server.getId());
                LOGGER.info("Exception:", e);
                result = MuleUtils.jsonResponse(applicationName, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (result == null) {
            //Then everything went OK
            result = MuleUtils.jsonResponse(applicationName, HttpStatus.OK);
        }
        return result;

    }

    @RequestMapping(value = "/mule/application/add", method = RequestMethod.POST)
    public ResponseEntity<String> addApplicationToServer(final MuleApplication muleApplication,
                                                         final BindingResult bindingResult) {
        final String muleApplicationName = muleApplication.getName();
        final int serverId = muleApplication.getMuleServerId();
        LOGGER.info("Entering addApplication with application name={} and server id={}", muleApplicationName, serverId);
        MuleApplication result = addApplication(muleApplicationName, serverId);

        return MuleUtils.jsonResponse(result, HttpStatus.OK);
    }

    protected MuleApplication addApplication(final String muleApplicationName, final int muleServerId) {
        final MuleApplication muleApplication = new MuleApplication();
        muleApplication.setName(muleApplicationName);
        muleApplication.setMuleServerId(muleServerId);
        muleApplicationDao.createMuleApplication(muleApplication);

        return muleApplication;
    }

}
