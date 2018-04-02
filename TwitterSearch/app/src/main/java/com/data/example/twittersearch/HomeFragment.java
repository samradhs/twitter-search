package com.data.example.twittersearch;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by SamradhShukla
 * on 02/04/18.
 */

public class HomeFragment extends Fragment {

  // todo remove keys
	private static final String CONSUMER_KEY    = "use your consumer key";
	private static final String CONSUMER_SECRET = "use your consumer secret";
	private static final String ACCESS_TOKEN    = "use your access token";
	private static final String ACCESS_SECRET   = "use your access secret";

	private static final String LOG_TAG         = "HomeFragment";
	private static final int    NUM_OF_PAGES    = 5;
	private static final int    TWEETS_PER_PAGE = 20;
	private static final String SEARCH_TERM     = "pregnancy";
  private static final int    PAGE_START      = 1;

  private long maxId          = -1;
  private List<Tweet> tweets  = new ArrayList<>();
  private boolean firstTime   = true;

  private View          contentView;
  private TweetAdapter  tweetAdapter;
  private MainActivity  activity;

  private boolean isLoading;
  private boolean isLastPage;
  private int currentPage = PAGE_START;

  static HomeFragment newInstance() {

    return new HomeFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

    activity        = (MainActivity) getActivity();
    contentView     = inflater.inflate(R.layout.fragment_home, container, false);
    RecyclerView rv = contentView.findViewById(R.id.tweet_cont);
    tweetAdapter    = new TweetAdapter(activity);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
        activity, LinearLayoutManager.VERTICAL, false);

    rv.setLayoutManager(linearLayoutManager);
    rv.setItemAnimator(new DefaultItemAnimator());
    rv.setAdapter(tweetAdapter);

    rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
      @Override
      protected void loadMoreItems() {
        isLoading = true;
        currentPage++;
        downloadSearches();
      }

      @Override
      public boolean isLastPage() {
        return isLastPage;
      }

      @Override
      public boolean isLoading() {
        return isLoading;
      }
    });

    downloadSearches();

    return contentView;
  }

  private void loadFirstPage() {

    ProgressBar progressBar  = contentView.findViewById(R.id.progress_bar);
    progressBar.setVisibility(View.GONE);
    tweetAdapter.addToList(tweets);

    if (currentPage <= NUM_OF_PAGES) {
      tweetAdapter.addLoadingFooter();

    } else{
      isLastPage = true;
      tweetAdapter.addSortFooter();
    }
  }

  private void loadNextPage(List<Tweet> addedTweets) {

    tweetAdapter.removeLoadingFooter();
    isLoading = false;

    tweetAdapter.addToList(addedTweets);

    if (currentPage != NUM_OF_PAGES) tweetAdapter.addLoadingFooter();
    else isLastPage = true;
  }

	private void downloadSearches() {
		ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connMgr == null) return;
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
      new DownloadTwitterTask().execute(SEARCH_TERM);

		} else {
			Log.e(LOG_TAG, "No network connection available.");
      Toast.makeText(activity, R.string.toast_internet_off, Toast.LENGTH_LONG).show();
		}
	}

  @SuppressWarnings("all")
	private class DownloadTwitterTask extends AsyncTask<String, Void, List<Tweet>> {

    @Override
    protected List<Tweet> doInBackground(String... strings) {

      ConfigurationBuilder cb = new ConfigurationBuilder();
      cb.setDebugEnabled(true)
          .setOAuthConsumerKey(CONSUMER_KEY)
          .setOAuthConsumerSecret(CONSUMER_SECRET)
          .setOAuthAccessToken(ACCESS_TOKEN)
          .setOAuthAccessTokenSecret(ACCESS_SECRET);

      TwitterFactory tf         = new TwitterFactory(cb.build());
      twitter4j.Twitter twitter = tf.getInstance();
      List<Tweet> addedTweets   = new ArrayList<>();

      try {

        Query query = new Query(strings[0]);
        query.setCount(TWEETS_PER_PAGE);
        if (maxId != -1) query.setMaxId(maxId - 1);
        QueryResult result = twitter.search(query);

        List<twitter4j.Status> tweetList = result.getTweets();
        Log.i(LOG_TAG, "tweets size: " + tweetList.size());

        for (twitter4j.Status tweet : tweetList) {

          Tweet tweet1 = new Tweet(tweet.getText(), tweet.getFavoriteCount(), tweet.getRetweetCount());
          tweets.add(tweet1);
          addedTweets.add(tweet1);
          Log.v(LOG_TAG, "tweet: " + tweet1.getText() + ", like: " + tweet1.getLikeCount() + ", retweet: " + tweet1.getRetweetCount());
          maxId = tweet.getId();
        }

      } catch (TwitterException te) {
        Log.e(LOG_TAG, te.toString());
        return null;
      }

      return addedTweets;
    }

    @Override
    protected void onPostExecute(List<Tweet> addedTweets) {

      if (firstTime) {
        firstTime = false;
        loadFirstPage();

      } else {
        loadNextPage(addedTweets);
      }
    }
  }
}
