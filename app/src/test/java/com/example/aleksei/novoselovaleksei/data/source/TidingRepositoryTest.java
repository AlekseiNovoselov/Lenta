package com.example.aleksei.novoselovaleksei.data.source;

import com.example.aleksei.novoselovaleksei.data.Tiding;

import org.junit.After;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.example.aleksei.novoselovaleksei.Injection.provideSchedulerProvider;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class TidingRepositoryTest {

    private List<Tiding> TIDINGS = Arrays.asList(
            new Tiding("Title1", 100L,
                    "Description1", null, "lenta.ru"),
            new Tiding("Title2", 200L,
                    "Description2", null, "gazeta.ru"));

    private TidingRepository mTidingRepository;

    @Mock
    private TidingDataSource mTidingRemoteDataSource;

    @Mock
    private TidingDataSource mTidingLocalDataSource;

    @Mock
    private TidingDataSource.LoadTidingsCallback mLoadTidingsCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<TidingDataSource.LoadTidingsCallback> mTidingsCallbackCaptor;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<TidingDataSource.LoadTidingsCallback> mTidingCallbackCaptor;

    @Before
    public void setupTidingsRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mTidingRepository= TidingRepository.getInstance(
                mTidingRemoteDataSource, mTidingLocalDataSource, provideSchedulerProvider());
    }

    @After
    public void destroyRepositoryInstance() {
        TidingRepository.destroyInstance();
    }

/*    @Test
    public void getTidings_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the tidings repository
        twoTidingsLoadCallsToRepository(mLoadTidingsCallback);

        // Then tidings were only requested once from Service API
        verify(mTidingRemoteDataSource).getTidings(any(TidingDataSource.LoadTidingsCallback.class));
    }

    @Test
    public void getTidings_requestsAllTidingsFromLocalDataSource() {
        // When tidings are requested from the tidings repository
        mTidingRepository.getTidings(mLoadTidingsCallback);

        // Then tidings are loaded from the local data source
        verify(mTidingLocalDataSource).getTidings(any(TidingDataSource.LoadTidingsCallback.class));
    }

    @Test
    public void saveTiding_savesTidingToServiceAPI() {
        // Given a stub task with title and description
        Tiding newTiding = new Tiding("Title", 100L,
                "Some Task Description", null, BaseSource.Source.LENTA);

        // When a task is saved to the tidings repository
        mTidingRepository.saveTiding(newTiding);

        // Then the service API and persistent repository are called and the cache is updated
        verify(mTidingRemoteDataSource).saveTiding(newTiding);
        verify(mTidingLocalDataSource).saveTiding(newTiding);
        assertThat(mTidingRepository.mCachedTidings.size(), is(1));
    }

    @Test
    public void deleteAllTidings_deleteTidingsToServiceAPIUpdatesCache() {

        mTidingRepository.mCachedTidings = new LinkedHashMap<>();
        mTidingRepository.mCachedTidings.put("TITLE1",
                new Tiding("TITLE1",100L,
                        "description1", null, BaseSource.Source.LENTA));
        mTidingRepository.mCachedTidings.put("TITLE2",
                new Tiding("TITLE2", 200L,
                        "description2", null, BaseSource.Source.LENTA));

        // When all tidings are deleted to the tidings repository
        mTidingRepository.deleteAllTidings(TIDINGS.get(0).getSource());

        // Verify the data sources were called
        verify(mTidingRemoteDataSource).deleteAllTidings(TIDINGS.get(0).getSource());
        verify(mTidingLocalDataSource).deleteAllTidings(TIDINGS.get(0).getSource());

        assertThat(mTidingRepository.mCachedTidings.size(), is(0));
    }

    @Test
    public void getTidingsWithDirtyCache_tidingsAreRetrievedFromRemote() {
        // When calling getTidings in the repository with dirty cache
        mTidingRepository.refreshTidings();
        mTidingRepository.getTidings(mLoadTidingsCallback);

        // And the remote data source has data available
        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);

        // Verify the tidings from the remote data source are returned, not the local
        verify(mTidingLocalDataSource, never()).getTidings(mLoadTidingsCallback);
        verify(mLoadTidingsCallback).onTidingLoaded(TIDINGS);
    }

    @Test
    public void getTidingsWithLocalDataSourceUnavailable_tidingsAreRetrievedFromRemote() {
        // When calling getTidings in the repository
        mTidingRepository.getTidings(mLoadTidingsCallback);

        // And the local data source has no data available
        setTidingsNotAvailable(mTidingLocalDataSource);

        // And the remote data source has data available
        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);

        // Verify the tidings from the local data source are returned
        verify(mLoadTidingsCallback).onTidingLoaded(TIDINGS);
    }

    @Test
    public void getTidingsWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // When calling getTidings in the repository
        mTidingRepository.getTidings(mLoadTidingsCallback);

        // And the local data source has no data available
        setTidingsNotAvailable(mTidingLocalDataSource);

        // And the remote data source has no data available
        setTidingsNotAvailable(mTidingRemoteDataSource);

        // Verify no data is returned
        verify(mLoadTidingsCallback).onDataNotAvailable();
    }

    @Test
    public void getTidings_refreshesLocalDataSource() {
        // Mark cache as dirty to force a reload of data from remote data source.
        mTidingRepository.refreshTidings();

        // When calling getTidings in the repository
        mTidingRepository.getTidings(mLoadTidingsCallback);

        // Make the remote data source return data
        setTidingsAvailable(mTidingRemoteDataSource, TIDINGS);

        // Verify that the data fetched from the remote data source was saved in local.
        verify(mTidingLocalDataSource, times(TIDINGS.size())).saveTiding(any(Tiding.class));
    }

    private void twoTidingsLoadCallsToRepository(TidingDataSource.LoadTidingsCallback callback) {
        // When tidings are requested from repository
        mTidingRepository.getTidings(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mTidingLocalDataSource).getTidings(mTidingsCallbackCaptor.capture());

        // Local data source doesn't have data yet
        mTidingsCallbackCaptor.getValue().onDataNotAvailable();

        // Verify the remote data source is queried
        verify(mTidingRemoteDataSource).getTidings(mTidingsCallbackCaptor.capture());

        // Trigger callback so tidings are cached
        mTidingsCallbackCaptor.getValue().onTidingLoaded(TIDINGS);

        mTidingRepository.getTidings(callback); // Second call to API
    }

    private void setTidingsNotAvailable(TidingDataSource dataSource) {
        verify(dataSource).getTidings(mTidingsCallbackCaptor.capture());
        mTidingsCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setTidingsAvailable(TidingDataSource dataSource, List<Tiding> tidings) {
        verify(dataSource).getTidings(mTidingCallbackCaptor.capture());
        mTidingCallbackCaptor.getValue().onTidingLoaded(tidings);
    }
    */

}