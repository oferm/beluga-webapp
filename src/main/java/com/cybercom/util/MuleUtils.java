package com.cybercom.util;

import com.cybercom.dao.MuleServerDao;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 6/3/13
 * Time: 11:02 AM
 */
public class MuleUtils {

    private final MuleServerDao muleServerDao;

    public MuleUtils(final MuleServerDao muleServerDao) {
        this.muleServerDao = muleServerDao;
    }

    public String stripPrefix(final int muleServerId, final String string) {
        return string.replace(muleServerDao.getMuleServer(muleServerId).getPrefix(), "");
    }

    public static ResponseEntity<String> jsonResponse(final Object o, final HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return new ResponseEntity<String>(json, headers, httpStatus);
    }
}
