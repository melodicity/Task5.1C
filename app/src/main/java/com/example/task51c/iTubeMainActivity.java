package com.example.task51c;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class iTubeMainActivity extends AppCompatActivity {

    // Declare widgets
    WebView webView;
    EditText etURLInput;
    Button btnPlay, btnAdd, btnPlaylist;

    // Declare playlist of URLS and //TODO DB Helper to manage saving this playlist
    //List<String> urlList = new ArrayList<>();;
    playlistDBHelper dbHelper = new playlistDBHelper(this);

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itube_main);

        // Initialise
        webView = findViewById(R.id.webView);
        etURLInput = findViewById(R.id.etURLInput);
        btnPlay = findViewById(R.id.btnPlay);
        btnAdd = findViewById(R.id.btnAdd);
        btnPlaylist = findViewById(R.id.btnPlaylist);

        webView.setBackgroundColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);

        // Enable JavaScript for the WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to load urls within the webView
        webView.setWebViewClient(new WebViewClient());

        // Check if a URL has been passed from the playlist activity
        if (getIntent().hasExtra("clicked_url")) {
            // Retrieve the clicked URL from the result data
            String url = getIntent().getStringExtra("clicked_url");

            // Set the URL input field to the clicked URL, and play it
            etURLInput.setText(url);
            playVideo();
        }

        // On "Play" click, load the URL as an embedded video
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });

        // On "Add" click, add the URL to a list
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etURLInput.getText().toString().trim();
                if (url.contains("youtube.com")) {
                    // Save it to the DB
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(playlistDBHelper.COLUMN_URL, url);
                    long id = db.insert(playlistDBHelper.TABLE_NAME, null, values);
                    if (id == -1) {
                        // Error saving to DB
                        Toast.makeText(iTubeMainActivity.this, "An error occurred saving to your playlist", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add the URL to the playlist data structure
                        //urlList.add(url);
                        Toast.makeText(iTubeMainActivity.this, "Video added to your playlist", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                    Toast.makeText(iTubeMainActivity.this, "Video could not be found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On "Playlist" click, create an intent to start the playlist Activity
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(iTubeMainActivity.this, iTubePlaylistActivity.class);
                //intent.putStringArrayListExtra("url_list", (ArrayList<String>) urlList);
                startActivity(intent);
            }
        });
    }

    // Method to play an embedded YouTube video based on the URL in the input field
    private void playVideo() {
        String url = etURLInput.getText().toString().trim();
        if (url.contains("youtube.com")) {
            // Get the video ID from the URL
            String id = url.substring(url.indexOf("v=") + 2);

            // Load an embed URL, constructed from the ID
            webView.loadUrl("https://www.youtube.com/embed/" + id + "?autoplay=1");
        } else {
            Toast.makeText(iTubeMainActivity.this, "Video could not be found", Toast.LENGTH_SHORT).show();
        }
    }
}
