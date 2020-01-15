package com.bankbabu.balance.adapters.account;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Account;
import java.util.List;


public class ShareAccountAdapter extends RecyclerView.Adapter<ShareAccountViewHolder> {

  private List<Account> accounts;

  public ShareAccountAdapter(List<Account> accounts) {
    this.accounts = accounts;
  }

  @NonNull
  @Override
  public ShareAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_account, parent, false);
    return new ShareAccountViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ShareAccountViewHolder holder, final int position) {
    holder.bind(accounts.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return accounts.size();
  }

}
