package com.bankbabu.balance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.utils.PfStatusUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PfStatusFragment extends BaseFragment {

    @BindView(R.id.button_sms) CardView buttonSms;
    @BindView(R.id.button_call) CardView buttonCall;
    @BindView(R.id.button_go_to_login) CardView buttonGoToLogin;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subtitle) TextView subtitle;

    @Override
    protected void initUi() {
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        title.setText(Html.fromHtml(getString(R.string.you_can_find_out_about_the_status)));
        subtitle.setText(Html.fromHtml(getString(R.string.please_keep_your_b_uan_number_b_ready)));
        loadAdMobBanner(R.id.ad_view, errorCode -> Log.d(CalculatorFragment.class.getSimpleName(), String.format("Could not load AdMob, Error code: [%s]", errorCode)));

        buttonSms.setOnClickListener(v -> {
            final Intent intent = PfStatusUtils.sms("7738299899", "EPFOHO UAN ENG");
            if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager())
                    != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), R.string.error_no_sms_app_where_found, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        buttonCall.setOnClickListener(v -> safeStartActivity(PfStatusUtils.call("011-22901406"), getContext()));

        buttonGoToLogin.setOnClickListener(v -> safeStartActivity(PfStatusUtils.webpage("https://epfindia.gov.in"), getContext()));
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_pf_status;
    }
}
