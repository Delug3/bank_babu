package com.bankbabu.balance.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.adapters.send_money.SendMoneyRecyclerViewAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.models.SendMoney;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendMoneyActivity extends BaseActivity {

    @BindView(R.id.recycler_view_states) RecyclerView recyclerView;
    @BindView(R.id.top_icon) ImageView topIcon;
    @BindView(R.id.top_title) TextView topTitle;
    @BindView(R.id.ad_view) AdView smallAdView;
    @BindView(R.id.big_ad_view) AdView bigAdView;

    @Override
    protected int getContentView() {
        return R.layout.activity_state;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("send-money");

        Intent intent = getIntent();
        int position = intent.getIntExtra(Constants.EXTRA_POST, -1);

        switch (position) {
            case 1:
                loadAdViewGoogle(smallAdView);
                topIcon.setImageResource(R.drawable.ic_send_money);
                topTitle.setText(R.string.send_money);
                configureToolbar(getString(R.string.send_money), R.drawable.ic_toolbar_arrow);
                final String[] sendMoney = getResources().getStringArray(R.array.send_money);
                final String[] sendMoneyCode = getResources().getStringArray(R.array.send_money_code);
                final TypedArray sendMOneyIcons = getResources().obtainTypedArray(R.array.send_money_icon);
                configureAdapter(sendMoney, sendMoneyCode, sendMOneyIcons);
                break;
            case 2:
                loadAdViewGoogle(smallAdView);
                topIcon.setImageResource(R.drawable.ic_my_profile);
                topTitle.setText(R.string.my_profile);
                configureToolbar(getString(R.string.my_profile), R.drawable.ic_toolbar_arrow);
                final String[] sendMoney1 = getResources().getStringArray(R.array.send_money_1);
                final String[] sendMoneyCode1 = getResources().getStringArray(R.array.send_money_code_1);
                final TypedArray sendMOneyIcons2 = getResources().obtainTypedArray(R.array.send_money_icon_2);
                configureAdapter(sendMoney1, sendMoneyCode1, sendMOneyIcons2);
                break;
            case 3:
                topIcon.setImageResource(R.drawable.ic_upi_pin);
                topTitle.setText(R.string.upi_pin);
                bigAdView.setVisibility(View.VISIBLE);
                loadAdViewGoogle(bigAdView);
                smallAdView.setVisibility(View.GONE);
                configureToolbar(getString(R.string.upi_pin), R.drawable.ic_toolbar_arrow);
                final String[] sendMoney2 = getResources().getStringArray(R.array.send_money_2);
                final String[] sendMoneyCode2 = getResources().getStringArray(R.array.send_money_code_2);
                final TypedArray sendMOneyIcons3 = getResources().obtainTypedArray(R.array.send_money_icon_3);
                configureAdapter(sendMoney2, sendMoneyCode2, sendMOneyIcons3);
                break;
            default:
                break;
        }
    }

    private void configureAdapter(final String[] titles, final String[] codes, final TypedArray icons) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        final RecyclerView.Adapter adapter = new SendMoneyRecyclerViewAdapter(
                createList(titles, codes, icons));
        recyclerView.setAdapter(adapter);
    }

    private List<SendMoney> createList(final String[] titles, final String[] codes, final TypedArray icons) {
        return new ArrayList<SendMoney>() {
            {
                for (int i = 0; i < titles.length; i++) {
                    add(new SendMoney(titles[i], codes[i], icons.getResourceId(i, R.drawable.ic_category_ambulance)));
                }
            }
        };
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
        Intent intent = new Intent(SendMoneyActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
