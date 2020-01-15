package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.fragments.PaymentReminderFragment;

public class HomeActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void initUi() {
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        configureToolbar(getString(R.string.payment_reminder), R.drawable.ic_toolbar_arrow);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PaymentReminderFragment())
                .commit();
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
        Intent intent = new Intent(HomeActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
