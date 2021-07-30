package com.example.btcqrscanner.ui.not_recognized;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.btcqrscanner.MainActivity;
import com.example.btcqrscanner.R;
import com.example.btcqrscanner.databinding.FragmentKeysBinding;
import com.example.btcqrscanner.ui.addresses.AddressesListViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class NotRecognizedFragment extends Fragment {

    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.not_recognized_fragment, container, false);

        assert activity != null;
        Collections.sort(activity.notRecognized);

        NotRecognizedViewAdapter adapter = new NotRecognizedViewAdapter(this.getActivity(), activity.notRecognized);
        list = root.findViewById(R.id.fragment_not_recognized_listView);
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