package codepath.com.codepath_instagram_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import codepath.com.codepath_instagram_app.model.Post;

public class PostDetailsActivity extends AppCompatActivity {

    //the post to display
    Post post;

    //the view objects
    TextView usernameEt;
    TextView descriptionEt;
    TextView createdAtEt;
    ImageView imageIv;
    ImageView profilePictureIv;
    ImageButton likeBtn;
    TextView likesTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        findAllViews();
        setAllViews();

    }

    private void setAllViews() {
        //Unwraps the post passed via the intent
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        String user = getIntent().getStringExtra("username");
        usernameEt.setText(user);
        descriptionEt.setText(post.getDescription());
        String rawDate = post.getCreatedAt().toString();
        createdAtEt.setText(getRelativeTimeAgo(rawDate));
        likesTv.setText(Integer.toString(post.getNumLikes()));
        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(this).load(image.getUrl()).into(imageIv);
        }
        ParseUser postUser = post.getUser();
        setProfilePicture(postUser);
        manageLikes(post);



    }

    private void setProfilePicture(ParseUser currentUser) {
        //runs a query on the currently logged in user
        final ParseQuery<ParseUser> userQuery = new ParseQuery<ParseUser>(ParseUser.class);
        userQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

        //gets the query information on a background thread
        userQuery.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> singletonUserList, com.parse.ParseException e) {
                ParseFile profilePicture = singletonUserList.get(0).getParseFile("profilePicture");
                if (profilePicture != null) {
                    // set profile picture
                    Glide.with(PostDetailsActivity.this).load(profilePicture.getUrl()).into(profilePictureIv);
                }
            }
        });

    }

    private void findAllViews() {
        //Binds the views to the elements in the xml file
        usernameEt = (TextView) findViewById(R.id.etUsernameProfile);
        descriptionEt = (TextView) findViewById(R.id.etDescription);
        createdAtEt = (TextView) findViewById(R.id.etCreatedAt);
        imageIv = (ImageView) findViewById(R.id.ivImage);
        profilePictureIv = (ImageView) findViewById(R.id.ivProfilePic);
        likeBtn = (ImageButton) findViewById(R.id.btLike);
        likesTv = (TextView) findViewById(R.id.etLike);
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void manageLikes(final Post post) {
        //initially sets the post to like view if it has been liked
        if (post.isLiked()){
            setLiked();
        }else{
            //else initially set to unlike view
            setUnliked();
        }
        setLikeListener(post);
    }


    private void setUnliked() {
        likeBtn.setImageResource(R.drawable.ufi_heart);
        likeBtn.setColorFilter(Color.argb(255, 0, 0, 0));
    }

    private void setLiked() {
        likeBtn.setImageResource(R.drawable.ufi_heart_active);
        likeBtn.setColorFilter(Color.argb(255, 255, 0, 0));
    }

    private void setLikeListener(final Post post) {
        likeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (!post.isLiked()){
                    //liked
                    post.like();
                    setLiked();
                }else{
                    //already liked
                    post.unlike();
                    setUnliked();
                }
                post.saveInBackground();
                likesTv.setText(Integer.toString(post.getNumLikes()));
            }
        });
    }

}
