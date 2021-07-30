package com.example.btcqrscanner.ui.not_recognized;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.btcqrscanner.R;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotRecognizedViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> notRecognized;

    public NotRecognizedViewAdapter(Activity context, ArrayList<String> notRecognized) {
        super(context, R.layout.not_recognized_item, notRecognized);

        this.context=context;
        this.notRecognized = notRecognized;


    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView=inflater.inflate(R.layout.not_recognized_item, null,true);

        TextView notRecognizedTextView = rowView.findViewById(R.id.not_recognized_item_value);
        notRecognizedTextView.setText(notRecognized.get(position));

        return rowView;

    };
}