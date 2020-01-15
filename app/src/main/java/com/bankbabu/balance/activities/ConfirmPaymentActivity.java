package com.bankbabu.balance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.models.Account;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmPaymentActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.user_payee_name) TextView userPayeeName;
    @BindView(R.id.user_bank_name) TextView userBankName;
    @BindView(R.id.user_account_number)TextView userAccountNumber;
    @BindView(R.id.user_account_type) TextView userAccountType;
    @BindView(R.id.user_mode) TextView userMode;
    @BindView(R.id.reset_button) Button resetButton;
    @BindView(R.id.confirm_button) Button confirmButton;

    @Override
    protected int getContentView() {
        return R.layout.activity_confirm_payment;
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setUi(Bundle savedInstanceState) {
        configureToolbar(getString(R.string.add_contact_title), R.drawable.ic_toolbar_arrow);

        Intent intent = getIntent();

        if (!intent.hasExtra(Constants.EXTRA_ACCOUNT)) {
            return;
        }

        Account account = (Account) intent.getSerializableExtra(Constants.EXTRA_ACCOUNT);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        if (account.getOwnerType() == Account.OwnerType.ACCOUNT) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.keppel));
            confirmButton.setBackgroundResource(R.drawable.toggle_button_sip_background_green);
        }

        userPayeeName.setText(account.getName().toUpperCase());
        userBankName.setText(account.getBankName().toUpperCase());
        userAccountNumber.setText(account.getNumber());
        userAccountType.setText(account.getType().toUpperCase());
        userMode.setText(R.string.bank_transfer);

        resetButton.setOnClickListener(v -> finish());
        confirmButton.setOnClickListener(v -> {
            long id = databaseHelper.insertAccount(account);
            if (recordIssavedToDb(id)) {
                boolean accountSelect = getIntent().getBooleanExtra(Constants.EXTRA_ACCOUNT_SELECT, false);

                if(accountSelect) {
                    Intent intent1 = new Intent(this, ShareBillActivity.class);

                    addAccountToIntent(account, intent1);

                    intent1.putExtra(Constants.EXTRA_BILL_ID, getIntent().getIntExtra(Constants.EXTRA_BILL_ID, 0));
                    intent1.putExtra(Constants.EXTRA_BILL_AMOUNT, getIntent().getStringExtra(Constants.EXTRA_BILL_AMOUNT));
                    intent1.putExtra(Constants.EXTRA_BILL_PAYEE,getIntent().getStringExtra(Constants.EXTRA_BILL_PAYEE));
                    intent1.putExtra(Constants.EXTRA_BILL_NOTE, getIntent().getStringExtra(Constants.EXTRA_BILL_NOTE));

                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(this, PaymentsActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                }
            }
        });

        loadAdViewGoogle(adView);
    }

    private void addAccountToIntent(Account account, Intent intent1) {
        intent1.putExtra(Constants.EXTRA_ACCOUNT_ID, account.getId());
        intent1.putExtra(Constants.EXTRA_ACCOUNT_NAME, account.getName());
        intent1.putExtra(Constants.EXTRA_ACCOUNT_NUMBER, account.getNumber());
        intent1.putExtra(Constants.EXTRA_BANK_NAME, account.getBankName());
        intent1.putExtra(Constants.EXTRA_ACCOUNT_TYPE, account.getType());
        intent1.putExtra(Constants.EXTRA_ACCOUNT_IFSC, account.getCode());
        intent1.putExtra(Constants.EXTRA_CONTACT, account.getContactNumber());
    }

    private boolean recordIssavedToDb(long id) {
        return id > 0;
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
