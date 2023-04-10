package app.developer.adminparkingspace.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.adminparkingspace.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.developer.adminparkingspace.dataclass.CityArea;
import app.developer.adminparkingspace.dataclass.ParkingSlot;
import app.developer.adminparkingspace.firebasestorage.firebaseStorageManager;
import app.developer.adminparkingspace.interfaces.onAreaItemClick;
import app.developer.adminparkingspace.utils.PickerManager;

public class ParkingFragment extends Fragment implements onAreaItemClick {
    PickerManager pm = PickerManager.getInstance();
    String areaName,cityName,parkingSlot_ET;
    EditText cityID_ET,areaID_ET,parkingID_ET;
    TextView parkingSlot_TV;
    Button search_btn,addSlot_btn;
    CardView CityCardView,ParkingCardView;
    Dialog loader;
    Boolean checkCityName =false,checkAreaName=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_parking, container, false);
        CardView dashboardHeader = getActivity().findViewById(R.id.dashboardHeader);
        dashboardHeader.setVisibility(View.GONE);
        initViews(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonClicks();
    }


    private void initViews(View v) {
        loader=pm.progressDialog(requireActivity());
        parkingSlot_TV=v.findViewById(R.id.parkingSlot_TV);
        cityID_ET=v.findViewById(R.id.cityID_ET);
        areaID_ET=v.findViewById(R.id.areaID_ET);
        parkingID_ET=v.findViewById(R.id.parkingID_ET);
        CityCardView=v.findViewById(R.id.CityCardView);
        ParkingCardView=v.findViewById(R.id.ParkingCardView);
        search_btn =v.findViewById(R.id.search_btn);
        addSlot_btn =v.findViewById(R.id.addSlot_btn);
    }


    private void buttonClicks() {
        search_btn.setOnClickListener(v -> getData());
        addSlot_btn.setOnClickListener(v -> setParkingSlotsData());
    }


    private void setParkingSlotsData() {
        parkingSlot_ET=parkingID_ET.getText().toString();

        if (parkingSlot_ET.isEmpty())
        {
            Toast.makeText(requireActivity(), "Please, enter number of parking slots", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!loader.isShowing()) loader.show();
        ParkingSlot parkingSlot = new ParkingSlot(parkingSlot_ET, areaName, cityName,pm.mAuth.getUid());
        pm.mDatabase.child("ParkingSlots").child(areaName).setValue(parkingSlot)
                .addOnSuccessListener(unused -> {

                    if(loader.isShowing()) loader.dismiss();
                    Toast.makeText(requireActivity(), "Added up the parking slots in "+areaName,
                            Toast.LENGTH_SHORT).show();
                    parkingID_ET.getText().clear();

                })
                .addOnFailureListener(e -> {
                    if(loader.isShowing()) loader.dismiss();
                    Toast.makeText(requireActivity(), "Failed to add slots "+ e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });

    }


    private void getParkingSlotData() {
        FirebaseDatabase.getInstance().getReference("ParkingSlots").child(areaName).child("slotID")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String parkingSlot=snapshot.getValue(String.class);
                            parkingSlot_TV.setText("Slots: "+parkingSlot);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getData() {
        cityName=cityID_ET.getText().toString();
        areaName=areaID_ET.getText().toString();

        if(areaName.isEmpty() || cityName.isEmpty())
        {
            Toast.makeText(requireActivity(), "Please complete the details", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String i: pm.citiesList){
            if(i.equalsIgnoreCase(cityName)){
                checkCityName=true;
                getAreaData();
                break;
            }else {
                checkCityName=false;
            }
        }
        if(!checkCityName)
        {
            Toast.makeText(requireActivity(), "City is not found", Toast.LENGTH_SHORT).show();
        }

    }

    private void getAreaData() {
        for (CityArea i : pm.areaList){
            if(i.getAreaName().equalsIgnoreCase(areaName)){
                checkAreaName=true;
                ParkingCardView.setVisibility(View.VISIBLE);
                CityCardView.setVisibility(View.GONE);
                getParkingSlotData();               //Getting parking slots

                break;
            }else{
                checkAreaName=false;
            }
        }

        if(!checkAreaName)
        {
            Toast.makeText(requireActivity(), "Area is not found", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongClick(int position) {

    }
}