package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.utils.schedulers.BaseSchedulerProvider;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;

public class TidingRepository implements TidingDataSource {

    private static TidingRepository INSTANCE = null;

    private final TidingDataSource mNewsLocalDataSource;
    private final TidingDataSource mNewsRemoteDataSource;
    private final BaseSchedulerProvider schedulerProvider;

    private Map<String, Tiding> mCachedTidings;

    private boolean mCacheIsDirty = false;

    private TidingRepository(@NonNull TidingDataSource newsRemoteDataSource,
                             @NonNull TidingDataSource newsLocalDataSource,
                             @NonNull BaseSchedulerProvider baseSchedulerProvider) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;
        schedulerProvider= baseSchedulerProvider;
    }

    public static TidingRepository getInstance(TidingDataSource newsRemoteDataSource,
                                               TidingDataSource newsLocalDataSource,
                                               BaseSchedulerProvider baseSchedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new TidingRepository(newsRemoteDataSource, newsLocalDataSource, baseSchedulerProvider);
        }
        return INSTANCE;
    }

    @Override
    public void saveTiding(@NonNull Tiding tiding) {
        mNewsRemoteDataSource.saveTiding(tiding);
        mNewsLocalDataSource.saveTiding(tiding);

        if (mCachedTidings == null) {
            mCachedTidings = new LinkedHashMap<>();
        }
        mCachedTidings.put(tiding.getTitle(), tiding);
    }

    @Override
    public void deleteAllTidings(String source) {
        mNewsRemoteDataSource.deleteAllTidings(source);
        mNewsLocalDataSource.deleteAllTidings(source);

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

        Observable<List<Tiding>> remoteTasks = getAndSaveRemoteTasks();

        if (mCacheIsDirty) {
            return remoteTasks;
        } else {
            Observable<List<Tiding>> localTasks = getAndCacheLocalTasks();
            return Observable.concat(localTasks, remoteTasks)
                    .filter(tasks -> !tasks.isEmpty())
                    .first();
        }
    }

    private Observable<List<Tiding>> getAndCacheLocalTasks() {
        return mNewsLocalDataSource.getTidings()
                .flatMap(tasks -> Observable.from(tasks)
                        .doOnNext(tiding -> mCachedTidings.put(tiding.getTitle(), tiding))
                        .toList());
    }

    private Observable<List<Tiding>> getAndSaveRemoteTasks() {
        return mNewsRemoteDataSource.getTidings()
                .flatMap(tasks -> Observable.from(tasks)
                        .toList()
                        .doOnNext(tidings -> {
                            if (tidings.size() > 0) {
                                refreshCache(tidings);
                                refreshLocalDataSource(tidings);
                            }
                        }).subscribeOn(schedulerProvider.computation()))
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
        mNewsLocalDataSource.deleteAllTidings(tidings.get(0).getSource());
        for (Tiding news : tidings) {
            mNewsLocalDataSource.saveTiding(news);
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
