package com.bankbabu.balance.adapters.bank;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Bank;
import java.util.List;

public class BankRecyclerViewAdapter extends RecyclerView.Adapter<BankViewHolder> {

  private List<Bank> banks;

  public BankRecyclerViewAdapter(List<Bank> versions) {
    banks = versions;
  }

  @NonNull
  @Override
  public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_bank, parent, false);
    return new BankViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final BankViewHolder bankViewHolder, final int i) {
    bankViewHolder.bind(banks.get(bankViewHolder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return banks.size();
  }
}
