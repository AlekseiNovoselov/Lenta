package com.example.aleksei.novoselovaleksei.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.source.NewsDataSource;

public class NewsLocalDataSource implements NewsDataSource {
    private static NewsLocalDataSource INSTANCE;

    // Prevent direct instantiation.
    private NewsLocalDataSource(@NonNull Context context) {

    }

    public static NewsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NewsLocalDataSource(context);
        }
        return INSTANCE;
    }
}
