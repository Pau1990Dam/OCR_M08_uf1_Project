package com.pruebascongit.pau.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pruebascongit.pau.tabs.Api.OCRapi;
import com.pruebascongit.pau.tabs.GsonUtils.JsonResponseToSummaryPojo;
import com.pruebascongit.pau.tabs.Pojos.Summary;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {


    private String fileSrc;

    private View view;
    private TextView fileSource;
    private EditText textResult;
    private ImageView imageFile;
    int iCount;
    private File image;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_details, container, false);

        Intent dataCaught = getActivity().getIntent();



        fileSource = (TextView) view.findViewById(R.id.fileSource);
        textResult = (EditText) view.findViewById(R.id.textResult);
        textResult.setEnabled(false);
        imageFile = (ImageView) view.findViewById(R.id.imageFile);
        imageFile.setImageResource(R.drawable.ic_camera);

        /*textResult.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textResult.requestFocus();
                        if(iCount == 1){
                            textResult.postDelayed(new Runnable() {
                                public void run() {
                                    InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    manager.showSoftInput(textResult, 0);
                                    textResult.selectAll();
                                }
                            }, 200);
                        }
                        iCount++;
                        break;

                    default:
                        break;
                }
                if(iCount >= 2) return false;
                else return true;
            }
        });*/

        if(dataCaught!= null) {

            if (dataCaught.getAction() != null) {
                externalIntentFilter(dataCaught);
            } else {
                explicitIntent(dataCaught);
            }
        }


        return view;
    }

    private void externalIntentFilter(Intent intentFilter){

        String action = intentFilter.getAction();
        String type = intentFilter.getType();

        // File from url
        if (Intent.ACTION_SEND.equals(action) && type != null) {

            String sharedText = intentFilter.getStringExtra(Intent.EXTRA_TEXT);

            if (sharedText != null) {

                switch (sharedText.substring(sharedText.lastIndexOf("."))){

                    case ".png":
                    case ".jpg":
                    case ".jpeg":
                    case ".bmp":
                    case ".gif":
                        fileSrc = sharedText;
                        serviceStarter("capture");
                        break;

                    case ".pdf":
                        fileSrc = sharedText;
                        serviceStarter("pdf");
                        break;

                    default:
                        textResult.setText(R.string.invalid_url);
                }

            }

        } else if (Intent.ACTION_VIEW.equals(action) && type != null) {

            Uri fileUri = intentFilter.getData();

            if (fileUri != null) {

                fileSrc = intentFilter.getData().getPath();
                serviceStarter("pdf");
            }
        }

    }

    private void  explicitIntent (Intent dataCaught){

        fileSrc = dataCaught.getStringExtra("preview");
        String mode = dataCaught.getStringExtra("callFor");

        serviceStarter(mode);

    }

    private void serviceStarter(String mode) {

        switch (mode) {

                case "capture":
                    loadImage();
                    checkFileSize();
                    break;

                case "pdf":
                    setPdfDrawable();
                    checkFileSize();
                    break;

                case "displayFromBD":
                    break;
            }


    }


    private void setPdfDrawable(){
        imageFile.setImageResource(R.drawable.ic_pdf);
    }

    private void loadImage() {
        System.out.println("File source -> "+fileSrc);
        Glide
                .with(getContext())
                .load(fileSrc)
                .apply(fitCenterTransform())
                .into(imageFile);
    }

    private void checkFileSize(){

        long imageSize =( ((new File(fileSrc)).length())/1024);
        System.out.println("IMAGESIZE -> "+imageSize);
        if(imageSize <= 1024 ) {
            fileSource.setText(fileSrc);
            startDownloadImageParsingResult();
        }else {
            textResult.setText(R.string.file_too_big);
        }
    }


    private void startDownloadImageParsingResult() {

        final String language;
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        language = preferencias.getString("lang", "esp");

        System.out.println("LANGUAGE -> "+language);

        OCRapi.Call(getContext(), fileSrc, language, new OCRapi.call() {

            @Override
            public void onCompleted(Exception e, JsonObject response) {

                if (response != null) {
                    Summary summary = JsonResponseToSummaryPojo.parseJson(response,fileSrc,language);
                    if (e != null) {
                        textResult.setText(e.toString()+"\n"+summary.getError());
                        //FAIL!! Show TOAST!
                    } else {

                        textResult.setText(summary.getContent());
                    }
                }else
                    textResult.setText(R.string.unknow_fail);

            }
        });
    }

}
/*
 SharedPreferences preferencias= PreferenceManager.getDefaultSharedPreferences(getContext());
        if(preferencias.getString("idioma_peliculas","es").equals("es")){
            llengua="es";
        }else
            llengua="en";

        if(preferencias.getString("lista_peliculas","populars").equals("populars")){
            apiSolicitadora.getPeliculesPopulars(adptador,llengua);
        }if(preferencias.getString("lista_peliculas","top_rated").equals("top_rated")) {
            apiSolicitadora.getPeliculesMillorValorades(adptador, llengua);
        }



DownloadUtility.getId(this, "ID", spinnerObj, new DownloadUtility.customCallBack() {
@Override
public void onCompleted(Exception e, Response<JsonObject> response) {
    if (response != null) {
        if (response.getHeaders().code() == 200) {
            //SUCCESS !! Open new intent!
        } else {
            //FAIL!! Show TOAST!
        }
}




        //Intent-filter

        Intent intentFilter = getActivity().getIntent();

        if(intentFilter!=null) {
            String action = intentFilter.getAction();
            String type = intentFilter.getType();


            if (Intent.ACTION_SEND.equals(action) && type != null) {
                // Uri data = intentFilter.getData();
                System.out.println("Tipo -> " + type +
                        "\n");
                String sharedText = intentFilter.getStringExtra(Intent.EXTRA_TEXT);

                if (sharedText != null) {
                    System.out.println("Text -> " + sharedText);
                    // Update UI to reflect text being shared
                }

            } else if (Intent.ACTION_VIEW.equals(action) && type != null) {

                Uri fileUri = intentFilter.getData();
                if(fileUri != null){
                    String filePath = intentFilter.getData().getPath();
                }
            }
        }

 */