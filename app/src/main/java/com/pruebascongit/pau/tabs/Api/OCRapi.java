package com.pruebascongit.pau.tabs.Api;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

/**
 * Created by pau on 7/06/17.
 */

public class OCRapi {

    private static final String BASE_URL = "https://api.ocr.space/parse/image";
    private static final String api_key = "d2c273817d88957";// d2c273817d88957 81831039a288957

    public interface call{
        void onCompleted(Exception e, JsonObject result);
    }

    public static void Call(Context context, String img_url, String language,
                            final call callback){

        System.out.println("Entered");

        JsonObject json = new JsonObject();
        json.addProperty("apikey", api_key);
        json.addProperty("isOverlayRequired", false);
        json.addProperty("url", "http://dl.a9t9.com/blog/ocr-online/screenshot.jpg");
        json.addProperty("language", "eng");

        //"http://dl.a9t9.com/blog/ocr-online/screenshot.jpg"

        /*
        .setMultipartParameter("goop", "noop")
.setMultipartFile("archive", "application/zip", new File("/sdcard/filename.zip"))
         */
        /*
        Ion.with(context)
                .load(BASE_URL)
                .setBodyParameter("apikey", api_key)
                .setBodyParameter("isOverlayRequired", "false")
                .setBodyParameter(type, img_url)
                .setBodyParameter("language", "eng")
                //.setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("Call recieved -> ");
                        System.out.println(result.toString());

                        if(callback != null){
                            callback.onCompleted(e,result);
                        }
                    }
                });
*/
        File f = new File(img_url);

        Ion.with(context)
                .load(BASE_URL)
                .setMultipartParameter("apikey", api_key)
                .setMultipartParameter("isOverlayRequired", "false")
               // .setMultipartParameter(type, "")
                .setMultipartFile(f.getName(),f)
                .setMultipartParameter("language",language)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        System.out.println("Call recieved -> ");
                        System.out.println(result.toString());

                        if(callback != null){
                            callback.onCompleted(e,result);
                        }
                    }
                });
    }



    /*

     */
}
/*
     Ion.with(context)
                .load(BASE_URL)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("Call recieved -> ");
                        System.out.println(result.toString());
                    }
                });



 */