package com.example.aleksei.novoselovaleksei.data.source;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.List;

import rx.Observable;

public interface TidingDataSource {

    void saveTiding(Tiding tiding);

    void deleteAllTidings(BaseSource.Source source);

    interface LoadTidingsCallback {

        void onTidingLoaded(List<Tiding> tidings);

        void onDataNotAvailable();
    }

    interface RemoteLoadTidingsCallback {
        void onRemoteTidingLoaded(List<Tiding> tidings);

        void onRemoteDataNotAvailable();
    }

    Observable<List<Tiding>> getTidings();

    void refreshTidings();
}
