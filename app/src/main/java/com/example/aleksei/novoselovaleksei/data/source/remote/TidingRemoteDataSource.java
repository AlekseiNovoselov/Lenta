package com.example.aleksei.novoselovaleksei.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;

import java.util.Arrays;
import java.util.List;

public class TidingRemoteDataSource implements TidingDataSource {

    private List<Tiding> TIDINGS = Arrays.asList(new Tiding("Title3", "Description3"),
            new Tiding("Title4", "Description4"));

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static TidingRemoteDataSource INSTANCE;

    // Prevent direct instantiation.
    private TidingRemoteDataSource() {}

    public static TidingRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TidingRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void saveTiding(Tiding tiding) {

    }

    @Override
    public void deleteAllTidings() {

    }

    @Override
    public void getTidings(@NonNull final LoadTidingsCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTidingLoaded(TIDINGS);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void refreshTidings() {

    }
}
