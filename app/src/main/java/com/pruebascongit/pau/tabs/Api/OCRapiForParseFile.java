package com.pruebascongit.pau.tabs.Api;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.*;

import java.io.File;

public class OCRapiForParseFile {

    private static final String BASE_URL = "https://api.ocr.space/parse/image";
    private static final String api_key = "d2c273817d88957";// d2c273817d88957 81831039a288957

    public interface call{
        void onCompleted(Exception e, JsonObject result);
    }

    public static void Call(Context context, File fileToParse, String language,
                            final OCRapiForParseFile.call callback){

        Ion.with(context)
                .load(BASE_URL)
                .setMultipartParameter("apikey", api_key)
                .setMultipartParameter("isOverlayRequired", "false")
                .setMultipartFile(fileToParse.getName(),fileToParse)
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
}