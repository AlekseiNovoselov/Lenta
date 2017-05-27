package com.example.aleksei.novoselovaleksei.ui;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.TidingRepository;

import java.util.ArrayList;
import java.util.List;

public class TidingsListPresenter implements TidingsListContract.Presenter {

    private final TidingRepository mNewsRepository;
    private final TidingsListContract.View mLentaView;

    private boolean mFirstLoad = true;

    public TidingsListPresenter(@NonNull TidingRepository newsRepository, @NonNull TidingsListFragment lentaFragment) {
        mNewsRepository = newsRepository;
        mLentaView = lentaFragment;
        mLentaView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTidings(false);
    }

    @Override
    public void loadTidings(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mLentaView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mNewsRepository.refreshTidings();
        }

        mNewsRepository.getTidings(new TidingDataSource.LoadNewsCallback() {
            @Override
            public void onTidingLoaded(List<Tiding> news) {
                List<Tiding> newsToShow = new ArrayList<Tiding>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
//                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
//                    EspressoIdlingResource.decrement(); // Set app as idle.
//                }

                // The view may not be able to handle UI updates anymore
                if (!mLentaView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mLentaView.setLoadingIndicator(false);
                }

                processTasks(newsToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mLentaView.isActive()) {
                    return;
                }
                mLentaView.showLoadingTidingsError();
            }
        });
    }

    private void processTasks(List<Tiding> newss) {
        if (newss.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mLentaView.showTidings(newss);
            // Set the filter label's text.
            //showFilterLabel();
        }
    }

    private void processEmptyTasks() {
        mLentaView.showNoTidings();
    }


}
