package com.corechan.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;


@Controller
@ApiIgnore
@RequestMapping("")
public class RootController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:/html/Apkdownload.html";
    }
}
