package com.bankbabu.balance.adapters.bank;

import android.view.View;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnBankItemClickEvent;
import com.bankbabu.balance.models.Bank;
import org.greenrobot.eventbus.EventBus;

public class BankViewHolder extends BaseViewHolder<Bank> {

  private TextView textViewTitle;

  BankViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void initUI() {
    textViewTitle = findViewById(R.id.text_view_title);
  }

  @Override
  public void bind(final Bank bank) {
    super.bind(bank);

    textViewTitle.setText(bank.getCode());
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnBankItemClickEvent(getItem()));
  }
}
