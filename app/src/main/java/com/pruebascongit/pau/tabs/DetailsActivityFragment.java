package com.pruebascongit.pau.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.pruebascongit.pau.tabs.Api.OCRapiForParseFile;
import com.pruebascongit.pau.tabs.Api.OCRapiForParseUrl;
import com.pruebascongit.pau.tabs.GsonUtils.JsonResponseToSummaryPojo;
import com.pruebascongit.pau.tabs.Pojos.Summary;
import com.pruebascongit.pau.tabs.Utils.getFileNameFrom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {


    private String fileSrc;
    private String filename;
    private View view;
    private TextView fileSource;
    private EditText textResult;
    private ImageView imageFile;
    private Summary summaryFromListView;

    public DetailsActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        summaryFromListView = null;
        setHasOptionsMenu(true);
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

        if(dataCaught!= null) {

            //Si es un intent extern intent.getAction() tindrá el vañor "ACTION_SEND" o "ACTION_VIEW"
            if (dataCaught.getAction() != null) {
                externalIntentFilter(dataCaught);
            } else {
                explicitIntent(dataCaught);
            }
        }


        return view;
    }

    /**
     * Filtramos el intent externo (intent-filter) para saber si se trata de una imagen o pdf en
     * una url o uri.
     * @param intentFilter
     */
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
                        serviceStarter("urlImage");
                        break;

                    case ".pdf":
                        fileSrc = sharedText;
                        serviceStarter("urlPdf");
                        break;

                    default:
                        textResult.setText(R.string.invalid_url);
                }

            }
            filename = getFileNameFrom.Url(fileSrc);
            //Pdf file from local o external storage i.e. uri
        } else if (Intent.ACTION_VIEW.equals(action) && type != null) {

            Uri fileUri = intentFilter.getData();

            if (fileUri != null) {

                fileSrc = fileUri.toString();

                filename = getFileNameFrom.Uri(getContext(), fileSrc);

                serviceStarter("pdf");
            }
        }

    }

    /**
     * Filtramos el intent que viene del app para saber si se trata de una foto o pdf a subir para
     * parsear o de un parseado almacenado en la bd que queremos ver o editar.
     * @param dataCaught
     */
    private void  explicitIntent (Intent dataCaught){


        String mode = dataCaught.getStringExtra("callFor");

        if(!mode.equals("displayFromDB"))
            fileSrc = dataCaught.getStringExtra("preview");
        else
            summaryFromListView = (Summary) dataCaught.getSerializableExtra("preview");

        serviceStarter(mode);

    }

    /**
     *
     * @param mode
     */
    private void serviceStarter(String mode) {

        switch (mode) {

                case "urlImage":
                    loadImage(false);
                    startDownloadingUrlParsingResult();
                    break;

                case "urlPdf":
                    setPdfDrawable();
                    startDownloadingUrlParsingResult();
                    break;

                case "capture":
                    loadImage(true);
                    //filename = getFileNameFrom.Uri(getContext(), fileSrc);
                    checkFileSize();
                    break;

                case "pdf":
                    setPdfDrawable();
                    checkFileSize();
                    break;

                case "displayFromDB":
                    setPdfDrawable();
                    fillLayoutViews();
                    break;
            }


    }


    private void setPdfDrawable(){
        imageFile.setImageResource(R.drawable.ic_pdf);
    }

    private void loadImage(boolean isUri) {
        System.out.println("Uri source -> "+fileSrc);

        if(isUri)
            Glide
                    .with(getContext())
                    .load(Uri.parse(fileSrc))
                    .apply(fitCenterTransform())
                    .into(imageFile);
        else
            Glide
                    .with(getContext())
                    .load(fileSrc)
                    .apply(fitCenterTransform())
                    .into(imageFile);
    }

    private void checkFileSize(){

        File fileToUpload = convertUriToFile();

        System.out.println("CheckFile Size  -> "+filename);

        if(fileToUpload != null && fileToUpload.length()/1024 <= 1024){

            startDownloadingFileParsingResult(fileToUpload);
        }

    }

    private void startDownloadingFileParsingResult(File fileToUpload) {
        final String language;
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        language = preferencias.getString("lang", "eng");

        System.out.println("LANGUAGE -> "+language+"; AND Filename -> "+fileToUpload.getName());

        OCRapiForParseFile.Call(getContext(), fileToUpload, language, new OCRapiForParseFile.call()
        {

            @Override
            public void onCompleted(Exception e, JsonObject response) {

                if (response != null) {
                    Summary summary = JsonResponseToSummaryPojo.parseJson(response,fileSrc,language);
                    if (e != null) {
                        textResult.setText(e.toString()+"\n"+summary.getError());
                        //FAIL!! Show TOAST!
                    } else {
                        //summaryFromListView = summary;
                        textResult.setText(summary.getContent());
                        System.out.println("Summary "+summary.getFileSrc());
                        fileSource.setText(fileSrc);
                    }
                }else
                    textResult.setText(R.string.unknow_fail);

            }
        });

        fileToUpload.deleteOnExit();
    }

    private void startDownloadingUrlParsingResult() {

        final String language;
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        language = preferencias.getString("lang", "eng");

        System.out.println("LANGUAGE -> "+language);

        OCRapiForParseUrl.Call(getContext(), fileSrc, language, new OCRapiForParseUrl.call() {

            @Override
            public void onCompleted(Exception e, JsonObject response) {

                if (response != null) {
                    Summary summary = JsonResponseToSummaryPojo.parseJson(response,fileSrc,language);
                    if (e != null) {
                        textResult.setText(e.toString()+"\n"+summary.getError());
                        //FAIL!! Show TOAST!
                    } else {
                        //summaryFromListView = summary;
                        textResult.setText(summary.getContent());
                        fileSource.setText(fileSrc);
                    }
                }else
                    textResult.setText(R.string.unknow_fail);

            }
        });


    }

    private void fillLayoutViews(){
        textResult.setText(summaryFromListView.getContent());
        fileSource.setText(summaryFromListView.getFileSrc());
    }

    public File convertUriToFile() {

        if (!fileSrc.isEmpty()) {
            Uri uri = Uri.parse(fileSrc);

            File f = createTempFile();

            writeUriContentToFile(f, uri);

            Toast.makeText(getContext(), "Tamaño del archivo -> " +f.getName()+" "+ f.length(), Toast.LENGTH_LONG).show();

            return f;

        } else{
            Toast.makeText(getContext(), "La uri no se ha  pillado -> " + fileSrc, Toast.LENGTH_LONG).show();
        }

        return null;

    }


    private void  writeUriContentToFile(File cacheFile, Uri uri){

        InputStream input = null;
        FileOutputStream outputStream = null;
        try {
            input =  getActivity().getContentResolver().openInputStream(uri);
            outputStream= new FileOutputStream(cacheFile, false);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.getFD().sync();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createTempFile(){

        File f = new File(getContext().getCacheDir(),"cacheData");

        f.mkdirs();

        filename = getFileNameFrom.Uri(getContext(),fileSrc);

        System.out.println("Whats my madafuking filename -> "+filename);

        File cacheFile = new File(f,filename);

        cacheFile.deleteOnExit();

        System.out.println("Whats my madafuking name -> "+cacheFile.getName());

        return cacheFile;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(isAdded())
            inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Si ve  de la bd no es pot editar, si no ve de la bd, es pot editar
        if(summaryFromListView != null) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.save) {

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


/*
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
*/

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





         private void startDownloadingFileParsingResult(File fileToUpload) {

        final String language;
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        language = preferencias.getString("lang", "eng");

        System.out.println("LANGUAGE -> "+language);

        OCRapiForParseFile.Call(getContext(), fileToUpload, language, new OCRapi.call() {

            @Override
            public void onCompleted(Exception e, JsonObject response) {

                if (response != null) {
                    Summary summary = JsonResponseToSummaryPojo.parseJson(response,fileSrc,language);
                    if (e != null) {
                        textResult.setText(e.toString()+"\n"+summary.getError());
                        //FAIL!! Show TOAST!
                    } else {
                        //summaryFromListView = summary;
                        textResult.setText(summary.getContent());
                        fileSource.setText(fileSrc);
                    }
                }else
                    textResult.setText(R.string.unknow_fail);

            }
        });
    }

    private void startDownloadingUrlParsingResult() {

        final String language;
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());

        language = preferencias.getString("lang", "eng");

        System.out.println("LANGUAGE -> "+language);

        OCRapiForParseUrl.Call(getContext(), fileSrc, language, new OCRapi.call() {

            @Override
            public void onCompleted(Exception e, JsonObject response) {

                if (response != null) {
                    Summary summary = JsonResponseToSummaryPojo.parseJson(response,fileSrc,language);
                    if (e != null) {
                        textResult.setText(e.toString()+"\n"+summary.getError());
                        //FAIL!! Show TOAST!
                    } else {
                        //summaryFromListView = summary;
                        textResult.setText(summary.getContent());
                        fileSource.setText(fileSrc);
                    }
                }else
                    textResult.setText(R.string.unknow_fail);

            }
        });

    }



          }else {
            try {
                cacheFile = File.createTempFile(
                        filename// filename.substring(0,filename.lastIndexOf(".")
                        ,filename.substring(filename.lastIndexOf(".")+1)
                        ,f);
cacheFile =
        cacheFile.renameTo()
        cacheFile.deleteOnExit();
        return  cacheFile;
        } catch (IOException e) {
        e.printStackTrace();
        }
        }

        return null;

*/
