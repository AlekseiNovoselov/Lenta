package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.List;

public interface TidingDataSource {

    void saveTiding(Tiding news);

    void deleteAllTidings(BaseSource.Source source);

    interface LoadTidingsCallback {

        void onTidingLoaded(List<Tiding> tidings);

        void onDataNotAvailable();
    }

    interface RemoteLoadTidingsCallback {
        void onRemoteTidingLoaded(List<Tiding> tidings);

        void onRemoteDataNotAvailable();
    }

    void getTidings(@NonNull LoadTidingsCallback callback);

    void refreshTidings();
}
