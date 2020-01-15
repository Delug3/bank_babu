package com.bankbabu.balance.adapters.tools;

import android.view.View;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnMainToolItemClickEvent;
import com.bankbabu.balance.models.MainTool;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class MainTooBigViewHolder extends BaseViewHolder<MainTool> {

    private TextView textViewName;
    private TextView textViewDescription;

    MainTooBigViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initUI() {
        textViewName = findViewById(R.id.text_view_title);
        textViewDescription = findViewById(R.id.text_view_subtitle);
    }

    @Override
    protected void setListener() {
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(final MainTool item) {
        super.bind(item);

        String bankBalanceItemTitle = itemView.getContext().getResources().getStringArray(R.array.tool_names)[0];
        if (item.getName().equalsIgnoreCase(bankBalanceItemTitle)) {
            YoYo.with(Techniques.Pulse)
                    .duration(1000)
                    .repeat(5)
                    .playOn(itemView);
        }

        textViewName.setText(item.getName());
        textViewDescription.setText(item.getDescription());
    }

    @Override
    public void onClick(final View view) {
        EventBus.getDefault().post(new OnMainToolItemClickEvent(getItem().getIntentClass()));
    }
}
