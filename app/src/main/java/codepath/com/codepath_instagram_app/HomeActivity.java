package codepath.com.codepath_instagram_app;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import codepath.com.codepath_instagram_app.fragments.ComposeFragment;
import codepath.com.codepath_instagram_app.fragments.PostsFragment;
import codepath.com.codepath_instagram_app.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        setupFragments();
    }

    private void setupFragments() {
        //The fragment manager replaces the content of the frame layout with the fragment
        //selected by the user
        final FragmentManager fragmentManager = getSupportFragmentManager();

        //sets a listener for each of the icons in the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Declaration of each fragment
                Fragment fragment;
                //Brings the user to a given fragment based on the item they have selected
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new PostsFragment();
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }

                //Transactions are the way that fragments are switched.
                // The fragment manager replaces the contents of the frame layout with the fragment
                //then commits the transaction
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;

            }

        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private void setupToolbar() {
        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ig_timeline, menu);
        return true;
    }
}



