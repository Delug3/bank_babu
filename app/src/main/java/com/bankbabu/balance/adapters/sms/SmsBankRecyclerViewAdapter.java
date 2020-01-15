package com.bankbabu.balance.adapters.sms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.SmsBank;
import java.util.List;

public class SmsBankRecyclerViewAdapter extends RecyclerView.Adapter<SmsBankViewHolder> {

  private List<SmsBank> smsBanks;

  public SmsBankRecyclerViewAdapter(List<SmsBank> versions) {
    smsBanks = versions;
  }

  @NonNull
  @Override
  public SmsBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_sms_bank, parent, false);
    return new SmsBankViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final SmsBankViewHolder holder, final int position) {
    holder.bind(smsBanks.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return smsBanks.size();
  }
}
