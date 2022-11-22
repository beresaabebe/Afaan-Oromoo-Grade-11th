package com.beckytech.afaanoromoograde11th.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beckytech.afaanoromoograde11th.R;
import com.beckytech.afaanoromoograde11th.model.Model;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.PageViewHolder> {

    private final List<Model> list;
    private final onBookClicked bookClicked;

    public Adapter(List<Model> list, onBookClicked bookClicked) {
        this.list = list;
        this.bookClicked = bookClicked;
    }

    public interface onBookClicked {
        void clickedBook(Model model);
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        Model model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.subTitle.setText(model.getSubTitle());

        holder.itemView.setOnClickListener(v -> bookClicked.clickedBook(model));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected static class PageViewHolder extends RecyclerView.ViewHolder {

        TextView title, subTitle;
        ImageView imageView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title.setSelected(true);
            subTitle = itemView.findViewById(R.id.subTitle);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
