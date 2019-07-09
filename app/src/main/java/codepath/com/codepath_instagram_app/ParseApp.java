package codepath.com.codepath_instagram_app;

import android.app.Application;

import com.parse.Parse;



public class ParseApp extends Application {
    //instantiates the Parse client
    @Override
    public void onCreate(){
        super.onCreate();
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
            //config variables that allow us to access our Parse server stored in Heroku
            .applicationId("fbu-ruthann-parsetagram")
            .clientKey("ruthanna")
            .server("http://fbu-ruthann-parsetagram.herokuapp.com/parse")
            .build();

        Parse.initialize(configuration);
    }
}
