package com.example.aleksei.novoselovaleksei.data.source.remote;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.gazeta.GazetaSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.lenta.LentaSource;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;

public class TidingRemoteDataSource implements TidingDataSource {

    private static TidingRemoteDataSource INSTANCE;

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
    public void deleteAllTidings(BaseSource.Source source) {

    }

    @Override
    public Observable<List<Tiding>> getTidings() {

        BaseSource lentaSource = new LentaSource();
        BaseSource gazetaSource = new GazetaSource();

        return Observable.zip(lentaSource.load(), gazetaSource.load(),
                (list1, list2) -> {
                    List<Tiding> combinedList = new ArrayList<>(list1);
                    combinedList.addAll(list2);
                    return combinedList;
                });
    }

    @Override
    public void refreshTidings() {

    }
}
