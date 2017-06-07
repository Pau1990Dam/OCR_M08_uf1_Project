package com.pruebascongit.pau.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.pruebascongit.pau.tabs.Api.OCRapi;

import java.io.File;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
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

        serviceStarter(dataCaught);


        return view;
    }

    private void serviceStarter(Intent dataCaught) {

        if (dataCaught != null) {

            fileSrc = dataCaught.getStringExtra("preview");

            switch (dataCaught.getStringExtra("callFor")) {
                    case "capture":
                        loadImage();
                        break;
                case "pdf":
                    imageFile.setImageResource(R.drawable.ic_pdf);
                    break;

                    case "displayFromBD":
                        break;
            }
            checkFileSize();


        }
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

    private void loadImage() {
        System.out.println("File source -> "+fileSrc);
        Glide
                .with(getContext())
                .load(fileSrc)
                .apply(fitCenterTransform())
                .into(imageFile);
    }

    private void startDownloadImageParsingResult() {

        String language;
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        language = preferencias.getString("lang", "esp");

        System.out.println("LANGUAGE -> "+language);

        OCRapi.Call(getContext(), fileSrc, language, new OCRapi.call() {

            @Override
            public void onCompleted(Exception e, JsonObject response) {

                if (response != null) {
                    if (e != null) {
                        textResult.setText(e.toString()+"\n"+response.toString());

                        //SUCCESS !! Open new intent!
                    } else {
                        textResult.setText(response.toString());
                        //FAIL!! Show TOAST!
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
 */