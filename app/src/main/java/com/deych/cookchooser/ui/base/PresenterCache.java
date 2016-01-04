package com.deych.cookchooser.ui.base;

import com.deych.cookchooser.shared_pref.Preferences;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
public class PresenterCache {

    private Map<Long, Object> presentersCache = new HashMap<>();

    private Preferences preferences;

    @Inject
    public PresenterCache(Preferences preferences) {
        this.preferences = preferences;
    }

    //    private final AtomicLong nextId = new AtomicLong(100);

    public long generateId() {
        long nextId = preferences.getPresenterCacheIdAndIncrement();
        Timber.d("called nextId = " + nextId);
        return nextId;
    }

    @SuppressWarnings("unchecked")
    public <P> P get(long index) {
        Timber.d("get index = " + index);
        return (P) presentersCache.get(index);
    }

    public <P> void put(long index, P presenter) {
        Timber.d("put index = " + index + " presenter = " + presenter);
        presentersCache.put(index, presenter);
    }

}
