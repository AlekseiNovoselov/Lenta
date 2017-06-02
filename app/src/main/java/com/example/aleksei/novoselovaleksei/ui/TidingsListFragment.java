package com.example.aleksei.novoselovaleksei.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.ArrayList;
import java.util.List;

public class TidingsListFragment extends Fragment implements TidingsListContract.View {

    private TidingsListContract.Presenter mPresenter;

    RecyclerView recyclerView;
    TidingsListAdapter adapter;

    public TidingsListFragment() {
        // Requires empty public constructor
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
        View root = inflater.inflate(R.layout.lenta_fragment, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.tidingsList);
        LinearLayoutManager layoutMgr = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutMgr);

        List<Tiding> tidings =  new ArrayList<>();
        adapter = new TidingsListAdapter(tidings);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNoTidings() {

    }

    @Override
    public void showLoadingTidingsError() {
        int a = 2;
    }

    @Override
    public void showTidings(List<Tiding> tidings) {
        adapter = new TidingsListAdapter(tidings);
        recyclerView.setAdapter(adapter);
    }
}
