package com.bankbabu.balance.adapters.carousel;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bankbabu.balance.R;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.Holder> {

    private List<CarouselItem> items = Collections.emptyList();
    private OnCarouselItemClicked listener;

    CarouselAdapter(OnCarouselItemClicked listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_carousel, parent, false);
        return new Holder(view);
    }

    void setItems(List<CarouselItem> items) {
        this.items = requireNonNull(items);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int index) {
        CarouselItem carouselItem = items.get(index);

        holder.title.setText(carouselItem.getTitle());
        holder.body.setText(carouselItem.getBody());

        if (index == 0) {
            holder.holder.setBackgroundResource(R.drawable.card_1);
        } else if(index == 1) {
            holder.holder.setBackgroundResource(R.drawable.card_2);
        } else if (index == 2) {
            holder.holder.setBackgroundResource(R.drawable.card_3);
        }
        holder.itemView.setOnClickListener(v -> listener.onCarouselItemClicked(index));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnCarouselItemClicked {
        void onCarouselItemClicked(int position);
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        ConstraintLayout holder;

        Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            holder = itemView.findViewById(R.id.holder);
        }
    }
}
