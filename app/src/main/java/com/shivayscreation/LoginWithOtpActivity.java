package com.shivayscreation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginWithOtpActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 123;
    private static final String SESSION_STATUS = "session_status";
    private static final String TAG = "LoginWithOtpActivity";

    // Declare UI elements
    Button login, signup;
    EditText contact;
    SharedPreferences sp;
    CheckBox rememberMe;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otp);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        usersRef = FirebaseDatabase.getInstance().getReference().child("shivayscreation");

        // Check if the user is already logged in
        if (isLoggedIn()) {
            // If logged in, redirect to the home activity or wherever needed
            redirectToHome();
        } else {
            // If not logged in, proceed with login process
            checkAndRequestSmsPermission();
        }
    }

    private boolean isLoggedIn() {
        // Retrieve session status from SharedPreferences
        return sp.getBoolean(SESSION_STATUS, false);
    }

    private void setLoggedIn(boolean loggedIn) {
        // Save session status to SharedPreferences
        sp.edit().putBoolean(SESSION_STATUS, loggedIn).apply();
    }

    private void checkAndRequestSmsPermission() {
        if (checkSmsPermission()) {
            initializeViews();
        } else {
            requestSmsPermission();
        }
    }

    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeViews();
            } else {
                showToast("SMS permission is required for this app.");
            }
        }
    }

    private void initializeViews() {
        // Initialize UI elements
        contact = findViewById(R.id.login_with_otp_contact);
        rememberMe = findViewById(R.id.login_with_otp_remeber);

        // Set onClickListeners
        signup = findViewById(R.id.login_with_otp_signup);
        signup.setOnClickListener(view -> startActivity(new Intent(LoginWithOtpActivity.this, SignupActivity.class)));

        login = findViewById(R.id.login_with_otp_login);
        login.setOnClickListener(view -> {
            String enteredContact = contact.getText().toString().trim();
            if (enteredContact.isEmpty()) {
                showToast("Contact number is required");
                return;
            }
            DatabaseReference userRef = usersRef.child(enteredContact);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String sName = dataSnapshot.child("name").getValue(String.class);
                        String sContact = dataSnapshot.child("contact").getValue(String.class);
                        String sEmail = dataSnapshot.child("email").getValue(String.class);
                        String sOTP = getRandomNumberString();
                        sp.edit().putString(ConstantSp.NAME, sName).apply();
                        sp.edit().putString(ConstantSp.CONTACT, sContact).apply();
                        sp.edit().putString(ConstantSp.EMAIL, sEmail).apply();
                        sp.edit().putString(ConstantSp.OTP_CODE, sOTP).apply();
                        sendOtp(enteredContact, "Your OTP Code Is: " + sOTP);
                        // Update session status
                        setLoggedIn(true);
                    } else {
                        showToast("User not found! Please sign up.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Database Error: " + databaseError.getMessage());
                }
            });
        });
    }

    private void sendOtp(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        showToast("OTP sent successfully");
        startActivity(new Intent(LoginWithOtpActivity.this, OtpActivity.class));
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(900000) + 100000;
        return String.valueOf(number);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void redirectToHome() {
        startActivity(new Intent(LoginWithOtpActivity.this, DashboardActivity.class));
        finish(); // Finish this activity to prevent going back to the login screen on pressing back
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Clear session status on logout
        setLoggedIn(false);
        finishAffinity();
    }
}
