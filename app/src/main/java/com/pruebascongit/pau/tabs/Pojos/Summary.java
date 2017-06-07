package com.pruebascongit.pau.tabs.Pojos;

/**
 * Created by pau on 7/06/17.
 */

public class Summary {

    private String fileSource;
    private String error;
    private String lang;
    private String date;
    private String content;

    public Summary() {
    }

    public Summary(String fileSource, String error, String lang, String date, String content) {
        this.fileSource = fileSource;
        this.error = error;
        this.lang = lang;
        this.date = date;
        this.content = content;
    }

    public String getFileSource() {
        return fileSource;
    }

    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
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
                "fileSource='" + fileSource + '\'' +
                ", error='" + error + '\'' +
                ", lang='" + lang + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
