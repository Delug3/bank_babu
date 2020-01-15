package com.bankbabu.balance.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Articles implements Serializable {

    public String title;
    public String content;

    public List<String> information;
    public List<String> moreinformation;

    public Articles() {
        this.title = title;
        this.content = content;
        this.information = information;
        this.moreinformation = moreinformation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getInformation() {
        return information;
    }

    public void setInformation(List<String> information) {
        this.information = information;
    }

    public List<String> getMoreinformation() {
        return moreinformation;
    }

    public void setMoreinformation(List<String> moreinformation) {
        this.moreinformation = moreinformation;
    }
}


