package com.example.btcqrscanner.ui.keys;



import android.annotation.SuppressLint;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.btcqrscanner.R;
import com.example.btcqrscanner.ui.addresses.Address;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KeysListViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<Key> keys;

    public KeysListViewAdapter(Activity context, ArrayList<Key> keys) {
        super(context, R.layout.key_item, keys.stream()
                .map(Key::getPrivateKey)
                .collect(Collectors.toList()));


        this.context=context;
        this.keys = keys;


    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View rowView=inflater.inflate(R.layout.key_item, null,true);

        TextView keyTextView = rowView.findViewById(R.id.key_item_private_key);

        TextView address1AddressTextView = rowView.findViewById(R.id.key_item_address1_address);
        TextView address2AddressTextView = rowView.findViewById(R.id.key_item_address2_address);
        TextView address3AddressTextView = rowView.findViewById(R.id.key_item_address3_address);
        TextView address4AddressTextView = rowView.findViewById(R.id.key_item_address4_address);

        TextView address1BalanceTextView = rowView.findViewById(R.id.key_item_address1_balance);
        TextView address2BalanceTextView = rowView.findViewById(R.id.key_item_address2_balance);
        TextView address3BalanceTextView = rowView.findViewById(R.id.key_item_address3_balance);
        TextView address4BalanceTextView = rowView.findViewById(R.id.key_item_address4_balance);

        TextView address1DateTextView = rowView.findViewById(R.id.key_item_address1_date);
        TextView address2DateTextView = rowView.findViewById(R.id.key_item_address2_date);
        TextView address3DateTextView = rowView.findViewById(R.id.key_item_address3_date);
        TextView address4DateTextView = rowView.findViewById(R.id.key_item_address4_date);

        keyTextView.setText(keys.get(position).getPrivateKey());

        address1AddressTextView.setText(keys.get(position).getAddressCompressed().getAddress());
        address2AddressTextView.setText(keys.get(position).getAddressUncompressed().getAddress());
        address3AddressTextView.setText(keys.get(position).getAddressBECH32().getAddress());
        address4AddressTextView.setText(keys.get(position).getAddressP2SH().getAddress());

        address1BalanceTextView.setText(keys.get(position).getAddressCompressed().getBalance());
        address2BalanceTextView.setText(keys.get(position).getAddressUncompressed().getBalance());
        address3BalanceTextView.setText(keys.get(position).getAddressBECH32().getBalance());
        address4BalanceTextView.setText(keys.get(position).getAddressP2SH().getBalance());

        address1DateTextView.setText(keys.get(position).getAddressCompressed().getDate());
        address2DateTextView.setText(keys.get(position).getAddressUncompressed().getDate());
        address3DateTextView.setText(keys.get(position).getAddressBECH32().getDate());
        address4DateTextView.setText(keys.get(position).getAddressP2SH().getDate());

        return rowView;

    };
}