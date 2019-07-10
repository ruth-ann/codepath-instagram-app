package codepath.com.codepath_instagram_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText handleInput;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //finds the relevant fields inside of the activity layout and initializes private variables
        usernameInput = findViewById(R.id.etUserName);
        passwordInput = findViewById(R.id.etPassword);
        emailInput = findViewById(R.id.etEmail);
        handleInput = findViewById(R.id.etHandle);
        signupBtn = findViewById(R.id.btSignup);

        //sets a listener for the signup button and captures the content entered by the user
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String handle = handleInput.getText().toString();
                signup(username, email, password, handle);


            }
        });
    }




    private void signup(String username, String email, String password, String handle){
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.put("handle", handle);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    openHome();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong

                    Log.e("SignUpActivity", "Signup failure.");
                    e.printStackTrace();
                }
            }
        });
    }


    private void openHome(){
        //opens the homeactivity for the user after signup is completed
        final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
