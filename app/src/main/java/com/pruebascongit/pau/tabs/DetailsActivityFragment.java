package com.pruebascongit.pau.tabs;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {


    private String fileSrc;

    private View view;
    private TextView fileSource;
    private EditText textResult;
    private ImageView imageFile;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_details, container, false);

        Intent dataCaught = getActivity().getIntent();

        fileSource = (TextView) view.findViewById(R.id.fileSource);
        textResult = (EditText) view.findViewById(R.id.textResult);
        imageFile = (ImageView) view.findViewById(R.id.imageFile);

        serviceStarter(dataCaught);


        return view;
    }

    private void serviceStarter(Intent dataCaught){

        if(dataCaught != null){

            switch (dataCaught.getStringExtra("caller")){
                case "capturer":
                    fileSrc = dataCaught.getStringExtra("preview");
                    break;
                case "list":
                    break;
            }
        }
    }
}
