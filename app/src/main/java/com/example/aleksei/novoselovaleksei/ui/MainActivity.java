package com.example.aleksei.novoselovaleksei.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aleksei.novoselovaleksei.Injection;
import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private TidingsListPresenter mLentaPresenter;

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TidingsListFragment tidingListFragment =
                (TidingsListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (tidingListFragment == null) {
            // Create the fragment
            tidingListFragment = TidingsListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tidingListFragment, R.id.contentFrame);
        }

        // Create the presenter
        mLentaPresenter = new TidingsListPresenter(
                Injection.provideTasksRepository(getApplicationContext()),
                tidingListFragment,
                Injection.provideSchedulerProvider());


    }
}
