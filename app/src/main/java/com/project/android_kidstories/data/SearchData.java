package com.project.android_kidstories.data;

import com.project.android_kidstories.data.model.Story;

import java.util.ArrayList;
import java.util.List;

public class SearchData {

    private static List<Story> stories;

    public static List<Story> getStories() {
        return stories;
    }

    public static void setStories(List<Story> stories) {
        SearchData.stories = stories;
    }


    public static List<String> filterData(String searchString){
        List<String> searchResults =  new ArrayList<String>();
        if(searchString != null){
            searchString = searchString.toLowerCase();

            for(Story rec :  stories){
                if(rec.getTitle().toLowerCase().contains(searchString)){
                    searchResults.add(rec.getTitle());
                }
            }
        }
        return searchResults;
    }
}
