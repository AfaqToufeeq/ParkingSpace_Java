package app.developer.adminparkingspace.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.adminparkingspace.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import app.developer.adminparkingspace.dataclass.CityArea;
import app.developer.adminparkingspace.dataclass.User;
import app.developer.adminparkingspace.utils.PickerManager;


public class HomeFragment extends Fragment {
    Spinner citySpinner;
    PickerManager pm = PickerManager.getInstance();
    CardView carParking_CV,bikeParking_CV;
    Button addAreaButton,btn_aboutUs;
    Dialog loader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        CardView dashboardHeader = getActivity().findViewById(R.id.dashboardHeader);
        dashboardHeader.setVisibility(View.VISIBLE);
        initViews(v);
        addCities();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(() -> {
            try {
                //Fetch Area Data From Firebase
                getAreaDatafromFirebase();
            }catch (Exception e)
            {
                Log.d("HomeFragment",e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try {
                //Fetch User Data From Firebase
                getUserDataFromFirebase();
            }catch (Exception e)
            {
                Log.d("HomeFragment",e.getMessage());
            }
        }).start();


        //Buttons
        clickButtons();
    }

    private void initViews(View v) {
        loader=pm.progressDialog(requireActivity());
        citySpinner = v.findViewById(R.id.citySpinner);
        bikeParking_CV = v.findViewById(R.id.bikeParking_CV);
        carParking_CV=v.findViewById(R.id.carParking_CV);
        addAreaButton=v.findViewById(R.id.addAreaButton);
        btn_aboutUs=v.findViewById(R.id.btn_aboutUs);
    }



    /**
     * Fetching user data from cloud
     **/
    private void getUserDataFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Admin")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            pm.userList.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {

                                if (snapshot1.getKey().equals(pm.mAuth.getUid()))
                                {
                                    User user = snapshot1.getValue(User.class);
                                    pm.userList.add(user);
                                }

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void addCities() {
        pm.citiesList.clear();
        pm.citiesList.add("Manama");
        pm.citiesList.add("Riffa");
        pm.citiesList.add("Sitra");
        pm.citiesList.add("Jidhafs");

        //Calling function to upload cities on cloud
        uploadCityDetails(pm.citiesList);
    }



    private void aboutUS() {
        PopupWindow popupWindow = new PopupWindow(requireActivity());
        View popupView = LayoutInflater.from(requireActivity()).inflate(R.layout.aboutus_popup_screen, null);
        popupWindow.setContentView(popupView);

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(btn_aboutUs, Gravity.CENTER, 0, 0);
    }



    private void setFragment(Fragment fragment){
        if (fragment == null) return;

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft =  fragmentManager.beginTransaction();
        ft.replace(R.id.dashBoard_FL,fragment).addToBackStack(null).commit();

    }

    private void clickButtons() {
        //City Spinner
        citySpinnerMethod();
        btn_aboutUs.setOnClickListener(v-> aboutUS());

        /**
         * Calling [function] getCityDatafromFirebase
         */
        addAreaButton.setOnClickListener(v-> getCityDatafromFirebase());
        carParking_CV.setOnClickListener(v-> setFragment(new ParkingFragment()));
}

    private void citySpinnerMethod() {
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                pm.cityName=pm.citiesList.get(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter setCitySpinner = new ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,pm.citiesList);
        setCitySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(setCitySpinner);
    }



    /**
     * Fetching City from firebase
     **/
    private void getCityDatafromFirebase() {
        if (!loader.isShowing()) loader.show();
        FirebaseDatabase.getInstance().getReference("Cities")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot snapshot1:snapshot.getChildren()) {

                                String getID = (String) snapshot1.getValue();

                                if (getID != null && getID.equalsIgnoreCase(pm.cityName)) {
                                    pm.checkCityName = true;
                                    break;
                                }
                                else {
                                    pm.checkCityName = false;
                                }
                            }

                            if (loader.isShowing()) loader.dismiss();
                            if (pm.checkCityName) {
                                setFragment(new CityAreaFragment());
                            } else {
                                Toast.makeText(requireActivity(),
                                        "Couldn't find "+ pm.cityName+" city",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (loader.isShowing()) loader.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (loader.isShowing()) loader.dismiss();
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    /**
     * Fetching Area from firebase
     **/
    private void getAreaDatafromFirebase() {
        pm.areaList.clear();
        FirebaseDatabase.getInstance().getReference("CityArea")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot snapshot1:snapshot.getChildren()) {
                                CityArea cityArea = snapshot1.getValue(CityArea.class);
                                pm.areaList.add(cityArea);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });

    }



    private void uploadCityDetails(
            ArrayList<String> areaName)
    {
        CityArea cityArea = new CityArea(areaName);
        for (String i : areaName)
        {
            pm.mDatabase.child("Cities").child(i).setValue(i)
                    .addOnFailureListener(e -> Log.d("HomeFragment_Cities", e.getMessage()));
        }
    }
}