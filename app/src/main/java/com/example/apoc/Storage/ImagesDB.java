package com.example.apoc.Storage;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.R;
import com.example.apoc.types.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImagesDB {
    private StorageReference storageRef;

    public ImagesDB() {
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void Upload(final Uri uri, final User user, final Context context) {
        if(uri == null){
            Toast.makeText(context,"Image doesnt exist", Toast.LENGTH_SHORT).show();
            return;
        }
        final String path = "images/" + uri.getLastPathSegment();
//        UploadTask uploadTask = storageRef.child(path).putFile(uri);
        StorageReference imageRef = storageRef.child(path);
        UploadTask uploadTask = imageRef.putFile(uri);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                user.setImageUrl(downloadUrl.toString());
                (new UsersDB()).updateItem(user);
            }
        });

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                String a = "";
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context,"Image uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showImage(String path, ImageView imageView, Context context) {
        Glide.with(context).load(path).into(imageView);
    }
    public static void showCircleImage(String path, ImageView imageView, Context context) {
        Glide.with(context).load(path).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
