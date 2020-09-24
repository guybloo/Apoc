package com.example.apoc.Storage;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.apoc.types.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImagesDB {
    private StorageReference storageRef;

    public ImagesDB() {
        storageRef = FirebaseStorage.getInstance().getReference();

    }

    public void Upload(Uri uri, final User user) {
//        Uri file = Uri.fromFile(new File(url));
//        StorageReference storageRef = ;

        UploadTask uploadTask = storageRef.child("images/" + uri.getLastPathSegment()).putFile(uri);

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
                user.setImageUrl(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
            }
        });
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
