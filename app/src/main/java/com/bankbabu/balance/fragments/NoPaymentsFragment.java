package com.bankbabu.balance.fragments;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnAccountAddClickEvent;
import com.bankbabu.balance.models.Account;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoPaymentsFragment extends BaseFragment {

    @BindView(R.id.account_wrapper) FrameLayout addAccountWrapper;
    @BindView(R.id.contacts_wrapper) FrameLayout addContactWrapper;

    public NoPaymentsFragment() {
        // Required empty public constructor
    }

    public static NoPaymentsFragment newInstance() {
        return new NoPaymentsFragment();
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void setUi(@Nullable Bundle savedInstanceState) {
        addContactWrapper.setOnClickListener(v -> EventBus.getDefault().post(new OnAccountAddClickEvent(Account.OwnerType.CONTACT)));
        addAccountWrapper.setOnClickListener(v -> EventBus.getDefault().post(new OnAccountAddClickEvent(Account.OwnerType.ACCOUNT)));
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_no_payments;
    }
}
