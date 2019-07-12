package codepath.com.codepath_instagram_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //Unwraps the post passed via the intent
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        //Binds the views to the elements in the xml file
        usernameEt = (TextView) findViewById(R.id.etUsernameProfile);
        descriptionEt = (TextView) findViewById(R.id.etDescription);
        createdAtEt = (TextView) findViewById(R.id.etCreatedAt);
        imageIv = (ImageView) findViewById(R.id.ivImage);

        usernameEt.setText(post.getUser().getUsername());
        descriptionEt.setText(post.getDescription());
        String rawDate = post.getCreatedAt().toString();
        String relativeDate = getRelativeTimeAgo(rawDate);
        createdAtEt.setText(getRelativeTimeAgo(rawDate)); //TODO format date
       // createdAtEt.setText(format(post.getCreatedAt()));
        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(this).load(image.getUrl()).into(imageIv);
        }




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




}
