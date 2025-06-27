package com.ini.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttribute {

    @ModelAttribute("lang")
    public String setLangAttr(HttpSession session) {
        String lang = (String) session.getAttribute("lang");
        return lang == null ? "ko" : lang;
    }
}