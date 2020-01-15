package com.bankbabu.balance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bankbabu.balance.R;
import com.bankbabu.balance._common.navigator.core.ToolbarId;
import com.bankbabu.balance.activities.ArticlesActivity;
import com.bankbabu.balance.activities.CalculatorActivity;
import com.bankbabu.balance.activities.GstCalculatorActivity;
import com.bankbabu.balance.activities.MainActivity;
import com.bankbabu.balance.activities.NearByActivity;
import com.bankbabu.balance.activities.SipCalculatorActivity;
import com.bankbabu.balance.activities.core.BaseActivity.AdLoadedListener;
import com.bankbabu.balance.adapters.carousel.CarouselItem;
import com.bankbabu.balance.adapters.carousel.CarouselView;
import com.bankbabu.balance.adapters.tools.MainToolsRecyclerViewAdapter;
import com.bankbabu.balance.events.OnMainToolItemClickEvent;
import com.bankbabu.balance.models.MainTool;
import com.bankbabu.balance.utils.DataContainer;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsFragment extends BaseFragment {

    @BindView(R.id.recycler_view_tools) RecyclerView recyclerViewTools;
    @BindView(R.id.items_carousel) CarouselView itemsCarousel;
    @BindView(R.id.item_atm) LinearLayout atmLayout;
    @BindView(R.id.item_gst) LinearLayout gstLayout;
    @BindView(R.id.item_sip) LinearLayout sipLayout;
    @BindView(R.id.item_more) LinearLayout moreLayout;
    @BindView(R.id.item_blog) LinearLayout blogLayout;
    @Override
    protected boolean withToolbar() {
        return true;
    }

    @Override
    protected void initUi() {
        initToolbar(ToolbarId.TOOLBAR_RATE, R.string.app_name);
        toolbarContainer.setVisibility(View.VISIBLE);
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("tools-activity");
        loadAdViewGoogle(findViewById(R.id.ad_view));
        recyclerViewTools.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerViewTools.setLayoutManager(layout);

        final String[] toolNames = getResources().getStringArray(R.array.tool_names);
        final String[] toolDescription = getResources().getStringArray(R.array.tool_description);
        final List<MainTool> data = new ArrayList<>();
        for (int i = 0; i < toolNames.length; i++) {
            data.add(new MainTool(toolNames[i], toolDescription[i], DataContainer.TOOL_CLASSES[i]));
        }

        final RecyclerView.Adapter adapter = new MainToolsRecyclerViewAdapter(data, true);
        recyclerViewTools.setAdapter(adapter);

        ArrayList<CarouselItem> carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(getString(R.string.card_1_title), getString(R.string.card_1_body)));
        carouselItems.add(new CarouselItem(getString(R.string.card_2_title), getString(R.string.card_2_body)));
        carouselItems.add(new CarouselItem(getString(R.string.card_3_title), getString(R.string.card_3_body)));
        itemsCarousel.setItems(carouselItems);
        itemsCarousel.setCardClickListener(position -> {
            if (position == 0) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else if (position == 1) {
                startActivity(new Intent(getActivity(), NearByActivity.class));
            } else {
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
            }
        });


        atmLayout.setOnClickListener(v -> onMainToolItemClickEvent(new OnMainToolItemClickEvent(NearByActivity.class)));
        gstLayout.setOnClickListener(v -> onMainToolItemClickEvent(new OnMainToolItemClickEvent(GstCalculatorActivity.class)));
        sipLayout.setOnClickListener(v -> onMainToolItemClickEvent(new OnMainToolItemClickEvent(SipCalculatorActivity.class)));
        moreLayout.setOnClickListener(v -> onMainToolItemClickEvent(new OnMainToolItemClickEvent(CalculatorActivity.class)));
        blogLayout.setOnClickListener(view -> onMainToolItemClickEvent(new OnMainToolItemClickEvent(ArticlesActivity.class)));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onMainToolItemClickEvent(final OnMainToolItemClickEvent event) {
        loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
            private final Intent intent = new Intent(getContext(),
                    event.getIntentClass()).putExtras(getParentActivity().getIntent());

            @Override
            public void onAdFailedToLoad() {
                super.onAdFailedToLoad();
                startActivity(intent);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(intent);
            }
        }, R.string.interstitial_tool);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tools;
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}

