package com.data.example.twittersearch;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

class Tweet {

  private static final String LOG_TAG = "Tweet";
  private static final String TEXT    = "text";
  private static final String LIKE    = "like";
  private static final String RETWEET = "retweet";

  private String  text = "";
  private int     likeCount;
  private int     retweetCount;
  private boolean bookmarked;

  Tweet() {

  }

  Tweet(String text, int likeCount, int retweetCount) {

    this.text         = text;
    this.likeCount    = likeCount;
    this.retweetCount = retweetCount;
  }

  Tweet(String jsonString) {

    try {
      JSONObject json = new JSONObject(jsonString);
      text            = json.getString(TEXT);
      likeCount       = json.getInt(LIKE);
      retweetCount    = json.getInt(RETWEET);

    } catch (JSONException e) {
      Log.e(LOG_TAG, e.toString());
    }
  }

  String getText() {

    return text;
  }

  int getLikeCount() {

    return likeCount;
  }

  int getRetweetCount() {

    return retweetCount;
  }

  void setBookmarked() {

    bookmarked = true;
  }

  boolean isBookmarked() {

    return bookmarked;
  }

  String toJsonString() {

    JSONObject json = new JSONObject();

    try {
      json.put(TEXT,    text);
      json.put(LIKE,    likeCount);
      json.put(RETWEET, retweetCount);

    } catch (JSONException e) {
      Log.e(LOG_TAG, e.toString());
      return "";
    }

    return json.toString();
  }
}
