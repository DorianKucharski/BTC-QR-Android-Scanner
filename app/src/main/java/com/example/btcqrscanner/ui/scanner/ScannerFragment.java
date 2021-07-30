package com.example.btcqrscanner.ui.scanner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.btcqrscanner.MainActivity;
import com.example.btcqrscanner.R;
import com.example.btcqrscanner.databinding.FragmentScannerBinding;
import com.example.btcqrscanner.ui.addresses.Address;
import com.example.btcqrscanner.ui.addresses.AddressesListViewAdapter;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;



public class ScannerFragment extends Fragment {


    private CodeScanner mCodeScanner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.fragment_scanner, container, false);

        CodeScannerView scannerView = root.findViewById(R.id.fragment_scanner_scanner_view);
        assert activity != null;
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);

        mCodeScanner.setDecodeCallback(result -> activity.onScannerResult(result.getText()
                .trim()
                .replace("bitcoin", "")
                .replace(":", "")));


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
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




}