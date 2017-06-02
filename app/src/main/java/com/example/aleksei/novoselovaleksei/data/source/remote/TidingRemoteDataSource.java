package com.example.aleksei.novoselovaleksei.data.source.remote;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import rx.Observable;

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
    public void saveTiding(Tiding tiding) {

    }

    @Override
    public void deleteAllTidings(BaseSource.Source source) {

    }

    private List<Call> mCalls = Collections.synchronizedList(new ArrayList<Call>());

    @Override
    public Observable<List<Tiding>> getTidings() {

//        for (Call call: mCalls) {
//            if (call.isExecuted()) {
//                call.cancel();
//            }
//        }
//        mCalls.clear();



        SourceFactory sourceFactory = new SourceFactory();
        BaseSource baseSource = sourceFactory.getSources().get(0);
        return baseSource.load();

//        for (BaseSource baseSource : sourceFactory.getSources()) {
//            final Call call = baseSource.load(new TidingDataSource.RemoteLoadTidingsCallback() {
//
//                @Override
//                public void onRemoteTidingLoaded(List<Tiding> tidings) {
//                    callback.onTidingLoaded(tidings);
//                }
//
//                @Override
//                public void onRemoteDataNotAvailable() {
//                    callback.onDataNotAvailable();
//                }
//            });
//            //mCalls.add(call);
//        }

    }

    @Override
    public void refreshTidings() {

    }
}
