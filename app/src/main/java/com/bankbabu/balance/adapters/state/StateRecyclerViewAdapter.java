package com.bankbabu.balance.adapters.state;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.State;
import java.util.List;

public class StateRecyclerViewAdapter extends RecyclerView.Adapter<StateViewHolder> {

  private List<State> states;

  public StateRecyclerViewAdapter(List<State> states) {
    this.states = states;
  }

  @NonNull
  @Override
  public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_state, parent, false);
    return new StateViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final StateViewHolder holder, final int position) {
    holder.bind(states.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return states.size();
  }

  public void setStates(final List<State> states) {
    this.states = states;
    notifyDataSetChanged();
  }
}