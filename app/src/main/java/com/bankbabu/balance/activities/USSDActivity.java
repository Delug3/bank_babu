package com.bankbabu.balance.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.utils.SharedPreferenceUtils;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class USSDActivity extends BaseActivity {

    @BindView(R.id.button_already) CardView buttonAlready;
    @BindView(R.id.button_dial) CardView buttonDial;
    @BindView(R.id.ad_view) AdView adView;
    public static final String EXTRA_BANK_NAME = "bank Name";


    @Override
    protected int getContentView() {
        return R.layout.activity_ussd;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("ussd-activity");
        loadAdViewGoogle(adView);

        setupToolbar();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        buttonAlready.setOnClickListener(v -> {
            final Intent intent = new Intent(USSDActivity.this, USSDMenuActivity.class);
            loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
                @Override
                public void onAdClosed() {
                    startActivity(intent);
                }

                @Override
                public void onAdFailedToLoad() {
                    startActivity(intent);
                }
            }, R.string.interstitial_full_screen);
        });

        buttonDial.setOnClickListener(v -> {
            SharedPreferenceUtils.getInstance().setValue(SharedPreferenceUtils.SETUP, true);
            makeCall("*99" + Uri.encode("#"));
        });
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
        Intent intent = new Intent(USSDActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
