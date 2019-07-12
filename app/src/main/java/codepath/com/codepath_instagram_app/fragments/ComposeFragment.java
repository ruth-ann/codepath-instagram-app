package codepath.com.codepath_instagram_app.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import codepath.com.codepath_instagram_app.LoginActivity;
import codepath.com.codepath_instagram_app.R;
import codepath.com.codepath_instagram_app.model.Post;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends Fragment {

    public final String TAG = "ComposeFragment";

    //variables that enable camera use
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;

    //variables linked to xml file
    private EditText descriptionInput;
    private Button captureImageBtn;
    private ImageView postImageIv;
    private Button createBtn;
    private Button logoutBtn;

    //The onCreateView method is called when Fragment should create its View object
    //hierarchy, either dynamically or via XML layout inflation
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //Third parameter is false since the bottom navigation view takes care of attachment
        return inflater.inflate(R.layout.fragment_compose, parent, false);

    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        findAllViews(view);

        //sets listeners for each of the relevant buttons
        setCaptureButtonListener(view);
        setCreateButtonListener(view);
        setLogoutButtonListener(view);

    }

    private void findAllViews(View view) {
        //Finds the views by id
        descriptionInput = view.findViewById(R.id.etDescription);
        postImageIv = view.findViewById(R.id.ivProfilePicture);
        captureImageBtn = view.findViewById(R.id.btTakePhoto);
        createBtn = view.findViewById(R.id.btUploadProfilePic);
    }


    private void setCaptureButtonListener(View view) {
        //Runs the function to launch the camera after the capture button is pressed
        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }


    private void setCreateButtonListener(View view){
        //Finds the create post button and sets a listener
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                photoFileErrorCheck();
                createPost(description, user, photoFile);
            }

        });
    }

    private void photoFileErrorCheck() {
        //Ensures that a photo file is present
        if (photoFile == null || postImageIv.getDrawable() == null){
            Log.e(TAG, "No photo to submit");
            Toast.makeText(getContext(), "There is no photo!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void setLogoutButtonListener(View view){
        //Finds the logout button and sets a listener
        logoutBtn = view.findViewById(R.id.btLogout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                openLogin();

            }

        });
    }


    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        //authority must match the authority in the provider tag of the xml file
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                postImageIv.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }


    private void createPost(String description, ParseUser user, File photoFile){

        //Creates the new post object to be added to the Parse server
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(new ParseFile(photoFile));
        newPost.setUser(user);

        //Saves the new post to the server and does error checking
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Log.d(TAG, "Create post success!");
                    Toast.makeText(getContext(), "Posted to Instagram!", Toast.LENGTH_SHORT).show();
                    descriptionInput.setText("");
                    postImageIv.setImageResource(0);
                }else{
                    Toast.makeText(getContext(), "Failed to create post.", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Create post failure");
                    e.printStackTrace();
                }
            }
        });
    }



    private void openLogin(){
        //takes the user to the login page
        final Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
