package app.developer.parkingspace.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.developer.parkingspace.dataclass.CityArea;
import app.developer.parkingspace.interfaces.onAreaItemClick;
import app.developer.parkingspaces.R;

public class CityAreaAdapter extends RecyclerView.Adapter<CityAreaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CityArea> cityList;
    private onAreaItemClick clickListener;

    public CityAreaAdapter(Context context, ArrayList<CityArea> cityList, onAreaItemClick clickListener) {
        this.context = context;
        this.cityList = cityList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public CityAreaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.area_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAreaAdapter.ViewHolder holder, int position) {
        //Set the position here!!!
        Uri areaUri=null;
        CityArea currentItem = cityList.get(position);

//        Log.d("ABC","rest: ${currentItem.RestaurantDetails!![0].restaurantName}")
//        currentItem.restaurantDetails!!.forEach {
//            Log.d("checkingFoodL","resta: $it")
//        }

        holder.cityName_TV.setText("City "+currentItem.getCityName());
        holder.areaTitle_TV.setText(currentItem.getAreaName());
        holder.areaDes_TV.setText(currentItem.getDescription());

        try{
            areaUri = Uri.parse(currentItem.getAreaUrl());
        }catch (Exception e)
        {
            Log.d("cityAreaAdapter",""+e.getMessage());
        }

        Glide.with(context)
                .load(areaUri)
                .placeholder(R.drawable.add_item_icon)
                .into(holder.areaImage_IV);


        holder.itemView.setOnClickListener(view->{
            clickListener.onItemClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView areaImage_IV;
        public TextView areaTitle_TV, areaDes_TV, cityName_TV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.areaTitle_TV = itemView.findViewById(R.id.areaTitle_TV);
            this.areaDes_TV = itemView.findViewById(R.id.areaDes_TV);
            this.cityName_TV = itemView.findViewById(R.id.cityName_TV);
            this.areaImage_IV = itemView.findViewById(R.id.area_IV);
        }
    }
}
