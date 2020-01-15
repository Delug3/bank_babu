package com.bankbabu.balance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnResetButtonClickEvent;
import com.bankbabu.balance.events.OnToggleButtonClickEvent;
import com.bankbabu.balance.models.CalculationType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EmiCalculatorHeaderFragment extends BaseFragment {

    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.loan_amount_button) ToggleButton loanAmountButton;
    @BindView(R.id.monthly_emi_button) ToggleButton monthlyEmiButton;
    @BindView(R.id.interest_button) ToggleButton interestButton;
    @BindView(R.id.period_button) ToggleButton periodButton;
    @BindView(R.id.calculator_image) ImageView calculatorImage;
    @BindView(R.id.calculator_title) TextView calculatorTitle;

    private final OnClickListener onToggleClickListener = view -> {
        ((RadioGroup) view.getParent()).check(view.getId());
        ((ToggleButton) view).setChecked(radioGroup.getCheckedRadioButtonId() == view.getId());

        EventBus.getDefault()
                .post(new OnToggleButtonClickEvent(convertViewIdToCalculationType(view.getId())));
    };


    @Override
    boolean useEventBus() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this,getActivity());
    }

    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
            }

            switch (i) {
                case R.id.monthly_emi_button:
                    calculatorImage.setImageResource(R.drawable.ic_calculator_emi);
                    calculatorTitle.setText(R.string.emi_header);
                    break;
                case R.id.interest_button:
                    calculatorImage.setImageResource(R.drawable.return_rate_icon);
                    calculatorTitle.setText(R.string.interest_header);
                    break;
                case R.id.period_button:
                    calculatorImage.setImageResource(R.drawable.investment_period_icon);
                    calculatorTitle.setText(R.string.period_header);
                    break;
                case R.id.loan_amount_button:
                    calculatorImage.setImageResource(R.drawable.sip_amount_icon);
                    calculatorTitle.setText(R.string.loan_amount_header);
                    break;
            }
        });

        loanAmountButton.setOnClickListener(onToggleClickListener);
        monthlyEmiButton.setOnClickListener(onToggleClickListener);
        interestButton.setOnClickListener(onToggleClickListener);
        periodButton.setOnClickListener(onToggleClickListener);

        setDefaultConfiguration();
    }

    private void setDefaultConfiguration() {
        monthlyEmiButton.setChecked(true);
        onToggleClickListener.onClick(monthlyEmiButton);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_emi_calculator_header;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onResetButtonClickEvent(OnResetButtonClickEvent event) {
        setDefaultConfiguration();
    }

    private CalculationType convertViewIdToCalculationType(final int id) {
        switch (id) {
            default:
            case R.id.loan_amount_button:
                return CalculationType.LOAN_AMOUNT;
            case R.id.monthly_emi_button:
                return CalculationType.EMI;
            case R.id.interest_button:
                return CalculationType.INTEREST;
            case R.id.period_button:
                return CalculationType.PERIOD;
        }
    }
}
