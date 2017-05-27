package com.example.aleksei.novoselovaleksei.ui;

import android.support.annotation.NonNull;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingDataSource;
import com.example.aleksei.novoselovaleksei.data.source.TidingRepository;

import java.util.ArrayList;
import java.util.List;

public class TidingsListPresenter implements TidingsListContract.Presenter {

    private final TidingRepository mTidingsRepository;
    private final TidingsListContract.View mTidingListView;

    private boolean mFirstLoad = true;

    public TidingsListPresenter(@NonNull TidingRepository tidingRepository,
                                @NonNull TidingsListFragment tidingListView) {
        mTidingsRepository = tidingRepository;
        mTidingListView = tidingListView;
        mTidingListView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTidings(false);
    }

    @Override
    public void loadTidings(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTidings(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTidings(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTidingListView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTidingsRepository.refreshTidings();
        }

        mTidingsRepository.getTidings(new TidingDataSource.LoadTidingsCallback() {
            @Override
            public void onTidingLoaded(List<Tiding> news) {
                List<Tiding> newsToShow = new ArrayList<Tiding>();

                // The view may not be able to handle UI updates anymore
                if (!mTidingListView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTidingListView.setLoadingIndicator(false);
                }

                processTasks(newsToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTidingListView.isActive()) {
                    return;
                }
                mTidingListView.showLoadingTidingsError();
            }
        });
    }

    private void processTasks(List<Tiding> newss) {
        if (newss.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mTidingListView.showTidings(newss);
            // Set the filter label's text.
            //showFilterLabel();
        }
    }

    private void processEmptyTasks() {
        mTidingListView.showNoTidings();
    }


}
