package com.pruebascongit.pau.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class ListTab extends Fragment{

    private View view;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_tab, container, false);

        ListView listView = (ListView) view.findViewById(R.id.lvParsedFiles);

        String test [] = {
                "Blue Ward",
                "Bog Wraith",
                "Blue Ward",
                "Bog Wraith",
                "Blue Ward",
                "Bog Wraith"
        };


        items = new ArrayList<>(Arrays.asList(test));
        adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.row,
                R.id.rowText,
                items
        );

        listView.setAdapter(adapter);


        return view;
    }
}
