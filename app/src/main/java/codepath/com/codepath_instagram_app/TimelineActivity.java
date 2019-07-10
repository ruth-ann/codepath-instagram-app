package codepath.com.codepath_instagram_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import codepath.com.codepath_instagram_app.model.Post;

import static java.security.AccessController.getContext;


public class TimelineActivity extends AppCompatActivity {

    RecyclerView postsRv;
    ArrayList<Post> posts;
    PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //find the RecyclerView
        postsRv = (RecyclerView) findViewById(R.id.rvPosts);

        //init the arraylist (data source)
        posts = new ArrayList<>();

        //construct the adapter from this datasource
        postAdapter = new PostAdapter(posts, getApplicationContext());
        //RecyclerView setup (layout manager, use adapter)

        postsRv.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        postsRv.setAdapter(postAdapter);
        loadTopPosts();


    }



    private void loadTopPosts(){
        final Post.Query postsQuery = new Post.Query();
        //uses methods defined in Post model to get the top 20 posts
        postsQuery.getTop().withUser();


        //gets the posts stored in the database on a background thread
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for (int i = 0; i < objects.size(); i++) {
                        Post post = new Post();
                        post.setUser(objects.get(i).getUser());
                        if (objects.get(i).getImage() != null){
                            post.setImage(objects.get(i).getImage());
                        }
                     //   post.setImage(objects.get(i).getImage());
                        post.setDescription(objects.get(i).getDescription());
                        postAdapter.notifyItemInserted(i);
                        posts.add(post);
                        //convert each entry in our objects list to a Post model
                        //add that Post model to our data source
                        // notify the adapter that we've added an item


                      /*  Log.d("TimelineActivity", "Post[" + i + "]="
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );*/

                    }
                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
