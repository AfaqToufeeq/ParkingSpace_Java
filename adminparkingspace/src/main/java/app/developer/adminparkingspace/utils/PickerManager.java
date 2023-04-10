package app.developer.adminparkingspace.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.developer.adminparkingspace.R;

import app.developer.adminparkingspace.dataclass.CityArea;
import app.developer.adminparkingspace.dataclass.ParkingSlot;
import app.developer.adminparkingspace.dataclass.User;


public class PickerManager {
    private static PickerManager pickerManager = null;
    private static Context context=null;


    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private PickerManager()
    {}
    public PickerManager(Context context)
    {
        this.context=context;
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized PickerManager getInstance()
    {
        if (pickerManager == null)
            pickerManager = new PickerManager();

        return pickerManager;
    }

    // Initialize Firebase
   public FirebaseAuth mAuth = FirebaseAuth.getInstance();
   public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public Uri downloadStorageUri=null;
    public String imageFileName=null;
    public ArrayList<CityArea> areaList = new ArrayList<>();
    public ArrayList<User> userList = new ArrayList<>();
    public ArrayList<String> citiesList= new ArrayList<>();
    public ArrayList<ParkingSlot> parkingList= new ArrayList<>();


    public String cityName=null;
    public Boolean checkCityName =false;

    public Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_progress_dialog);
        dialog.setCancelable(false);
         ImageView lottieAnimation = dialog.findViewById(R.id.gif);
         Glide.with(context).load(R.drawable.load_gif).into(lottieAnimation);
        return dialog;
    }

}
