package com.deych.cookchooser.models;

import com.deych.cookchooser.api.entities.Invite;
import com.deych.cookchooser.api.service.InvitesService;
import com.deych.cookchooser.db.entities.User;
import com.deych.cookchooser.db.tables.MealTable;
import com.deych.cookchooser.user_scope.UserScope;
import com.deych.cookchooser.util.RetryWithDelayIf;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Observable;

/**
 * Created by deigo on 22.01.2016.
 */
@UserScope
public class InvitesModel {

    public static final int ERROR_USER_NOT_EXIST = 1;
    public static final int ERROR_OTHER = 2;
    public static final int ERROR_ALREADY_SENT = 3;
    public static final int ERROR_NOT_FOUND = 4;
    public static final int ERROR_ACCESS_DENIED = 5;


    private InvitesService invitesService;
    private User user;
    private StorIOSQLite storIOSQLite;

    @Inject
    public InvitesModel(InvitesService invitesService, User user, StorIOSQLite storIOSQLite) {
        this.invitesService = invitesService;
        this.user = user;
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<List<Invite>> load() {
        return invitesService.getInvites().retryWhen(
                new RetryWithDelayIf(1, 3, TimeUnit.SECONDS, t -> (t instanceof IOException)));
    }

    public Observable<Invite> invite(String sendTo) {
        return invitesService.invite(sendTo);
    }

    public int handleInviteError(Throwable e) {
        if (!(e instanceof HttpException)) {
            return ERROR_OTHER;
        }
        HttpException error = (HttpException) e;
        if (error.code() == 400) {
            return ERROR_USER_NOT_EXIST;
        }
        if (error.code() == 409) {
            return ERROR_ALREADY_SENT;
        }
        return ERROR_OTHER;
    }

    public Observable<Boolean> accept(Invite invite) {
        return invitesService.accept(invite.getId())
                .flatMap(user -> storIOSQLite.put()
                        .object(user)
                        .prepare()
                        .asRxObservable())
                .flatMap(putResult -> {
                    return Observable.just(putResult.wasUpdated());
                })
                .doOnNext(result -> {
                    if (result) {
                        storIOSQLite.delete()
                                .byQuery(DeleteQuery.builder().table(MealTable.TABLE).build())
                                .prepare()
                                .executeAsBlocking();
                    }
                });
    }

    public int handleAcceptError(Throwable e) {
        if (!(e instanceof HttpException)) {
            return ERROR_OTHER;
        }
        HttpException error = (HttpException) e;
        if (error.code() == 404) {
            return ERROR_NOT_FOUND;
        }
        if (error.code() == 403) {
            return ERROR_ACCESS_DENIED;
        }
        return ERROR_OTHER;
    }
}
