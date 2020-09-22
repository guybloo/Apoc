package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apoc.types.User;

public class ProfileEdit extends AppCompatActivity {

    public static final String USER_DATA = "user";

    private ImageView image;
    private TextView email;
    private EditText nickname;
    private EditText phone;
    private Button location;
    private SwitchCompat status;
    private SwitchCompat statusInGroup;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER_DATA);
        if (user == null) {
            finish();
        }
        initializeUI();
    }

    private void initializeUI() {
        image = findViewById(R.id.image);

        email = findViewById(R.id.email);
        email.setText(user.getEmail());

        nickname = findViewById(R.id.nickname);
        nickname.setText(user.getNickName());

        phone = findViewById(R.id.phone);
        phone.setText(user.getPhone());

        location = findViewById(R.id.location);
        status = findViewById(R.id.status);
        statusInGroup = findViewById(R.id.status_in_group);


    }

}