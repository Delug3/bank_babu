package com.bankbabu.balance.adapters.bank;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnBankStateFavoriteClickEvent;
import com.bankbabu.balance.events.OnBankStateItemClickEvent;
import com.bankbabu.balance.models.Bank;
import org.greenrobot.eventbus.EventBus;

public class BankStateViewHolder extends BaseViewHolder<Bank> {

  private ImageView imageViewBookmark;

  private TextView textViewTitle;

  BankStateViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void initUI() {
    imageViewBookmark = findViewById(R.id.image_view_bookmark);
    textViewTitle = findViewById(R.id.text_view_title);
  }

  @Override
  public void bind(final Bank bank) {
    super.bind(bank);

    textViewTitle.setText(bank.getName());

    if (bank.getFav() == 0) {
      imageViewBookmark.setImageResource(R.drawable.ic_heart_off);
    } else {
      imageViewBookmark.setImageResource(R.drawable.ic_heart_on);
    }
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
    imageViewBookmark.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        EventBus.getDefault().post(new OnBankStateFavoriteClickEvent(getItem()));
      }
    });
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnBankStateItemClickEvent(getItem()));
  }
}
