package codepath.com.codepath_instagram_app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import codepath.com.codepath_instagram_app.model.Post;


public class ParseApp extends Application {
    //instantiates the Parse client
    @Override
    public void onCreate(){
        super.onCreate();

        //tells Parse that this is a custom model we created to encapsulate our data
        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
            //config variables that allow us to access our Parse server stored in Heroku
            .applicationId("fbu-ruthann-parsetagram")
            .clientKey("ruthanna")
            .server("http://fbu-ruthann-parsetagram.herokuapp.com/parse")
            .build();

        Parse.initialize(configuration);
    }
}

