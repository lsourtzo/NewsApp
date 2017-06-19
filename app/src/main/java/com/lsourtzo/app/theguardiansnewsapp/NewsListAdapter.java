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
        String temp3 = currentArticle.getCat();
        if (temp3.length() > 60) {
            int temp2 = temp3.lastIndexOf(".");
            if ((temp2 <= 60)&&(temp2>=1)) {
                temp3 = currentArticle.getTitle().substring(0, temp2);
            } else {
                temp3 = currentArticle.getTitle().substring(0, 50) + " ...";
            }
        }

        //check title size ...
        String temp4 = currentArticle.getTitle();
        if (temp4.length() > 80) {
            int temp5 = temp4.lastIndexOf(".");
            if ((temp5 <= 80)&&(temp5>=1)) {
                temp4 = currentArticle.getTitle().substring(0, temp5+1);
            } else {
                temp4 =currentArticle.getTitle().substring(0, 80) + " ...";
            }
        }

        if (position<=4){
            isMain = 1;
        }else if (position<=15){
            isMain = 2;
        }else{
            isMain=3;
        }
        NewsList article = new NewsList(temp3,temp4,currentArticle.getTime(),currentArticle.getImageURL(),isMain);
        binding.setNews(article);

        return binding.getRoot();
    }
}