package app.developer.parkingspace.applicationclass;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //To Prevent Dark Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Enabling Offline Capabilities on
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
