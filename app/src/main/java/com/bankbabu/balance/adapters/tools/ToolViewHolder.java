package com.bankbabu.balance.adapters.tools;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnToolItemClickEvent;
import com.bankbabu.balance.models.Tool;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class ToolViewHolder extends BaseViewHolder<Tool> {

    private LinearLayout itemLayout;
    private ImageView imageView;
    private TextView textViewTitle;
    private TextView comingSoon;

    ToolViewHolder(final View view) {
        super(view);
    }

    @Override
    protected void initUI() {
        itemLayout = itemView.findViewById(R.id.item_layout);
        imageView = itemView.findViewById(R.id.image_view);
        textViewTitle = itemView.findViewById(R.id.text_view_title);
        comingSoon = itemView.findViewById(R.id.coming_soon);
    }

    @Override
    protected void setListener() {
        super.setListener();
        itemLayout.setOnClickListener(this);
    }

    @Override
    public void bind(final Tool item) {
        super.bind(item);
        imageView.setImageDrawable(itemView.getContext().getResources().getDrawable(item.getIcon()));
        textViewTitle.setText(item.getTitle());

        if (item.isDisabled()) {
            itemLayout.setBackgroundResource(R.drawable.calculator_item_bg_disabled);
            textViewTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.calculator_disabled_text_color));
            comingSoon.setVisibility(View.VISIBLE);
            imageView.setAlpha(0.2F);
        } else {
            itemLayout.setBackgroundResource(R.drawable.calculator_item_bg);
            textViewTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
            comingSoon.setVisibility(View.GONE);
            imageView.setAlpha(1F);

        }
    }

    @Override
    public void onClick(final View view) {
        EventBus.getDefault().post(new OnToolItemClickEvent(getItem()));
    }
}
