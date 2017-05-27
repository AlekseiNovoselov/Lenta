package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.List;

public interface TidingDataSource {

    void saveTiding(Tiding news);

    void deleteAllTidings();

    interface LoadNewsCallback {

        void onTidingLoaded(List<Tiding> mews);

        void onDataNotAvailable();
    }

    void getTidings(@NonNull LoadNewsCallback callback);

    void refreshTidings();
}
