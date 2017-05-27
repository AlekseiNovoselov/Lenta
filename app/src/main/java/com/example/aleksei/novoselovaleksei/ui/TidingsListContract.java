package com.example.aleksei.novoselovaleksei.ui;


import com.example.aleksei.novoselovaleksei.BasePresenter;
import com.example.aleksei.novoselovaleksei.BaseView;
import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.List;

public interface TidingsListContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        boolean isActive();

        void showNoTidings();

        void showLoadingTidingsError();

        void showTidings(List<Tiding> tidings);
    }

    interface Presenter extends BasePresenter {
        void loadTidings(boolean forceUpdate);
    }
}
