package com.example.aleksei.novoselovaleksei;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.source.TidingRepository;
import com.example.aleksei.novoselovaleksei.data.source.local.TidingLocalDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.TidingRemoteDataSource;
import com.example.aleksei.novoselovaleksei.utils.schedulers.BaseSchedulerProvider;
import com.example.aleksei.novoselovaleksei.utils.schedulers.SchedulerProvider;

public class Injection {

    public static TidingRepository provideTasksRepository(@NonNull Context context) {
        return TidingRepository.getInstance(TidingRemoteDataSource.getInstance(),
                TidingLocalDataSource.getInstance(context, provideSchedulerProvider()), provideSchedulerProvider());
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
