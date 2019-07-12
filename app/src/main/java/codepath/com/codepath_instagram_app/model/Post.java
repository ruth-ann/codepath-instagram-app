package codepath.com.codepath_instagram_app.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;


//ensures that any logic around accessing and mutating the Parse user
//object is handled here

//@Parcel
@ParseClassName("Post")
public class Post extends ParseObject {
    private static String KEY_DESCRIPTION = "description";
    private static String KEY_IMAGE = "image";
    private static String KEY_USER = "user";
    private static String KEY_CREATED_AT = "createdAt";
    private static String KEY_LIKED_BY = "likedBy";



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

    //time stamp
    public Date getCreatedDate() {
        return getCreatedAt();
    }



    public JSONArray getLikedBy() {
        JSONArray array = getJSONArray(KEY_LIKED_BY);
        if (array == null){
            return new JSONArray();
        }else{
            return array;
        }

    }


    public boolean isLiked() {

        JSONArray array = getLikedBy();
        for (int i = 0; i < array.length(); i++){
            try{
                if (array.getJSONObject(i).getString("objectId").equals(ParseUser.getCurrentUser().getObjectId())){
                    return true;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;

    }

    public void like() {
        ParseUser user = ParseUser.getCurrentUser();
        add(KEY_LIKED_BY, user);

    }

    public void unlike() {

        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(user);
        removeAll(KEY_LIKED_BY, users); //removes anything inside our array
    }

    public int getNumLikes() {
        return getLikedBy().length();

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
