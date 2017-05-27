package com.example.aleksei.novoselovaleksei.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.data.Tiding;

import java.util.List;

public class TidingsListFragment extends Fragment implements TidingsListContract.View {

    private TidingsListContract.Presenter mPresenter;

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
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.lenta_fragment, container, false);


        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showNoTidings() {

    }

    @Override
    public void showLoadingTidingsError() {

    }

    @Override
    public void showTidings(List<Tiding> tidings) {

    }
}
