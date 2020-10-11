package com.example.apoc.Storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.ProfileEdit;
import com.example.apoc.location.LocationTracker;
import com.example.apoc.types.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * images storage db manager
 */
public class ImagesDB {
    private StorageReference storageRef;
    private OnImageUploadListener listener;

    /**
     * cunstructor
     */
    public ImagesDB() {
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    /**
     * event
     */
    public interface OnImageUploadListener {
        void onImageUpload();
    }

    /**
     * sets listener
     * @param eventListener
     */
    public void setImageUploadListener(OnImageUploadListener eventListener) {
        listener = eventListener;
    }

    /**
     * notify upload for events
     */
    protected void notifyUploaded() {
        if (listener != null) {
            listener.onImageUpload();
        }
    }

    /**
     * uploads a new image to db
     * @param uri the image details to upload
     * @param user the specific user
     * @param context activity
     */
    public void Upload(final Uri uri, final User user, final Context context) {
        if(uri == null){
            Toast.makeText(context,"Image doesnt exist", Toast.LENGTH_SHORT).show();
            return;
        }
        final String path = "images/" + uri.getLastPathSegment();
        StorageReference imageRef = storageRef.child(path);
        UploadTask uploadTask = imageRef.putFile(uri);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                user.setImageUrl(downloadUrl.toString());
                (new UsersDB()).updateField(user.getId(),UsersDB.IMAGE,downloadUrl.toString());
                notifyUploaded();
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
            }
        });
    }

    /**
     * show original image
     * @param path image path
     * @param imageView the layout
     * @param context activity
     */
    public static void showImage(String path, ImageView imageView, Context context) {
        Glide.with(context).load(path).into(imageView);
    }

    /**
     * show image in a circle shape from url
     * @param path image path
     * @param imageView the layout
     * @param context activity
     */
    public static void showCircleImage(String path, ImageView imageView, Context context) {
        Glide.with(context).load(path).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    /**
     * show image in a circle from bitmap
     * @param image the botmap
     * @param imageView the layout
     * @param context activity
     */
    public static void showCircleBitmapImage(Bitmap image, ImageView imageView, Context context) {
        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
