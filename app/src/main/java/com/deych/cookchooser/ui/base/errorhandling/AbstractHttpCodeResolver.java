package com.deych.cookchooser.ui.base.errorhandling;

/**
 * Created by deigo on 26.01.2016.
 */
public abstract class AbstractHttpCodeResolver<V> implements Resolver<V> {

    protected int code;

    public AbstractHttpCodeResolver(int code) {
        this.code = code;
    }

    protected abstract boolean tryResolve(V view);

    @Override
    public void resolve(V view) {
        if (!tryResolve(view)) {
            notResolved();
        }
    }

    private void notResolved() {
        throw new RuntimeException(String.format("Unhandled HTTP Error Code %d", code));
    }
}
