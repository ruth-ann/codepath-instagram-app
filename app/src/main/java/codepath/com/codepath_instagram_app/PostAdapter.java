package codepath.com.codepath_instagram_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

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

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups by id in the xml file
            imageIv = (ImageView) itemView.findViewById(R.id.ivImage);
            profilePicIv = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            usernameTv = (TextView) itemView.findViewById(R.id.etHandle);
            descriptionTv = (TextView) itemView.findViewById(R.id.etDescription);
            descUsernameTv = (TextView) itemView.findViewById(R.id.etDescriptionHandle); //TODO fix et versus tv
            likesTv = (TextView) itemView.findViewById(R.id.etLike);
            commentsTv = (TextView) itemView.findViewById(R.id.etComment);
            likeBtn = (ImageButton) itemView.findViewById(R.id.btLike);

            commentBtn = (ImageButton) itemView.findViewById(R.id.btComment);


            //itemView's onClickListener
            itemView.setOnClickListener(this);


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
                // show the activity
                context.startActivity(intent);
            }

        }

        public void bind(final Post post) {

            descriptionTv.setText(post.getDescription());
            usernameTv.setText(post.getUser().getUsername());
            descUsernameTv.setText(post.getUser().getUsername());
            likesTv.setText(Integer.toString(post.getNumLikes()));
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl())/*.transform(new CircleTransform(context))*/.into(imageIv);
            }
            ParseFile profilePhoto = post.getUser().getParseFile("profilePicture");
            if (profilePhoto != null) {
                Glide.with(context).load(profilePhoto.getUrl()).into(profilePicIv);
            }


            if (post.isLiked()){
                likeBtn.setImageResource(R.drawable.ufi_heart_active);
                likeBtn.setColorFilter(Color.argb(255, 255, 0, 0));
            }else{
                likeBtn.setImageResource(R.drawable.ufi_heart);
                likeBtn.setColorFilter(Color.argb(255, 0, 0, 0));
            }
            likeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    if (!post.isLiked()){
                        //liked
                        post.like();
                        likeBtn.setImageResource(R.drawable.ufi_heart_active);
                        likeBtn.setColorFilter(Color.argb(255, 255, 0, 0));


                    }else{
                        post.unlike();
                        likeBtn.setImageResource(R.drawable.ufi_heart);
                        likeBtn.setColorFilter(Color.argb(255, 0, 0, 0));
                    }

                    post.saveInBackground();

                    likesTv.setText(Integer.toString(post.getNumLikes()));

                }
            });



            commentBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                }
            });

        }


    }


    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }


    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }


    //stack overflow code
  /*  public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;


            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }

    }*/
}
