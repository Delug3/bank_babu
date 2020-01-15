package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.events.OnScrollToFocusDownEvent;
import com.bankbabu.balance.fragments.SipCalculatorFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SipCalculatorActivity extends BaseActivity {

    @BindView(R.id.nested_scroll_view) NestedScrollView nestedScrollView;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sip_calculator;
    }

    @Override
    protected void initUi()
    {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        configureToolbar(getString(R.string.systematic_investment_plan_sip), R.drawable.ic_toolbar_arrow);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SipCalculatorFragment())
                .commit();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onScrollToPosition(final OnScrollToFocusDownEvent event) {
        nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN));
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
        Intent intent = new Intent(SipCalculatorActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null) {
            int push = getIntent().getIntExtra(Constants.EXTRA_PUSH, -1);
            if (push == 1) {
                startActivity(new Intent(SipCalculatorActivity.this, WelcomeActivity.class));
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

}
