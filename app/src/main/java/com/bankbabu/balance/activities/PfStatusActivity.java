package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.fragments.PfStatusFragment;


public class PfStatusActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_pf_status;
    }

    @Override
    protected void initUi() {

    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        configureToolbar(getString(R.string.pf_status), R.drawable.ic_toolbar_arrow);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PfStatusFragment())
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
        Intent intent = new Intent(PfStatusActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null) {
            int push = getIntent().getIntExtra(Constants.EXTRA_PUSH, -1);
            if (push == 1) {
                startActivity(new Intent(PfStatusActivity.this, WelcomeActivity.class));
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

}
