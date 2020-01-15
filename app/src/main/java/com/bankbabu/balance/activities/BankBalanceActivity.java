package com.bankbabu.balance.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankBalanceActivity extends BaseActivity {
    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.text_view_bank_balance)TextView textViewBankBalance;
    @BindView(R.id.text_view_mini_statement)TextView textViewMiniStatement;
    @BindView(R.id.text_view_customer_care)TextView textViewCustomerCare;
    @BindView(R.id.text_view_banking_tools)TextView textViewTools;
    @BindView(R.id.wrapper_bank_balance)CardView wrapperBankBalance;
    @BindView(R.id.wrapper_mini_statement)CardView wrapperMiniStatement;
    @BindView(R.id.wrapper_customer_care)CardView wrapperCustomerCare;

    private String bankName, bankBalance, bankStatement, customerCare;


    @Override
    protected int getContentView() {
        return R.layout.activity_bank_balance;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("bank-balance");

        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            bankName = intent.getExtras().getString(Constants.EXTRA_BANK_NAME);
            bankBalance = intent.getExtras().getString(Constants.EXTRA_BANK_BALANCE);
            bankStatement = intent.getExtras().getString(Constants.EXTRA_BANK_STATEMENT);
            customerCare = intent.getExtras().getString(Constants.EXTRA_CUSTOMER_CARE);
        }
        textViewBankBalance.setText(bankBalance);
        textViewMiniStatement.setText(bankStatement);
        textViewCustomerCare.setText(customerCare);

        configureToolbar(bankName, R.drawable.ic_toolbar_arrow);

        loadAdViewGoogle(adView);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        wrapperBankBalance.setOnClickListener(v -> showCallDialog(bankBalance));

        final String numberPattern = "[0-9]+";
        final String number = bankStatement;

        if (number.matches(numberPattern) && bankStatement.length() > 0) {
            wrapperMiniStatement.setVisibility(View.VISIBLE);
        } else {
            wrapperMiniStatement.setVisibility(View.GONE);
        }

        wrapperMiniStatement.setOnClickListener(v -> showCallDialog(bankStatement));

        wrapperCustomerCare.setOnClickListener(v -> showCallDialog(customerCare));

        textViewTools.setOnClickListener(v -> startActivity(new Intent(BankBalanceActivity.this, CalculatorActivity.class)
                .putExtras(getIntent())));
    }

    private void showCallDialog(final String number) {

        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_call_now, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.btn_call).setOnClickListener(v -> {
            makeCall(number);
            alertDialog.dismiss();
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        setDialogWith(alertDialog);
    }

    private void setDialogWith(AlertDialog alertDialog) {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        alertDialog.getWindow().setLayout(width, alertDialog.getWindow().getAttributes().height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bank_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.share:
                share();
                return true;

            case R.id.home:
                launchHomeScreen();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        if (bankName.equals("State Bank of India")) {
            showInformationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(BankBalanceActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showInformationDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.sbi_information_dialog)
                .setPositiveButton(R.string.dialog_ok, (dialog, which) -> finish())
                .setOnCancelListener(dialog -> finish())
                .create()
                .show();
    }
}
