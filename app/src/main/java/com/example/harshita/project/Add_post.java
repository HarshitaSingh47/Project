package com.example.harshita.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.content;

public class Add_post extends AppCompatActivity {

    public EditText etext1;
    public TextView postslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        // EditText myEditText = (EditText)findViewById(R.id.etext);
        // Firebase.setAndroidContext(this);

        Firebase.setAndroidContext(this);
        final EditText etext1 = (EditText) findViewById(R.id.etext1);
        Button b2 = (Button) findViewById(R.id.b2);
        postslist = (TextView) findViewById(R.id.list);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitpost();
            }
        });
    }
    //Creating firebase object
//                Firebase ref = new Firebase(Config.FIREBASE_URL);
//
//                //Getting values to store
//                String name = etext1.getText().toString().trim();
//
//
//                //Creating Post object
//                Post p = new Post();
//                Log.e("STUFF","HAPPENED");
//
//                //Adding values
//                p.setPost(name);
//
//
//                //Storing values to firebase
//                ref.child("Post").setValue(p);
//                Log.e("Posted","posted");
//
//                //Value event listener for realtime data update
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            //Getting the data from snapshot
//                            Log.e("getting data","processng...");
//                            Post p = postSnapshot.getValue(Post.class);
//                            Log.e("got data","data");
//                            //Adding it to a string
//                            String string = "Name: " + p.getPost();
//                            Log.e("string",string);
//
//                            //Displaying it on textview
//                            postslist.setText(string);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        System.out.println("The read failed: " + firebaseError.getMessage());
//                    }
//                });
//
//            }
//        });
//    }


    private void submitpost() {
        final String mTitle=title.getText().toString();
        final String mContent=content.getText().toString();


        if (TextUtils.isEmpty(mTitle)) {
            content.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(mContent)) {
            content.setError(REQUIRED);
            return;
        }

        FirebaseUser user=auth.getCurrentUser();
        final String userId=user.getUid();


        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //get user value
                        User user=dataSnapshot.getValue(User.class);

                        if(user==null){
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewPost.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, mTitle, mContent);
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Complaint complaint = new Complaint(userId, username, title, body);
        Map<String, Object> complaintValues = complaint.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, complaintValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, complaintValues);



        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
    }
}


