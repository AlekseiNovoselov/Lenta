package com.example.aleksei.novoselovaleksei.data.source.remote.gazeta;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RssGazetaRetrofitAdapter {
    String RSS_GAZETA_LINK = "http://www.gazeta.ru/";
    @GET("/export/rss/lenta.xml")
    Call<RssGazetaFeed> getItems();
}
