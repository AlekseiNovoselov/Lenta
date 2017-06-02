package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TidingRepository implements TidingDataSource {

    private static TidingRepository INSTANCE = null;

    private final TidingDataSource mNewsLocalDataSource;
    private final TidingDataSource mNewsRemoteDataSource;

    Map<String, Tiding> mCachedTidings;

    private boolean mCacheIsDirty = false;

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
    public void saveTiding(@NonNull Tiding tiding) {
        mNewsRemoteDataSource.saveTiding(tiding);
        mNewsLocalDataSource.saveTiding(tiding);

        // Do in memory cache update to keep the app UI up to date
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
    public void getTidings(@NonNull final LoadTidingsCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCachedTidings != null && !mCacheIsDirty) {
            callback.onTidingLoaded(new ArrayList<>(mCachedTidings.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getNewsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mNewsLocalDataSource.getTidings(new LoadTidingsCallback() {
                @Override
                public void onTidingLoaded(List<Tiding> tidings) {
                    refreshCache(tidings);
                    callback.onTidingLoaded(new ArrayList<>(mCachedTidings.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getNewsFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getNewsFromRemoteDataSource(@NonNull final LoadTidingsCallback callback) {
        mNewsRemoteDataSource.getTidings(new LoadTidingsCallback() {
            @Override
            public void onTidingLoaded(List<Tiding> tidings) {
                refreshCache(tidings);
                refreshLocalDataSource(tidings);
                callback.onTidingLoaded(new ArrayList<>(mCachedTidings.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
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
