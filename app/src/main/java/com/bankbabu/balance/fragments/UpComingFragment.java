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
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.AddEditFormActivity;
import com.bankbabu.balance.activities.ShareBillActivity;
import com.bankbabu.balance.adapters.bill.BillExpandableListAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.events.OnBillClickEvent;
import com.bankbabu.balance.events.OnShareBillClickEvent;
import com.bankbabu.balance.models.Bill;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.fab_add_payment) FloatingActionButton fab;
    @BindView(R.id.expandable_list_view) ExpandableListView expandableListView;
    @BindView(R.id.wrapper_add_bill) LinearLayout wrapperAddBill;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ad_view) AdView adView;

    private DatabaseHelper databaseHelper;
    private BillExpandableListAdapter listAdapter;
    private List<Bill> bills;

    @Override
    boolean useEventBus() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this, getActivity());
    }

    @SuppressLint("InflateParams")
    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        swipeRefreshLayout.setOnRefreshListener(this);
        loadAdViewGoogle(adView);

        swipeRefreshLayout.post(
                () -> {
                    swipeRefreshLayout.setRefreshing(true);
                    updateBills();
                }
        );

        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddEditFormActivity.class)));
    }

    private void updateBills() {
        swipeRefreshLayout.setRefreshing(true);

        bills = databaseHelper.getAllUnpaidBills();

        if (bills.size() > 0) {
            wrapperAddBill.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
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
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_up_coming;
    }

    private void shareBill(final Bill bill) {
        Intent intent = new Intent(getActivity(), ShareBillActivity.class);
        intent.putExtra(Constants.EXTRA_BILL_ID, bill.getId());
        intent.putExtra(Constants.EXTRA_BILL_PAYEE, bill.getPayee());
        intent.putExtra(Constants.EXTRA_BILL_AMOUNT, bill.getAmount());
        intent.putExtra(Constants.EXTRA_BILL_NOTE, bill.getNotes());
        startActivity(intent);
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
    public void onShareBillClickEvent(OnShareBillClickEvent event) {
        shareBill(event.getBill());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onBillClickEvent(OnBillClickEvent event) {
        final Bill bill = event.getBill();
        if (!containsBill(bill)) {
            return;
        }

        final View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_menu_unpaid, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        alertDialog.show();


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        setDialogWith(alertDialog);

        final CardView buttonEdit = dialogView.findViewById(R.id.button_edit_bill);
        final CardView buttonDelete = dialogView.findViewById(R.id.button_delete_bill);
        final CardView buttonMarkPaid = dialogView.findViewById(R.id.button_mark_as_paid);
        final CardView buttonShareBill = dialogView.findViewById(R.id.button_share_bill);

        buttonShareBill.setOnClickListener(view -> {
            alertDialog.dismiss();
            shareBill(bill);
        });

        buttonEdit.setOnClickListener(view -> {
            alertDialog.dismiss();
            editBill(bill);
        });

        buttonDelete.setOnClickListener(view -> {
            showConfirmDialog(R.drawable.ic_delete_dialog,
                    getString(R.string.delete_dialog_title),
                    getString(R.string.delete_dialog_body),
                    getString(R.string.delete_dialog_confirm), () -> {
                        int count = databaseHelper.deleteBill(bill.getId());
                        if (count > 0) {
                            updateBills();
                        }
                    });
            alertDialog.dismiss();
        });

        buttonMarkPaid.setOnClickListener(view -> {
            showConfirmDialog(R.drawable.ic_dialog_mark_payed,
                    getString(R.string.completed_dialog_title),
                    getString(R.string.completed_dialog_body),
                    getString(R.string.completed_dialog_confirm), () -> {
                        int count = databaseHelper.setPaidStatus(bill.getId());
                        if (count > 0) {
                            updateBills();
                        }
                    });
            alertDialog.dismiss();
        });

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

    private void setDialogWith(AlertDialog alertDialog) {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);

        alertDialog.getWindow().setLayout(width, alertDialog.getWindow().getAttributes().height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void editBill(final Bill bill) {
        Intent intent = new Intent(getActivity(), AddEditFormActivity.class);
        intent.putExtra(Constants.EXTRA_BILL_ID, bill.getId());
        intent.putExtra(Constants.EXTRA_BILL_PAYEE, bill.getPayee());
        intent.putExtra(Constants.EXTRA_BILL_AMOUNT, bill.getAmount());
        intent.putExtra(Constants.EXTRA_BILL_CATEGORY_NAME, bill.getCategoryName());
        intent.putExtra(Constants.EXTRA_BILL_CATEGORY_ICON, bill.getCategoryIcon());
        intent.putExtra(Constants.EXTRA_BILL_NOTE, bill.getNotes());
        intent.putExtra(Constants.EXTRA_BILL_DUE_DATE, bill.getDueDate());
        intent.putExtra(Constants.EXTRA_BILL_CUSTOM_DUE_DATE, bill.getCustomDueDate());
        intent.putExtra(Constants.EXTRA_BILL_TYPE, bill.getBillType());
        intent.putExtra(Constants.EXTRA_BILL_REPEAT, bill.getRepeat());
        intent.putExtra(Constants.EXTRA_BILL_NEXT_DUE_DATE, bill.getNextDueDate());
        intent.putExtra(Constants.EXTRA_BILL_REPEAT_EVERY, bill.getRepeatEvery());
        intent.putExtra(Constants.EXTRA_BILL_REPEAT_BY, bill.getRepeatByType().toString());
        intent.putExtra(Constants.EXTRA_BILL_STATUS, bill.getStatus());
        intent.putExtra(Constants.EXTRA_BILL_BUTTON, getString(R.string.tag_update));
        intent.putExtra(Constants.EXTRA_BILL_NOTIFICATION, bill.getNotification());
        startActivity(intent);
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
