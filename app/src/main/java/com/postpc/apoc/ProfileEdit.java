package com.postpc.apoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.postpc.apoc.DB.DBItem;
import com.postpc.apoc.DB.DBWrapper;
import com.postpc.apoc.DB.GroupsDB;
import com.postpc.apoc.DB.ItemsDB;
import com.postpc.apoc.DB.LogDB;
import com.postpc.apoc.DB.UsersDB;
import com.postpc.apoc.Storage.ImagesDB;
import com.postpc.apoc.location.LocationInfo;
import com.postpc.apoc.location.LocationTracker;
import com.postpc.apoc.Enums.Fears;
import com.postpc.apoc.Displays.GridDisplay;
import com.postpc.apoc.types.Group;
import com.postpc.apoc.types.Item;
import com.postpc.apoc.types.ItemCount;
import com.postpc.apoc.types.Message;
import com.postpc.apoc.types.User;
import com.postpc.apoc.Enums.UserStatus;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ProfileEdit class
 */
public class ProfileEdit extends AppCompatActivity {

    public static final String USER_DATA = "user";
    public final String STATUS_UNCHANGED = "You must choose your status to proceed!";
    public final String LOCATION_UNDEFINED = "You must set your location to proceed!";
    public final String PROFILE_UPDATED_LOG = "%s info has been updated";
    public final int IMAGE_SIZE = 500;
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
    private GridLayout fearsLayouts;
    private GridLayout skillsLayouts;
    private Button save;
    private ConstraintLayout statusInGroupLayout;
    private LocationInfo newLocation;
    private Uri imageUri;
    private User user;
    private Context cnt;
    private LocationTracker locationTracker;
    private boolean imageUploaded, itemsUpdated;

    /**
     * starts the ProfileEdit activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        cnt = this;
        imagesDB = new ImagesDB();
        imagesDB.setImageUploadListener(new ImagesDB.OnImageUploadListener() {
            @Override
            public void onImageUpload() {
                imageUploaded = true;
                if(itemsUpdated){
                    Toast.makeText(cnt,"Profile updated",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        imageUploaded = false;
        itemsUpdated = false;

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER_DATA);
        if (user == null) {
            finish();
        }
        initializeUI();
    }

    /**
     * create the ProfileEdit page - ui
     */
    private void initializeUI() {
        fearsLayouts = findViewById(R.id.profile_edit_fears);
        skillsLayouts = findViewById(R.id.profile_edit_skills);
        GridDisplay gridDisplay = new GridDisplay(getApplicationContext(),user, fearsLayouts, skillsLayouts, true, 6);

        image = findViewById(R.id.image);
        image.getLayoutParams().height = IMAGE_SIZE;
        image.getLayoutParams().width = IMAGE_SIZE;
        if(!user.getImageUrl().equals("")){
            ImagesDB.showCircleImage(user.getImageUrl(),image,this);
        }

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
                locationTracker = new LocationTracker(cnt);

                locationTracker.setLocationUpdateListener(new LocationTracker.OnLocationUpdateListener() {
                    @Override
                    public void onLocationUpdate() {
                        if(locationTracker.getInfo().getAccuracy() <= LocationTracker.ACCURACY){
                            newLocation = locationTracker.getInfo();
                            locationTracker.stopTracking();
                            Toast.makeText(cnt,"Location Updated",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                locationTracker.startTracking();
            }
        });

        status = findViewById(R.id.status);
        statusInGroup = findViewById(R.id.status_in_group);
        statusInGroupLayout = findViewById(R.id.status_layout);

        if(!user.isUndefined()){
            status.setChecked(!user.isLoneWolf());
            statusInGroup.setChecked(user.isAlpha());

            status.setEnabled(false);
            statusInGroup.setEnabled(false);
        }

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    statusInGroup.setEnabled(true);
                }
                else{
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

    /**
     * saves all the details of the user and updates the dbs
     */
    private void saveProfile(){
        UsersDB udb = new UsersDB();
        final GroupsDB groupsDB = new GroupsDB();
        if (!status.isChecked())
        {
            user.setStatus(UserStatus.loneWolf.name());
        }
        else {
            if (statusInGroup.isChecked())
            {
                user.setStatus(UserStatus.alpha.name());
                user.setIsGrouped(true);

                groupsDB.getGroupByUser(user.getId());
                groupsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                    @Override
                    public void onGetAll() {
                    }

                    @Override
                    public void onGetSpecific() {
                        Group group = (Group)groupsDB.getItemById(user.getId());
                        if(group == null){
                            group = new Group(user.getNickName(), user.getId(), user.getFears());
                            groupsDB.addItem(group);
                            group.addMember(user);
                        }
                        else {
                            group.setFears(user.getFears());
                            groupsDB.updateItem(group);
                        }
                    }
                });
            } else {
                user.setStatus(UserStatus.beta.name());
            }
        }

        user.setNickName(nickname.getText().toString());
        user.setPhone(phone.getText().toString());
        if(newLocation!=null) {
            user.setLocationInfo(newLocation);
        }

        if(user.getLocationInfo() == null){
            Toast.makeText(cnt,LOCATION_UNDEFINED,Toast.LENGTH_LONG).show();
            return;
        }
        if(user.isUndefined())
        {
            Toast.makeText(cnt,STATUS_UNCHANGED,Toast.LENGTH_LONG).show();
            return;
        }

        if(imageUri!= null) {
            imagesDB.Upload(imageUri, user, this);
        }
        else{
            imageUploaded = true;
        }
        updateItems(udb); // in the end it updates the users db
    }

    /**
     * updates all the relevant items for the user fears in the items list
     * @param udb - users db
     */
    private void updateItems(final UsersDB udb){
        final ItemsDB itemsDB = new ItemsDB();
        itemsDB.getItemsByFears(user.getFears());
        itemsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {
            }

            @Override
            public void onGetSpecific() {
                ArrayList<ItemCount> newItems = new ArrayList<>();

                for(DBItem item : itemsDB.getItems().values()){
                    boolean added = false;
                    for(ItemCount itemCount : user.getItems()){
                        if(item.getId().equals(itemCount.getName())){
                            itemCount.setMax(getMaxCountByFears((Item)item, user.getFears()));
                            newItems.add(itemCount);
                            added = true;
                            break;
                        }
                    }
                    if(!added) {
                        newItems.add(new ItemCount(item.getId()));
                    }
                }
                user.setItems(newItems);
                udb.updateItem(user);
                (new LogDB()).addItem(new Message(String.format(PROFILE_UPDATED_LOG,user.getId()),user.getId()));
                itemsUpdated = true;
                if(imageUploaded){
                    Toast.makeText(cnt,"Profile updated",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    /**
     * get the max number of needed items form the user fears
     * @param item - the item that is in need for more then one fear
     * @param fears - the fears of the user
     * @return
     */
    private double getMaxCountByFears(Item item, ArrayList<Fears> fears){
        double max = 0;
        for(Fears fear : fears){
            if(item.getAmount(fear) > max){
                max = item.getAmount(fear);
            }
        }
        return max;
    }

    /**
     * opens the gallery for choosing profile image for the user
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * update the user's imageUri after it chooses an image
     * @param requestCode - PICK_IMAGE_REQUEST
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri ourImageUri = data.getData();
            if(ourImageUri == null){
                return;
            }
            imageUri = ourImageUri;

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , ourImageUri);
                ImagesDB.showCircleBitmapImage(bitmap,image,this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        locationTracker.onPermissionResult(requestCode, permission, grantResults);
    }
}