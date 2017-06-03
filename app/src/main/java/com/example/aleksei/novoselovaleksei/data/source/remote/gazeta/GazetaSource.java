package com.example.aleksei.novoselovaleksei.data.source.remote.gazeta;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.RssBaseItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

import static com.example.aleksei.novoselovaleksei.data.source.remote.gazeta.RssGazetaRetrofitAdapter.RSS_GAZETA_LINK;

public class GazetaSource extends BaseSource {

    public GazetaSource() {
        super(RSS_GAZETA_LINK);
    }

    @Override
    protected Observable<List<Tiding>> execute(Retrofit retrofit) {
        return Observable.create(subscriber -> {
            RssGazetaRetrofitAdapter retrofitService = retrofit.create(RssGazetaRetrofitAdapter.class);
            Call<RssGazetaFeed> call = retrofitService.getItems();
            call.enqueue(new Callback<RssGazetaFeed>() {
                @Override
                public void onResponse(Call<RssGazetaFeed> call, Response<RssGazetaFeed> response) {
                    RssGazetaFeed feed = response.body();
                    List<RssGazetaItem> mItems = feed.getChannel().getGazetaItemList();

                    GazetaSource source = new GazetaSource();
                    List<Tiding> tidings = source.getTidings(mItems);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(tidings);
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onFailure(Call<RssGazetaFeed> call, Throwable t) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new ArrayList<>());
                        subscriber.onCompleted();
                    }
                }
            });
        });
    }

    public List<Tiding> getTidings(List<RssGazetaItem> mItems) {
        List<Tiding> tidings = new ArrayList<>();
        for (RssBaseItem item: mItems) {
            try {
                String title = item.getTitle();
                Date date = formatter.parse(item.getPublicationDate());
                String description = item.getDescription();
                tidings.add(new Tiding(title, date.getTime(), description, null, getSource()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tidings;
    }

}
