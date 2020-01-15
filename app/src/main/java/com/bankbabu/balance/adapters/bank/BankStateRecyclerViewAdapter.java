package com.bankbabu.balance.adapters.bank;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Bank;
import java.util.List;

public class BankStateRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

  private static final int VIEW_TYPE_BANK = 2;

  private List<Bank> states;

  public BankStateRecyclerViewAdapter(List<Bank> versions) {
    states = versions;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_state, parent, false);
    return new BankStateViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder bankViewHolder, final int i) {
    ((BankStateViewHolder) bankViewHolder).bind(states.get(bankViewHolder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return states.size();
  }

  public void setStates(final List<Bank> states) {
    this.states = states;
    notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(final int position) {
    return VIEW_TYPE_BANK;
  }
}
