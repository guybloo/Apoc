package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apoc.Storage.ImagesDB;
import com.example.apoc.location.LocationInfo;
import com.example.apoc.location.LocationTracker;
import com.example.apoc.types.User;
import com.example.apoc.types.UserStatus;
//import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ProfileEdit extends AppCompatActivity {

    public static final String USER_DATA = "user";
    private static final int PICK_IMAGE_REQUEST = 1;
    private final int LOCATION_ACCURACY = 20;

    private ImagesDB imagesDB;

    private ImageView image;
    private Button pickImg;
    private TextView email;
    private EditText nickname;
    private EditText phone;
    private Button location;
    private SwitchCompat status;
    private SwitchCompat statusInGroup;

    private Button save;

    private LinearLayout statusInGroupLayout;
    private LocationInfo newLocation;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        imagesDB = new ImagesDB();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER_DATA);
        if (user == null) {
            finish();
        }
        initializeUI();

    }

    private void initializeUI() {
        image = findViewById(R.id.image);
        pickImg = findViewById(R.id.pickImg);

        pickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        email = findViewById(R.id.email);
        email.setText(user.getEmail());

        nickname = findViewById(R.id.nickname);
        nickname.setText(user.getNickName());

        phone = findViewById(R.id.phone);
        phone.setText(user.getPhone());

        location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add waiting indication
                LocationTracker location = new LocationTracker(getApplicationContext());
                location.startTracking();
                while(location.getInfo().getAccuracy() > LOCATION_ACCURACY){};
                newLocation = location.getInfo();
            }
        });

        status = findViewById(R.id.status);
        statusInGroup = findViewById(R.id.status_in_group);
        statusInGroupLayout = findViewById(R.id.status_layout);

//        statusInGroupLayout.setVisibility(View.INVISIBLE);
        if(!user.getStatus().equals(UserStatus.undefined.name())){
            status.setChecked(!user.getStatus().equals(UserStatus.loneWolf.name()));
            statusInGroup.setChecked(user.getStatus().equals(UserStatus.alpha.name()));

            status.setEnabled(false);
            statusInGroup.setEnabled(false);
        }

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                    statusInGroupLayout.setVisibility(View.VISIBLE);
                    statusInGroup.setEnabled(true);
                }
                else{
//                    statusInGroupLayout.setVisibility(View.INVISIBLE);
                    statusInGroup.setEnabled(false);
                }
            }
        });

        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile(){
        if(!status.isChecked()){
            user.setStatus(UserStatus.loneWolf.name());
        }
        else{
            if(statusInGroup.isChecked()){
                user.setStatus(UserStatus.alpha.name());
            }
            else {
                user.setStatus(UserStatus.beta.name());
            }
        }

        user.setNickName(nickname.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setLocationInfo(newLocation);

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri ourImageUri = data.getData();

            imagesDB.Upload(ourImageUri, user);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , ourImageUri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Picasso.with(this).load(ourImageUri).into(image);
        }
    }

    public String getPathFromURI(Uri ContentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver()
                .query(ContentUri, proj, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            res = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();
        }

        return res;
    }

}