package com.example.aleksei.novoselovaleksei.data.source.remote.lenta;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RssRetrofitAdapter {
    String RSS_LENTA_LINK = "http://lenta.ru/";
    @GET("/rss")
    Call<RssLentaFeed> getItems();
}
