package com.example.aleksei.novoselovaleksei.data.source.remote;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;

public class TidingRemoteDataSource implements TidingDataSource {

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
    public void saveTiding(Tiding news) {

    }

    @Override
    public void deleteAllTidings() {

    }

    @Override
    public void getTidings(@NonNull LoadNewsCallback callback) {

    }

    @Override
    public void refreshTidings() {

    }
}
