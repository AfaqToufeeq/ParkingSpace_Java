package app.developer.parkingspace.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.ArrayList;

import app.developer.parkingspace.interfaces.onAreaItemClick;
import app.developer.parkingspace.interfaces.onParkingItemClick;
import app.developer.parkingspaces.R;
import app.developer.parkingspace.dataclass.ParkingSlot;

public class ParkinglotAdapter extends RecyclerView.Adapter<ParkinglotAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ParkingSlot> parkingList;
    private onParkingItemClick clickListener;
    private int fetchedParkingSlot;
    private String parking=null;
    private Boolean checkSingleClick=false;
    public ParkinglotAdapter(Context context, ArrayList<ParkingSlot> parkingList, int fetchedParkingSlot, onParkingItemClick clickListener) {
        this.context = context;
        this.parkingList = parkingList;
        this.fetchedParkingSlot = fetchedParkingSlot;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ParkinglotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.parkinglot_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ParkinglotAdapter.ViewHolder holder, int position) {
        //Set the position here!!!

            //setting up the booked cars
        for (ParkingSlot currentItem : parkingList)
        {
            if (currentItem.getSlotID().equals(position+""))
            {
                holder.block_FL.setBackgroundColor(Color.parseColor("#7E000000"));
                holder.block_IV.setImageResource(R.drawable.car3);
                holder.block_IV.setScaleX(-1);
            }
        }


        holder.block_TV.setText("P"+(position+1));

        holder.itemView.setOnClickListener(view->{
            ColorDrawable viewColor = (ColorDrawable) holder.block_FL.getBackground();

            if(viewColor.getColor() == context.getResources().getColor(R.color.lightyellowcolor))       //For yellow color //parking space
            {
               if (!checkSingleClick)
               {
                   holder.block_FL.setBackgroundColor(R.color.mainColorBlue);
                   holder.block_IV.setImageResource(R.drawable.car3);
                   holder.block_IV.setScaleX(-1);
                   parking="booked";
                   checkSingleClick=true;
               }else {
                   parking="notBooked";
               }
            }else if(viewColor.getColor() != context.getResources().getColor(R.color.gray)){
                holder.block_FL.setBackgroundColor(Color.parseColor("#40FFC107"));
                holder.block_IV.setImageResource(R.drawable.parking1);
                holder.block_IV.setScaleX(1);
                parking="notBooked";
                checkSingleClick=false;
            }

            clickListener.onItemClick(position,parking);
        });

    }

    @Override
    public int getItemCount() {
        return fetchedParkingSlot;
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
