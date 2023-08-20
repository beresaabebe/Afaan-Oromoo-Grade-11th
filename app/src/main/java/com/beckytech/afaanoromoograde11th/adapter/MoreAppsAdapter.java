package com.beckytech.afaanoromoograde11th.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.afaanoromoograde11th.R;
import com.beckytech.afaanoromoograde11th.model.MoreAppsModel;

import java.util.List;

public class MoreAppsAdapter extends RecyclerView.Adapter<MoreAppsAdapter.MoreAppHolder> {
    private final List<MoreAppsModel> list;
    private final OnAppClicked onAppClicked;
    Context context;

    public MoreAppsAdapter(List<MoreAppsModel> list, OnAppClicked onAppClicked, Context context) {
        this.list = list;
        this.onAppClicked = onAppClicked;
        this.context = context;
    }

    @NonNull
    @Override
    public MoreAppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoreAppHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.more_app_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoreAppHolder holder, int position) {
        MoreAppsModel model = list.get(position);
        holder.appName.setText(model.getAppName());
        holder.appImage.setImageResource(model.getAppImage());
        holder.bgColor.setBackgroundColor(ContextCompat.getColor(context, model.getColor()));
        holder.itemView.setOnClickListener(view -> onAppClicked.clickedApp(model));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnAppClicked {
        void clickedApp(MoreAppsModel model);
    }

    public static class MoreAppHolder extends RecyclerView.ViewHolder {
        private final TextView appName;
        private final ImageView appImage;
        private final RelativeLayout bgColor;

        public MoreAppHolder(@NonNull View itemView) {
            super(itemView);
            appImage = itemView.findViewById(R.id.image_more);
            appName = itemView.findViewById(R.id.app_name);
            bgColor = itemView.findViewById(R.id.relative_bg);
        }
    }
}
