package com.example.aleksei.novoselovaleksei.ui;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.ui.holders.helper.DescriptionExpandableView;
import com.example.aleksei.novoselovaleksei.ui.holders.helper.TidingItemExpandableView;

import java.util.ArrayList;
import java.util.List;

public class TidingsListFragment extends Fragment implements TidingsListContract.View {

    private TidingsListContract.Presenter mPresenter;

    private RecyclerView recyclerView;
    private TidingListAdapter adapter;

    private SwipeRefreshLayout srl;

    private Bundle mSavedInstanceState;

    private View root;
    private View noTidings;

    public TidingsListFragment() {

    }

    public static TidingsListFragment newInstance() {
        return new TidingsListFragment();
    }

    @Override
    public void setPresenter(TidingsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.tiding_list_fragment, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.tidings_rv);
        noTidings = root.findViewById(R.id.no_tidings);
        recyclerView.addItemDecoration(new MarginDecoration(getContext()));
        LinearLayoutManager layoutMgr = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutMgr);

        srl = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) srl;
        swipeRefreshLayout.setScrollUpChild(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.refreshTidings());

        List<Tiding> tidings =  new ArrayList<>();
        showTidings(tidings);

        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoTidings() {
        recyclerView.setVisibility(View.GONE);
        noTidings.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoInternet() {
        showMessage(getString(R.string.no_internet_error));
    }

    @Override
    public void showLoadingTidingsError() {
        showMessage(getString(R.string.loading_tidings_error));
    }

    private void showMessage(String message) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showTidings(List<Tiding> tidings) {
        List<TidingItemExpandableView> groups = new ArrayList<>();

        for (Tiding tiding : tidings) {
            List<DescriptionExpandableView> items = new ArrayList<>();
            items.add(new DescriptionExpandableView(tiding.getDescription()));
            TidingItemExpandableView tidingV = new TidingItemExpandableView(tiding.getTitle(), items);
            groups.add(tidingV);
        }

        adapter = new TidingListAdapter(groups, tidings);
        recyclerView.setAdapter(adapter);

        if (mSavedInstanceState != null) {
            adapter.onRestoreInstanceState(mSavedInstanceState);
        }

        recyclerView.setVisibility(View.VISIBLE);
        noTidings.setVisibility(View.GONE);
    }

    public class MarginDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public MarginDecoration(Context context) {
            margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
        }

        @Override
        public void getItemOffsets(
                Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(margin, margin, margin, margin);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedInstanceState = savedInstanceState;
        }
    }

    @Override
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}
