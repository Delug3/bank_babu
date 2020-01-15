package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Application;
import com.bankbabu.balance.fragments.CalculatorFragment;

import java.util.Objects;


public class CalculatorActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_calculator;
    }

    @Override
    protected void initUi() {
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        configureToolbar(getString(R.string.calculators), R.drawable.ic_toolbar_arrow);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new CalculatorFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calculators_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.home:
                launchHomeScreen();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(CalculatorActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (((Application) getApplication()).isShowCalculatorAdOnBack()) {
            loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
                @Override
                public void onAdFailedToLoad() {
                    ((Application) getApplicationContext()).setShowCalculatorAdOnBack(false);
                    finish();
                }

                @Override
                public void onAdClosed() {
                    ((Application) getApplicationContext()).setShowCalculatorAdOnBack(false);
                    finish();
                }
            }, R.string.interstitial_full_screen);
        } else {
            finish();
        }
    }
}
