package com.cybercom.uploader;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 5/13/13
 * Time: 3:59 PM
 */
public class UploadItem {

    private CommonsMultipartFile fileData;

    public CommonsMultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(final CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }
}
