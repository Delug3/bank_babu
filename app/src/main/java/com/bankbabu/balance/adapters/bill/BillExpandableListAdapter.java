package com.bankbabu.balance.adapters.bill;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnBillClickEvent;
import com.bankbabu.balance.events.OnShareBillClickEvent;
import com.bankbabu.balance.models.Bill;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class BillExpandableListAdapter extends BaseExpandableListAdapter {

    private List<Bill> bills;

    public BillExpandableListAdapter(List<Bill> listDataHeader) {
        this.bills = listDataHeader;
    }

    @Override
    public int getGroupCount() {
        return this.bills.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return bills.get(groupPosition).getChildBillList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        final Bill bill = (Bill) getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_group, null);
        }

        final TextView textViewGroupName = convertView.findViewById(R.id.text_view_group_name);
        textViewGroupName.setTypeface(null, Typeface.BOLD);
        textViewGroupName.setText(bill.getDueDate());

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.bills.get(groupPosition);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Bill bill = (Bill) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_child, null);
        }

        final TextView textViewPayee = convertView.findViewById(R.id.text_view_payee);
        final TextView textViewNote = convertView.findViewById(R.id.text_view_note);
        final MoneyTextView textViewAmount = convertView.findViewById(R.id.text_view_amount);

        final ImageView icon = convertView.findViewById(R.id.image_view_icon);
        final ImageView imageViewRepeat = convertView.findViewById(R.id.image_view_repeat);
        final ImageView imageViewNotification = convertView.findViewById(R.id.image_view_notification);

        final LinearLayout container = convertView.findViewById(R.id.fragment_container);
        final LinearLayout wrapperShare = convertView.findViewById(R.id.wrapper_share);
        final View divider = convertView.findViewById(R.id.divider);

        if (bill.getStatus() == 1) {
            icon.setImageResource(R.drawable.ic_payed);
            divider.setBackgroundResource(R.color.colorPrimary);

            container.setBackgroundResource(R.color.athens_gray);
            wrapperShare.setVisibility(View.GONE);
        } else {
            icon.setImageResource(R.drawable.ic_mark_complete);
            if (bill.getBillType() != null) {
                if (bill.getBillType().equals(convertView.getContext().getString(R.string.payable))) {
                    container.setBackgroundResource(R.color.cosmos);
                    wrapperShare.setBackgroundResource(R.color.cinnabar);
                    divider.setBackgroundResource(R.color.cinnabar);
                } else {
                    container.setBackgroundResource(R.color.bill_notpayable);
                    wrapperShare.setBackgroundResource(R.color.share_notpayable);
                    divider.setBackgroundResource(R.color.share_notpayable);
                }
            }
        }

        if (bill.getRepeat() == 1) {
            imageViewRepeat.setVisibility(View.VISIBLE);
        } else {
            imageViewRepeat.setVisibility(View.GONE);
        }

        if (bill.getNotification().equals("0")) {
            imageViewNotification.setVisibility(View.GONE);
        } else {
            imageViewNotification.setVisibility(View.VISIBLE);
        }

        textViewPayee.setText(bill.getPayee());
        textViewNote.setText(bill.getNotes());
        textViewAmount.setAmount(Float.parseFloat(bill.getAmount()));

        container.setOnClickListener(view -> EventBus.getDefault().post(new OnBillClickEvent(bill)));
        imageViewRepeat.setOnClickListener(view -> EventBus.getDefault().post(new OnBillClickEvent(bill)));
        imageViewNotification.setOnClickListener(view -> EventBus.getDefault().post(new OnBillClickEvent(bill)));
        icon.setOnClickListener(view -> EventBus.getDefault().post(new OnBillClickEvent(bill)));

        wrapperShare.setOnClickListener(view -> EventBus.getDefault().post(new OnShareBillClickEvent(bill)));

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bills.get(groupPosition).getChildBillList().get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setBills(final List<Bill> bills) {
        this.bills = bills;
        notifyDataSetChanged();
    }
}
