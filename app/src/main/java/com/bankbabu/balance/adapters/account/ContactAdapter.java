package com.bankbabu.balance.adapters.account;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Account;

import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<AccountViewHolder> {

  private List<Account> accounts;

  public ContactAdapter(List<Account> accounts) {
    this.accounts = accounts;
  }

  @NonNull
  @Override
  public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_contact, parent, false);
    return new AccountViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final AccountViewHolder holder, final int position) {
    holder.bind(accounts.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return accounts.size();
  }

  public void setAccounts(final List<Account> accounts) {
    this.accounts = accounts;
    notifyDataSetChanged();
  }
}
