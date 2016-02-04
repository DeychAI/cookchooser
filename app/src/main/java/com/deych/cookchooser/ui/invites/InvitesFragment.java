package com.deych.cookchooser.ui.invites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deych.cookchooser.App;
import com.deych.cookchooser.R;
import com.deych.cookchooser.api.entities.Invite;
import com.deych.cookchooser.ui.base.BaseFragment;
import com.deych.cookchooser.ui.base.Presenter;
import com.deych.cookchooser.ui.base.config.impl.ActionBarConfig;
import com.deych.cookchooser.ui.base.config.impl.CompositeConfig;
import com.deych.cookchooser.ui.base.config.impl.FabConfig;
import com.deych.cookchooser.ui.base.config.impl.TabLayoutConfig;
import com.deych.cookchooser.ui.base.config.UiConfig;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 23.01.2016.
 */
public class InvitesFragment extends BaseFragment implements InvitesView{

    @Inject
    InvitesPresenter presenter;
    private InvitesAdapter adapter;
    private FabConfig fabConfig;

    @Override
    protected void setUpComponents() {
        App.get(getContext()).getUserComponent().inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void setPresenter(Presenter presenter) {
        this.presenter = (InvitesPresenter) presenter;
    }

    private UiConfig uiConfig;

    @Bind(R.id.list)
    RecyclerView list;

    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InvitesAdapter(this::askAcceptInvite);
        list.setAdapter(adapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabConfig = new FabConfig(getActivity())
                .show()
                .drawableRes(R.drawable.ic_invites_fab)
                .listener(v -> askSentInvite());
        uiConfig = new CompositeConfig()
                .add(fabConfig)
                .add(new ActionBarConfig(getActivity())
                        .title(R.string.title_invites))
                .add(new TabLayoutConfig(getActivity())
                        .hide());
        uiConfig.apply();

        presenter.bindView(this);
        presenter.loadInvites();

        refreshLayout.setOnRefreshListener(presenter::loadInvites);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        presenter.unbindView(this);
        uiConfig.release();
    }

    private void askAcceptInvite(Invite invite) {
        AcceptInviteDialog dialog = AcceptInviteDialog.newInstance(invite.getFrom());
        dialog.setListener(() -> presenter.acceptInvite(invite));
        dialog.show(getFragmentManager(), "accept.dialog");
    }

    private void askSentInvite() {
        SentInviteDialog dialog = new SentInviteDialog();
        dialog.setListener(presenter::sendInvite);
        dialog.show(getFragmentManager(), "sent.dialog");
    }

    @Override
    public void setData(List<Invite> invites) {
        adapter.setList(invites);
    }

    @Override
    public void inviteSent() {
        Snackbar.make(fabConfig.fab(), "Приглашение отправлено", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorValidateEmail() {
        Snackbar.make(fabConfig.fab(), "Неверный Email!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorInviteUserNotExist() {
        Snackbar.make(fabConfig.fab(), "Пользователь не существует!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void errorInviteAlreadySent() {
        Snackbar.make(fabConfig.fab(), "Приглашение пользователю уже отправлено!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void inviteAccepted() {
        Snackbar.make(fabConfig.fab(), "Приглашение принято", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void generalError() {
        Snackbar.make(fabConfig.fab(), R.string.error_network, Snackbar.LENGTH_SHORT).show();
    }
}
