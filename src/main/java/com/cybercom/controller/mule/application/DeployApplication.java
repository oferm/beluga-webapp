/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.controller.mule.application;

import com.cybercom.dao.MuleServerDao;
import com.cybercom.dao.objects.MuleServer;
import com.cybercom.jmx.MuleServerList;
import com.cybercom.uploader.UploadItem;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/deployApplication")
public class DeployApplication {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MuleServerDao muleServerDao;
    @Autowired
    private MuleServerList muleServerList;

    @RequestMapping(method = RequestMethod.GET)
    public String getUploadForm(final Model model) {
        model.addAttribute(new UploadItem());
        model.addAttribute("uploadLocations", getMuleDeployUrls());
        model.addAttribute("serverNames", muleServerList.getServerNames());
        return "deployApplication";
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(final UploadItem uploadItem, final BindingResult bindingResult) {
        final ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error("Error code: {}. Error message: {}", error.getCode(), error.getDefaultMessage());
            }
            return new ModelAndView("deployApplication");
        }

        final String fileName = uploadItem.getFileData().getFileItem().getName();
        long sizeKb = uploadItem.getFileData().getSize() / 1000;
        logger.info("Upload filename {} with size {} kb", fileName, sizeKb);

        if (!validateUploadedItem(uploadItem)) {
            modelAndView.addObject("uploadComplete", true);
            modelAndView.addObject("fileName", fileName);
            modelAndView.addObject("success", false);
            return modelAndView;
        } else {
            try {
                transferFile(uploadItem);
                modelAndView.addObject("success", true);
            } catch (IOException e) {
                logger.error("Could not deploy file to mule", e);
            }

            modelAndView.addObject("fileName", fileName);
            modelAndView.addObject("uploadComplete", true);

            return modelAndView;
        }

    }

    private boolean validateUploadedItem(final UploadItem uploadItem) {
        boolean result = true;
        final CommonsMultipartFile fileData = uploadItem.getFileData();
        if (!fileData.getOriginalFilename().endsWith(".zip")) {
            logger.info("the uploaded file does not end with .zip");
            result = false;
        }
        if (fileData.isEmpty()) {
            logger.info("The uploaded file is empty");
            result = false;
        }
        if (fileData.getSize() == 0) {
            logger.info("the uploaded file size is 0");
        }

        return result;
    }

    /**
     * Sends the uploadItem to the Mule Server via HTTP POST
     *
     * @param uploadItem The uploaded item
     */
    private void transferFile(final UploadItem uploadItem) throws IOException {

        CommonsMultipartFile fileData = uploadItem.getFileData();
        List<String> muleDeployUrls = getMuleDeployUrls();
        String originalFileName = fileData.getFileItem().getName();
        for (String muleDeployUrl : muleDeployUrls) {
            final File tempFile = moveTempFile(fileData, originalFileName);
            sendToMule(tempFile, muleDeployUrl);
            tempFile.delete();
        }
    }

    /**
     * Sends the file to the Mule Server for deployment
     *
     * @param file          The file to send to Mule
     * @param muleDeployUrl The Mule Server's deploy URL
     */
    private void sendToMule(final File file, final String muleDeployUrl) {
        FileDataBodyPart fdp = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(fdp);
        WebResource webResource = Client.create().resource(muleDeployUrl);
        String resultServer = webResource.type(MediaType.MULTIPART_FORM_DATA).accept(MediaType.TEXT_HTML)
                .post(String.class, formDataMultiPart);
        logger.info("Output from Mule server: {}", resultServer);
    }

    /**
     * Moves the uploaded file to a temporary location
     *
     * @param fileData         The uploaded file
     * @param originalFileName The original file name
     * @return The file at the temporary location with the original filename
     * @throws IOException
     */
    private File moveTempFile(final CommonsMultipartFile fileData, final String originalFileName) throws IOException {
        String deployFileName = buildDeployFileName(originalFileName);
        logger.info("Moving to {}", deployFileName);
        final File tempFile = new File(deployFileName);
        fileData.transferTo(tempFile);
        return tempFile;
    }

    /**
     * Builds the final file name with the directory being java's temp dir
     *
     * @param originalFileName The original file name
     * @return The complete path to the new file.
     */
    private String buildDeployFileName(final String originalFileName) {
        String tempDir = System.getProperty("java.io.tmpdir");
        StringBuilder builder = new StringBuilder();
        builder.append(tempDir);
        builder.append("/");
        builder.append(originalFileName);
        return builder.toString();
    }

    private List<String> getMuleDeployUrls() {
        final List<String> result = new ArrayList<>();
        List<MuleServer> muleServers = muleServerDao.getAllMuleServers();
        for (MuleServer muleServer : muleServers) {
            result.add(muleServer.buildDeployUrl());
        }

        return result;
    }

    @RequestMapping("success")
    public ModelAndView success() {
        final ModelAndView modelAndView = new ModelAndView("deployApplication");
        modelAndView.addObject("result", "success");
        return modelAndView;
    }
}
