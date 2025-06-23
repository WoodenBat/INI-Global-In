package com.ini.translate.vo;

import lombok.Data;

@Data
public class TranslateRequest {
    private String title;
    private String target;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}