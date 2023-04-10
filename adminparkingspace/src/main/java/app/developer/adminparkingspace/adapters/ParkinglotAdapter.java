package app.developer.adminparkingspace.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.adminparkingspace.R;

import java.util.ArrayList;

import app.developer.adminparkingspace.dataclass.ParkingSlot;
import app.developer.adminparkingspace.interfaces.onAreaItemClick;


public class ParkinglotAdapter extends RecyclerView.Adapter<ParkinglotAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ParkingSlot> parkingList;
    private onAreaItemClick clickListener;

    public ParkinglotAdapter(Context context, ArrayList<ParkingSlot> parkingList, onAreaItemClick clickListener) {
        this.context = context;
        this.parkingList = parkingList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ParkinglotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.parkinglot_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkinglotAdapter.ViewHolder holder, int position) {
        //Set the position here!!!
        Uri areaUri=null;
//        ParkingSlot currentItem = parkingList.get(position);

//        Log.d("ABC","rest: ${currentItem.RestaurantDetails!![0].restaurantName}")
//        currentItem.restaurantDetails!!.forEach {
//            Log.d("checkingFoodL","resta: $it")
//        }



//        try{
//            areaUri = Uri.parse(currentItem.getAreaUrl());
//        }catch (Exception e)
//        {
//            Log.d("restaurantAdapter",""+e.getMessage());
//        }

//        Glide.with(context)
//                .load(areaUri)
//                .placeholder(R.drawable.add_item_icon)
//                .into(holder.areaImage_IV);


        holder.itemView.setOnClickListener(view->{
            ColorDrawable viewColor = (ColorDrawable) holder.block_FL.getBackground();
            Log.d("sad","Sda "+viewColor.getColor());
            clickListener.onItemClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView block_IV;
        public TextView block_TV;
        public FrameLayout block_FL;

        public ViewHolder(View itemView) {
            super(itemView);
            this.block_IV = itemView.findViewById(R.id.block_IV);
            this.block_TV = itemView.findViewById(R.id.block_TV);
            this.block_FL = itemView.findViewById(R.id.block_FL);
        }
    }
}
