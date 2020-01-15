package com.bankbabu.balance.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.AddEditFormActivity;
import com.bankbabu.balance.adapters.bill.BillExpandableListAdapter;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.events.OnBillClickEvent;
import com.bankbabu.balance.models.Bill;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PaidFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.expandable_list_view) ExpandableListView expandableListView;
    @BindView(R.id.wrapper_add_bill) LinearLayout wrapperAddBill;
    @BindView(R.id.btn_refresh) LinearLayout refreshButton;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab_add_payment) FloatingActionButton fab;
    @BindView(R.id.ad_view) AdView adView;

    private DatabaseHelper databaseHelper;
    private List<Bill> bills;
    private BillExpandableListAdapter listAdapter;


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
        databaseHelper = new DatabaseHelper(getActivity());
        loadAdViewGoogle(adView);

        refreshButton.setOnClickListener(v -> updateBills());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    updateBills();
                }
        );

        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddEditFormActivity.class)));
    }

    private void updateBills() {
        swipeRefreshLayout.setRefreshing(true);

        bills = databaseHelper.getAllPaidBills();

        if (bills.size() > 0) {
            wrapperAddBill.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            if (listAdapter == null) {
                listAdapter = new BillExpandableListAdapter(bills);
                expandableListView.setAdapter(listAdapter);
            } else {
                listAdapter.setBills(bills);
            }
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                expandableListView.expandGroup(i);
            }
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            wrapperAddBill.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_paid;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBills();
    }

    @Override
    public void onRefresh() {
        updateBills();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onBillClickEvent(OnBillClickEvent event) {
        if (!containsBill(event.getBill())) {
            return;
        }

        showDialog(event.getBill());
    }

    private boolean containsBill(Bill bill) {
        boolean result = false;
        for (Bill bill1 : bills) {
            if (bill1.getId() == bill.getId()) {
                result = true;
                break;
            }

            if (!bill1.getChildBillList().isEmpty()) {
                for (Bill bill2 : bill1.getChildBillList()) {
                    if (bill2.getId() == bill.getId()) {
                        result = true;
                        break;
                    }
                }
            }

            if (result) {
                break;
            }
        }
        return result;
    }

    @SuppressLint("InflateParams")
    private void showDialog(final Bill bill) {
        final View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_menu_paid, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        final CardView btnDelete = dialogView.findViewById(R.id.button_delete_bill);
        final CardView btnMarkPaid = dialogView.findViewById(R.id.button_mark_as_unpaid);

        btnDelete.setOnClickListener(view -> {
            showConfirmDialog(R.drawable.ic_delete_dialog,
                    getString(R.string.delete_dialog_title),
                    getString(R.string.delete_dialog_body),
                    getString(R.string.delete_dialog_confirm), () -> {
                        int count = databaseHelper.deleteBill(bill.getId());
                        if (count > 0) {
                            alertDialog.dismiss();
                            updateBills();
                        }
                    });
            alertDialog.dismiss();
        });

        btnMarkPaid.setOnClickListener(view -> {
            showConfirmDialog(R.drawable.ic_dialog_mark_pending,
                    getString(R.string.dialog_pending_title),
                    getString(R.string.dialog_pending_body),
                    getString(R.string.dialog_pending_confirm),
                    () -> {
                        int count = databaseHelper.setBillUnPaid(bill.getId());
                        if (count > 0) {
                            alertDialog.dismiss();
                            updateBills();
                        }
                    });
            alertDialog.dismiss();
        });
    }

    private void showConfirmDialog(@DrawableRes int icon, String title, String bodyText, String confirmText, Runnable onConfirm) {
        final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm, null);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view1)
                .setCancelable(true)
                .create();

        ImageView iconImage = view1.findViewById(R.id.call_icon);
        iconImage.setImageResource(icon);


        TextView titleText = view1.findViewById(R.id.dialog_title);
        titleText.setText(title);

        TextView body = view1.findViewById(R.id.dialog_subtitle);
        body.setText(Html.fromHtml(bodyText));

        view1.findViewById(R.id.btn_call).setOnClickListener(v -> {
            onConfirm.run();
            dialog.dismiss();
        });
        view1.<TextView>findViewById(R.id.confirmText).setText(confirmText);
        view1.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
