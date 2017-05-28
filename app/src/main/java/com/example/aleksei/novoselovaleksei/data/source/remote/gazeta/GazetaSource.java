package com.example.aleksei.novoselovaleksei.data.source.remote.gazeta;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.RssBaseItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.aleksei.novoselovaleksei.data.source.remote.gazeta.RssGazetaRetrofitAdapter.RSS_GAZETA_LINK;

public class GazetaSource extends BaseSource {

    public GazetaSource() {
        super(RSS_GAZETA_LINK);
    }

    @Override
    protected Call execute(Retrofit retrofit, final @NonNull TidingDataSource.RemoteLoadTidingsCallback callback) {
        RssGazetaRetrofitAdapter retrofitService = retrofit.create(RssGazetaRetrofitAdapter.class);
        Call<RssGazetaFeed> call = retrofitService.getItems();
        call.enqueue(new Callback<RssGazetaFeed>() {
            @Override
            public void onResponse(Call<RssGazetaFeed> call, Response<RssGazetaFeed> response) {
                RssGazetaFeed feed = response.body();
                List<RssGazetaItem> mItems = feed.getChannel().getGazetaItemList();

                GazetaSource source = new GazetaSource();
                List<Tiding> tidings = source.getTidings(mItems);
                callback.onRemoteTidingLoaded(tidings);
            }

            @Override
            public void onFailure(Call<RssGazetaFeed> call, Throwable t) {
                callback.onRemoteDataNotAvailable();
            }
        });
        return call;
    }

    @Override
    public Source getSource() {
        return Source.GAZETA;
    }

    public List<Tiding> getTidings(List<RssGazetaItem> mItems) {
        List<Tiding> tidings = new ArrayList<>();
        for (RssBaseItem item: mItems) {
            String title = item.getTitle();
            String publicationDate = item.getPublicationDate();
            String description = item.getDescription();
            tidings.add(new Tiding(title, publicationDate, description, null, getSource()));
        }
        return tidings;
    }

}
