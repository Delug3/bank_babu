package com.bankbabu.balance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.utils.InternetConnection;
import com.bankbabu.balance.utils.SharedPreferenceUtils;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankMenuActivity extends BaseActivity {
    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.text_view_bank_name) TextView textViewBankName;
    @BindView(R.id.wrapper_bank_balance) LinearLayout wrapperBankBalance;
    @BindView(R.id.wrapper_ussd_banking) LinearLayout wrapperUSSD;
    @BindView(R.id.wrapper_sms_banking) LinearLayout wrapperSMS;
    @BindView(R.id.wrapper_Internet_banking) LinearLayout wrapperInternetBanking;
    @BindView(R.id.wrapper_nearest_atm) LinearLayout wrapperNearestAtm;
    @BindView(R.id.wrapper_nearest_bank) LinearLayout wrapperNearestBank;

    private String bankName, bankBalance, bankStatement, customerCare, internetBanking;

    @Override
    protected boolean useFacebookInterstitialAd() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bank_menu;
    }

    @Override
    protected void initUi() {

        ButterKnife.bind(this);
        loadAdViewGoogle(adView);

    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("bank-menu");

        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            bankName = intent.getExtras().getString(Constants.EXTRA_BANK_NAME);
            bankBalance = intent.getExtras().getString(Constants.EXTRA_BANK_BALANCE);
            bankStatement = intent.getExtras().getString(Constants.EXTRA_BANK_STATEMENT);
            customerCare = intent.getExtras().getString(Constants.EXTRA_CUSTOMER_CARE);
            internetBanking = intent.getExtras().getString(Constants.EXTRA_INTERNET_BANKING);
        }
        configureToolbar(bankName, R.drawable.ic_toolbar_arrow);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        textViewBankName.setText(bankName);

        wrapperNearestAtm.setOnClickListener(view -> showMap(getString(R.string.format_atm, bankName)));

        wrapperNearestBank.setOnClickListener(view -> showMap(getString(R.string.format_bank, bankName)));

        wrapperBankBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(BankMenuActivity.this, BankBalanceActivity.class);
                intent.putExtra(Constants.EXTRA_BANK_NAME, bankName);
                intent.putExtra(Constants.EXTRA_BANK_BALANCE, bankBalance);
                intent.putExtra(Constants.EXTRA_BANK_STATEMENT, bankStatement);
                intent.putExtra(Constants.EXTRA_CUSTOMER_CARE, customerCare);

                loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        startActivity(intent);
                    }

                    @Override
                    public void onAdFailedToLoad() {
                        startActivity(intent);
                    }
                }, R.string.interstitial_full_screen);
            }
        });

        wrapperUSSD.setOnClickListener(v -> {
            final boolean setUp = SharedPreferenceUtils.getInstance()
                    .getBooleanValue(SharedPreferenceUtils.SETUP, false);

            loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
                @Override
                public void onAdClosed() {
                    loadNextActivity();
                }

                private void loadNextActivity() {
                    if (!setUp) {
                        final Intent intent1 = new Intent(BankMenuActivity.this, USSDActivity.class);
                        intent1.putExtra(USSDActivity.EXTRA_BANK_NAME, bankName);
                        startActivity(intent1);
                    } else {
                        final Intent intent1 = new Intent(BankMenuActivity.this, USSDMenuActivity.class);
                        intent1.putExtra(USSDMenuActivity.EXTRA_BANK_NAME, bankName);
                        startActivity(intent1);
                    }
                }

                @Override
                public void onAdFailedToLoad() {
                    loadNextActivity();
                }
            }, R.string.interstitial_full_screen);

        });

        wrapperSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(BankMenuActivity.this, SmsBanksActivity.class);
                intent.putExtra(Constants.EXTRA_BANK_NAME, bankName);
                loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        startActivity(intent);
                    }

                    @Override
                    public void onAdFailedToLoad() {
                        startActivity(intent);
                    }
                }, R.string.interstitial_full_screen);
            }
        });

        wrapperInternetBanking.setOnClickListener(v -> {
            if (InternetConnection.checkConnection(getApplicationContext())) {
                showInternetExistDialog();
            } else {
                showNoInternetDialog();
            }
        });
    }

    @SuppressLint("InflateParams")
    private void showInternetExistDialog() {

        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_internet_banking, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.btn_step_out).setOnClickListener(v -> {
            try {
                final Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(internetBanking));
                startActivity(browser);
            } catch (Exception e) {
                Toast.makeText(BankMenuActivity.this, getString(R.string.no_browser_install),
                        Toast.LENGTH_LONG).show();
            }
            alertDialog.dismiss();
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        setDialogWith(alertDialog);
    }

    @SuppressLint("InflateParams")
    private void showNoInternetDialog() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_no_internet, null);

        final AlertDialog alertDialogNoInternet = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setView(dialogView).create();

        final Button buttonYes = dialogView.findViewById(R.id.button_yes);

        buttonYes.setOnClickListener(view -> {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            alertDialogNoInternet.dismiss();
        });

        alertDialogNoInternet.show();
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
                finish();
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

    private void launchHomeScreen() {
        Intent intent = new Intent(BankMenuActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        BankMenuActivity.super.onBackPressed();
    }
}
