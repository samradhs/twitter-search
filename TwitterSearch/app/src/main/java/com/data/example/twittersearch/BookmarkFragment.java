package com.data.example.twittersearch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

public class BookmarkFragment extends Fragment {

  private BookmarkAdapter adapter;
  private MainActivity    activity;
  private View            contentView;
  private boolean         bookmarksPresent;

  static BookmarkFragment newInstance() {

    return new BookmarkFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

    activity    = (MainActivity) getActivity();
    contentView = inflater.inflate(R.layout.fragment_bookmark, container, false);
    Set<String> allBookmarks = SharedPrefManager.getAllBookmarks(activity);

    if (allBookmarks.size() > 0) {
      bookmarksPresent = true;
      initRecyclerView(allBookmarks);
    }

    return contentView;
  }

  void addBookmark(Tweet tweet) {

    if (!bookmarksPresent) initRecyclerView(new HashSet<String>());
    adapter.add(tweet);
  }

  private void initRecyclerView(Set<String> allBookmarks) {

    adapter = new BookmarkAdapter(activity, allBookmarks);
    LinearLayoutManager manager = new LinearLayoutManager(activity);

    RecyclerView bookmarkCont = contentView.findViewById(R.id.bookmark_cont);
    bookmarkCont.setVisibility(View.VISIBLE);
    bookmarkCont.setLayoutManager(manager);
    bookmarkCont.setAdapter(adapter);

    View placeholder = contentView.findViewById(R.id.placeholder);
    placeholder.setVisibility(View.GONE);
  }
}
