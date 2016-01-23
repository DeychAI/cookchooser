package com.deych.cookchooser.ui.invites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deych.cookchooser.R;
import com.deych.cookchooser.api.entities.Invite;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 23.01.2016.
 */
public class InvitesAdapter extends RecyclerView.Adapter<InvitesAdapter.ViewHolder> {

    private List<Invite> list = Collections.emptyList();
    private ItemClickListener listener;

    public InvitesAdapter(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Invite> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvFrom)
        TextView tvFrom;

        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }

        public void bind(Invite invite, ItemClickListener listener) {
            tvFrom.setText(invite.getFrom());
            itemView.setOnClickListener(v -> listener.inviteSelected(invite));
        }
    }

    public interface ItemClickListener {
        void inviteSelected(Invite invite);
    }
}
