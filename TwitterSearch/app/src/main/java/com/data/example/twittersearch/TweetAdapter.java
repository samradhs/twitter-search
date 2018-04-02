package com.data.example.twittersearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int TWEET   = 0;
  private static final int LOADING = 1;
  private static final int SORT    = 2;

  private MainActivity activity;
  private List<Tweet>  tweets;
  private boolean      isLoadingAdded;

  TweetAdapter(MainActivity activity) {

    this.activity = activity;
    this.tweets   = new ArrayList<>();
  }

  private void add(Tweet tweet) {

    tweets.add(tweet);
    notifyItemInserted(tweets.size() - 1);
  }

  void addToList(List<Tweet> addedTweets) {

    tweets.addAll(addedTweets);
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    RecyclerView.ViewHolder holder = null;
    LayoutInflater inflater        = LayoutInflater.from(parent.getContext());

    switch (viewType) {

      case TWEET:
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        holder         = new TweetHolder(tweetView);
        break;

      case LOADING:
        View loading = inflater.inflate(R.layout.item_progress, parent, false);
        holder       = new LoadingHolder(loading);
        break;

      case SORT:
        View sort = inflater.inflate(R.layout.item_sort, parent, false);
        holder    = new SortHolder(sort);
    }

    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    Tweet tweet = tweets.get(position);

    switch (getItemViewType(position)) {

      case TWEET:
        TweetHolder tweetHolder = (TweetHolder) holder;
        tweetHolder.bindData(tweet);
        break;

      case LOADING:
      case SORT:
        break;
    }
  }

  @Override
  public int getItemCount() {

    return tweets == null ? 0 : tweets.size();
  }

  @Override
  public int getItemViewType(int position) {

    if (position != tweets.size() - 1) {
      return TWEET;
    } else if (isLoadingAdded) {
      return LOADING;
    } else {
      return SORT;
    }
//    return (position == tweets.size() - 1 && isLoadingAdded) ? LOADING : TWEET;
  }

  void addLoadingFooter() {

    isLoadingAdded = true;
    add(new Tweet());
  }

  void removeLoadingFooter() {

    isLoadingAdded  = false;
    int position    = tweets.size() - 1;
    Tweet item      = getItem(position);

    if (item != null) {
      tweets.remove(position);
      notifyItemRemoved(position);
    }
  }

  void addSortFooter() {

    add(new Tweet());
  }

  private Tweet getItem(int position) {

    return tweets.get(position);
  }

  private class TweetHolder extends RecyclerView.ViewHolder {

    private TextView tweetText;
    private TextView tweetLikes;
    private TextView reTweets;
    private TextView btnAdd;

    TweetHolder(View view) {
      super(view);

      tweetText   = view.findViewById(R.id.tweet_text);
      tweetLikes  = view.findViewById(R.id.like_text);
      reTweets    = view.findViewById(R.id.re_tweet_text);
      btnAdd      = view.findViewById(R.id.add_bookmark);
    }

    void bindData(final Tweet tweet) {

      tweetText.setText(tweet.getText());
      tweetLikes.setText(activity.getString(R.string.tweet_like_text, tweet.getLikeCount()));
      reTweets.setText(activity.getString(R.string.tweet_retweet_text, tweet.getRetweetCount()));

      btnAdd.setVisibility(View.VISIBLE);
      btnAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if (tweet.isBookmarked()) {
            Toast.makeText(activity, R.string.toast_already_bookmark, Toast.LENGTH_SHORT).show();

          } else {
            tweet.setBookmarked();
            SharedPrefManager.addBookmark(activity, tweet.toJsonString());
            activity.addBookmark(tweet);
            Toast.makeText(activity, R.string.toast_bookmarked, Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  private class LoadingHolder extends RecyclerView.ViewHolder {

    LoadingHolder(View itemView) {

      super(itemView);
    }
  }

  private class SortHolder extends RecyclerView.ViewHolder {

    SortHolder(View view) {
      super(view);

      TextView sortLike    = view.findViewById(R.id.sort_like);
      TextView sortRetweet = view.findViewById(R.id.sort_retweet);

      sortLike.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          sortByLike();
        }
      });

      sortRetweet.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          sortByRetweet();
        }
      });
    }

    private void sortByLike() {

      Toast.makeText(activity, R.string.toast_sorted_by_like, Toast.LENGTH_SHORT).show();
      tweets.remove(tweets.size() - 1);

      Collections.sort(tweets, new Comparator<Tweet>() {
        @Override
        public int compare(Tweet first, Tweet second) {

          int firstLike = first.getLikeCount();
          int secondLike = second.getLikeCount();

          if (secondLike > firstLike) return 1;
          else if (secondLike < firstLike) return -1;
          else return 0;
        }
      });

      addSortFooter();
      notifyDataSetChanged();
    }


    private void sortByRetweet() {

      Toast.makeText(activity, R.string.toast_sorted_by_retweets, Toast.LENGTH_SHORT).show();
      tweets.remove(tweets.size() - 1);

      Collections.sort(tweets, new Comparator<Tweet>() {
        @Override
        public int compare(Tweet first, Tweet second) {

          int firstRetweet  = first.getRetweetCount();
          int secondRetweet = second.getRetweetCount();

          if (secondRetweet > firstRetweet) return 1;
          else if (secondRetweet < firstRetweet) return -1;
          else return 0;
        }
      });

      addSortFooter();
      notifyDataSetChanged();
    }
  }
}
