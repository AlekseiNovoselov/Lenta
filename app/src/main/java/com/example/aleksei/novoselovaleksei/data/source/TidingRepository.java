package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

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

    Map<String, Tiding> mCachedTidings;

    private boolean mCacheIsDirty = false;

    private TidingRepository(@NonNull TidingDataSource newsRemoteDataSource,
                             @NonNull TidingDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;
    }

    public static TidingRepository getInstance(TidingDataSource newsRemoteDataSource,
                                               TidingDataSource newsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TidingRepository(newsRemoteDataSource, newsLocalDataSource);
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
    public void deleteAllTidings(BaseSource.Source source) {
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
                            refreshCache(tidings);
                            refreshLocalDataSource(tidings);
                        }))
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

    private void clear(Map<String, Tiding> mCachedTidings, BaseSource.Source source) {
        Set<String> toRemove = new HashSet<>();
        for (Tiding tiding : mCachedTidings.values()) {
            if (tiding.getSource() == source) {
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
