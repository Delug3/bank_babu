package com.bankbabu.balance.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnGstRateChangedEvent;
import com.bankbabu.balance.events.OnInitialAmountIsEmptyEvent;
import com.bankbabu.balance.events.OnInitialAmountTextChangedEvent;
import com.bankbabu.balance.events.OnResetButtonClickEvent;
import com.bankbabu.balance.utils.AbstractTextWatcher;
import com.bankbabu.balance.utils.LocaleHelper;
import com.bankbabu.balance.views.CurrencyEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GstCalculatorHeaderFragment extends BaseFragment {

    @BindView(R.id.edit_text_initial_amount) CurrencyEditText editTextInitialAmount;
    @BindView(R.id.text_view_gst_rate) TextView textViewGstRate;
    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.button_gst_rate_six) ToggleButton buttonGstRateSix;
    @BindView(R.id.button_gst_rate_twelve) ToggleButton buttonGstRateTwelve;
    @BindView(R.id.button_gst_rate_eighteen) ToggleButton buttonGstRateEighteen;
    @BindView(R.id.button_gst_rate_twenty_eight) ToggleButton buttonGstRateTwentyEight;

    private final OnClickListener onToggleClickListener = new OnClickListener() {
        @Override
        public void onClick(final View view) {
            ((RadioGroup) view.getParent()).check(view.getId());
            ((ToggleButton) view).setChecked(radioGroup.getCheckedRadioButtonId() == view.getId());
            final String gstRate = ((ToggleButton) view).getText().toString();
            textViewGstRate.setText(gstRate);
            EventBus.getDefault().post(new OnGstRateChangedEvent(gstRate));
        }
    };


    @Override
    boolean useEventBus() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
                if (view.getId() == i) {
                    view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                } else {
                    view.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                }
            }
        });
        buttonGstRateSix.setOnClickListener(onToggleClickListener);
        buttonGstRateTwelve.setOnClickListener(onToggleClickListener);
        buttonGstRateEighteen.setOnClickListener(onToggleClickListener);
        buttonGstRateTwentyEight.setOnClickListener(onToggleClickListener);

        editTextInitialAmount.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                EventBus.getDefault()
                        .post(new OnInitialAmountTextChangedEvent(editTextInitialAmount.getRawValue()));
            }
        });

        setDefaultConfiguration();
    }

    private void setDefaultConfiguration() {
        editTextInitialAmount.setText("");
        editTextInitialAmount.setLocale(LocaleHelper.getIndiaLocale());
        buttonGstRateTwelve.setChecked(true);
        onToggleClickListener.onClick(buttonGstRateTwelve);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_gst_calculator_header;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onInitialAmountIsEmpty(OnInitialAmountIsEmptyEvent event) {
        if (event.isEmpty()) {
            editTextInitialAmount.setError(getString(R.string.error_initial_amount_is_empty));
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onResetButtonClickEvent(OnResetButtonClickEvent event) {
        setDefaultConfiguration();
    }
}
