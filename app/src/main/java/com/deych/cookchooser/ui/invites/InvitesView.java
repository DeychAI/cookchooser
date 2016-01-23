package com.deych.cookchooser.ui.invites;

import com.deych.cookchooser.api.entities.Invite;

import java.util.List;

/**
 * Created by deigo on 23.01.2016.
 */
public interface InvitesView {
    void setData(List<Invite> invites);
    void inviteSent();
    void errorValidateEmail();
    void errorInviteUserNotExist();
    void errorInviteAlreadySent();
    void inviteAccepted();
    void hideRefresh();
    void generalError();
}
