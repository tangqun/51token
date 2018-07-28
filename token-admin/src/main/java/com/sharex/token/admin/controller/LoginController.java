package com.sharex.token.admin.controller;

import com.sharex.token.admin.entity.RESTful;
import com.sharex.token.admin.entity.req.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index() {

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RESTful index(Login login) {

        return null;
    }
}
