package com.bankbabu.balance.adapters.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;

public class NewCategoryRecyclerViewAdapter extends RecyclerView.Adapter<NewCategoryViewHolder> {

  private int[] categories;

  public NewCategoryRecyclerViewAdapter(int[] data) {
    categories = data;
  }

  @NonNull
  @Override
  public NewCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    final View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_new_category, parent, false);

    return new NewCategoryViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final NewCategoryViewHolder holder, final int position) {
    holder.bind(categories[holder.getAdapterPosition()]);
  }

  @Override
  public int getItemCount() {
    return categories.length;
  }


}
