package com.app.media.player.exo;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by yinxm on 2017/1/12.
 * 功能:
 */

public class ExoPlayerUtil {

    private static final String TAG = "ExoPlayerUtil";
    private static final String APPLICATION_NAME = "exoplayer";


    /**
     * 默认边下边播最大缓存300M
     */
    private final long MAX_CACHE_SIZE = 300 * 1024 * 1024;

    private ExoPlayerUtil() {
    }

    private static ExoPlayerUtil instance = new ExoPlayerUtil();


    protected String userAgent;
    private Context context;

    public static ExoPlayerUtil getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        userAgent = Util.getUserAgent(context, APPLICATION_NAME);
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(context, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set BANDWIDTH_METER as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    public DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter,
                                                     DefaultBandwidthMeter bandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? bandwidthMeter : null);
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    /**
     * 默认使用buildDataSourceFactory，需要开启边下边播，使用builCachedDataSourceFactory
     * @param context
     * @return
     */
    public DataSource.Factory builCachedDataSourceFactory(Context context) {

        final DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, APPLICATION_NAME));
        Cache cache = new SimpleCache(context.getCacheDir(),
                new LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE));
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache,
                dataSourceFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        return cacheDataSourceFactory;
    }


    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set BANDWIDTH_METER as a listener to the new
     *                          DataSource factory.
     * @return A new HttpDataSource factory.
     */
    public HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter,
                                                             DefaultBandwidthMeter bandwidthMeter) {
        return buildHttpDataSourceFactory(useBandwidthMeter ? bandwidthMeter : null);
    }

}
