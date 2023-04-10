package app.developer.adminparkingspace.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.adminparkingspace.R;

import app.developer.adminparkingspace.utils.PickerManager;

public class LoginActivity extends AppCompatActivity {

    EditText email_ET,pass_ET;
    Button loginBtn,signupBtn;
    ProgressDialog progress;
    PickerManager pm= PickerManager.getInstance();
    //Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        buttonClicks();
    }

    private void initViews() {
        //Progress Dialog
        progress = new ProgressDialog(this);
        progress.setTitle("Logging in");
        progress.setMessage("Logging into your account...");
        progress.setCancelable(false);

        //Assigning Views
        email_ET = findViewById(R.id.email_ET);
        pass_ET = findViewById(R.id.pass_ET);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);


    }

    private void buttonClicks() {
        //Login method
       loginBtn.setOnClickListener( view-> loginUser());

       //SignUp Button
       signupBtn.setOnClickListener(v->{
           startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
           finish();
       });
    }

    private void loginUser() {
        String email = email_ET.getText().toString();
        String pass = pass_ET.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please Enter the login details", Toast.LENGTH_SHORT).show();
            return;
        }

       if(!progress.isShowing()) progress.show();
       pm.mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, task -> {
           if(task.isSuccessful()){
               if (progress.isShowing()) progress.dismiss();
               Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
               finish();
           }
           else{
               if (progress.isShowing()) progress.dismiss();
               Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(pm.mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }
    }
}