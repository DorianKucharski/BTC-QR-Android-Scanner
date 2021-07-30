package com.example.btcqrscanner.ui.addresses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btcqrscanner.MainActivity;
import com.example.btcqrscanner.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class AddressesFragment extends Fragment {

    ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.fragment_addresses, container, false);

        assert activity != null;
        Collections.sort(activity.addresses);

        AddressesListViewAdapter adapter = new AddressesListViewAdapter(this.getActivity(), activity.addresses);
        list = root.findViewById(R.id.fragment_addresses_listView);
        list.setAdapter(adapter);
        return root;

    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}