package codepath.com.codepath_instagram_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import codepath.com.codepath_instagram_app.model.Post;

import static okhttp3.internal.http.HttpDate.format;

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
        usernameEt = (TextView) findViewById(R.id.etUsername);
        descriptionEt = (TextView) findViewById(R.id.etDescription);
        createdAtEt = (TextView) findViewById(R.id.etCreatedAt);
        imageIv = (ImageView) findViewById(R.id.ivImage);

        usernameEt.setText(post.getUser().getUsername());
        descriptionEt.setText(post.getDescription());
        String rawDate = format(post.getCreatedAt());
        createdAtEt.setText(rawDate); //TODO format date
       // createdAtEt.setText(format(post.getCreatedAt()));
        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(this).load(image.getUrl()).into(imageIv);
        }


    }
}
