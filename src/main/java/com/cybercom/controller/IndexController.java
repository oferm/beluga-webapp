/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybercom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author scr
 */
@Controller
public class IndexController {

    @RequestMapping("/index.html")
    public String handleGetIndex() {
        //forward to view index.jsp
        return "index";
    }
}
