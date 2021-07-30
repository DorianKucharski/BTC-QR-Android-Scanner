package com.example.btcqrscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;


import com.example.btcqrscanner.ui.addresses.Address;
import com.example.btcqrscanner.ui.keys.Key;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btcqrscanner.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import info.blockchain.api.APIException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import info.blockchain.api.blockexplorer.BlockExplorer;


public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    public ArrayList<Address> addresses = new ArrayList<>();
    public ArrayList<Key> keys = new ArrayList<>();
    public ArrayList<String> notRecognized = new ArrayList<>();
    ArrayList<String> scanned = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.btcqrscanner.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }

        drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_scanner, R.id.nav_keys, R.id.nav_addresses, R.id.nav_not_recognized)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupWithNavController(navigationView, navController);

        checkPermissions();

        loadData();


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                onClearButtonClick();
                return true;

            case R.id.menu_export:
                exportData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    private void exportData() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");

        i.putExtra(Intent.EXTRA_SUBJECT, "BTC QR Scanner");
        i.putExtra(Intent.EXTRA_TEXT, dataToString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public String dataToString() {
        StringBuilder s = new StringBuilder("Private keys:\n");
        for (Key k : keys) {
            s.append(k.toEmail());
            s.append("\n");
        }
        s.append("--------------------------------------------------------------------------\n");
        s.append("Addresses:\n");
        for (Address a : addresses) {
            s.append(a.toEmail());
        }
        s.append("\n --------------------------------------------------------------------------\n");
        s.append("Not recognized:\n");
        for (String n : notRecognized) {
            s.append(n);
            s.append("\n");
        }
        return s.toString();
    }

    private void onClearButtonClick() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete all data?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearData();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void clearData() {
        addresses.clear();
        keys.clear();
        scanned.clear();
        notRecognized.clear();
        saveData();
    }

    private void loadData() {
        loadList("addresses.bin", addresses);
        loadList("keys.bin", keys);
        loadList("notRecognized.bin", notRecognized);
        getScannedStringsFromCollections();
    }

    private void saveData() {
        saveList("addresses.bin", addresses);
        saveList("keys.bin", keys);
        saveList("notRecognized.bin", notRecognized);
    }

    private void saveList(String filename, ArrayList<?> arrayList) {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(arrayList);
            os.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadList(String filename, ArrayList<?> arrayList) {
        try {
            FileInputStream fis = this.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            arrayList.addAll(Objects.requireNonNull(arrayList.getClass().cast(is.readObject())));
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScannedStringsFromCollections() {
        for (Key k : keys) {
            scanned.addAll(k.getAddresses());
        }
        scanned.addAll(addresses.stream().map(Address::getAddress).collect(Collectors.toList()));
        scanned.addAll(notRecognized);
    }


    public void checkPermissions() {
        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.CAMERA};

        Dexter.withContext(this).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }

        }).check();
    }

    void makeToast(String string) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show());
    }


    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    public Address getAddress(String address) throws IOException, JSONException, ParseException {
        String url = "https://api.blockcypher.com/v1/btc/main/addrs/" + address;
        String result = doGetRequest(url);

        JSONObject obj = new JSONObject(result);
        int balance = 0;
        int lastTransactionDate = 0;

        if (obj.getInt("n_tx") > 0) {
            balance = obj.getInt("balance");
            JSONObject lastTx = new JSONObject(obj.getJSONArray("txrefs").get(0).toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            Date date = format.parse(lastTx.getString("confirmed"));
            assert date != null;
            lastTransactionDate = (int) (date.getTime() / 1000);
        }

        return new Address(address, balance, lastTransactionDate);
    }

    public Key getKey(String privateKey) throws IOException, JSONException, ParseException {
        Key key = new Key(privateKey);

        key.addressUncompressed = getAddress(key.addressUncompressed.getAddress());
        key.addressCompressed = getAddress(key.addressCompressed.getAddress());
        key.addressP2SH = getAddress(key.addressP2SH.getAddress());
        key.addressBECH32 = getAddress(key.addressBECH32.getAddress());

        return key;
    }


    public void onScannerResult(String result) {
        makeToast("scanned");

        if (scanned.contains(result)) {
            return;
        }

        scanned.add(result);

        int type = Validator.validate(result);

        if (type == Validator.NOT_KNOWN) {
            notRecognized.add(result);
            saveData();
            return;
        }

        new Thread(() -> {
            try {
                if (Validator.isAddress(type)) {
                    addresses.add(getAddress(result));
                    saveData();
                } else if (Validator.isPrivateKey(type)) {
                    keys.add(getKey(result));
                    saveData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}