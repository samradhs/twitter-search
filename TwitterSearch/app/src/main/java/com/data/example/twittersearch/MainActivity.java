package com.data.example.twittersearch;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

public class MainActivity extends Activity {

  private static final String HOME_TAG      = "homeTag";
  private static final String BOOKMARK_TAG  = "bookmarkTag";
  private static final String FRAGMENT_TAG  = "fragmentTag";

  private String fragmentTag = HOME_TAG;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.app_name);

    if (savedInstanceState == null) {
      loadHomeFragment();

    } else {
      switch (savedInstanceState.getString(FRAGMENT_TAG, HOME_TAG)) {

        case HOME_TAG:
          loadHomeFragment();
          break;

        case BOOKMARK_TAG:
          loadBookmarkFragment();
      }
    }

    BottomNavigationView bottomView = findViewById(R.id.bottom_navigation);
    bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

          case R.id.action_home:
            loadHomeFragment();
            return true;

          case  R.id.action_bookmark:
            loadBookmarkFragment();
            return true;

          default:
            return false;
        }
      }
    });
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    outState.putString(FRAGMENT_TAG, fragmentTag);
    super.onSaveInstanceState(outState);
  }

  private void loadHomeFragment() {

    FragmentManager fragmentManager = getFragmentManager();
    fragmentTag = HOME_TAG;

    if (getFragmentManager().findFragmentByTag(HOME_TAG) != null) {
      fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(HOME_TAG)).commit();
    } else {
      fragmentManager.beginTransaction().add(R.id.main_content, HomeFragment.newInstance(), HOME_TAG).commit();
    }

    if (fragmentManager.findFragmentByTag(BOOKMARK_TAG) != null) {
      fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(BOOKMARK_TAG)).commit();
    }
  }

  private void loadBookmarkFragment() {

    FragmentManager fragmentManager = getFragmentManager();
    fragmentTag = BOOKMARK_TAG;

    if (fragmentManager.findFragmentByTag(BOOKMARK_TAG) != null) {
      fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(BOOKMARK_TAG)).commit();
    } else {
      fragmentManager.beginTransaction().add(R.id.main_content, BookmarkFragment.newInstance(), BOOKMARK_TAG).commit();
    }

    if (fragmentManager.findFragmentByTag(HOME_TAG) != null) {
      fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(HOME_TAG)).commit();
    }
  }

  void addBookmark(Tweet tweet) {

    BookmarkFragment bookmarkFragment = (BookmarkFragment) getFragmentManager().findFragmentByTag(BOOKMARK_TAG);
    if (bookmarkFragment == null) return;
    bookmarkFragment.addBookmark(tweet);
  }
}
