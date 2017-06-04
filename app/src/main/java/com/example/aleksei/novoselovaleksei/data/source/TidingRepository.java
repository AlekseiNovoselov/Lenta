package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.utils.schedulers.BaseSchedulerProvider;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;

public class TidingRepository implements TidingDataSource {

    @Nullable
    private static TidingRepository INSTANCE = null;

    @NonNull
    private final TidingDataSource mLocalDataSource;

    @NonNull
    private final TidingDataSource mRemoteDataSource;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    @Nullable
    Map<String, Tiding> mCachedTidings;

    @VisibleForTesting
    boolean mCacheIsDirty = false;

    private TidingRepository(@NonNull TidingDataSource remoteDataSource,
                             @NonNull TidingDataSource localDataSource,
                             @NonNull BaseSchedulerProvider baseSchedulerProvider) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
        schedulerProvider= baseSchedulerProvider;
    }

    public static TidingRepository getInstance(TidingDataSource remoteDataSource,
                                               TidingDataSource localDataSource,
                                               BaseSchedulerProvider baseSchedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new TidingRepository(remoteDataSource, localDataSource, baseSchedulerProvider);
        }
        return INSTANCE;
    }

    @Override
    public void saveTiding(@NonNull Tiding tiding) {
        mRemoteDataSource.saveTiding(tiding);
        mLocalDataSource.saveTiding(tiding);

        if (mCachedTidings == null) {
            mCachedTidings = new LinkedHashMap<>();
        }
        mCachedTidings.put(tiding.getTitle(), tiding);
    }

    @Override
    public void deleteAllTidings(String source) {
        mRemoteDataSource.deleteAllTidings(source);
        mLocalDataSource.deleteAllTidings(source);

        if (mCachedTidings == null) {
            mCachedTidings = new LinkedHashMap<>();
        }
        mCachedTidings.clear();
    }

    @Override
    public Observable<List<Tiding>> getTidings() {
        if (mCachedTidings != null && !mCacheIsDirty) {
            return Observable.from(mCachedTidings.values()).toList();
        } else if (mCachedTidings == null) {
            mCachedTidings = new LinkedHashMap<>();
        }

        Observable<List<Tiding>> remoteTidings = getAndSaveRemoteTidings();

        if (mCacheIsDirty) {
            return remoteTidings;
        } else {
            Observable<List<Tiding>> localTidings = getAndCacheLocalTidings();
            return Observable.concat(localTidings, remoteTidings)
                    .filter(tidings -> !tidings.isEmpty())
                    .first();
        }
    }

    private Observable<List<Tiding>> getAndCacheLocalTidings() {
        return mLocalDataSource.getTidings()
                .flatMap(tidings -> Observable.from(tidings)
                        .doOnNext(tiding -> mCachedTidings.put(tiding.getTitle(), tiding))
                        .toList());
    }

    private Observable<List<Tiding>> getAndSaveRemoteTidings() {
        return mRemoteDataSource.getTidings()
                .flatMap(tidings1 -> Observable.from(tidings1)
                        .toList()
                        .doOnNext(tidings -> {
                            if (tidings.size() > 0) {
                                refreshCache(tidings);
                                refreshLocalDataSource(tidings);
                            }
                        })
                        .subscribeOn(schedulerProvider.computation()))
                .doOnCompleted(() -> mCacheIsDirty = false);
    }

    private void refreshCache(List<Tiding> tidings) {
        if (mCachedTidings == null) {
            mCachedTidings = new LinkedHashMap<>();
        }
        clear(mCachedTidings, tidings.get(0).getSource());
        for (Tiding tiding : tidings) {
            mCachedTidings.put(tiding.getTitle(), tiding);
        }
        mCacheIsDirty = false;
    }

    private void clear(Map<String, Tiding> mCachedTidings, String source) {
        Set<String> toRemove = new HashSet<>();
        for (Tiding tiding : mCachedTidings.values()) {
            if (tiding.getSource().equals(source)) {
                toRemove.add(tiding.getTitle());
            }
        }
        for (String key : toRemove) {
            if (mCachedTidings.containsKey(key)) {
                mCachedTidings.remove(key);
            }
        }
    }

    private void refreshLocalDataSource(List<Tiding> tidings) {
        mLocalDataSource.deleteAllTidings(tidings.get(0).getSource());
        for (Tiding tiding : tidings) {
            mLocalDataSource.saveTiding(tiding);
        }
    }

    @Override
    public void refreshTidings() {
        mCacheIsDirty = true;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
