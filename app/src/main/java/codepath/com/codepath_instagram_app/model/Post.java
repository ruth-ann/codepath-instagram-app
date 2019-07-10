package codepath.com.codepath_instagram_app.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


//ensures that any logic around accessing and mutating the Parse user
//object is handled here
@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_CREATED_AT = "createdAt";

    //accessors and mutators for our private strings
    //description
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    //image
    public ParseFile getImage(){ //ParseFile is a class in the SDK that allows us to easily access images
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }
    //user
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }


    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    //query of a post class
    public static class Query extends ParseQuery<Post> {

        //constructor
        public Query(){
            super(Post.class);
        }

        //gets the top 20 posts
        public Query getTop(int limit){
            setLimit(limit);
            return this; //builder pattern allows users to chain methods
        }

        //the post unpacks the associated user
        public Query withUser(){
            include("user");
            return this;
        }

        public Query sortDescending(){
            addDescendingOrder(KEY_CREATED_AT);
            return this;
        }

        public Query sortAscending(){
            addAscendingOrder(KEY_CREATED_AT);
            return this;
        }

        public Query getUserPosts(){
            whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            return this;
        }

    }


}
