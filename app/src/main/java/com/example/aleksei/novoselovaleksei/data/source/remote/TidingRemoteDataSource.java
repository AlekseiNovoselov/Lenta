package com.example.aleksei.novoselovaleksei.data.source.remote;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

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
    public void deleteAllTidings() {

    }

    private List<Call> mCalls = Collections.synchronizedList(new ArrayList<Call>());

    @Override
    public void getTidings(@NonNull final LoadTidingsCallback callback) {

        for (Call call: mCalls) {
            if (call.isExecuted()) {
                call.cancel();
            }
            mCalls.remove(call);
        }

        SourceFactory sourceFactory = new SourceFactory();
        for (BaseSource baseSource : sourceFactory.getSources()) {
            final Call call = baseSource.load(new TidingDataSource.RemoteLoadTidingsCallback() {

                @Override
                public void onRemoteTidingLoaded(List<Tiding> tidings) {
                    callback.onTidingLoaded(tidings);
                }

                @Override
                public void onRemoteDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
            mCalls.add(call);
        }
    }

    @Override
    public void refreshTidings() {

    }
}
