package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.adapters.state.StateRecyclerViewAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.AssetDatabase;
import com.bankbabu.balance.events.OnStateFavoriteClickEvent;
import com.bankbabu.balance.events.OnStateItemClickEvent;
import com.bankbabu.balance.models.State;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateActivity extends BaseActivity {

  @BindView(R.id.ad_view) AdView mAdView;
  @BindView(R.id.recycler_view_states) RecyclerView recyclerView;
  private AssetDatabase assetDatabase;
  private StateRecyclerViewAdapter adapter;


  @Override
  protected boolean useFacebookInterstitialAd() {
    return true;
  }

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
    return R.layout.activity_state;
  }

  @Override
  protected void setUi(final Bundle savedInstanceState) {
    logEvent("state-activity");

    loadAdViewGoogle(mAdView);
    assetDatabase = new AssetDatabase(StateActivity.this);

    configureToolbar(getString(R.string.app_name));

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    updateStates();
  }

  @Override
  protected boolean useGoogleInterstitialAd() {
    return true;
  }

  private void updateStates() {
    final List<State> states = assetDatabase.getStates();
    if (adapter == null) {
      recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
      adapter = new StateRecyclerViewAdapter(states);
      recyclerView.setAdapter(adapter);
    } else {
      adapter.setStates(states);
    }
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

  @Subscribe
  @SuppressWarnings("unused")
  public void onStateItemClickEvent(OnStateItemClickEvent event) {
    Intent intent = new Intent(this, HolidayActivity.class);
    intent.putExtra(Constants.EXTRA_STATE_ID, event.getState().getStateId());
    intent.putExtra(Constants.EXTRA_STATE_NAME, event.getState().getStateName());
    startActivity(intent);
  }

  @Subscribe
  @SuppressWarnings("unused")
  public void onStateFavoriteClickEvent(OnStateFavoriteClickEvent event) {
    int count = assetDatabase.setStateFavorite(event.getState().getStateId(),
        event.getState().getGridView() == 1);
    if (count > 0) {
      updateStates();
    }
  }

  @Override
  public void onBackPressed() {
    if (getIntent().getExtras() != null) {
      int push = getIntent().getIntExtra(Constants.EXTRA_PUSH, -1);
      if (push == 1) {
        startActivity(new Intent(StateActivity.this, WelcomeActivity.class));
        finish();
      } else {
        StateActivity.super.onBackPressed();
      }
    } else {
      super.onBackPressed();
    }
  }

}
