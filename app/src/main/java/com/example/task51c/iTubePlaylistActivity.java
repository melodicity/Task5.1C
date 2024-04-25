package com.example.task51c;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class iTubePlaylistActivity extends AppCompatActivity implements PlaylistRVAdapter.ItemClickListener {

    RecyclerView rvPlaylist;
    PlaylistRVAdapter adapter;

    // DB Helper to read the playlist from storage
    playlistDBHelper dbHelper = new playlistDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_itube_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(Color.BLACK);

        // Create a DB Helper to get the playlist of videos (URL list)
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database to keep from the query
        String[] projection = { playlistDBHelper._ID, playlistDBHelper.COLUMN_URL};
        Cursor cursor = db.query(playlistDBHelper.TABLE_NAME, projection, null, null, null, null, null);

        // Add each element from the database to a list of URLs
        ArrayList<String> urlList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndexOrThrow(playlistDBHelper.COLUMN_URL));
            urlList.add(url);
        }
        cursor.close();
        db.close();

        // Setup the recycler view and adapter
        rvPlaylist = findViewById(R.id.rvPlaylist);
        assert urlList != null;
        adapter = new PlaylistRVAdapter(urlList, this);
        adapter.setClickListener(this);
        rvPlaylist.setAdapter(adapter);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(this));
    }

    // On click of a url item, go back to the main iTube activity, and play that video
    @Override
    public void onItemClick(View v, int position) {
        String url = adapter.getItem(position);
        Intent intent = new Intent(iTubePlaylistActivity.this, iTubeMainActivity.class);
        intent.putExtra("clicked_url", url);
        startActivity(intent);
        finish();
    }
}