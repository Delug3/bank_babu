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
import com.bankbabu.balance.fragments.GstCalculatorFragment;
import com.bankbabu.balance.fragments.GstCalculatorHeaderFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GstCalculatorActivity extends BaseActivity {

    @BindView(R.id.nested_scroll_view) NestedScrollView nestedScrollView;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gst_calculator;
    }

    @Override
    protected void initUi() {
        configureToolbar(getString(R.string.gst), R.drawable.ic_toolbar_arrow);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_gst_calculator, new GstCalculatorFragment())
                .replace(R.id.fragment_gst_calculator_header, new GstCalculatorHeaderFragment())
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
        Intent intent = new Intent(GstCalculatorActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null) {
            int push = getIntent().getIntExtra(Constants.EXTRA_PUSH, -1);
            if (push == 1) {
                startActivity(new Intent(GstCalculatorActivity.this, WelcomeActivity.class));
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

}
