package com.app.media.bean.bo;

import java.util.LinkedHashMap;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/21
 */
public class ProgressListCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -7914079930134440787L;
    private int mLimitSize = 0;

    public ProgressListCache(int limitSize) {
        super();
        mLimitSize = limitSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry eldest) {
        return size() > mLimitSize;
    }
}
