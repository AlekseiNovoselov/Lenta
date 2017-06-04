package com.example.aleksei.novoselovaleksei.data.source;

import com.example.aleksei.novoselovaleksei.data.Tiding;
import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.example.aleksei.novoselovaleksei.Injection.provideSchedulerProvider;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TidingRepositoryTest {

    private List<Tiding> TIDINGS = Arrays.asList(
            new Tiding("Title1", 100L,
                    "Description1", null, "lenta.ru"),
            new Tiding("Title2", 200L,
                    "Description2", null, "gazeta.ru"));

    private TidingRepository mTidingRepository;

    private TestSubscriber<List<Tiding>> mTidingsTestSubscriber;

    @Mock
    private TidingDataSource mTidingRemoteDataSource;

    @Mock
    private TidingDataSource mTidingLocalDataSource;

    @Before
    public void setupTidingsRepository() {
        MockitoAnnotations.initMocks(this);
        mTidingRepository= TidingRepository.getInstance(
                mTidingRemoteDataSource, mTidingLocalDataSource, provideSchedulerProvider());
        mTidingsTestSubscriber = new TestSubscriber<>();
    }

    @After
    public void destroyRepositoryInstance() {
        TidingRepository.destroyInstance();
    }

    @Test
    public void getTasks_repositoryCachesAfterFirstSubscription_whenTidingsAvailableInLocalStorage() {

        setTidingsAvailable(mTidingLocalDataSource, TIDINGS);
        setTidingsNotAvailable(mTidingRemoteDataSource);

        TestSubscriber<List<Tiding>> testSubscriber1 = new TestSubscriber<>();
        mTidingRepository.getTidings().subscribe(testSubscriber1);

        TestSubscriber<List<Tiding>> testSubscriber2 = new TestSubscriber<>();
        mTidingRepository.getTidings().subscribe(testSubscriber2);

        verify(mTidingRemoteDataSource).getTidings();
        verify(mTidingLocalDataSource).getTidings();

        assertFalse(mTidingRepository.mCacheIsDirty);
        testSubscriber1.assertValue(TIDINGS);
        testSubscriber2.assertValue(TIDINGS);
    }

    @Test
    public void getTasks_repositoryCachesAfterFirstSubscription_whenTidingsAvailableInRemoteStorage() {

        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);
        setTidingsNotAvailable(mTidingLocalDataSource);

        mTidingRepository.getTidings().subscribe(mTidingsTestSubscriber);

        verify(mTidingRemoteDataSource).getTidings();
        verify(mTidingLocalDataSource).getTidings();

        assertFalse(mTidingRepository.mCacheIsDirty);
        mTidingsTestSubscriber.assertValue(TIDINGS);
    }

    private void setTidingsNotAvailable(TidingDataSource dataSource) {
        when(dataSource.getTidings()).thenReturn(Observable.just(Collections.emptyList()));
    }

    private void setTidingsAvailable(TidingDataSource dataSource, List<Tiding> tidings) {
        when(dataSource.getTidings()).thenReturn(Observable.just(tidings).concatWith(Observable.never()));
    }

    @Test
    public void getTidings_requestsAllTidingsFromLocalDataSource() {
        setTidingsAvailable(mTidingLocalDataSource, TIDINGS);
        setTidingsNotAvailable(mTidingRemoteDataSource);

        mTidingRepository.getTidings().subscribe(mTidingsTestSubscriber);

        verify(mTidingLocalDataSource).getTidings();
        mTidingsTestSubscriber.assertValue(TIDINGS);
    }

    @Test
    public void deleteAllTidings_deleteTidingsToServiceAPIUpdatesCache() {

        mTidingRepository.mCachedTidings = new LinkedHashMap<>();
        mTidingRepository.mCachedTidings.put("TITLE1",
                new Tiding("TITLE1",100L,
                        "description1", null, "lenta.ru"));
        mTidingRepository.mCachedTidings.put("TITLE2",
                new Tiding("TITLE2", 200L,
                        "description2", null, "gazeta.ru"));

        mTidingRepository.deleteAllTidings(TIDINGS.get(0).getSource());

        verify(mTidingRemoteDataSource).deleteAllTidings(TIDINGS.get(0).getSource());
        verify(mTidingLocalDataSource).deleteAllTidings(TIDINGS.get(0).getSource());

        assertThat(mTidingRepository.mCachedTidings.size(), is(0));
    }


    @Test
    public void getTidingsWithDirtyCache_tidingsAreRetrievedFromRemote() {

        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);
        mTidingRepository.refreshTidings();
        mTidingRepository.getTidings().subscribe(mTidingsTestSubscriber);

        verify(mTidingLocalDataSource, never()).getTidings();
        verify(mTidingRemoteDataSource).getTidings();
        mTidingsTestSubscriber.assertValue(TIDINGS);
    }


    @Test
    public void getTidingsWithLocalDataSourceUnavailable_tidingsAreRetrievedFromRemote() {
        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);
        setTidingsNotAvailable(mTidingLocalDataSource);
        mTidingRepository.getTidings().subscribe(mTidingsTestSubscriber);

        verify(mTidingRemoteDataSource).getTidings();
        mTidingsTestSubscriber.assertValue(TIDINGS);
    }


    @Test
    public void getTidingsWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        setTidingsNotAvailable(mTidingLocalDataSource);
        setTidingsNotAvailable(mTidingRemoteDataSource);

        mTidingRepository.getTidings().subscribe(mTidingsTestSubscriber);

        mTidingsTestSubscriber.assertNoValues();
    }

    @Test
    public void getTidings_refreshesLocalDataSource() {
        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);
        mTidingRepository.refreshTidings();

        mTidingRepository.getTidings().subscribe(mTidingsTestSubscriber);

        verify(mTidingLocalDataSource, times(TIDINGS.size())).saveTiding(any(Tiding.class));
        mTidingsTestSubscriber.assertValue(TIDINGS);
    }

}