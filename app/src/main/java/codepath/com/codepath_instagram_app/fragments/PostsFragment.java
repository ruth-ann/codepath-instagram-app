package codepath.com.codepath_instagram_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.com.codepath_instagram_app.PostAdapter;
import codepath.com.codepath_instagram_app.R;
import codepath.com.codepath_instagram_app.model.Post;

public class PostsFragment extends Fragment {

    protected SwipeRefreshLayout swipeContainer;
    public static final String TAG = "PostFragment";
    private RecyclerView postsRv;
    protected ArrayList<Post> mPosts;
    protected PostAdapter postAdapter;



    //onCreateView to inflate the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        connectRecyclerView(view);
        setUpSwipeContainer(view);
        queryPosts();
    }

    private void setUpSwipeContainer(View view) {
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });
        configureRefreshingColors();

    }

    private void configureRefreshingColors() {
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void connectRecyclerView(View view) {
        //find the RecyclerView
        postsRv = (RecyclerView) view.findViewById(R.id.rvPosts);
        //init the arraylist (data source)
        mPosts = new ArrayList<>();
        //construct the adapter from this datasource
        postAdapter = new PostAdapter(mPosts, getContext());
        //RecyclerView setup (layout manager, use adapter)
        postsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        postsRv.setAdapter(postAdapter);
    }


    protected void queryPosts(){
        final Post.Query postsQuery = new Post.Query();
        //uses methods defined in Post model to get the top 20 posts
        postsQuery.getTop(20).withUser().sortDescending();

        //gets the posts stored in the database on a background thread
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                updateData(posts, e);
            }
        });
    }

    private void updateData(List<Post> posts, ParseException e) {
        //error handling for when posts have not loaded
        if (e != null) {
            Log.e(TAG, "Error with query");
            Toast.makeText(getContext(), "Error loading posts", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            swipeContainer.setRefreshing(false);
            return;
        }

        //updates the adapter and the post list if posts have successfully loaded
        mPosts.addAll(posts);
        postAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }


}
