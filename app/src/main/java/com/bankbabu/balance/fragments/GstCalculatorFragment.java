package com.bankbabu.balance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnGstRateChangedEvent;
import com.bankbabu.balance.events.OnInitialAmountIsEmptyEvent;
import com.bankbabu.balance.events.OnInitialAmountTextChangedEvent;
import com.bankbabu.balance.events.OnResetButtonClickEvent;
import com.bankbabu.balance.events.OnScrollToFocusDownEvent;
import com.bankbabu.balance.utils.CalculatorHelper;
import com.bankbabu.balance.utils.LocaleHelper;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GstCalculatorFragment extends BaseFragment {

    @BindView(R.id.button_add_gst) RadioButton buttonAddGst;
    @BindView(R.id.button_calculate) Button buttonCalculate;
    @BindView(R.id.reset_button) Button resetButton;

    @BindView(R.id.text_view_amount_gst) MoneyTextView textViewAmountGst;
    @BindView(R.id.text_view_total_gst) MoneyTextView textViewTotalGst;
    @BindView(R.id.text_view_cgst) MoneyTextView textViewCgst;
    @BindView(R.id.text_view_sgst) MoneyTextView textViewSgst;

    @BindView(R.id.total_calc_wrapper) LinearLayout totalCalcWrapper;
    @BindView(R.id.total_calc_placeholder) LinearLayout totalCalculationPlaceholder;

    private int gstRate;
    private double initialAmount;

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
        loadAdMobBanner(R.id.ad_view, errorCode -> Log.d(GstCalculatorFragment.class.getSimpleName(),
                String.format("Could not load AdMob, Error code: [%s]", errorCode)));

        buttonCalculate.setOnClickListener(v -> {
            hideKeyboard();
            if (!validateFields()) {
                return;
            }
            calculate();

            resetButton.setBackgroundResource(R.drawable.toggle_button_sip_background);
            resetButton.setTextColor(getResources().getColor(R.color.whisper));
            buttonCalculate.setBackgroundResource(R.drawable.gst_procent_border);
            buttonCalculate.setTextColor(getResources().getColor(R.color.colorPrimary));

            totalCalculationPlaceholder.setVisibility(View.GONE);
            totalCalcWrapper.setVisibility(View.VISIBLE);
        });
        resetButton.setOnClickListener(v -> {
            resetButton.setBackgroundResource(R.drawable.gst_procent_border);
            resetButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            buttonCalculate.setBackgroundResource(R.drawable.toggle_button_sip_background);
            buttonCalculate.setTextColor(getResources().getColor(R.color.white));

            totalCalculationPlaceholder.setVisibility(View.VISIBLE);
            totalCalcWrapper.setVisibility(View.GONE);
            EventBus.getDefault().post(new OnResetButtonClickEvent());
        });
    }

    private void hideKeyboard() {
        View view = getParentActivity().getCurrentFocus();
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) getParentActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private boolean validateFields() {
        EventBus.getDefault().post(new OnInitialAmountIsEmptyEvent(initialAmount == 0));

        return !(initialAmount == 0);
    }

    private void calculate() {
        // DEFAULT VALUE
        if (gstRate == 0) {
            gstRate = 12;
        }

        final String currencySymbol = Currency.getInstance(LocaleHelper.getIndiaLocale()).getSymbol();

        final double amount = CalculatorHelper
                .calculateAmount(initialAmount, gstRate, buttonAddGst.isChecked());

        textViewCgst.setAmount((float) CalculatorHelper.calculateCgst(amount), currencySymbol);
        textViewSgst.setAmount((float) CalculatorHelper.calculateSgst(amount), currencySymbol);
        textViewTotalGst.setAmount((float) (CalculatorHelper.calculateCgst(amount) + CalculatorHelper.calculateSgst(amount)),
                currencySymbol);
        if(buttonAddGst.isChecked()) {
            textViewAmountGst
                    .setAmount((float) (CalculatorHelper.calculateCgst(amount) + CalculatorHelper.calculateSgst(amount) + initialAmount),
                            currencySymbol);
        } else {
            textViewAmountGst.setAmount((float) (initialAmount), currencySymbol);
        }

        EventBus.getDefault().post(new OnScrollToFocusDownEvent());
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_gst_calculator;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGstRateChanged(OnGstRateChangedEvent event) {
        gstRate = Integer.parseInt(event.getGstRate().replace("%", ""));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onInitialAmountTextChanged(OnInitialAmountTextChangedEvent event) {
        initialAmount = event.getInitialAmount();
    }
}
