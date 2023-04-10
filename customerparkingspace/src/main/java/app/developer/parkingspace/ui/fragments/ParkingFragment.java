package app.developer.parkingspace.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import app.developer.parkingspace.adapters.ParkinglotAdapter;
import app.developer.parkingspace.dataclass.ParkingSlot;
import app.developer.parkingspace.interfaces.onParkingItemClick;
import app.developer.parkingspace.utils.PickerManager;
import app.developer.parkingspaces.R;

public class ParkingFragment extends Fragment implements onParkingItemClick {
    PickerManager pm = PickerManager.getInstance();
    RecyclerView parkingRecyclerview;
    RelativeLayout ownerInfo_RL;
    EditText fullName,email,phone,carModelNo;
    TextView datePickerTV,timePickerTV,priceTV;
    String Position=null,Parking=null,FullName=null,Email=null,Phone=null,CarModelNo=null,TimeDate=null,Price=null;
    int fetchedParkingSlot;
    Button confirmParking,backButton,proceed,datePickerButton,timePickerButton;
    Dialog loader;


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

        getParkingSlotData();
        buttonClicks();

    }

    private void buildRecyclerView() {

        parkingRecyclerview.setHasFixedSize(true);
        confirmParking.setVisibility(View.VISIBLE);
        confirmParking.setClickable(false);

        if (loader.isShowing()) loader.dismiss();
        //setting Adapter
        parkingRecyclerview.setAdapter(new ParkinglotAdapter(
                requireActivity(),
                pm.parkingList,
                fetchedParkingSlot,
                ParkingFragment.this
        ));
    }

    private void initViews(View v) {
        loader=pm.progressDialog(requireActivity());
        parkingRecyclerview =v.findViewById(R.id.parkingRecyclerview);
        timePickerTV =v.findViewById(R.id.timePickerTV);
        datePickerTV =v.findViewById(R.id.datePickerTV);
        ownerInfo_RL =v.findViewById(R.id.ownerInfo_RL);
        fullName =v.findViewById(R.id.fullName);
        email =v.findViewById(R.id.email);
        phone =v.findViewById(R.id.phone);
        carModelNo =v.findViewById(R.id.carModelNo);
        confirmParking =v.findViewById(R.id.confirmParking);
        proceed =v.findViewById(R.id.proceedButton);
        datePickerButton = v.findViewById(R.id.date_picker_button);
        timePickerButton = v.findViewById(R.id.time_picker_button);
        priceTV = v.findViewById(R.id.priceTV);

    }
    private void buttonClicks() {
        //to confirm Booking
        confirmParking.setOnClickListener(v-> checkParkingData());
        proceed.setOnClickListener(v-> userInfo());

        datePickerButton.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireActivity(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // do something with the selected date
                        TimeDate= dayOfMonth +"-"+monthOfYear+"-"+year1;
                        datePickerTV.setText(dayOfMonth+"-"+monthOfYear+"-"+year1);
                        Log.d("TimeDate","Date "+TimeDate);
                    },
                    year,
                    month,
                    day);
            datePickerDialog.show();
        });

        timePickerButton.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireActivity(),
                    (view, hourOfDay, minute1) -> {
                        // do something with the selected time
                        String time=TimeDate+" , ";
                        TimeDate = hourOfDay+" "+minute1;
                        timePickerTV.setText(hourOfDay+" "+minute1);
                        Log.d("TimeDate","Time "+TimeDate);
                    },
                    hour,
                    minute,
                    false);
            timePickerDialog.show();
        });
    }

    private void checkParkingData() {
        if(Parking.equals("booked"))
        {
            parkingRecyclerview.setVisibility(View.GONE);
            confirmParking.setVisibility(View.GONE);
            ownerInfo_RL.setVisibility(View.VISIBLE);

            if(ParkinglotAdapter.count<fetchedParkingSlot/2)
            {
                Price="5.00";
                Price = String.valueOf(Double.parseDouble(Price)+3.00);
            }else{
                Price="5.00";
            }

            priceTV.setText("price:                                "+Price+" BHD/24hr" );
        }
    }

    private void userInfo() {

        FullName = fullName.getText().toString();
        Email = email.getText().toString();
        Phone = phone.getText().toString();
        CarModelNo = carModelNo.getText().toString();


        if (FullName.isEmpty()|| Email.isEmpty() || Phone.isEmpty() || CarModelNo.isEmpty()){
            Toast.makeText(requireActivity(),"Please fill the details",Toast.LENGTH_SHORT).show();
            return;
        }

        uploadUserParkingData(Position,Parking,FullName,Phone,Email,CarModelNo,TimeDate,pm.chooseArea,pm.chooseCity,Price);
    }

    private void uploadUserParkingData(String position, String parking, String fullName, String phone, String email, String carModelNo, String timeDate, String chooseArea, String chooseCity, String price) {

        if(!loader.isShowing()) loader.show();
        ParkingSlot parkingSlot = new ParkingSlot(position, parking, fullName,phone,email,carModelNo,timeDate,chooseArea,chooseCity,pm.mAuth.getUid(),Price);
        pm.mDatabase.child("BookedParking").child(position+fullName).setValue(parkingSlot)
                .addOnSuccessListener(unused -> {

                    if(loader.isShowing()) loader.dismiss();
                    Toast.makeText(requireActivity(), "Booked",
                            Toast.LENGTH_SHORT).show();

                    setFragment(new BookedParkingFragment());


                })
                .addOnFailureListener(e -> {
                    if(loader.isShowing()) loader.dismiss();
                    Toast.makeText(requireActivity(), "Failed to book "+ e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Fetching parking slots allot by admin
     **/
    private void getParkingSlotData() {
        if (!loader.isShowing()) loader.show();
        FirebaseDatabase.getInstance().getReference("ParkingSlots").child(pm.chooseArea).child("slotID")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            fetchedParkingSlot= Integer.parseInt(snapshot.getValue(String.class));
                            bookedParking();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (loader.isShowing()) loader.dismiss();
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler(Looper.getMainLooper()).postDelayed(()->{
            if (fetchedParkingSlot==0)
            {
                if (loader.isShowing()) loader.dismiss();
            }
        },5000);
    }


    public void bookedParking(){

        FirebaseDatabase.getInstance().getReference("BookedParking")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                ParkingSlot parkingSlot = snapshot1.getValue(ParkingSlot.class);
                                if (parkingSlot.getParkingArea().equalsIgnoreCase(pm.chooseArea))
                                {
                                    pm.parkingList.add(parkingSlot);
                                }

                            }

                            buildRecyclerView();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (loader.isShowing()) loader.dismiss();
                        Toast.makeText(requireActivity(),error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });

        new Handler(Looper.getMainLooper()).postDelayed(()->{
            if (pm.parkingList.size()==0)
            {
                if (loader.isShowing()) loader.dismiss();
                buildRecyclerView();
            }
        },5000);
    }


    private void setFragment(Fragment fragment){
        if (fragment == null) return;

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft =  fragmentManager.beginTransaction();
        ft.replace(R.id.dashBoard_FL,fragment).addToBackStack(null).commit();

    }

    @Override
    public void onItemClick(int position, String parking) {
       if (parking.equals("booked")){
           Position= String.valueOf(position);
           Parking=parking;
           confirmParking.setClickable(true);
           confirmParking.setEnabled(true);
       }
    }

    @Override
    public void onLongClick(int position, String parking) {

    }
}