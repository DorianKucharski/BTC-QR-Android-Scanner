package com.example.btcqrscanner.ui.addresses;

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

public class AddressesListViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<Address> addresses;

    public AddressesListViewAdapter(Activity context, ArrayList<Address> addresses) {
        super(context, R.layout.address_item, addresses.stream()
                .map(Address::getAddress)
                .collect(Collectors.toList()));


        this.context=context;
        this.addresses = addresses;


    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView=inflater.inflate(R.layout.address_item, null,true);

        TextView addressTextView = rowView.findViewById(R.id.address_item_address);
        TextView balanceTextView = rowView.findViewById(R.id.address_item_balance);
        TextView dateTextView = rowView.findViewById(R.id.address_item_date);

        addressTextView.setText(addresses.get(position).getAddress());
        balanceTextView.setText(addresses.get(position).getBalance());
        dateTextView.setText(addresses.get(position).getDate());

        return rowView;

    };
}  