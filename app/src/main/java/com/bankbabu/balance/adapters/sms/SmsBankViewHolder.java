package com.bankbabu.balance.adapters.sms;

import android.view.View;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnSmsBankItemClickEvent;
import com.bankbabu.balance.models.SmsBank;
import org.greenrobot.eventbus.EventBus;

public class SmsBankViewHolder extends BaseViewHolder<SmsBank> {

  private TextView textViewTitle;

  SmsBankViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void initUI() {
    textViewTitle = findViewById(R.id.text_view_title);
  }

  @Override
  public void bind(final SmsBank bank) {
    super.bind(bank);

    textViewTitle.setText(bank.getTitle());
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnSmsBankItemClickEvent(getItem()));
  }
}
