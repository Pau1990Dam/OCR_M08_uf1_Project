package com.pruebascongit.pau.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class ListTab extends Fragment {

    private View view;
    private Realm realm;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_tab, container, false);

        ListView listView = (ListView) view.findViewById(R.id.lvParsedFiles);

        /*
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Summary summary = (Summary) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("callFor", "displayFromDB");
                intent.putExtra("preview",summary);
                startActivity(intent);
            }
        });
*/
        return view;
    }

    private void initRealm() {
        // Initialize Realm
        Realm.init(getContext());

        // Configure Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("OCRs.realm")
                .build();

        // Clear the realm from last time
        //Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);
    }


}
