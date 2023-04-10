package app.developer.adminparkingspace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.adminparkingspace.R;

import java.util.ArrayList;

import app.developer.adminparkingspace.dataclass.ParkingSlot;

public class BookedParkingAdapter extends RecyclerView.Adapter<BookedParkingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ParkingSlot> parkingList;

    public BookedParkingAdapter(Context context, ArrayList<ParkingSlot> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.booked_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Set the position here!!!
        ParkingSlot currentItem = parkingList.get(position);

        holder.slotTV.setText("P"+(Integer.parseInt(currentItem.getSlotID())+1));
        holder.ownerNameTV.setText(currentItem.getDriverName());
        holder.phoneTV.setText(currentItem.getDriverPhone());
        holder.locationTV.setText(currentItem.getParkingCity()+" "+currentItem.getParkingArea());
        holder.dateTimeTV.setText(currentItem.getParkingTime());
        holder.vehicleTV.setText(currentItem.getVehicleModel());

    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView slotTV,ownerNameTV,phoneTV,vehicleTV,locationTV,dateTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.slotTV = itemView.findViewById(R.id.slotTV);
            this.ownerNameTV = itemView.findViewById(R.id.ownerNameTV);
            this.phoneTV = itemView.findViewById(R.id.phoneTV);
            this.vehicleTV = itemView.findViewById(R.id.vehicleTV);
            this.locationTV = itemView.findViewById(R.id.locationTV);
            this.dateTimeTV = itemView.findViewById(R.id.dateTimeTV);
        }
    }
}
