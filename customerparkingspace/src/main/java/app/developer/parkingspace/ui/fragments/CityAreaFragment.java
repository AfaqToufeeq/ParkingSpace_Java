package app.developer.parkingspace.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.developer.parkingspaces.R;
import app.developer.parkingspace.adapters.CityAreaAdapter;
import app.developer.parkingspace.dataclass.CityArea;
import app.developer.parkingspace.interfaces.onAreaItemClick;
import app.developer.parkingspace.utils.PickerManager;


public class CityAreaFragment extends Fragment
        implements onAreaItemClick {
    PickerManager pm = PickerManager.getInstance();
    ConstraintLayout layoutEmptyList;
    RecyclerView areaRV;
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
        buildRecyclerView();
        onbackPressed();

    }

    private void initViews(View v) {
        loader=pm.progressDialog(requireActivity());
        layoutEmptyList = v.findViewById(R.id.layout_emptyList);
        areaRV = v.findViewById(R.id.areaRecyclerview);
    }

    private void buttonClicks() {

    }

    private void buildRecyclerView() {
        if(!loader.isShowing()) loader.show();
        areaRV.setHasFixedSize(true);
        areaRV.setLayoutManager(new LinearLayoutManager(requireActivity()));

        setVisibility();  //visibility

        //setting Adapter
        areaRV.setAdapter(new CityAreaAdapter(
                requireActivity(),
                pm.areaList,
                CityAreaFragment.this
        ));

        if(loader.isShowing()) loader.dismiss();

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
        if(!pm.areaList.isEmpty())
        {
            layoutEmptyList.setVisibility(View.GONE);
            areaRV.setVisibility(View.VISIBLE);
        }
        else{
            layoutEmptyList.setVisibility(View.VISIBLE);
            areaRV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        //Getting areaName on user click from list
        pm.chooseArea = pm.areaList.get(position).getAreaName();
        setFragment(new ParkingFragment());
    }

    @Override
    public void onLongClick(int position) {

    }
}