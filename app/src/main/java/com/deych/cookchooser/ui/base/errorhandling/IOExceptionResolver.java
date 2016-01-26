package com.deych.cookchooser.ui.base.errorhandling;

import com.deych.cookchooser.ui.base.views.NetworkErrorView;

/**
 * Created by deigo on 26.01.2016.
 */
public class IOExceptionResolver<V extends NetworkErrorView> implements Resolver<V>{

    @Override
    public void resolve(V view) {
        view.showNetworkError();
    }
}
