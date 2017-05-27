package com.example.aleksei.novoselovaleksei.lenta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aleksei.novoselovaleksei.R;

public class LentaFragment extends Fragment implements LentaContract.View {

    private LentaContract.Presenter mPresenter;

    public LentaFragment() {
        // Requires empty public constructor
    }

    public static LentaFragment newInstance() {
        return new LentaFragment();
    }

    @Override
    public void setPresenter(LentaContract.Presenter presenter) {
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
}
