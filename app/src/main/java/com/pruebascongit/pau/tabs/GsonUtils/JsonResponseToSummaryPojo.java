package com.pruebascongit.pau.tabs.GsonUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pruebascongit.pau.tabs.Pojos.Summary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pau on 9/06/17.
 */

public class JsonResponseToSummaryPojo {

     public static Summary parseJson(JsonObject json, String fileName, String lang){

         Gson gson = new Gson();
         JsonObject root = gson.fromJson( json, JsonObject.class);
         String jsonString = json.toString();

         Summary summary = new Summary();
         summary.setContent(content(jsonString));
         summary.setError(exitErrorCode(jsonString));
         summary.setDate(date());
         summary.setfileName(fileName.substring(fileName.indexOf("/")+1));
         summary.setLang(lang);

         return summary;
     }

    private static String exitErrorCode(String json){

        JsonElement jelement = new JsonParser().parse(json);
        JsonObject  jobjRoot = jelement.getAsJsonObject();

        switch (jobjRoot.get("OCRExitCode").getAsString()){
            case "1":
                return "File parsing completed successfully";
            case "2":
                return "File partially parsed";
            case "3":
                return "Failed parsing";
            case "4":
            default:
                return "Error ocurred when attempting parse";
        }

    }

    private static String content(String json){

        JsonElement jelement = new JsonParser().parse(json);
        JsonObject  jobjRoot = jelement.getAsJsonObject();
        JsonArray parsedResults = jobjRoot.getAsJsonArray("ParsedResults");
        JsonObject textOverlay = parsedResults.get(0).getAsJsonObject();

        return textOverlay.get("ParsedText").getAsString();
    }

    private static String date(){

        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", new Locale("es","ES")).format(new Date());
    }


}
