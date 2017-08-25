package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ninhp on 03-Jul-17.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewItem> {

    ArrayList<ReviewItem> reviewList = new ArrayList<>();

    public ReviewAdapter(Context context, int textViewResourceId, ArrayList<ReviewItem> objects) {
        super(context, textViewResourceId, objects);
        reviewList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_review, null);
        TextView authorTextView = (TextView) v.findViewById(R.id.review_author);
        TextView contentTextView = (TextView) v.findViewById(R.id.review_content);
//        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        authorTextView.setText(reviewList.get(position).getAuthor());
        contentTextView.setText(reviewList.get(position).getContent());
        return v;

    }
}
