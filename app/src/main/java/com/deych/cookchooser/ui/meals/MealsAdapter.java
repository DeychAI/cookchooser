package com.deych.cookchooser.ui.meals;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
    private ItemClickListener listener;

    public MealsAdapter(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Meal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.id.meal_adapter_first_item;
        }
        if (position == list.size() - 1) {
            return R.id.meal_adapter_last_item;
        }
        return R.id.meal_adapter_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_layout, parent, false);
        view.setPadding(
                context.getResources().getDimensionPixelSize(R.dimen.dimen_1x),
                context.getResources().getDimensionPixelSize(
                        viewType == R.id.meal_adapter_first_item ? R.dimen.dimen_1x : R.dimen.dimen_0_5x),
                context.getResources().getDimensionPixelSize(R.dimen.dimen_1x),
                context.getResources().getDimensionPixelSize(
                        viewType == R.id.meal_adapter_last_item ? R.dimen.dimen_1x : R.dimen.dimen_0_5x));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(listener, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvName)
        TextView tvName;

        @Bind(R.id.ivIcon)
        ImageView ivIcon;

        View itemView;

        Meal meal;

        ItemClickListener listener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }

        public void bind(ItemClickListener listener, Meal meal) {
            this.meal = meal;
            this.listener = listener;
            if (meal.getRevision() == 0) {
                tvName.setTextColor(Color.LTGRAY);
            } else {
                tvName.setTextColor(Color.DKGRAY);
            }
            tvName.setText(meal.getName());
            ivIcon.setImageResource(meal.getColor().drawableRes());
            itemView.setOnClickListener(v -> listener.mealSelected(meal));
            itemView.setOnLongClickListener(this::showPopup);
        }

        private boolean showPopup(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.meals_popup);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_meals_delete) {
                    listener.deleteMeal(meal);
                    return true;
                }
                return false;
            });
            popupMenu.show();
            return true;
        }
    }

    public interface ItemClickListener {
        void mealSelected(Meal meal);
        void deleteMeal(Meal meal);
    }
}
