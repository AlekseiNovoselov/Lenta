package com.example.aleksei.novoselovaleksei.data.source.remote.lenta;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

import static com.example.aleksei.novoselovaleksei.data.source.remote.lenta.RssRetrofitAdapter.RSS_LENTA_LINK;

public class LentaSource extends BaseSource {

    public LentaSource() {
        super(RSS_LENTA_LINK);
    }

    protected Observable<List<Tiding>> execute(Retrofit retrofit) {
        return Observable.create(subscriber -> {
            RssRetrofitAdapter retrofitService = retrofit.create(RssRetrofitAdapter.class);
            Call<RssLentaFeed> call = retrofitService.getItems();
            call.enqueue(new Callback<RssLentaFeed>() {
                @Override
                public void onResponse(Call<RssLentaFeed> call, Response<RssLentaFeed> response) {
                    RssLentaFeed feed = response.body();
                    List<RssLentaItem> mItems = feed.getChannel().getLentaItemList();

                    LentaSource source = new LentaSource();
                    List<Tiding> tidings = source.getTidings(mItems);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(tidings);
                        subscriber.onCompleted();
                    }

                }

                @Override
                public void onFailure(Call<RssLentaFeed> call, Throwable t) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new ArrayList<>());
                        subscriber.onCompleted();
                    }
                }
            });
        });
    }

    private List<Tiding> getTidings(List<RssLentaItem> mItems) {
        List<Tiding> tidings = new ArrayList<>();
        for (RssLentaItem item: mItems) {
            try {
                String title = item.getTitle();
                Date date = formatter.parse(item.getPublicationDate());
                String description = item.getDescription();
                Enclosure enclosure = item.getEnclosure();
                String imageUrl = null;
                if (enclosure != null)  {
                    imageUrl = enclosure.getUrl();
                }
                tidings.add(new Tiding(title, date.getTime(), description, imageUrl, getSource()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return tidings;
    }
}
