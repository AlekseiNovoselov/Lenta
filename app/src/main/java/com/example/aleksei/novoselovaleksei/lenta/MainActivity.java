package com.example.aleksei.novoselovaleksei.lenta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aleksei.novoselovaleksei.Injection;
import com.example.aleksei.novoselovaleksei.R;
import com.example.aleksei.novoselovaleksei.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    private LentaPresenter mLentaPresenter;

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LentaFragment lentaFragment =
                (LentaFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (lentaFragment == null) {
            // Create the fragment
            lentaFragment = LentaFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), lentaFragment, R.id.contentFrame);
        }

        // Create the presenter
        mLentaPresenter = new LentaPresenter(
                Injection.provideTasksRepository(getApplicationContext()), lentaFragment);


    }
}
