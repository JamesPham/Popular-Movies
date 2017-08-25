package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ninhp on 6/18/2017.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    ArrayList<Trailer> trailerList = new ArrayList<>();

    public TrailerAdapter(Context context, int textViewResourceId, ArrayList<Trailer> objects) {
        super(context, textViewResourceId, objects);
        trailerList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.trailer_list_view, null);
        TextView textView = (TextView) v.findViewById(R.id.trailer_text_view);
//        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        textView.setText(trailerList.get(position).getTrailerName());
        return v;

    }

}