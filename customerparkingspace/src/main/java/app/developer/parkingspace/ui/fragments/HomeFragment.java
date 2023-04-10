package app.developer.parkingspace.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

import app.developer.parkingspace.dataclass.CityArea;
import app.developer.parkingspace.dataclass.User;
import app.developer.parkingspace.utils.PickerManager;
import app.developer.parkingspaces.R;


public class HomeFragment extends Fragment {
    Spinner citySpinner;
    PickerManager pm = PickerManager.getInstance();
    CardView carParking_CV,bikeParking_CV;
    Button btn_aboutUs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fetching City data from firebase
        getCities();

        //Fetching Area data from firebase
        getAreaDataFromFirebase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        CardView dashboardHeader = getActivity().findViewById(R.id.dashboardHeader);
        dashboardHeader.setVisibility(View.VISIBLE);
        initViews(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(() -> {
            try {
                //Fetch User Data From Firebase
                getUserDataFromFirebase();
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

        //buttons
        clickButtons();
    }

    private void initViews(View v) {
        citySpinner = v.findViewById(R.id.citySpinner);
        bikeParking_CV = v.findViewById(R.id.bikeParking_CV);
        carParking_CV=v.findViewById(R.id.carParking_CV);
        btn_aboutUs=v.findViewById(R.id.btn_aboutUs);
    }

    private void getCities() {
        // Initialize the Firebase Realtime Database client
        FirebaseDatabase.getInstance().getReference("Cities")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            pm.citiesList.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                String value= (String) snapshot1.getValue();
                                pm.citiesList.add(value);
                            }

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                if (pm.citiesList.isEmpty()){
                                    Toast.makeText(requireActivity(), "Internet Connectivity Issue",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }, 3000);
                            //City Spinner
                            citySpinnerMethod();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAreaDataFromFirebase() {

        FirebaseDatabase.getInstance().getReference("CityArea")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            pm.tempAreaList.clear();
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                CityArea cityArea = snapshot1.getValue(CityArea.class);
                                pm.tempAreaList.add(cityArea);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setFragment(Fragment fragment){
        if (fragment == null) return;

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft =  fragmentManager.beginTransaction();
        ft.replace(R.id.dashBoard_FL,fragment).addToBackStack(null).commit();

    }

    private void clickButtons() {

        //Calling fragment navigation method
        carParking_CV.setOnClickListener(v-> setFragment(new CityAreaFragment()));
        btn_aboutUs.setOnClickListener(v-> aboutUS());
}


    /**
     * Fetching user data from cloud
     **/
    private void getUserDataFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Customers")
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

    private void citySpinnerMethod() {
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                pm.areaList.clear();
                pm.chooseCity= (String) pm.citiesList.get(position);

                if(pm.chooseCity!=null)
                {
                    // Use filter to extract the specific areas according to city
                    pm.areaList = (ArrayList<CityArea>) pm.tempAreaList.stream()
                            .filter(n -> n.getCityName().equalsIgnoreCase(pm.chooseCity))
                            .collect(Collectors.toList());

                    for (CityArea i: pm.areaList)
                    {
                        Log.d("CheckingCity",""+i.getCityName());
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter setCitySpinner = new ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,pm.citiesList);
        setCitySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(setCitySpinner);

    }
}