package com.deych.cookchooser.ui.presenters;

import com.deych.cookchooser.CookChooserRobolectricUnitTestRunner;
import com.deych.cookchooser.MockRxSchedulerFactory;
import com.deych.cookchooser.api.entities.Invite;
import com.deych.cookchooser.models.InvitesModel;
import com.deych.cookchooser.ui.invites.InvitesPresenter;
import com.deych.cookchooser.ui.invites.InvitesView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by deigo on 23.01.2016.
 */
@RunWith(CookChooserRobolectricUnitTestRunner.class)
public class InvitesPresenterTest {

    private InvitesModel invitesModel;
    private InvitesView invitesView;
    private InvitesPresenter invitesPresenter;
    private String email = "test@test.com";

    @Before
    public void before() {
        invitesModel = mock(InvitesModel.class);
        invitesView = mock(InvitesView.class);

        invitesPresenter = new InvitesPresenter(invitesModel, new MockRxSchedulerFactory());
        invitesPresenter.bindView(invitesView);
    }

    @Test
    public void loadInvites_shouldSetData() {

        List<Invite> invites = Arrays.asList(
                new Invite(1, "test1@test.com"),
                new Invite(2, "test2@test.com")
        );

        when(invitesModel.load()).thenReturn(Observable.just(invites));

        invitesPresenter.loadInvites();
        verify(invitesView).hideRefresh();
        verify(invitesView).setData(invites);
    }

    @Test
    public void loadInvites_shouldHideProgressOnError() {
        when(invitesModel.load()).thenReturn(Observable.error(new IOException()));

        invitesPresenter.loadInvites();
        verify(invitesView).hideRefresh();
    }

    @Test
    public void sendInvite_shouldShowValidateError() {
        invitesPresenter.sendInvite("test_incorrect_email");
        verify(invitesView).errorValidateEmail();
    }

    @Test
    public void sendInvite_shouldCallInviteSent() {
        Invite invite = new Invite(1, "q@q.com");

        when(invitesModel.invite(anyString())).thenReturn(Observable.just(invite));

        invitesPresenter.sendInvite(email);

        verify(invitesView).inviteSent();
    }

    @Test
    public void sendInvite_shouldShowErrorInviteAlreadySent() {
        when(invitesModel.invite(anyString())).thenReturn(Observable.error(new RuntimeException()));
        when(invitesModel.handleInviteError(Matchers.<Throwable>any())).thenReturn(InvitesModel.ERROR_ALREADY_SENT);

        invitesPresenter.sendInvite(email);
        verify(invitesView).errorInviteAlreadySent();
    }

    @Test
    public void sendInvite_shouldShowUserNotExist() {
        when(invitesModel.invite(anyString())).thenReturn(Observable.error(new RuntimeException()));
        when(invitesModel.handleInviteError(Matchers.<Throwable>any())).thenReturn(InvitesModel.ERROR_USER_NOT_EXIST);

        invitesPresenter.sendInvite(email);
        verify(invitesView).errorInviteUserNotExist();
    }

    @Test
    public void sendInvite_shouldShowGeneralError() {
        when(invitesModel.invite(anyString())).thenReturn(Observable.error(new RuntimeException()));
        when(invitesModel.handleInviteError(Matchers.<Throwable>any())).thenReturn(InvitesModel.ERROR_OTHER);

        invitesPresenter.sendInvite(email);
        verify(invitesView).generalError();
    }

    @Test
    public void acceptInvite_shouldShowInviteAccepted() {
        when(invitesModel.accept(any())).thenReturn(Observable.just(true));

        invitesPresenter.acceptInvite(new Invite(1, "q@q.com"));
        verify(invitesView).inviteAccepted();
    }

    //TODO add error handling test for accept()
}
