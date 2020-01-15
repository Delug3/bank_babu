package com.bankbabu.balance.adapters.account;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnAccountItemClickEvent;
import com.bankbabu.balance.events.OnShareAccountClickEvent;
import com.bankbabu.balance.models.Account;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class ShareAccountViewHolder extends BaseViewHolder<Account> {

  private TextView textViewName, textViewNumber, textViewBankName;
  private ConstraintLayout container;
  private ImageView wrapperShare;

  ShareAccountViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void initUI() {
    container = itemView.findViewById(R.id.fragment_container);
    wrapperShare = itemView.findViewById(R.id.wrapper_share);

    textViewName = itemView.findViewById(R.id.text_view_name);
    textViewNumber = itemView.findViewById(R.id.text_view_number);

    textViewBankName = itemView.findViewById(R.id.text_view_bank_name);
//    textViewType = itemView.findViewById(R.id.text_view_type);
  }

  @Override
  protected void setListener() {
    super.setListener();
  }

  @Override
  public void bind(final Account item) {
    super.bind(item);
    final Account account = getItem();
    final int position = getAdapterPosition();

    textViewName.setText(account.getName());
    textViewNumber.setText(account.getNumber());
    textViewBankName.setText(account.getBankName());
//    textViewType.setText(account.getType());

    if (getAdapterPosition() % 2 == 0) {
      container.setBackgroundResource(R.color.bill_payable);
      wrapperShare.setBackgroundResource(R.color.share_payable);
    } else {
      container.setBackgroundResource(R.color.bill_notpayable);
      wrapperShare.setBackgroundResource(R.color.share_notpayable);
    }

    itemView.setOnClickListener(this);

    wrapperShare.setOnClickListener(view -> {

      container.setBackgroundResource(R.color.pay1);
      wrapperShare.setVisibility(View.GONE);
      EventBus.getDefault().post(new OnShareAccountClickEvent(account));

      if (position % 2 == 0) {
        container.setBackgroundResource(R.color.bill_payable);
      } else {
        container.setBackgroundResource(R.color.bill_notpayable);
      }
    });
  }

  @Override
  public void onClick(final View view) {
    EventBus.getDefault().post(new OnAccountItemClickEvent(getItem()));
  }
}
