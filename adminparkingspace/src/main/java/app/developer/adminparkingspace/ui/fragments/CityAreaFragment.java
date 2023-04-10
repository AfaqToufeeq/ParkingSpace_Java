package app.developer.adminparkingspace.ui.fragments;

import static java.nio.file.Paths.get;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.developer.adminparkingspace.R;

import java.io.ByteArrayOutputStream;

import app.developer.adminparkingspace.adapters.CityAreaAdapter;
import app.developer.adminparkingspace.dataclass.CityArea;
import app.developer.adminparkingspace.firebasestorage.firebaseStorageManager;
import app.developer.adminparkingspace.interfaces.onAreaItemClick;
import app.developer.adminparkingspace.utils.PickerManager;


public class CityAreaFragment extends Fragment
        implements onAreaItemClick {
    PickerManager pm = PickerManager.getInstance();

    Bitmap bitmap=null;
    Uri uri =null, imgURI =null;
    String areaName,areaDescription,areaID;
    ImageView AreaImage;
    EditText AreaName,AreaDescription,cityID_ET;
    Button uploadButton,searchAreaButton,searchCity_btn;
    CardView CityCardView,parentCardView;
    int Camera_REQUEST_CODE = 200,Gallery_REQUEST_CODE = 100;


    CityAreaAdapter adapter;
    Dialog loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_city_area, container, false);
        CardView dashboardHeader = getActivity().findViewById(R.id.dashboardHeader);
        dashboardHeader.setVisibility(View.GONE);
        initViews(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonClicks();
        onbackPressed();

    }

    private void initViews(View v) {
        loader=pm.progressDialog(requireActivity());
        AreaImage = v.findViewById(R.id.AreaImage);
        AreaName = v.findViewById(R.id.AreaName);
        AreaDescription = v.findViewById(R.id.AreaDescription);
        uploadButton=v.findViewById(R.id.uploadButton);
//        cityID_ET=v.findViewById(R.id.cityID_ET);
        CityCardView=v.findViewById(R.id.CityCardView);
        parentCardView=v.findViewById(R.id.parentCardView);
//        searchCity_btn=v.findViewById(R.id.searchCity_btn);

    }

    private void buttonClicks() {

//        searchCity_btn.setOnClickListener(v-> citySearch());

        uploadButton.setOnClickListener(v -> uploadAreaData());

        /**
         * Registering FoodImage View for ContextMenu
         * @View [binding.FoodImage]
         */
        registerForContextMenu(AreaImage);
    }


    private void citySearch() {
//        cityName = cityID_ET.getText().toString();
//
//        if(cityName.isEmpty())
//        {
//            Toast.makeText(requireActivity(), "Please Complete the details", Toast.LENGTH_SHORT).show();
//            return;
//        }


//        getCityDatafromFirebase();

    }

//    private void getCityDatafromFirebase() {
//        if (!loader.isShowing()) loader.show();
//        FirebaseDatabase.getInstance().getReference("Cities")
//                .addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    for (DataSnapshot snapshot1:snapshot.getChildren()) {
//
//                        String getID = (String) snapshot1.getValue();
//
//                        if (getID != null && getID.equalsIgnoreCase(pm.cityName)) {
//                            checkCityName = true;
//                        }
//                    }
//
//                    if (loader.isShowing()) loader.dismiss();
//                    if (checkCityName) {
//                        parentCardView.setVisibility(View.VISIBLE);
//                        CityCardView.setVisibility(View.GONE);
//                    } else {
//                        Toast.makeText(requireActivity(),
//                                "Couldn't find city named"+ pm.cityName,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//                if (loader.isShowing()) loader.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                if (loader.isShowing()) loader.dismiss();
//                Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void uploadAreaData() {
        areaName = AreaName.getText().toString();
        areaDescription = AreaDescription.getText().toString();
        imgURI = uri;

        if(imgURI == null){
            Toast.makeText(requireActivity(),"Please select image first",Toast.LENGTH_SHORT).show();
            return;
        }
        if(areaName.isEmpty() || areaDescription.isEmpty())
        {
            Toast.makeText(requireActivity(), "Please complete the details", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * Using Async to upload Data in background thread
         * @fun [function] uploadDetails
         */

        if(!loader.isShowing()) loader.show();

        Thread firebaseThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
                uploadDetails();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }); firebaseThread.start();


        /**
         *  Storing Files/Images
         * in Firebase Storage
         * @param imageFileName storing the name of FoodItem
         */
        new Thread(() -> {
            pm.imageFileName= "CityArea/"+areaName+".png";
            try {
                new firebaseStorageManager().uploadImage(requireActivity(),imgURI);
            }catch (Exception e)
            {
                if (loader.isShowing()) loader.dismiss();
                Log.d("FirebaseStorage",e.getMessage());
            }
        }).start();
    }

    private void uploadDetails() {

        uploadAreaDetails(
                pm.mAuth.getUid(),
                areaName,
                areaDescription,
                pm.cityName,
                pm.downloadStorageUri
         );
    }

    private void uploadAreaDetails(
            String uid,
            String areaName,
            String areaDescription,
            String cityName,
            Uri downloadStorageUri)
    {
        CityArea cityArea = new CityArea(cityName, areaName, downloadStorageUri.toString(), areaDescription,uid);

        pm.mDatabase.child("CityArea").child(areaName).setValue(cityArea)
                .addOnSuccessListener(unused -> {

                    if(loader.isShowing()) loader.dismiss();
                    Toast.makeText(requireActivity(), "Data Uploaded Successfully",
                            Toast.LENGTH_SHORT).show();
                    AreaName.getText().clear();
                    AreaDescription.getText().clear();
                    AreaImage.setBackgroundResource(R.drawable.add_item_icon);
                })
                .addOnFailureListener(e -> {
                    if(loader.isShowing()) loader.dismiss();
                    Toast.makeText(requireActivity(), "Failed to upload data "+ e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Pick option");
        requireActivity().getMenuInflater().inflate(R.menu.selectimage_menu, menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        try {
            switch (item.getItemId()) {
                case R.id.cameraIMG:
                    cameraImage();
                    break;
                case R.id.galleryIMG:
                    selectGalleryImage();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

        }catch (Exception e)
        {
            Toast.makeText(requireActivity(),"Please, give Camera and storage permission",Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }


    /**
     * Handling Images from and Gallery
     */
    private void selectGalleryImage() {
       try {
           Intent intent = new Intent(Intent.ACTION_PICK);
           intent.setType("image/*");
           startActivityForResult(intent, Gallery_REQUEST_CODE);
       }catch (Exception e)
       {
           Toast.makeText(requireActivity(),"Please, give Camera and storage permission",Toast.LENGTH_SHORT).show();
       }
    }

    private void cameraImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Camera_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Camera_REQUEST_CODE && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap!=null)
            {
                uri = getImageUri(requireActivity(),bitmap);
            }
            AreaImage.setImageURI(uri);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == Gallery_REQUEST_CODE){
            uri = data != null ? data.getData() : null;
            AreaImage.setImageURI(uri); // handle chosen image
            AreaImage.setTag(bitmap);
        }
    }

    /** To Convert Bitmap into Uri
     * @param inContext as Context
     * @param inImage as Bitmap
     * */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "Title",
                null
        );
        return Uri.parse(path);
    }

    private void onbackPressed() {

    }

    private void setFragment(Fragment fragment){
        if (fragment == null) return;

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.dashBoard_FL,fragment)
                .addToBackStack(null)
                .commit();
    }
    private void setVisibility() {

    }

    @Override
    public void onItemClick(int position) {
        setFragment(new ParkingFragment());
    }

    @Override
    public void onLongClick(int position) {

    }
}