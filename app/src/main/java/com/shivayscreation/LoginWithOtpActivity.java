package com.shivayscreation;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

public class LoginWithOtpActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 123;

    Button login, signup;
    EditText contact;
    SQLiteDatabase db;

    SharedPreferences sp;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otp);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        // Check and request SMS permission
        if (checkSmsPermission()) {
            // Permission already granted, continue with your activity logic
            initializeViews();
        } else {
            // Permission not granted, request it
            requestSmsPermission();
        }
    }

    private void initializeViews() {
        db = openOrCreateDatabase("Shivays_Creation", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        contact = findViewById(R.id.login_with_otp_contact);

        rememberMe = findViewById(R.id.login_with_otp_remeber);

        signup = findViewById(R.id.login_with_otp_signup);
        signup.setOnClickListener(view -> new CommonMethod(LoginWithOtpActivity.this, SignupActivity.class));

        login = findViewById(R.id.login_with_otp_login);

        login.setOnClickListener(view -> {
            if (contact.getText().toString().trim().equals("")) {
                contact.setError("Contact No. Required");
            } else if (contact.getText().toString().trim().length() < 10) {
                contact.setError("Valid Contact No. Required");
            } else {
                String selectQuery = "SELECT * FROM USERS WHERE CONTACT='" + contact.getText().toString() + "'";
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        // Extract user data from the cursor
                        String sUserId = cursor.getString(0);
                        String sName = cursor.getString(1);
                        String sEmail = cursor.getString(2);
                        String sContact = cursor.getString(3);
                        String sPassword = cursor.getString(4);
                        String sGender = cursor.getString(5);
                        String sCity = cursor.getString(6);
                        String sDob = cursor.getString(7);

                        // Save user data to SharedPreferences
                        sp.edit().putString(ConstantSp.ID, sUserId).commit();
                        sp.edit().putString(ConstantSp.NAME, sName).commit();
                        sp.edit().putString(ConstantSp.EMAIL, sEmail).commit();
                        sp.edit().putString(ConstantSp.CONTACT, sContact).commit();
                        sp.edit().putString(ConstantSp.PASSWORD, sPassword).commit();
                        sp.edit().putString(ConstantSp.GENDER, sGender).commit();
                        sp.edit().putString(ConstantSp.CITY, sCity).commit();
                        sp.edit().putString(ConstantSp.DOB, sDob).commit();

                        if (rememberMe.isChecked()) {
                            sp.edit().putString(ConstantSp.REMEMBER, "Yes").commit();
                        } else {
                            sp.edit().putString(ConstantSp.REMEMBER, "").commit();
                        }

                        Log.d("USER_DATA", sUserId + "\n" + sName + "\n" + sEmail + "\n" + sContact + "\n" + sPassword + "\n" + sGender + "\n" + sCity + "\n" + sDob);
                    }

                    String sOTP = getRandomNumberString();
                    sp.edit().putString(ConstantSp.OTP_CODE, sOTP).commit();

                    // Check and request SMS permission before sending SMS
                    if (checkSmsPermission()) {
                        sendSms(contact.getText().toString(), "Your OTP Code Is : " + sOTP);
                    } else {
                        showToast("SMS permission is required to send OTP. Please grant the permission.");
                        requestSmsPermission();
                    }

                } else {
                    new CommonMethod(LoginWithOtpActivity.this, "Login Unsuccessfully");
                }
            }
        });
    }

    private boolean checkSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;  // For devices below Marshmallow, permission is granted at installation time
    }

    private void requestSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, continue with your activity logic
                initializeViews();
            } else {
                // Permission denied, show a Toast and request permission again
                showToast("SMS permission is required for this app.");
                requestSmsPermission();
            }
        }
    }

    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
        new CommonMethod(LoginWithOtpActivity.this, "Sms Send Successfully");
        new CommonMethod(LoginWithOtpActivity.this, OtpActivity.class);
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
