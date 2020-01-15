package com.bankbabu.balance.adapters.tools;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.models.MainTool;

import java.util.List;

public class MainToolsRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder<MainTool>> {

    private List<MainTool> tools;

    private boolean bigItem;

    public MainToolsRecyclerViewAdapter(List<MainTool> data, final boolean bigItem) {
        this.tools = data;
        this.bigItem = bigItem;
    }

    @NonNull
    @Override
    public BaseViewHolder<MainTool> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.list_item_tool_main_big:
                return new MainTooBigViewHolder(view);
            default:
            case R.layout.list_item_tool_main:
                return new MainToolViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        if (bigItem) {
            return R.layout.list_item_tool_main_big;
        }

        return R.layout.list_item_tool_main;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<MainTool> holder,
                                 final int listPosition) {
        holder.bind(tools.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

}
