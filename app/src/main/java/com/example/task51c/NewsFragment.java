package com.example.task51c;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Fragment covers whole screen with data from one news item
public class NewsFragment extends Fragment implements RelatedRVAdapter.ItemClickListener {

    // Declare widgets
    TextView tvFragmentTitle, tvFragmentDescription;
    ImageView ivFragmentImage;
    RecyclerView rvFragmentRelatedNews;
    RelatedRVAdapter adapter;

    List<NewsItem> relatedNewsList = new ArrayList<>();

    // Required empty constructor
    public NewsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        // Initialise widgets
        tvFragmentTitle = rootView.findViewById(R.id.tvFragmentTitle);
        tvFragmentDescription = rootView.findViewById(R.id.tvFragmentDescription);
        ivFragmentImage = rootView.findViewById(R.id.ivFragmentImage);
        rvFragmentRelatedNews = rootView.findViewById(R.id.rvFragmentRelatedNews);

        // Get the news item data from arguments
        Bundle args = getArguments();
        if (args != null) {
            relatedNewsList = args.getParcelableArrayList("related_news");
            NewsItem newsItem = args.getParcelable("news_item");
            if (newsItem != null) {
                // Set news item data to views
                tvFragmentTitle.setText(newsItem.getTitle());
                tvFragmentDescription.setText(newsItem.getDescription());
                ivFragmentImage.setImageResource(newsItem.getImage());
            }
        }

        // Initialise RecyclerView adapter and layout manager
        adapter = new RelatedRVAdapter(relatedNewsList, requireContext());
        adapter.setClickListener(this);
        rvFragmentRelatedNews.setAdapter(adapter);
        rvFragmentRelatedNews.setLayoutManager(new LinearLayoutManager(requireContext()));

        return rootView;
    }

    // On click of a news item, change news info to that item
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(View v, int position) {
        tvFragmentTitle.setText(relatedNewsList.get(position).getTitle());
        tvFragmentDescription.setText(relatedNewsList.get(position).getDescription());
        ivFragmentImage.setImageResource(relatedNewsList.get(position).getImage());

        Collections.shuffle(relatedNewsList);
        adapter.notifyDataSetChanged();
    }
}