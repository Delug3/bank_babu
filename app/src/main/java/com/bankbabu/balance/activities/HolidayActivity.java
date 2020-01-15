package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.adapters.holiday.HolidayRecyclerViewAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.AssetDatabase;
import com.bankbabu.balance.models.Holiday;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolidayActivity extends BaseActivity {

  @BindView(R.id.recycler_view_holidays) RecyclerView recyclerView;
  @Override
  protected int getContentView() {
    return R.layout.activity_holiday;
  }

  @Override
  protected void initUi() {
    ButterKnife.bind(this);
  }

  @Override
  protected void setUi(final Bundle savedInstanceState) {
    logEvent("holiday");

    final Intent intent = getIntent();

    if (intent.getExtras() == null) {
      return;
    }

    final int stateId = intent.getExtras().getInt(Constants.EXTRA_STATE_ID);
    final String stateName = intent.getExtras().getString(Constants.EXTRA_STATE_NAME);

    configureToolbar(stateName);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    final AssetDatabase assetDatabase = new AssetDatabase(HolidayActivity.this);

    recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

    final List<Holiday> holidays = assetDatabase.getHolidaysByStateId(stateId);
    final RecyclerView.Adapter adapter = new HolidayRecyclerViewAdapter(holidays);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
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

      case R.id.rate:
        rateApp();
        return true;

      default:
        return super.onOptionsItemSelected(menuItem);
    }
  }
}
