package com.ini.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
	public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return cleanXSS(super.getParameter(name));
    }

    private String cleanXSS(String value) {
        if (value != null) {
            value = value.replace("<", "&lt;")
                         .replace(">", "&gt;")
                         .replace("\"", "&quot;")
                         .replace("'", "&#x27;");
        }
        return value;
    }
}
