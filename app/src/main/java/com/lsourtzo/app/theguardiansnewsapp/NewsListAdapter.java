package com.lsourtzo.app.theguardiansnewsapp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lsourtzo.app.theguardiansnewsapp.databinding.ItemMainBinding;

import java.util.ArrayList;

/**
 * Created by lsourtzo on 14/05/2017.
 */

public class NewsListAdapter extends ArrayAdapter<NewsList> {

    private int isMain;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists
     */
    public NewsListAdapter(Activity context, ArrayList<NewsList> newsList){
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, R.layout.item_main, newsList);
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent ) {
        View listItemView = convertView;
        int whereTheDoteIs;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_main, parent, false);
        }

        ItemMainBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.item_main, parent, false);
        binding.setNews(this.getItem(position));

        // Get the {@link Word} object located at this position in the list
        NewsList currentArticle = getItem(position);

        //check title size ...
        String category = currentArticle.getCat();
        if (category.length() > 60) {
            whereTheDoteIs = category.lastIndexOf(".");
            if ((whereTheDoteIs <= 60)&&(whereTheDoteIs>=1)) {
                category = currentArticle.getTitle().substring(0, whereTheDoteIs);
            } else {
                category = currentArticle.getTitle().substring(0, 50) + " ...";
            }
        }

        //check title size ...
        String basicArticle = currentArticle.getTitle();
        if (basicArticle.length() > 80) {
            whereTheDoteIs = basicArticle.lastIndexOf(".");
            if ((whereTheDoteIs <= 80)&&(whereTheDoteIs>=1)) {
                basicArticle = currentArticle.getTitle().substring(0, whereTheDoteIs+1);
            } else {
                basicArticle =currentArticle.getTitle().substring(0, 80) + " ...";
            }
        }

        if (position<=4){
            isMain = 1;
        }else if (position<=15){
            isMain = 2;
        }else{
            isMain=3;
        }
        NewsList article = new NewsList(category,basicArticle,currentArticle.getTime(),currentArticle.getImageURL(),currentArticle.getlinkText(),isMain);
        binding.setNews(article);
        return binding.getRoot();
    }
}