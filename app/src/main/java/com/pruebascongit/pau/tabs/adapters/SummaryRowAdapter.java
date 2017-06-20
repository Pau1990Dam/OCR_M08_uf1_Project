package com.pruebascongit.pau.tabs.adapters;

import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListAdapter;
import android.widget.TextView;

import com.pruebascongit.pau.tabs.Pojos.Summary;
import com.pruebascongit.pau.tabs.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;


/**
 * Created by pau on 12/06/17.
 */

public class SummaryRowAdapter extends RealmBaseAdapter<Summary> implements ListAdapter {

    public SummaryRowAdapter(@Nullable OrderedRealmCollection<Summary> realmResults) {
        super(realmResults);
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.row, viewGroup, false);
        }

        if(this.adapterData != null){

            Summary summary = adapterData.get(position);

            TextView imageName = (TextView) view.findViewById(R.id.imageName);
            TextView imageData = (TextView) view.findViewById(R.id.imageDate);

            imageName.setText(summary.getFileSrc());
            imageData.setText(summary.getDate());


        }
        return view;
    }
}
/*

        // Magic is this.Magic
        if (this.adapterData != null) {

            // Get item
            Book item  = adapterData.get(position);

            TextView name = (TextView) convertView.findViewById(R.id.mTitle);
            TextView age = (TextView) convertView.findViewById(R.id.author);
            ImageView available = (ImageView) convertView.findViewById(R.id.available);

            // Populate view
            name.setText(item.getTitle());
            age.setText(item.getAuthor());

            if (item.isAvailable()) {
                available.setImageDrawable(ContextCompat.getDrawable(available.getContext(),
                        R.drawable.ic_available));
            } else {
                available.setImageDrawable(ContextCompat.getDrawable(available.getContext(),
                        R.drawable.ic_no_available));
            }

        }
        return convertView;
 */