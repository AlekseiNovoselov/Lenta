package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.List;

public interface TidingDataSource {

    void saveTiding(Tiding news);

    void deleteAllTidings();

    interface LoadTidingsCallback {

        void onTidingLoaded(List<Tiding> tidings);

        void onDataNotAvailable();
    }

    interface GetTidingCallback {

        void onTaskLoaded(Tiding tiding);

        void onDataNotAvailable();
    }

    void getTidings(@NonNull LoadTidingsCallback callback);

    void refreshTidings();
}
