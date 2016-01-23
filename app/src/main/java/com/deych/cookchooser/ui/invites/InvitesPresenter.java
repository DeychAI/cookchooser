package com.deych.cookchooser.ui.invites;

import android.text.TextUtils;

import com.deych.cookchooser.api.entities.Invite;
import com.deych.cookchooser.models.InvitesModel;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.util.RxSchedulerFactory;

import javax.inject.Inject;

/**
 * Created by deigo on 23.01.2016.
 */
public class InvitesPresenter extends Presenter<InvitesView> {

    private InvitesModel invitesModel;
    private RxSchedulerFactory rxSchedulerFactory;

    @Inject
    public InvitesPresenter(InvitesModel invitesModel, RxSchedulerFactory rxSchedulerFactory) {
        this.invitesModel = invitesModel;
        this.rxSchedulerFactory = rxSchedulerFactory;
    }

    public void loadInvites() {
        invitesModel.load()
                .subscribeOn(rxSchedulerFactory.io())
                .observeOn(rxSchedulerFactory.mainThread())
                .subscribe(invites -> {
                    if (view() != null) {
                        view().hideRefresh();
                        view().setData(invites);
                    }
                }, t -> {
                    if (view() != null) {
                        view().hideRefresh();
                    }
                });
    }

    public void sendInvite(String sendTo) {
        if (!validateEmail(sendTo)){
            if (view() != null) {
                view().errorValidateEmail();
            }
            return;
        }

        invitesModel.invite(sendTo)
                .subscribeOn(rxSchedulerFactory.io())
                .observeOn(rxSchedulerFactory.mainThread())
                .subscribe(invite -> {
                    if (view() != null) {
                        view().inviteSent();
                    }
                }, e -> {
                    int error = invitesModel.handleInviteError(e);
                    if (error == InvitesModel.ERROR_ALREADY_SENT) {
                        if (view() != null) {
                            view().errorInviteAlreadySent();
                        }
                    } else if (error == InvitesModel.ERROR_USER_NOT_EXIST) {
                        if (view() != null) {
                            view().errorInviteUserNotExist();
                        }
                    } else {
                        if (view() != null) {
                            view().generalError();
                        }
                    }
                });
    }

    private boolean validateEmail(String sendTo) {
        return !(TextUtils.isEmpty(sendTo) || !android.util.Patterns.EMAIL_ADDRESS.matcher(sendTo).matches());
    }

    public void acceptInvite(Invite invite) {
        invitesModel.accept(invite)
                .subscribeOn(rxSchedulerFactory.io())
                .observeOn(rxSchedulerFactory.mainThread())
                .subscribe(result -> {
                    if (view() != null) {
                        view().inviteAccepted();
                    }
                    loadInvites();
                }, e -> {
                    int error = invitesModel.handleAcceptError(e);
                    //TODO
                    if (view() != null) {
                        view().generalError();
                    }
                });

    }
}
