package com.pruebascongit.pau.tabs.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

/**
 * Created by pau on 19/06/17.
 */

public class getFileNameFrom {

    public static String Uri(Context context, String StringUri){

        Cursor cursor = null;
        String filename = "";
        Uri uri = Uri.parse(StringUri);

        ContentResolver cR = context.getContentResolver();

        if(uri.getScheme().equals("content")){
            try{
                cursor = cR.query(uri,null,null,null,null);
                if (cursor != null && cursor.moveToFirst()) {
                    filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }
        }else{
            filename = uri.getPath();
            filename = filename.substring(filename.lastIndexOf('/')+1);
        }

        return filename.isEmpty()? "noFilenameDetected" : filename;
    }

    public static String Url(String url){

        String filename = "";

        filename = url.substring(url.lastIndexOf('/')+1);

        return filename.isEmpty()? "noFilenameDetected" : filename;
    }
}
