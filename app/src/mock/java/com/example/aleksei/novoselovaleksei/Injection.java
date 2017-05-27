package com.example.aleksei.novoselovaleksei;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.source.NewsRepository;
import com.example.aleksei.novoselovaleksei.data.source.local.NewsLocalDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.NewsRemoteDataSource;

public class Injection {
    public static NewsRepository provideTasksRepository(@NonNull Context context) {
        return NewsRepository.getInstance(NewsRemoteDataSource.getInstance(),
                NewsLocalDataSource.getInstance(context));
    }
}
