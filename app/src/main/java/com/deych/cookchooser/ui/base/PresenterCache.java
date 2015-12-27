package com.deych.cookchooser.ui.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by deigo on 17.12.2015.
 */
@Singleton
public class PresenterCache {

    private final AtomicLong nextId = new AtomicLong();

    private Map<Long, Object> presentersCache = new HashMap<>();

    public long generateId() {
        Timber.d("called nextId = " + nextId);
        return nextId.getAndIncrement();
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
