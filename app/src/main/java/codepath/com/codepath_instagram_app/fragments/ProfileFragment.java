package codepath.com.codepath_instagram_app.fragments;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import codepath.com.codepath_instagram_app.model.Post;

public class ProfileFragment extends PostsFragment {
    @Override
    protected void queryPosts() {
        final Post.Query postsQuery = new Post.Query();
        //uses methods defined in Post model to get the top 20 posts
        postsQuery.getTop(20).withUser().sortDescending().getUserPosts();
        //TODO make better

        //gets the posts stored in the database on a background thread
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                mPosts.addAll(posts);
                postAdapter.notifyDataSetChanged();


            }
        });
    }
}

