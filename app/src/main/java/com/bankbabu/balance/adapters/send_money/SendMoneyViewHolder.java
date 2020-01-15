package com.bankbabu.balance.adapters.send_money;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.models.SendMoney;


public class SendMoneyViewHolder extends BaseViewHolder<SendMoney> {

    private TextView textViewTitle;
    private ImageView itemIcon;

    SendMoneyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initUI() {
        textViewTitle = itemView.findViewById(R.id.text_view_title);
        itemIcon = itemView.findViewById(R.id.item_icon);
    }

    @Override
    protected void setListener() {
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(final SendMoney item) {
        super.bind(item);
        int width = itemView.getContext().getResources().getDisplayMetrics().widthPixels;
        int height = width / 5;

        itemView.setLayoutParams(new CardView.LayoutParams(LayoutParams.MATCH_PARENT, height));
        textViewTitle.setText(item.getTitle());
        itemIcon.setImageResource(item.getIconId());
    }

    @Override
    public void onClick(final View view) {
        final View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_call_now, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialogView.findViewById(R.id.btn_call).setOnClickListener(v -> {
            onCallClicked();
            alertDialog.dismiss();
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        setDialogWith(alertDialog, itemView.getContext());
    }

    private void onCallClicked() {
        String number = getItem().getNumber();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number + Uri.encode("#")));
        itemView.getContext().startActivity(intent);
    }

    private void setDialogWith(AlertDialog alertDialog, Context context) {
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);

        alertDialog.getWindow().setLayout(width, alertDialog.getWindow().getAttributes().height);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }
}
