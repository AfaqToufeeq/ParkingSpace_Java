package app.developer.adminparkingspace.ui.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.adminparkingspace.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.developer.adminparkingspace.dataclass.User;
import app.developer.adminparkingspace.ui.activities.LoginActivity;
import app.developer.adminparkingspace.utils.PickerManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {
    PickerManager pm = PickerManager.getInstance();
    TextView nameTv, phoneTv,emailTv;
    Button signOutbtn;
    CircleImageView circleImageView;
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        CardView dashboardHeader = getActivity().findViewById(R.id.dashboardHeader);
        dashboardHeader.setVisibility(View.GONE);
        initViews(v);
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userData();
        buttonClicks();
    }

    private void userData() {
        nameTv.setText(pm.userList.get(0).getUserName());
        emailTv.setText(pm.userList.get(0).getEmail());
        phoneTv.setText(pm.userList.get(0).getPhone());
    }

    private void buttonClicks() {
        signOutbtn.setOnClickListener(view -> alert());
    }

    private void initViews(View v) {
        nameTv = v.findViewById(R.id.UserNameTxt);
        emailTv = v.findViewById(R.id.email);
        phoneTv = v.findViewById(R.id.phone);
        circleImageView = v.findViewById(R.id.ProfileImageView);
        signOutbtn = v.findViewById(R.id.signOutButton);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging Out...");

    }






    public void alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Logout")
                .setMessage("Do you really want to logout ?")

                .setPositiveButton("yes", (dialog, which) -> {
                    dialog.dismiss();
                    progressDialog.show();
                    Toast.makeText(getActivity(), "Logged Out ", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent logout = new Intent(getActivity(), LoginActivity.class);
                    progressDialog.dismiss();
                    startActivity(logout);
                    getActivity().finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    progressDialog.dismiss();
                })
                .create();
        alertDialog.show();
    }
}

