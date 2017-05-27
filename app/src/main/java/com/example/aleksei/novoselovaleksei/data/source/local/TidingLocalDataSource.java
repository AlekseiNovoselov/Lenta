package com.example.aleksei.novoselovaleksei.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;

public class TidingLocalDataSource implements TidingDataSource {
    private static TidingLocalDataSource INSTANCE;

    // Prevent direct instantiation.
    private TidingLocalDataSource(@NonNull Context context) {

    }

    public static TidingLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TidingLocalDataSource(context);
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
    public void getTidings(@NonNull LoadTidingsCallback callback) {

    }

    @Override
    public void refreshTidings() {

    }
}
