package com.bankbabu.balance.adapters.account;

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
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public class AccountViewHolder extends BaseViewHolder<Account> {

    private TextView textViewName;
    private TextView textViewBankName;
    private TextView textViewNumber;
    private ImageView wrapperShare;

    AccountViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initUI() {
        textViewName = findViewById(R.id.text_view_name);
        textViewBankName = findViewById(R.id.text_view_bank_name);
        textViewNumber = findViewById(R.id.text_view_number);
        wrapperShare = findViewById(R.id.wrapper_share);
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void bind(final Account item) {
        super.bind(item);
        itemView.setOnClickListener(v -> EventBus.getDefault().post(new OnAccountItemClickEvent(getItem())));

        textViewName.setText(item.getName().toUpperCase());
        final String number = itemView.getContext()
                .getString(R.string.format_account_number_short, item.getNumber());
        textViewNumber.setText(number.toUpperCase());
        textViewBankName.setText(item.getBankName().toUpperCase());

        wrapperShare.setOnClickListener(view -> share());
    }

    private void share() {
        EventBus.getDefault().post(new OnShareAccountClickEvent(getItem()));
    }
}
