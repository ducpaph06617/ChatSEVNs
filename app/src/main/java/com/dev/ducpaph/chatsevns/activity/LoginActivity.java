package com.dev.ducpaph.chatsevns.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.ducpaph.chatsevns.R;
import com.dev.ducpaph.chatsevns.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static com.dev.ducpaph.chatsevns.firebase.FirebaseQuery.USERS;


public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private SharedPreferences sharedPreferences;
    private CheckBox cbRememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);
        cbRememberPass = findViewById(R.id.cbRememberPass);
        sharedPreferences = getSharedPreferences("SaveUser", MODE_PRIVATE);
        edtUsername.setText(sharedPreferences.getString("Usernamee", ""));
        edtPassword.setText(sharedPreferences.getString("Passwordd", ""));
        cbRememberPass.setChecked(sharedPreferences.getBoolean("check", false));

    }

    public void signIn(View view) {

        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();

        if (username.isEmpty()) {
            edtUsername.setError(getString(R.string.notify_sign_up_data_empty));
            return;
        } else if (password.isEmpty()) {
            edtPassword.setError(getString(R.string.notify_sign_up_data_empty));
            return;
        } else {


            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

            // truy vấn vào nhánh username mà người dùng nhập
            DatabaseReference users = firebaseDatabase.getReference(USERS).child(username);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {

                        showAlertDialog();

                        //Toast.makeText(LoginActivity.this,getString(R.string.notify_user_is_not_exists), Toast.LENGTH_SHORT).show();
                    } else {

                        // lấy dữ liệu từ dataSnapshot gán vào model User,
                        // lưu ý : biến ở User cần trùng khớp với tên các giá trị trên firebase
                        User user = dataSnapshot.getValue(User.class);

                        if (user.password.equals(password)) {

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            setCheckBox();

                        } else {

                            showAlertDialog2();

                            //Toast.makeText(LoginActivity.this, getString(R.string.notify_wrong_password), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void signUp(View view) {

        startActivity(new Intent(this, SignUpActivity.class));
    }

    void setCheckBox() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (cbRememberPass.isChecked()) {
            editor.putString("Usernamee", edtUsername.getText().toString().trim());
            editor.putString("Passwordd", edtPassword.getText().toString().trim());
            editor.putBoolean("check", true);
            editor.commit();

        } else {
            editor.putString("Usernamee", "");
            editor.putString("Passwordd", "");
            editor.putBoolean("check", false);
            editor.commit();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_dialog);
        builder.setMessage(R.string.notify_user_is_not_exists);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    public void showAlertDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_dialog);
        builder.setMessage(R.string.notify_wrong_password);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
