package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android_kidstories.adapters.CustomSearchAdapter;
import com.project.android_kidstories.data.SearchData;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;

import butterknife.BindView;

public class SearchResultsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Log.e("TAGGG","INTO DOLLAR");
        listView.setOnClickListener(this);
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
            listView.setAdapter(adapter);

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            String info = intent.getDataString();
            Log.e("TAGGG",info);
            String selectedSuggestionStoryId =  info.substring(0,info.indexOf("&"));
            String selectedSuggestionStoryTitle = info.substring(info.indexOf("&"));
            //execution comes here when an item is selected from search suggestions
            //you can continue from here with user selected search item

            Intent mine = new Intent(this, SingleStoryActivity.class);
            mine.putExtra(SingleStoryActivity.STORY_ID_KEY, selectedSuggestionStoryId);
            mine.putExtra(SingleStoryActivity.STORY_NAME_KEY, selectedSuggestionStoryTitle);
            startActivity(mine);
            Toast.makeText(this, "selected search suggestion "+selectedSuggestionStoryId,
                    Toast.LENGTH_LONG).show();
            Log.e("TAGGG",selectedSuggestionStoryTitle);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,
                "clicked search result item is"+((TextView)v).getText().toString(),
                Toast.LENGTH_SHORT).show();
    }
}
