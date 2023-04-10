package app.developer.parkingspace.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.developer.parkingspace.adapters.BookedParkingAdapter;
import app.developer.parkingspace.adapters.ParkinglotAdapter;
import app.developer.parkingspace.dataclass.ParkingSlot;
import app.developer.parkingspace.utils.PickerManager;
import app.developer.parkingspaces.R;


public class BookedParkingFragment extends Fragment {
    PickerManager pm = PickerManager.getInstance();
    RecyclerView bookedRecyclerview;
    Dialog loader;
    ConstraintLayout layout_emptyList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_booked_parking, container, false);
        CardView dashboardHeader = getActivity().findViewById(R.id.dashboardHeader);
        dashboardHeader.setVisibility(View.GONE);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        loader=pm.progressDialog(requireActivity());
        bookedRecyclerview=v.findViewById(R.id.bookedRecyclerview);
        layout_emptyList=v.findViewById(R.id.layout_emptyList);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // fetched bookedParking data from cloud
        bookedParking();
    }

    private void buildRecyclerView() {

        bookedRecyclerview.setHasFixedSize(true);
        bookedRecyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));

        if (loader.isShowing()) loader.dismiss();
        //setting Adapter
        bookedRecyclerview.setAdapter(new BookedParkingAdapter(
                requireActivity(),
                pm.parkingList
        ));
    }


    private void setFragment(Fragment fragment){
        if (fragment == null) return;

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft =  fragmentManager.beginTransaction();
        ft.replace(R.id.dashBoard_FL,fragment).addToBackStack(null).commit();

    }

    public void bookedParking(){

        if (!loader.isShowing()) loader.show();

        pm.parkingList.clear();
        FirebaseDatabase.getInstance().getReference("BookedParking")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                ParkingSlot parkingSlot = snapshot1.getValue(ParkingSlot.class);
                                if (parkingSlot.getAuthID().equalsIgnoreCase(pm.mAuth.getUid()))
                                {
                                    pm.parkingList.add(parkingSlot);
                                }

                            }
                            buildRecyclerView();
                            setVisibility();

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
                setVisibility();
            }
        },5000);
    }

    private void setVisibility() {
        if (pm.parkingList.size()==0)
        {
            layout_emptyList.setVisibility(View.VISIBLE);
            bookedRecyclerview.setVisibility(View.GONE);
        }else{
            layout_emptyList.setVisibility(View.GONE);
            bookedRecyclerview.setVisibility(View.VISIBLE);
        }
    }
}