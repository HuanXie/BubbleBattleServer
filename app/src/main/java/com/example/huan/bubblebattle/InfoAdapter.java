package com.example.huan.bubblebattle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by huan on 2016/1/2.
 */
public class InfoAdapter extends ArrayAdapter<Info> {
    public InfoAdapter(Context context, ArrayList<Info> infos) {
        super(context, 0, infos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Info info = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.target_item, parent, false);
        }
        // Lookup view for data population
        TextView item_title = (TextView) convertView.findViewById(R.id.item_title);
        TextView item_value = (TextView) convertView.findViewById(R.id.item_value);
        // Populate the data into the template view using the data object
        item_title.setText(info.title);
        item_value.setText(info.value);
        // Return the completed view to render on screen
        return convertView;
    }
}
