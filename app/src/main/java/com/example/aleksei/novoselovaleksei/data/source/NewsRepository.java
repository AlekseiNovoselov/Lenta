package com.example.aleksei.novoselovaleksei.data.source;

import android.support.annotation.NonNull;

public class NewsRepository implements NewsDataSource {

    private static NewsRepository INSTANCE = null;

    private final NewsDataSource mNewsLocalDataSource;
    private final NewsDataSource mNewsRemoteDataSource;

    // Prevent direct instantiation.
    private NewsRepository(@NonNull NewsDataSource newsRemoteDataSource,
                            @NonNull NewsDataSource newsLocalDataSource) {
        mNewsRemoteDataSource = newsRemoteDataSource;
        mNewsLocalDataSource = newsLocalDataSource;
    }

    public static NewsRepository getInstance(NewsDataSource newsRemoteDataSource,
                                              NewsDataSource newsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository(newsRemoteDataSource, newsLocalDataSource);
        }
        return INSTANCE;
    }
}
