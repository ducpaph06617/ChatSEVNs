package com.dev.ducpaph.chatsevns.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev.ducpaph.chatsevns.R;
import com.dev.ducpaph.chatsevns.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static com.dev.ducpaph.chatsevns.firebase.FirebaseQuery.USERS;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    public void signUp(View view) {
        // get all value in edit text
        final String firstName = edtFirstName.getText().toString().trim();
        final String lastName = edtLastName.getText().toString().trim();
        final String username = edtUsername.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (firstName.isEmpty()) {
            edtFirstName.setError(getString(R.string.notify_sign_up_data_empty));
            return;
        } else if (lastName.isEmpty()) {
            edtLastName.setError(getString(R.string.notify_sign_up_data_empty));
            return;
        } else if (username.isEmpty()) {
            edtUsername.setError(getString(R.string.notify_sign_up_data_empty));
            return;
        }
        else if (password.isEmpty()) {
            edtPassword.setError(getString(R.string.notify_sign_up_data_empty));
            return;
        } else {
            // dont forget validating value

            // pls do it yourself

            // Firstly, we check username is exits or not

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference users = database.getReference(USERS).child(username);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // chua co user voi username duoc nhap
                    if (dataSnapshot.getValue() == null) {

                        // hoc vien tu khoi tao model User
                        User user = new User();
                        user.firstName = firstName;
                        user.lastName = lastName;
                        user.password = password;
                        user.username = username;


                        // them user vao nhanh Users
                        users.setValue(user, new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                Log.e("ABC", "CBC " + databaseReference.getRef().getKey());

                                Log.e("ABC", "CBC " + databaseError.getMessage());

                            }
                        });


                        // username da ton tai, thong bao chon username khac
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                getString(R.string.notify_user_is_exits), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ABCCC",databaseError.getMessage());
                }
            });


        }
    }
}
