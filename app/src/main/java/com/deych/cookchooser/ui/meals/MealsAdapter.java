package com.deych.cookchooser.ui.meals;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deych.cookchooser.R;
import com.deych.cookchooser.db.entities.Meal;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder>{

    private List<Meal> list = Collections.emptyList();

    public void setList(List<Meal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.meal_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvName)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Meal meal) {
            if (meal.getRevision() == 0) {
                tvName.setTextColor(Color.LTGRAY);
            } else {
                tvName.setTextColor(Color.DKGRAY);
            }
            tvName.setText(meal.getName());
        }
    }
}
