package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.adapters.sms.SmsBankRecyclerViewAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.AssetDatabase;
import com.bankbabu.balance.events.OnSmsBankItemClickEvent;
import com.bankbabu.balance.models.SmsBank;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmsBanksActivity extends BaseActivity {

    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.small_ad_view) AdView smallAdView;
    @BindView(R.id.recycler_view_sms_banking) RecyclerView recyclerView;;
    @BindView(R.id.placeholder) TextView placeholder;;

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sms_banking;
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("sms-banking");

        final String bankName = getIntent().getStringExtra(Constants.EXTRA_BANK_NAME);
        configureToolbar(bankName, R.drawable.ic_toolbar_arrow);


        final AssetDatabase assetDatabase = new AssetDatabase(SmsBanksActivity.this);
        final List<SmsBank> smsBanks = assetDatabase.getSmsBanksByName(bankName);
        if (smsBanks.isEmpty()) {
            placeholder.setVisibility(View.VISIBLE);
            loadAdViewGoogle(adView);
            smallAdView.setVisibility(View.GONE);
        } else {
            loadAdViewGoogle(smallAdView);
            adView.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        final RecyclerView.Adapter adapter = new SmsBankRecyclerViewAdapter(smsBanks);
        recyclerView.setAdapter(adapter);
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

    private void launchHomeScreen() {
        Intent intent = new Intent(SmsBanksActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onSmsBankItemClickEvent(OnSmsBankItemClickEvent event) {
        Intent intent = new Intent(this, SendSmsActivity.class);
        intent.putExtra(Constants.EXTRA_BANK_TITLE, event.getBank().getTitle());
        intent.putExtra(Constants.EXTRA_BANK_NAME, event.getBank().getBankName());
        intent.putExtra(Constants.EXTRA_BANK_MESSAGE, event.getBank().getMessage());
        intent.putExtra(Constants.EXTRA_BANK_INFO, event.getBank().getInfo());
        intent.putExtra(Constants.EXTRA_BANK_PHONE_NUMBER, event.getBank().getPhoneNumber());
        startActivity(intent);
    }
}
