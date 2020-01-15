package com.bankbabu.balance.adapters.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Category;
import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

  private List<Category> categories;

  public CategoryRecyclerViewAdapter(List<Category> data) {
    categories = data;
  }

  @NonNull
  @Override
  public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_category, parent, false);
    return new CategoryViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
    holder.bind(categories.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return categories.size();
  }

  public void setCategories(final List<Category> categories) {
    this.categories = categories;
    notifyDataSetChanged();
  }
}
