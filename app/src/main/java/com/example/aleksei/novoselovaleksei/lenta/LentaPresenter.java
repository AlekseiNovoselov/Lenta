package com.example.aleksei.novoselovaleksei.lenta;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.source.NewsRepository;

public class LentaPresenter implements LentaContract.Presenter {

    private final NewsRepository mNewsRepository;
    private final LentaContract.View mLentaView;

    public LentaPresenter(@NonNull NewsRepository newsRepository,@NonNull LentaFragment lentaFragment) {
        mNewsRepository = newsRepository;
        mLentaView = lentaFragment;
        mLentaView.setPresenter(this);
    }

    @Override
    public void start() {
        // TODO - load news;
    }


}
