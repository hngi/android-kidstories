package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android_kidstories.adapters.CustomSearchAdapter;
import com.project.android_kidstories.data.SearchData;
import com.project.android_kidstories.ui.MainActivity;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;

import butterknife.BindView;

public class SearchResultsActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;

    String selectedSuggestionStoryId;
    String selectedSuggestionStoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearch();
    }

    private void handleSearch() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);

            CustomSearchAdapter adapter = new CustomSearchAdapter(this,
                    android.R.layout.simple_dropdown_item_1line,
                    SearchData.filterData(searchQuery));
            if(!adapter.isEmpty())
                listView.setAdapter(adapter);
            else{
                Toast.makeText(this,"Story Not found",Toast.LENGTH_LONG).show();
                finish();
            }

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            String info = intent.getDataString();
            Log.e("TAGGG",info);
            selectedSuggestionStoryId = info.substring(0,info.indexOf("&"));
            selectedSuggestionStoryTitle = info.substring(info.indexOf("&")+1);

            Intent mine = new Intent(this, SingleStoryActivity.class);
            mine.putExtra(SingleStoryActivity.STORY_ID_KEY, Integer.valueOf(selectedSuggestionStoryId));
            mine.putExtra(SingleStoryActivity.STORY_NAME_KEY, selectedSuggestionStoryTitle);
            startActivity(mine);
            finish();
        }
    }

}
