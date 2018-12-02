package com.app.media.sdk;

import android.content.Context;

import com.app.media.cache.AppCacheManager;
import com.app.media.cache.ICache;
import com.app.media.cache.iml.DiskLruCacheIml;
import com.app.media.manager.MediaPlayServiceManger;

import java.io.IOException;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/23
 */
public class MediaPlaySdk {

    public static void init(Context context) {
        try {
            ICache cache = new DiskLruCacheIml(context.getCacheDir(), 1, 1, 10000);
            AppCacheManager.getInstance().init(context, cache);
            MediaPlayServiceManger.getInstance().startAndBindService(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
