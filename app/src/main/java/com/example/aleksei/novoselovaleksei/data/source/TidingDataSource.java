package com.example.aleksei.novoselovaleksei.data.source;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.List;

import rx.Observable;

public interface TidingDataSource {

    void saveTiding(Tiding tiding);

    void deleteAllTidings(String source);

    interface LoadTidingsCallback {

        void onTidingLoaded(List<Tiding> tidings);

        void onDataNotAvailable();
    }

    Observable<List<Tiding>> getTidings();

    void refreshTidings();
}
