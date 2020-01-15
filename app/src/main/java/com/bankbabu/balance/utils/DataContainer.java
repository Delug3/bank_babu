package com.bankbabu.balance.utils;


import android.content.Context;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.HomeActivity;
import com.bankbabu.balance.activities.MainActivity;
import com.bankbabu.balance.activities.PaymentsActivity;
import com.bankbabu.balance.models.Tool;

import java.util.ArrayList;

public class DataContainer {

    public static final int[] SUB_CATEGORY_ICONS =
            new int[]{R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_book,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_arrow_down, R.drawable.ic_category_arrow_up,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_locked,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_wallet,
                    R.drawable.ic_category_ambulance, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other,};

    public static final Class[] TOOL_CLASSES = {MainActivity.class, PaymentsActivity.class, HomeActivity.class};

    public static final int[] CATEGORY_ICONS =
            new int[]{R.drawable.ic_category_other,
                    R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other,
                    R.drawable.ic_category_other, R.drawable.ic_category_other,
                    R.drawable.ic_category_other};

    public static ArrayList<Tool> getArrayListCalculatorScreen(final Context context) {
        final ArrayList<Tool> tools = new ArrayList<>();
        tools.add(new Tool(R.drawable.ic_calculator_gst_icon,
                context.getString(R.string.gst_calculator).toUpperCase(), false));

        tools.add(new Tool(R.drawable.ic_calculator_sip,
                context.getString(R.string.sip_calculator).toUpperCase(), false));

        tools.add(new Tool(R.drawable.ic_calculator_emi,
                context.getString(R.string.emi_calculator).toUpperCase(), false));

        tools.add(new Tool(R.drawable.ic_calculator_pf,
                context.getString(R.string.pf_status).toUpperCase(), false));

        tools.add(new Tool(R.drawable.ic_calculator_fd,
                context.getString(R.string.fd_calculator).toUpperCase(), true));

        tools.add(new Tool(R.drawable.ic_calculator_rd,
                context.getString(R.string.calculator_rd).toUpperCase(), true));

        tools.add(new Tool(R.drawable.ic_calculator_home_loan,
                context.getString(R.string.calculator_home_loan).toUpperCase(), true));

        tools.add(new Tool(R.drawable.ic_calculator_car_loan,
                context.getString(R.string.calculator_car_loan).toUpperCase(), true));

        tools.add(new Tool(R.drawable.ic_calculator_pd,
                context.getString(R.string.calculator_personal_loan).toUpperCase(), true));

        return tools;
    }
}

