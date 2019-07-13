package codepath.com.codepath_instagram_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import codepath.com.codepath_instagram_app.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private static List<Post> mPosts;
    private Context context;

    //pass in the Posts array in the constructor
    public PostAdapter(List<Post> posts, Context context) {
        this.mPosts = posts;
        this.context = context;
    }

    //for each row, inflate the layout and cache references into the ViewHolder
    //called when new rows are created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }


    //bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Post post = mPosts.get(position);
        //populate the views according to this data
        holder.bind(post);
    }

    //gets the number of items
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageIv;
        public ImageView profilePicIv;
        public TextView usernameTv;
        public TextView descriptionTv;
        public TextView descUsernameTv;
        public ImageButton likeBtn;
        public ImageButton commentBtn;
        public TextView likesTv;
        public TextView commentsTv;
        public TextView createdAtTv;

        public ViewHolder(View itemView) {
            super(itemView);
            findAllViews();

            //itemView's onClickListener
            itemView.setOnClickListener(this);
        }

        private void findAllViews() {
            //perform findViewById lookups by id in the xml file
            imageIv = (ImageView) itemView.findViewById(R.id.ivImage);
            profilePicIv = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            usernameTv = (TextView) itemView.findViewById(R.id.etHandle);
            createdAtTv = (TextView) itemView.findViewById(R.id.etCreatedAt);
            descriptionTv = (TextView) itemView.findViewById(R.id.etDescription);
            descUsernameTv = (TextView) itemView.findViewById(R.id.etDescriptionHandle); //TODO fix et versus tv
            likesTv = (TextView) itemView.findViewById(R.id.etLike);
            commentsTv = (TextView) itemView.findViewById(R.id.etComment);
            likeBtn = (ImageButton) itemView.findViewById(R.id.btLike);
            commentBtn = (ImageButton) itemView.findViewById(R.id.btComment);
        }

        @Override
        public void onClick(View v) {
            //gets item position
            int position = getAdapterPosition();
            //make sure the position is valid (that it exists in the view)
            if (position != RecyclerView.NO_POSITION) {
                //get the post at the position (will not work if the class is static)
                Post post = mPosts.get(position);
                //create intent for the new activity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                //serialize the movie using parceler, uses the short name of the movie as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                intent.putExtra("username", post.getUser().getUsername());
                // show the activity
                context.startActivity(intent);
            }

        }

        public void bind(final Post post) {

            setAllViews(post);
            manageLikes(post);
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


        private void setAllViews(Post post) {

            descriptionTv.setText(post.getDescription());
            usernameTv.setText(post.getUser().getUsername());
            descUsernameTv.setText(post.getUser().getUsername());
            likesTv.setText(Integer.toString(post.getNumLikes()));

            String rawDate = post.getCreatedAt().toString();
            createdAtTv.setText(getRelativeTimeAgo(rawDate));

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl())/*.transform(new CircleTransform(context))*/.into(imageIv);
            }
            ParseFile profilePhoto = post.getUser().getParseFile("profilePicture");
            if (profilePhoto != null) {
                Glide.with(context).load(profilePhoto.getUrl()).into(profilePicIv);
            }

        }
        private void setUnliked() {
            likeBtn.setImageResource(R.drawable.ufi_heart);
            likeBtn.setColorFilter(Color.argb(255, 0, 0, 0));
        }

        private void setLiked() {
            likeBtn.setImageResource(R.drawable.ufi_heart_active);
            likeBtn.setColorFilter(Color.argb(255, 255, 0, 0));
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
