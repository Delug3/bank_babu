package com.bankbabu.balance.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bankbabu.balance.R;
import com.bankbabu.balance._common.navigator.core.FragmentData;
import com.bankbabu.balance._common.navigator.core.FragmentId;
import com.bankbabu.balance._common.navigator.manager.AppManagerUI;
import com.bankbabu.balance._common.navigator.manager.core.ManagerUI;
import com.bankbabu.balance.activities.core.BaseActivity;

public class MainActivity extends BaseActivity {

    @Nullable
    @Override
    public ManagerUI initManagerUI() {
        return new AppManagerUI(this, new FragmentData(FragmentId.BANK_BALANCE_CHECK, new Bundle()));
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected boolean useFacebookInterstitialAd() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUi() {
    }

    @Override
    public void onToolbarItemClicked(@org.jetbrains.annotations.Nullable final View view) {
       onBackPressed();
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("main-activity");
        askWriteExternalStoragePermissions();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            MainActivity.super.onBackPressed();
        }
    }
}
