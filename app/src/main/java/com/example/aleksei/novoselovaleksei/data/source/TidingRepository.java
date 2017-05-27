package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TidingRepository implements TidingDataSource {

    private static TidingRepository INSTANCE = null;

    private final TidingDataSource mNewsLocalDataSource;
    private final TidingDataSource mNewsRemoteDataSource;

    Map<String, Tiding> mCachedNews;

    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
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
    public void saveTiding(@NonNull Tiding news) {
        mNewsRemoteDataSource.saveTiding(news);
        mNewsLocalDataSource.saveTiding(news);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedNews == null) {
            mCachedNews = new LinkedHashMap<>();
        }
        mCachedNews.put(news.getId(), news);
    }

    @Override
    public void deleteAllTidings() {
        mNewsRemoteDataSource.deleteAllTidings();
        mNewsLocalDataSource.deleteAllTidings();

        if (mCachedNews == null) {
            mCachedNews = new LinkedHashMap<>();
        }
        mCachedNews.clear();
    }

    @Override
    public void getTidings(@NonNull final LoadNewsCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCachedNews != null && !mCacheIsDirty) {
            callback.onTidingLoaded(new ArrayList<>(mCachedNews.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getNewsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mNewsLocalDataSource.getTidings(new LoadNewsCallback() {
                @Override
                public void onTidingLoaded(List<Tiding> tasks) {
                    refreshCache(tasks);
                    callback.onTidingLoaded(new ArrayList<>(mCachedNews.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getNewsFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getNewsFromRemoteDataSource(@NonNull final LoadNewsCallback callback) {
        mNewsRemoteDataSource.getTidings(new LoadNewsCallback() {
            @Override
            public void onTidingLoaded(List<Tiding> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTidingLoaded(new ArrayList<>(mCachedNews.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Tiding> newss) {
        if (mCachedNews == null) {
            mCachedNews = new LinkedHashMap<>();
        }
        mCachedNews.clear();
        for (Tiding news : newss) {
            mCachedNews.put(news.getId(), news);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Tiding> newss) {
        mNewsLocalDataSource.deleteAllTidings();
        for (Tiding news : newss) {
            mNewsLocalDataSource.saveTiding(news);
        }
    }

    @Override
    public void refreshTidings() {

    }
}
