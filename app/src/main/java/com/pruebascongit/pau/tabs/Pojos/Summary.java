package com.pruebascongit.pau.tabs.Pojos;

/**
 * Created by pau on 7/06/17.
 */

public class Summary {

    private String fileName;
    private String error;
    private String lang;
    private String date;
    private String content;

    public Summary() {
    }

    public Summary(String fileName, String error, String lang, String date, String content) {
        this.fileName = fileName;
        this.error = error;
        this.lang = lang;
        this.date = date;
        this.content = content;
    }

    public String getfileName() {
        return fileName;
    }

    public void setfileName(String fileName) {
        this.fileName= fileName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "fileName='" + fileName + '\'' +
                ", error='" + error + '\'' +
                ", lang='" + lang + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
