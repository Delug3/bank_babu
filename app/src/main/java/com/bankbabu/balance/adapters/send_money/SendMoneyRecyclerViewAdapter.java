package com.bankbabu.balance.adapters.send_money;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.SendMoney;
import java.util.List;


public class SendMoneyRecyclerViewAdapter extends RecyclerView.Adapter<SendMoneyViewHolder> {

  private final List<SendMoney> sendMonies;

  public SendMoneyRecyclerViewAdapter(final List<SendMoney> sendMonies) {
    this.sendMonies = sendMonies;
  }

  @NonNull
  @Override
  public SendMoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_new_category, parent, false);
    return new SendMoneyViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final SendMoneyViewHolder holder, final int position) {
    holder.bind(sendMonies.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return sendMonies.size();
  }
}
