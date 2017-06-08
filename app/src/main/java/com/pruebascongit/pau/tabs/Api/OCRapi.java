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

        String knowIfisUrlorNot;

        if(!img_url.contains(":")){
            knowIfisUrlorNot = "";
        }else
            knowIfisUrlorNot = img_url.substring(0,img_url.indexOf(":"));


        if(knowIfisUrlorNot.equals("http") || knowIfisUrlorNot.equals("https") ){
            Ion.with(context)
                    .load(BASE_URL)
                    .setBodyParameter("apikey", api_key)
                    .setBodyParameter("isOverlayRequired", "false")
                    .setBodyParameter("url", img_url)
                    .setBodyParameter("language", language)
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
        }else {
            File f = new File(img_url);

            Ion.with(context)
                    .load(BASE_URL)
                    .setMultipartParameter("apikey", api_key)
                    .setMultipartParameter("isOverlayRequired", "false")
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

    }

}
