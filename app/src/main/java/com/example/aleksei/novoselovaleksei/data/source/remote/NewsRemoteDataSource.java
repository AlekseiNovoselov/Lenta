package com.example.aleksei.novoselovaleksei.data.source.remote;

import com.example.aleksei.novoselovaleksei.data.source.NewsDataSource;

public class NewsRemoteDataSource implements NewsDataSource {

    private static NewsRemoteDataSource INSTANCE;

    // Prevent direct instantiation.
    private NewsRemoteDataSource() {}

    public static NewsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NewsRemoteDataSource();
        }
        return INSTANCE;
    }
}
