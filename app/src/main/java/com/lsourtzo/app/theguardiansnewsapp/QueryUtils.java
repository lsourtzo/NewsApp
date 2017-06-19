/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lsourtzo.app.theguardiansnewsapp;

import android.app.ListActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils extends ListActivity {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the  dataset and return a list of {@link List} objects.
     */
    public static List<NewsList> fetchNewsListData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link news}s
        List<NewsList> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link news}s
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link List} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<NewsList> extractFeatureFromJson(String newsJson) {
        // If the JSON string is empty or null, then return early.
        // values
        String cat = "";
        String title = "";
        String time = "";
        String linkURL = "";
        String imageURL = "";

        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<NewsList> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject temp = new JSONObject(newsJson);
            JSONObject baseJsonResponse = temp.getJSONObject("response");

            if (!baseJsonResponse.getString("pageSize").equals("0")) {
                // Extract the JSONArray associated with the key called "items",
                // which represents a list of features (or news).
                JSONArray newsArray = baseJsonResponse.getJSONArray("results");

                // For each news in the bookArray, create an {@link news} object
                for (int i = 0; i < newsArray.length(); i++) {

                    // Get a single news at position i within the list of news
                    JSONObject result = newsArray.getJSONObject(i);

                    // For a given news, extract the JSONObject associated with the
                    // key called "properties", which represents a list of all volumeInfo
                    // for that news.
                    //JSONObject result = currentBook.getJSONObject("results");

                    //Extract all keys checking if they has value
                    if (result.has("sectionName")) {
                        cat = result.getString("sectionName");
                    } else {
                        cat = "No title";
                    }
                    if (result.has("webTitle")) {
                        title = result.getString("webTitle");
                    } else {
                        title = "No title";
                    }
                    if (result.has("webUrl")) {
                        linkURL = result.getString("webUrl");
                    } else {
                        linkURL = "-";
                    }
                    if (result.has("webPublicationDate")) {
                        String tempTime = result.getString("webPublicationDate");
                        int tCharacterPosition = tempTime.lastIndexOf("T");
                        int zCharacterPosition = tempTime.lastIndexOf("Z");
                        time = tempTime.substring(tCharacterPosition+1,zCharacterPosition-3);
                    } else {
                        time = "-";
                    }

                    if (result.has("fields")) {
                        JSONObject images = result.getJSONObject("fields");
                        if (images.has("thumbnail")) {
                            imageURL = images.getString("thumbnail");
                        } else {
                            imageURL = "null";
                        }
                    }else {
                        imageURL = "null";
                    }

                    // Create a new {@link news} object with the magnitude, location, time,
                    // and url from the JSON response.
                    NewsList newsList = new NewsList(cat,title, time,imageURL, linkURL);

                    // Add the new {@link news} to the list of news.
                    news.add(newsList);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;
    }

}
