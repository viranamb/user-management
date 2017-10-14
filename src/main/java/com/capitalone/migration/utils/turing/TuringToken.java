package com.capitalone.migration.utils.turing;

public class TuringToken {

    private String tokenizedSsn;

    private String id;

    private String text;

    private String developerText;

    public String getTokenizedSsn() {
        return tokenizedSsn;
    }

    public void setTokenizedSsn(String tokenizedSsn) {
        this.tokenizedSsn = tokenizedSsn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDeveloperText() {
        return developerText;
    }

    public void setDeveloperText(String developerText) {
        this.developerText = developerText;
    }
}