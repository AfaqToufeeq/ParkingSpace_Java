package app.developer.parkingspace.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import app.developer.parkingspace.ui.fragments.BookedParkingFragment;
import app.developer.parkingspace.ui.fragments.CityAreaFragment;
import app.developer.parkingspace.ui.fragments.UserProfileFragment;
import app.developer.parkingspaces.R;
import app.developer.parkingspace.ui.fragments.HomeFragment;
import app.developer.parkingspace.utils.PickerManager;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Spinner profileSpinner;
    CardView dashboardHeader;
    PickerManager pm=PickerManager.getInstance();
    String  userName = "";
    String[] profileName = {userName,"Sign Out"};
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        fragment= getData();
        addFragmentToActivity(fragment);
        bottomNavigation();
        buttonClicks();
    }
    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        profileSpinner = findViewById(R.id.profileSpinner);
    }
    private void buttonClicks() {
        //Spinner Click Listener
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                if(profileName[position].equals("Sign Out"))
                {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter setSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,profileName);
        setSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(setSpinner);

    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.dashBoard_FL);
        if (fragment instanceof CityAreaFragment) {
            addFragmentToActivity(new HomeFragment());
        }else {
            super.onBackPressed();
        }

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }

    public Fragment getData() {
//        val fragment = intent.getStringExtra("fragmentCart")
//        when(fragment){
//            "addToCartFragment" -> return addToCartFragment()
//             else -> return restaurantsFragment()
//        }
        return new HomeFragment();
    }
    private void addFragmentToActivity(Fragment fragment){
        if (fragment == null) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft =  fragmentManager.beginTransaction();
        ft.replace(R.id.dashBoard_FL,fragment)
                .addToBackStack(null)
                .commit();
    }
    private void bottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.HomeMenu:
                    addFragmentToActivity(new HomeFragment());
                    break;
                case R.id.bookingMenu:
                    addFragmentToActivity(new BookedParkingFragment());
                    break;
                case R.id.ProfileMenu:
                    addFragmentToActivity(new UserProfileFragment());
                    break;
            }
            return true;
        });
    }
}