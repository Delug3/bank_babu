package com.bankbabu.balance.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class USSDMenuActivity extends BaseActivity {
    public static final String EXTRA_BANK_NAME = "bank Name";
    @BindView(R.id.wrapper_message) CardView wrapperMessage;
    @BindView(R.id.wrapper_receive_money) CardView wrapperReceiveMoney;
    @BindView(R.id.wrapper_check_balance) CardView wrapperCheckBalance;
    @BindView(R.id.wrapper_profile) CardView wrapperProfile;
    @BindView(R.id.wrapper_upi_pin) CardView wrapperUpiPin;
    @BindView(R.id.wrapper_pending_requests) CardView wrapperPendingRequests;;
    @BindView(R.id.wrapper_transaction) CardView wrapperTransaction;;
    @BindView(R.id.ad_view) AdView adView;

    @Override
    protected int getContentView() {
        return R.layout.activity_ussd_menu;
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("ussd-menu");
        loadAdViewGoogle(adView);

        setupToolbar();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        wrapperMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(USSDMenuActivity.this, SendMoneyActivity.class);
                intent.putExtra(Constants.EXTRA_POST, 1);
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

        wrapperCheckBalance.setOnClickListener(v -> makeCall("*99*3" + Uri.encode("#")));

        wrapperProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(USSDMenuActivity.this, SendMoneyActivity.class);
                intent.putExtra(Constants.EXTRA_POST, 2);
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

        wrapperUpiPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(USSDMenuActivity.this, SendMoneyActivity.class);
                intent.putExtra(Constants.EXTRA_POST, 3);

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

        wrapperPendingRequests.setOnClickListener(v -> makeCall("*99*5" + Uri.encode("#")));

        wrapperReceiveMoney.setOnClickListener(v -> makeCall("*99*2" + Uri.encode("#")));

        wrapperTransaction.setOnClickListener(v -> makeCall("*99*6" + Uri.encode("#")));
    }

    private void setupToolbar() {
        String toolbarTitle;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRA_BANK_NAME)) {
            toolbarTitle = String.format("%s - %s", getIntent().getStringExtra(EXTRA_BANK_NAME), getString(R.string.ussd_banking));
        } else {
            toolbarTitle = getString(R.string.ussd_banking);
        }
        configureToolbar(toolbarTitle, R.drawable.ic_toolbar_arrow);
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
        Intent intent = new Intent(USSDMenuActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
