package com.bankbabu.balance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.EmiCalculatorActivity;
import com.bankbabu.balance.activities.GstCalculatorActivity;
import com.bankbabu.balance.activities.PfStatusActivity;
import com.bankbabu.balance.activities.SipCalculatorActivity;
import com.bankbabu.balance.adapters.tools.ToolsRecyclerViewAdapter;
import com.bankbabu.balance.adapters.tools.ToolsRecyclerViewAdapter.AdapterType;
import com.bankbabu.balance.events.OnToolItemClickEvent;
import com.bankbabu.balance.utils.DataContainer;

import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CalculatorFragment extends BaseFragment {


    @BindView(R.id.recycler_view_more_banks) RecyclerView recyclerView;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void initUi(){
        ButterKnife.bind(this,getActivity());
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        loadAdMobBanner(R.id.ad_view, errorCode -> Log.d(CalculatorFragment.class.getSimpleName(), String.format("Could not load AdMob, Error code: [%s]", errorCode)));

        final int spanCount = getResources().getInteger(R.integer.gallery_columns);
        final LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        final RecyclerView.Adapter adapter = new ToolsRecyclerViewAdapter(
                DataContainer.getArrayListCalculatorScreen(Objects.requireNonNull(getContext())),
                AdapterType.CALENDAR_TOOL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_calculator;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onToolItemClick(OnToolItemClickEvent event) {
        launchNextActivity(event.getTool().getIcon());
    }

    private void launchNextActivity(final int icon) {
        switch (icon) {
            case R.drawable.ic_calculator_gst_icon:
                startActivity(new Intent(getContext(), GstCalculatorActivity.class));
                break;
            case R.drawable.ic_calculator_sip:
                startActivity(new Intent(getContext(), SipCalculatorActivity.class));
                break;
            case R.drawable.ic_calculator_emi:
                startActivity(new Intent(getContext(), EmiCalculatorActivity.class));
                break;
            case R.drawable.ic_calculator_pf:
                startActivity(new Intent(getContext(), PfStatusActivity.class));
                break;


        }
    }
}
