package codepath.com.codepath_instagram_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checks whether there is an existing user session
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.getSessionToken(); //TODO fix
            openHome();
        }else {

            //finds the relevant fields inside of the activity layout and initializes private variables
            usernameInput = findViewById(R.id.etUserName);
            passwordInput = findViewById(R.id.etPassword);
            loginBtn = findViewById(R.id.btLogin);
            signupBtn = findViewById(R.id.btSignup);

            //sets a listener for the login button and captures the content entered by the user
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();


                    login(username, password);

                }

            });

            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSignUp();
                }

            });

        }
    }

    private void login(String username, String password){
        //authenticates the user in a background thread
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    //Login was successful. Takes the user to their home page
                    Log.d("LoginActivity", "Login successful!");
                    openHome();
                } else{
                    // Login didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e("LoginActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }

    private void openHome(){
        //opens the homeactivity for the user  after credentials are checked
        final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSignUp(){
        //takes the user to the signup page
        final Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
