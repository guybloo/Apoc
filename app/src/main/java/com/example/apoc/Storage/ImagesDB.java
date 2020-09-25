package com.example.apoc.Storage;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
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
//        Uri file = Uri.fromFile(new File(url));
//        StorageReference storageRef = ;
        final String path = "images/" + uri.getLastPathSegment();
        UploadTask uploadTask = storageRef.child(path).putFile(uri);

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
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
//                user.setImageUrl(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                user.setImageUrl(path);
                Toast.makeText(context,"Image uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showImage(String path, ImageView imageView, Context context) {
        Glide.with(context).load("gs://apoc-4f783.appspot.com/images/image:81").into(imageView);
    }

//    public void Download(){
//        File localFile = File.createTempFile("images", "jpg");
//        riversRef.getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // Successfully downloaded data to local file
//                        // ...
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle failed download
//                // ...
//            }
//        });
//    }

}
