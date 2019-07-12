package codepath.com.codepath_instagram_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import codepath.com.codepath_instagram_app.PostAdapter;
import codepath.com.codepath_instagram_app.ProfilePictureActivity;
import codepath.com.codepath_instagram_app.R;
import codepath.com.codepath_instagram_app.model.Post;

public class ProfileFragment extends Fragment {

    protected SwipeRefreshLayout swipeContainer;


    public static final String TAG = "ProfileFragment";
    private RecyclerView postsRv;
    protected ArrayList<Post> mPosts;
    protected PostAdapter postAdapter;
    private ImageView profilePictureIv;
    private TextView usernameTv;



    //onCreateView to inflate the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        findAllViews(view);
        connectRecyclerView(view);
        setHeader();
        setUpSwipeContainer(view);
        setProfilePictureListener();
        queryPosts();
    }

    private void findAllViews(View view) {
        //finds views by id
        profilePictureIv = view.findViewById(R.id.ivProfilePictureProfile);
        usernameTv = view.findViewById(R.id.etUsernameProfile);
    }

    private void setHeader() {
        //gets the profile picture of the user and their username and adds it to the header on the profile page
        ParseUser currentUser = ParseUser.getCurrentUser();
        setUsername(currentUser);
        setProfilePicture(currentUser);
    }

    private void setUsername(ParseUser currentUser) {
        String username = currentUser.getString("username");
        //set the username on the profile page
        usernameTv.setText(username);
    }

    private void setProfilePicture(ParseUser currentUser) {

        //runs a query on the currently logged in user
        final ParseQuery<ParseUser> userQuery = new ParseQuery<ParseUser>(ParseUser.class);
        userQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

        //gets the query information on a background thread
        userQuery.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> singletonUserList, ParseException e) {
                ParseFile profilePicture = singletonUserList.get(0).getParseFile("profilePicture");
                if (profilePicture != null) {
                    // set profile picture
                    Glide.with(getContext()).load(profilePicture.getUrl()).into(profilePictureIv);
                }
            }
        });

    }

    private void setProfilePictureListener() {
        profilePictureIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUpdateProfilePic();
            }
        });
    }


    private void setUpSwipeContainer(View view) {
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Code to refresh the list here.
                // Call to swipeContainer.setRefreshing(false) is done
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
        postsQuery.getTop(20).withUser().sortDescending().getUserPosts();
        //TODO make better

        //gets the posts stored in the database on a background thread
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                updateData(posts, e);
            }
        });
    }

    private void updateData(List<Post> posts, ParseException e) {
        if (e != null) {
            Log.e(TAG, "Error with query");
            Toast.makeText(getContext(), "Error loading posts", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            swipeContainer.setRefreshing(false);
            return;
        }
        mPosts.addAll(posts);
        postAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }


    public void launchUpdateProfilePic(){
        Intent openProfilePicActivity = new Intent(getContext(), ProfilePictureActivity.class);
        startActivity(openProfilePicActivity);
    }
}
