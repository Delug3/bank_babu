package com.bankbabu.balance.adapters.tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Tool;

import java.util.ArrayList;


public class ToolsRecyclerViewAdapter extends RecyclerView.Adapter<ToolViewHolder> {

    private final ArrayList<Tool> tools;
    private final AdapterType type;

    public ToolsRecyclerViewAdapter(final ArrayList<Tool> tools, final AdapterType type) {
        this.tools = tools;
        this.type = type;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        switch (type) {
            default:
            case TOOL:
                return new ToolViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item_tool, parent, false));
            case CALENDAR_TOOL:
                return new ToolViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item_tool_calendar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ToolViewHolder holder, final int position) {
        holder.bind(tools.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    public enum AdapterType {
        TOOL, CALENDAR_TOOL
    }
}
