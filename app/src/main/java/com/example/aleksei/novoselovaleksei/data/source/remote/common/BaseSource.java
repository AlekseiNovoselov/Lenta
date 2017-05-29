package com.example.aleksei.novoselovaleksei.data.source.remote.common;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public abstract class BaseSource {

    private String mBaseUrl;
    protected DateFormat formatter;

    public BaseSource(String baseUrl) {
        mBaseUrl = baseUrl;
        formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    }

    public Call load(@NonNull final TidingDataSource.RemoteLoadTidingsCallback callback) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        SimpleXmlConverterFactory conv = SimpleXmlConverterFactory.createNonStrict();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)
                .addConverterFactory(conv)
                .build();

        return execute(retrofit, callback);

    }

    abstract protected Call execute(Retrofit retrofit, @NonNull final TidingDataSource.RemoteLoadTidingsCallback callback);


    public enum Source {
        LENTA, GAZETA
    }

    protected abstract Source getSource();
}
