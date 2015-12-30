package com.deych.cookchooser.user_scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by deigo on 19.12.2015.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}
