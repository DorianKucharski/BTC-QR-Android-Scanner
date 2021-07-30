package com.example.btcqrscanner;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class BlockchainCallback implements Callback {


    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        String responseString = Objects.requireNonNull(response.body()).string();
        process(responseString);
    }

    public abstract void process(String response);
}
