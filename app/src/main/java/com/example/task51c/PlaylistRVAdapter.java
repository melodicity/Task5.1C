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

public class PlaylistRVAdapter extends RecyclerView.Adapter<PlaylistRVAdapter.ViewHolder> {
    // Initialise fields
    private final List<String> urlList;
    private final Context context;
    private static ItemClickListener clickListener;

    // Constructor takes the list of items to show and context
    public PlaylistRVAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
    }

    // Inflates the row layout from xml when needed
    @NonNull
    @Override
    public PlaylistRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.playlist_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    // Binds the data to the view and elements in each row
    @Override
    public void onBindViewHolder(@NonNull PlaylistRVAdapter.ViewHolder holder, int position) {
        holder.tvURL.setText(urlList.get(position));
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return urlList.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvURL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvURL = itemView.findViewById(R.id.tvURL);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    // Method for getting item at click position
    public String getItem(int position) {
        return urlList.get(position);
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