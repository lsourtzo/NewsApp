package com.lsourtzo.app.theguardiansnewsapp;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsourtzo on 14/05/2017.
 */

public class Fragment_Activity extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsList>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    android.app.FragmentManager fragmentManager;
    TextView CenterMessage;
    TextView errorTextView;
    WebView browser;
    TextView footerButton;
    private static final int NEWS_LOADER_ID = 1;
    private NewsListAdapter mAdapter;
    String requestUrl;
    String startURL;
    int Page;
    LoaderManager loaderManager;
    ProgressBar loadingIndicator;
    ProgressBar loadingIndicator2;

    public Fragment_Activity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = inflater.inflate(R.layout.content_main, container, false);
        loaderManager = getLoaderManager();
        browser = (WebView) rootView.findViewById(R.id.webView);

        //get Url from main Activity
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            requestUrl = getArguments().getString("finalUrl");
            startURL= getArguments().getString("finalUrl");
            Page = getArguments().getInt("Page");
            requestUrl=requestUrl+"&page="+Page;
            Page=Page+1;
        } else {
            try {
                loadingIndicator.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            requestUrl = "https://content.guardianapis.com/search?page-size=30&show-fields=thumbnail&api-key=6089f2c9-7906-4836-bbf2-a9b6cab55f02";
        }


        loadingIndicator = (ProgressBar) rootView.findViewById(R.id.loading_indicator);
        loadingIndicator2 = (ProgressBar) rootView.findViewById(R.id.loading_indicator2);
        errorTextView = (TextView) rootView.findViewById(R.id.errorMessage);

        //Fill Adapter
        // Create a new adapter that takes an empty list of books as input
        mAdapter = new NewsListAdapter(getActivity(), new ArrayList<NewsList>());
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Add a footer to the ListView
        //LayoutInflater inflater2 = getLayoutInflater();
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_layout, listView, false);
        listView.addFooterView(footer,null,false);

        // set footerbutton
        footerButton = (TextView) listView.findViewById(R.id.footerMessage);
        // and call footerButton Listener...
        footerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToNextPage();
            }
        });
        listView.setAdapter(mAdapter);

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsList article = mAdapter.getItem(position);
                // Send to Main Activity if Layer is visible
                ((MainActivity) getActivity()).isViewVisible("true");
                browser.getSettings().setLoadWithOverviewMode(true);
                browser.getSettings().setUseWideViewPort(true);
                browser.getSettings().setJavaScriptEnabled(true);
                browser.loadUrl(article.getlinkText());
                browser.setVisibility(View.VISIBLE);
                loadingIndicator2.setVisibility(View.VISIBLE);

                browser.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        loadingIndicator2.setProgress(progress);
                        if (progress == 100) {
                            loadingIndicator2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Update empty state with no connection error message
            errorTextView.setText("No Internet Connection");
            errorTextView.setVisibility(View.VISIBLE);
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void closeView() {
        browser.setVisibility(View.GONE);
        loadingIndicator2.setVisibility(View.GONE);
        browser.stopLoading();
        browser.loadUrl("about:blank");
    }

    @Override
    public Loader<List<NewsList>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new NewsListLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsList>> loader, List<NewsList> data) {
        // Hide loading indicator because the data has been loaded
        CenterMessage = (TextView) getActivity().findViewById(R.id.errorMessage);
        CenterMessage.setVisibility(View.GONE);
        // Hide Message because the data has been loaded
        try {
            loadingIndicator.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clear the adapter of previous books data
        mAdapter.clear();

        // If there is a valid list of {@link books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            errorTextView.setText(R.string.noNews);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsList>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    public void goToNextPage() {
        Class fragmentClass = null;
        Fragment fragment = null;

        //renew fragment activity
        try {
            fragmentClass = Fragment_Activity.class;
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString("finalUrl", startURL);
        bundle.putInt("Page", Page);
        // set Fragmentclass Arguments
        fragment.setArguments(bundle);
        fragmentManager = getFragmentManager();
        // Replace the existing fragment...by Reseting the stack.
        fragmentManager.popBackStackImmediate("0", 0);
        int count = fragmentManager.getBackStackEntryCount();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(String.valueOf(count)).commit();
        // show loading indicator
        try {
            loadingIndicator.setVisibility(View.VISIBLE);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
