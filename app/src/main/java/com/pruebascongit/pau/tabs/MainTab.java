package com.pruebascongit.pau.tabs;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class MainTab extends Fragment implements View.OnClickListener {

    String seleccioString;

    static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final int ACTIVITAT_SELECCIONAR_EXPLORADOR = 2;


    private View view;
    private ImageButton filePicker;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_tab, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.photoFab);
        filePicker = (ImageButton) view.findViewById(R.id.filePicker);

        filePicker.setOnClickListener(this);
        fab.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.photoFab:
                dispatchTakePictureIntent();
                break;

            case R.id.filePicker:
                showFileChooser();
                break;

            default:
                break;
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                seleccioString =  Uri.fromFile(photoFile).toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES+"/MyOCRMapp");
        if(!storageDir.exists() && !storageDir.mkdirs()){
            Log.d("Fail","failed to create directory");
            System.out.println("MAAAAALLLLLLLL!!!! ");
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getPath()),"image/*|application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select an image or pdf with textual content"),
                    ACTIVITAT_SELECCIONAR_EXPLORADOR);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    startOCRintent("capture");
                }

                break;

            case ACTIVITAT_SELECCIONAR_EXPLORADOR:
                if (resultCode == RESULT_OK) {

                    Uri seleccio = data.getData();
                    seleccioString = seleccio.toString();

                    ContentResolver cR = getContext().getContentResolver();

                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(seleccio));

                    System.out.println("Type -> "+type);

                    if(type.equals("pdf"))
                        startOCRintent("pdf");
                    else
                        startOCRintent("capture");
                }

            default:
                System.out.println("Default entry! ");
                break;
        }

    }

    private void startOCRintent(String type){

        Intent intent = new Intent(getContext(),DetailsActivity.class);
        intent.putExtra("callFor",type);
        intent.putExtra("preview",seleccioString);
        startActivity(intent);
    }



}
/*
 Cursor returnCursor =
                                getContext().getContentResolver().query(audoUri, null, null, null, null);
                        try {
                            InputStream input = getContext().getContentResolver().openInputStream(audoUri);
                            File target = new File("anonimous_temp.pdf");
                            OutputStream outputStream = new FileOutputStream(target);
                            int read = 0;
                            byte[] bytes = new byte[1024];
                            while ((read = input.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, read);
                            }
                            System.out.println("File size -> "+target.length());;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                             try {
                    InputStream input = getContext().getContentResolver().openInputStream(intentFilter.getData());
                    RequestBody.create(
                            MediaType.parse(getContext().getContentResolver().getType(intentFilter.getData())),
                            input
                    );
        byte [] file = readBytes(input);

        } catch (FileNotFoundException e) {
                e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                }



                    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }


 */