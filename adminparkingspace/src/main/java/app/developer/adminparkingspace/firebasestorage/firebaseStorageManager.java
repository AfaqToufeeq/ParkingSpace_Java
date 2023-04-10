package app.developer.adminparkingspace.firebasestorage;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.developer.adminparkingspace.utils.PickerManager;

public class firebaseStorageManager {
    PickerManager pm = PickerManager.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    Dialog loader;

     public void uploadImage(Context mContext, Uri imageUri){


         UploadTask uploadTask = mStorageRef.child(pm.imageFileName).putFile(imageUri);
         uploadTask.addOnSuccessListener(taskSnapshot -> {
             Task<Uri> downloadURL = mStorageRef.child(pm.imageFileName).getDownloadUrl();
             downloadURL.addOnSuccessListener(
                     uri -> pm.downloadStorageUri=uri)
                     .addOnFailureListener(e -> {

                     });
         }).addOnFailureListener(e -> {

             Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_SHORT).show();
         });


     }
}
