package com.example.aleksei.novoselovaleksei.ui;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.TidingRepository;
import com.example.aleksei.novoselovaleksei.utils.schedulers.BaseSchedulerProvider;
import com.example.aleksei.novoselovaleksei.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TidingsListPresenterTest {

    private static List<Tiding> TIDINGS;

    @Mock
    private TidingRepository mTidingsRepository;

    @Mock
    private TidingsListContract.View mTidingView;

    private BaseSchedulerProvider mSchedulerProvider;

    private TidingsListPresenter mTidingPresenter;

    @Before
    public void setupTasksPresenter() {
        MockitoAnnotations.initMocks(this);
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mTidingPresenter = new TidingsListPresenter(mTidingsRepository, mTidingView, mSchedulerProvider);
        when(mTidingView.isActive()).thenReturn(true);
        TIDINGS = Arrays.asList(
                new Tiding("Title1", 100L,
                        "Description1", null, "lenta.ru"),
                new Tiding("Title2", 200L,
                        "Description2", null, "gazeta.ru"));
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        mTidingPresenter = new TidingsListPresenter(mTidingsRepository, mTidingView, mSchedulerProvider);
        verify(mTidingView).setPresenter(mTidingPresenter);
    }

    @Test
    public void loadAllTasksFromRepositoryAndLoadIntoView() {
        when(mTidingsRepository.getTidings()).thenReturn(Observable.just(TIDINGS));
        mTidingPresenter.loadTidings(true);

        verify(mTidingView).setLoadingIndicator(true);
        verify(mTidingView).setLoadingIndicator(false);
    }

    @Test
    public void errorLoadingTasks_ShowsError() {
        when(mTidingsRepository.getTidings()).thenReturn(Observable.error(new Exception()));
        mTidingPresenter.loadTidings(true);
        verify(mTidingView).showLoadingTidingsError();
    }

}