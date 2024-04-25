package com.example.task51c;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TopStoriesRVAdapter.ItemClickListener, NewsRVAdapter.ItemClickListener {

    /*
     NOTE:  The functionality of both subtask 1 and 2 are in the same app.
            The requirement of designing the news app with only one activity has been met,
            but more activities are used for the iTube app.
     */

    // Declare widgets
    RecyclerView rvTopStories, rvNews;
    TopStoriesRVAdapter topStoriesRVAdapter;
    NewsRVAdapter newsRVAdapter;
    FragmentContainerView fragmentNews;
    Button btnSubtask2;

    // Declare news lists
    List<NewsItem> newsItems = new ArrayList<>();
    List<NewsItem> randTopStoriesList = new ArrayList<>();
    List<NewsItem> randNewsList = new ArrayList<>();
    List<NewsItem> relatedNewsList = new ArrayList<>();
    NewsItem itemSelected;
    String[] newsTitles;
    String[] newsDescriptions;
    int[] newsImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise widgets
        rvTopStories = findViewById(R.id.rvTopStories);
        rvNews = findViewById(R.id.rvNews);
        fragmentNews = findViewById(R.id.fragmentNews);
        btnSubtask2 = findViewById(R.id.btnSubtask2);

        // Initialise the news data arrays
        newsTitles = getResources().getStringArray(R.array.news_titles);
        newsDescriptions = getResources().getStringArray(R.array.news_descriptions);

        // Retrieve the TypedArray for news images
        TypedArray typedArray = getResources().obtainTypedArray(R.array.news_images);
        int n = typedArray.length();
        newsImages = new int[n];
        for (int i = 0; i < n; i++) {
            newsImages[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();

        // Populate the news list with data from their arrays
        for (int i = 0; i < newsTitles.length; i++) {
            newsItems.add(new NewsItem(i, newsTitles[i], newsDescriptions[i], newsImages[i]));
        }

        // Shuffle the newsItems list, and split it into two lists for the recycler views
        Collections.shuffle(newsItems);
        int mid = newsItems.size() / 2;
        randTopStoriesList = newsItems.subList(0, mid);
        randNewsList = newsItems.subList(mid, newsItems.size());
        relatedNewsList = newsItems;

        // Initialise each RecyclerView's adapter and layout manager
        topStoriesRVAdapter = new TopStoriesRVAdapter(randTopStoriesList, this);
        topStoriesRVAdapter.setClickListener(this);
        rvTopStories.setAdapter(topStoriesRVAdapter);
        rvTopStories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        newsRVAdapter = new NewsRVAdapter(randNewsList, this);
        newsRVAdapter.setClickListener(this);
        rvNews.setAdapter(newsRVAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getWindow().setNavigationBarColor(Color.WHITE);

        // On click of btnSubtask2, go to the iTube App activity
        // NOTE: This is for subtask 2 - treat this as a separate app
        btnSubtask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the iTube App activity
                Intent intent = new Intent(MainActivity.this, iTubeMainActivity.class);
                startActivity(intent);
            }
        });
    }

    // On click of a news item, show a fragment of the news info and related items
    @Override
    public void onItemClick(View v, int position) {
        // Get the adapter which was interacted with
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();

        if (adapter != null) {
            // Get the item clicked from the adapter
            if (adapter instanceof TopStoriesRVAdapter) {
                itemSelected = ((TopStoriesRVAdapter) adapter).getItem(position);
            } else if (adapter instanceof NewsRVAdapter) {
                itemSelected = ((NewsRVAdapter) adapter).getItem(position);
            }

            if (itemSelected != null) {
                // Create a fragment
                Fragment fragment = new NewsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Pass selected news item data to the fragment
                Bundle args = new Bundle();
                args.putParcelable("news_item", itemSelected);
                args.putParcelableArrayList("related_news", (ArrayList<? extends Parcelable>) relatedNewsList);
                fragment.setArguments(args);

                // Show the fragment
                fragmentNews.setVisibility(View.VISIBLE);
                fragmentTransaction.replace(R.id.fragmentNews, fragment).commit();
            }
        }
    }

    // Change back button functionality
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (fragmentNews.getVisibility() == View.VISIBLE) {
                // If the news fragment is visible, hide it and consume the event
                fragmentNews.setVisibility(View.GONE);
                return true;    // event handled
            } else {
                // If the news fragment is not visible, do not override back button functionality
                return super.onKeyDown(keyCode, event);   // proceed with regular back button action
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}