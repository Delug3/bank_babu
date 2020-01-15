package com.bankbabu.balance.fragments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearByFragment extends BaseFragment {

    @BindView(R.id.wrapper_print) LinearLayout wrapperPrint;
    @BindView(R.id.wrapper_xerox) LinearLayout wrapperXerox;
    @BindView(R.id.wrapper_atm) LinearLayout wrapperAtm;
    @BindView(R.id.wrapper_bank) LinearLayout wrapperBank;
    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.text_view_search) EditText editTextSearch;
    @BindView(R.id.button_yes) Button buttonYes;

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("near-by");

        loadAdViewGoogle(adView);

        wrapperAtm.setOnClickListener(view -> showMap(getString(R.string.atm)));
        wrapperXerox.setOnClickListener(view -> showMap(getString(R.string.xerox_shop)));
        wrapperBank.setOnClickListener(view -> showMap(getString(R.string.bank)));
        wrapperPrint.setOnClickListener(view -> showMap(getString(R.string.print_shop)));

        buttonYes.setOnClickListener(view -> {
            final String data = editTextSearch.getText().toString();
            if (data.length() > 0) {
                showMap(data);
            } else {
                Toast.makeText(getContext(), R.string.enter_data_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_near_by;
    }
}
