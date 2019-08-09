package com.dev.ducpaph.chatsevns.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseQuery<T> {
    public static String TAG = "FB";


    public static final String MESSAGES = "Messages";
    public static final String GROUPS = "Groups";
    public static final String USERS = "Users";

    public static void checkExitsUsername(String username, ValueEventListener valueEventListener) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(USERS).child(username);

        DatabaseReference users = database.getReference(USERS).child(username);

        users.addListenerForSingleValueEvent(valueEventListener);
    }


    public static void writeTo(String ref) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages");

        myRef.setValue("Hello, World!");
    }

    public static void readFrom() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}
