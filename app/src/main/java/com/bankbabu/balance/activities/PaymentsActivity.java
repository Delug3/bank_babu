package com.bankbabu.balance.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.events.OnAccountAddClickEvent;
import com.bankbabu.balance.events.OnAccountItemClickEvent;
import com.bankbabu.balance.events.OnAccountPageScrolled;
import com.bankbabu.balance.events.OnDeleteAccountClickEvent;
import com.bankbabu.balance.events.OnEditAccountClickEvent;
import com.bankbabu.balance.events.OnShareAccountClickEvent;
import com.bankbabu.balance.fragments.AccountsFragment;
import com.bankbabu.balance.fragments.NoPaymentsFragment;
import com.bankbabu.balance.models.Account;
import com.bankbabu.balance.utils.ShareUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private DatabaseHelper databaseHelper;
    private boolean accountSelect;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_payment_list;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("payment-states");
        configureToolbar(getString(R.string.accounts), R.drawable.ic_toolbar_arrow);
        loadAdViewGoogle(findViewById(R.id.ad_view));

        if(hasPaymentExtra(Constants.EXTRA_ACCOUNT_SELECT)) {
            accountSelect = getIntent().getExtras().getBoolean(Constants.EXTRA_ACCOUNT_SELECT);
        }

        databaseHelper = new DatabaseHelper(getApplicationContext());
        updatePayments();
    }

    private boolean hasPaymentExtra(String paymentExtra) {
        return getIntent().getExtras() != null && getIntent().hasExtra(paymentExtra);
    }

    private void updatePayments() {
        final List<Account> accounts = databaseHelper.getAllAccounts();

        if (accounts.isEmpty()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, NoPaymentsFragment.newInstance())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new AccountsFragment())
                    .commit();
        }

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null) {
            int push = getIntent().getIntExtra(Constants.EXTRA_PUSH, -1);
            if (push == 1) {
                startActivity(new Intent(PaymentsActivity.this, WelcomeActivity.class));
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePayments();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onEditAccountItemClickEvent(OnEditAccountClickEvent event) {
        Intent intent = new Intent(this, AddEditPaymentActivity.class);
        intent.putExtra(Constants.EXTRA_BILL_NOTIFICATION, "not");
        intent.putExtra(Constants.EXTRA_ACCOUNT_ID, event.getItem().getId());
        intent.putExtra(Constants.EXTRA_ACCOUNT_NAME, event.getItem().getName());
        intent.putExtra(Constants.EXTRA_ACCOUNT_NUMBER, event.getItem().getNumber());
        intent.putExtra(Constants.EXTRA_ACCOUNT_IFSC, event.getItem().getCode());
        intent.putExtra(Constants.EXTRA_BANK_NAME, event.getItem().getBankName());
        intent.putExtra(Constants.EXTRA_BILL_NOTE, event.getItem().getNote());
        intent.putExtra(Constants.EXTRA_CONTACT, event.getItem().getContactNumber());
        intent.putExtra(Constants.EXTRA_ACCOUNT_TYPE, event.getItem().getType());
        intent.putExtra(Constants.EXTRA_OWNER_TYPE, event.getItem().getOwnerType().getDbValue());
        startActivity(intent);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onShareAccountItemClickEvent(OnShareAccountClickEvent event) {
        ShareUtils.shareString(this, event.getAccount().getShareString(this));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onAccountAddClickEvent(OnAccountAddClickEvent event) {
        Intent intent = new Intent(this, AddEditPaymentActivity.class);
        intent.putExtra(Constants.EXTRA_OWNER_TYPE, event.getOwnerType().getDbValue());
        intent.putExtra(Constants.EXTRA_ACCOUNT_SELECT, accountSelect);
        intent.putExtra(Constants.EXTRA_BILL_ID, getIntent().getIntExtra(Constants.EXTRA_BILL_ID, 0));
        intent.putExtra(Constants.EXTRA_BILL_AMOUNT, getIntent().getStringExtra(Constants.EXTRA_BILL_AMOUNT));
        intent.putExtra(Constants.EXTRA_BILL_PAYEE,getIntent().getStringExtra(Constants.EXTRA_BILL_PAYEE));
        intent.putExtra(Constants.EXTRA_BILL_NOTE, getIntent().getStringExtra(Constants.EXTRA_BILL_NOTE));

        startActivity(intent);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onAccountPageScrolled(OnAccountPageScrolled event) {
        if (event.getPageIndex() == 0) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.keppel));
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onDeleteAccountItemClickEvent(OnDeleteAccountClickEvent event) {
        long count = databaseHelper.deleteAccount(event.getItem().getId());
        if (count > 0) {
            updatePayments();
        }
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onAccountItemClickEvent(OnAccountItemClickEvent event) {
        if(accountSelect) {
            Intent intent = new Intent();

            addAccountToIntent(event, intent);

            setResult(Activity.RESULT_OK, intent);
            finish();
            return;
        }

        showOptionsDialog(event);
    }

    private void showOptionsDialog(OnAccountItemClickEvent event) {
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_menu_unpaid, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        final CardView buttonEditBill = dialogView.findViewById(R.id.button_edit_bill);
        final CardView buttonDeleteBill = dialogView.findViewById(R.id.button_delete_bill);
        final CardView buttonMarkAsPaid = dialogView.findViewById(R.id.button_mark_as_paid);
        final CardView buttonShareBill = dialogView.findViewById(R.id.button_share_bill);

        if (buttonMarkAsPaid != null) {
            buttonMarkAsPaid.setVisibility(View.GONE);
        }
        buttonEditBill.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            EventBus.getDefault().post(new OnEditAccountClickEvent(event.getItem()));
        });

        buttonDeleteBill.setOnClickListener(view12 -> {
            alertDialog.dismiss();
            EventBus.getDefault().post(new OnDeleteAccountClickEvent(event.getItem()));
        });

        buttonShareBill.setOnClickListener(v -> {
            alertDialog.dismiss();

            share();
        });

        alertDialog.show();
    }

    private void addAccountToIntent(OnAccountItemClickEvent event, Intent intent) {
        intent.putExtra(Constants.EXTRA_ACCOUNT_ID, event.getItem().getId());
        intent.putExtra(Constants.EXTRA_ACCOUNT_NAME, event.getItem().getName());
        intent.putExtra(Constants.EXTRA_ACCOUNT_NUMBER, event.getItem().getNumber());
        intent.putExtra(Constants.EXTRA_BANK_NAME, event.getItem().getBankName());
        intent.putExtra(Constants.EXTRA_ACCOUNT_TYPE, event.getItem().getType());
        intent.putExtra(Constants.EXTRA_ACCOUNT_IFSC, event.getItem().getCode());
        intent.putExtra(Constants.EXTRA_CONTACT, event.getItem().getContactNumber());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
