package com.data.example.twittersearch;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

class SharedPrefManager {

  private static final String BOOKMARK_PREF = "bookmark_pref";
  private static final String BOOKMARKS     = "bookmarks";

  static void addBookmark(Context context, String bookmarkToAdd) {

    SharedPreferences sharedPref    = context.getSharedPreferences(BOOKMARK_PREF, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    Set<String> bookmarksSet        = sharedPref.getStringSet(BOOKMARKS, new HashSet<String>());

    bookmarksSet.add(bookmarkToAdd);
    editor.putStringSet(BOOKMARKS, bookmarksSet);
    editor.apply();
  }

  static Set<String> getAllBookmarks(Context context) {

    SharedPreferences sharedPref = context.getSharedPreferences(BOOKMARK_PREF, Context.MODE_PRIVATE);
    return sharedPref.getStringSet(BOOKMARKS, new HashSet<String>());
  }
}
