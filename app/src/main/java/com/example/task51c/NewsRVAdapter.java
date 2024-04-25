package com.example.task51c;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.ViewHolder> {
    // Initialise fields
    private final List<NewsItem> newsItems;
    private final Context context;
    private static ItemClickListener clickListener;

    // Constructor takes the list of items to show and context
    public NewsRVAdapter(List<NewsItem> newsItems, Context context) {
        this.newsItems = newsItems;
        this.context = context;
    }

    // Inflates the row layout from xml when needed
    @NonNull
    @Override
    public NewsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    // Binds the data to the view and elements in each row
    @Override
    public void onBindViewHolder(@NonNull NewsRVAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(newsItems.get(position).getTitle());
        holder.tvDescription.setText(newsItems.get(position).getDescription());
        holder.ivImage.setImageResource(newsItems.get(position).getImage());
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvDescription;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvDescription = itemView.findViewById(R.id.tvNewsDescription);
            ivImage = itemView.findViewById(R.id.ivNewsImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // Method for getting item at click position
    public NewsItem getItem(int position) {
        return newsItems.get(position);
    }

    // Allow click events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    // MainActivity implements this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }
}