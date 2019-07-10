package codepath.com.codepath_instagram_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

import codepath.com.codepath_instagram_app.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> mPosts;
    private static Context context;

    //pass in the Posts array in the constructor
    public PostAdapter (List<Post> posts, Context context){
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageIv;
       // public ImageView profilePicIv;
        public TextView usernameTv;
        public TextView descriptionTv;

        public ViewHolder (View itemView){
            super(itemView);

            //perform findViewById lookups in the xml file
            imageIv = (ImageView)  itemView.findViewById(R.id.ivImage);
            //profilePicIv = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            usernameTv = (TextView) itemView.findViewById(R.id.tvHandle);
            descriptionTv = (TextView) itemView.findViewById(R.id.tvDescription);

        }

        public void bind(Post post) {

            descriptionTv.setText(post.getDescription());
            usernameTv.setText(post.getUser().getUsername()); //TODO figure out how to get a user
            ParseFile image = post.getImage();
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(imageIv);
            }
        }
    }
}
