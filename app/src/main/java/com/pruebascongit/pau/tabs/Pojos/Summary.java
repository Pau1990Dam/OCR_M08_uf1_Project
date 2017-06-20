package com.pruebascongit.pau.tabs.Pojos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;


public class Summary extends RealmObject{

    private String id;
    private String fileSrc;
    private String error;
    private String lang;
    private String date;
    private String content;

    public Summary() {
        id = "Object creation time [ "+new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", new Locale("es","ES"))
                .format(new Date())+" ]";
    }

    public Summary(String fileSrc, String error, String lang, String date, String content) {

        id = "Object creation time [ "+new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", new Locale("es","ES"))
                .format(new Date())+" ]";

        this.fileSrc = fileSrc;
        this.error = error;
        this.lang = lang;
        this.date = date;
        this.content = content;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setfileName(String fileName) {
        this.fileSrc = fileName;
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

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Summary{" +
                "id='" + id + '\'' +
                "fileSrc='" + fileSrc + '\'' +
                ", error='" + error + '\'' +
                ", lang='" + lang + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
