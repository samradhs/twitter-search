package com.data.example.twittersearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context     context;
  private List<Tweet> bookmarks;

  BookmarkAdapter(Context context, Set<String> bookmarks) {

    this.context    = context;
    this.bookmarks  = new ArrayList<>();

    for (String bookmark : bookmarks) {
      this.bookmarks.add(new Tweet(bookmark));
    }
  }

  void add(Tweet tweet) {

    bookmarks.add(tweet);
    notifyItemInserted(bookmarks.size() - 1);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
    return new BookmarkHolder(tweetView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    BookmarkHolder bookmarkHolder = (BookmarkHolder) holder;
    bookmarkHolder.bindData(bookmarks.get(position));
  }

  @Override
  public int getItemCount() {

    return bookmarks == null ? 0 : bookmarks.size();
  }

  @SuppressWarnings("unused")
  private Tweet getItem(int position) {

    return bookmarks.get(position);
  }

  private class BookmarkHolder extends RecyclerView.ViewHolder {

    private TextView tweetText;
    private TextView tweetLikes;
    private TextView reTweets;

    BookmarkHolder(View view) {
      super(view);

      tweetText   = view.findViewById(R.id.tweet_text);
      tweetLikes  = view.findViewById(R.id.like_text);
      reTweets    = view.findViewById(R.id.re_tweet_text);
    }

    void bindData(final Tweet tweet) {

      tweetText.setText(tweet.getText());
      tweetLikes.setText(context.getString(R.string.tweet_like_text, tweet.getLikeCount()));
      reTweets.setText(context.getString(R.string.tweet_retweet_text, tweet.getRetweetCount()));
    }
  }
}
