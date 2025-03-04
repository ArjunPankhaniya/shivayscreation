package com.shivayscreation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    Button login, signup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText name, email, contact, password, confirmPassword, dob;

    RadioGroup gender;

    Spinner city;
    ArrayList<String> arrayList;
    Calendar calendar;

    String sCity;
    String sGender;

    DatabaseReference usersRef; // Firebase Database reference

    static class User {
        private String userId;
        private String name;
        private String email;
        private String contact;
        private String password;
        private String gender;
        private String city;
        private String dob;

        // Empty constructor required for Firebase
        public User() {}

        public User(String userId, String name, String email, String contact, String password, String gender, String city, String dob) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.contact = contact;
            this.password = password;
            this.gender = gender;
            this.city = city;
            this.dob = dob;
        }

        // Getters and setters for all fields
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Database reference
        usersRef = FirebaseDatabase.getInstance().getReference("shivayscreation");

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);

        dob = findViewById(R.id.signup_dob);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));
            }
        };

        dob.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignupActivity.this,
                    dateClick,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        city = findViewById(R.id.signup_city);

        arrayList = new ArrayList<>();
        arrayList.add("Select City");
        arrayList.add("Gandhinagar");
        arrayList.add("Rajkot");
        arrayList.add("Ahmedabad");
        arrayList.add("Demo");
        arrayList.add("XYZ");
        arrayList.add("Surat");

        arrayList.remove(3);
        arrayList.add(3, "Vadodara");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = arrayList.get(i);
                    // You can perform any necessary operations related to city selection here
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender = findViewById(R.id.signup_gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedRadioButton = findViewById(i);
                if (selectedRadioButton != null) {
                    sGender = selectedRadioButton.getText().toString();
                } else {
                    // Handle the case when no RadioButton is selected
                }
            }
        });

        signup = findViewById(R.id.signup_signup);
        login = findViewById(R.id.signup_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = name.getText().toString();
                if (userName.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email field
                String userEmail = email.getText().toString();
                if (userEmail.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!userEmail.matches(emailPattern)) {
                    Toast.makeText(SignupActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the contact number is empty
                String userContact = contact.getText().toString();
                if (userContact.isEmpty()) {
                    // Display a message indicating that contact number is required
                    Toast.makeText(SignupActivity.this, "Contact number is required", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method
                }


                // Validate password field
                String userPassword = password.getText().toString();
                if (userPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate confirm password field
                String userConfirmPassword = confirmPassword.getText().toString();
                if (userConfirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!userPassword.equals(userConfirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate gender field
                if (sGender == null || sGender.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate city field
                if (sCity == null || sCity.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please select your city", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate date of birth field
                String userDob = dob.getText().toString();
                if (userDob.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Check if the contact number already exists in Firebase
                usersRef.child(userContact).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User with the same contact number already exists
                            // Display a message or handle accordingly (e.g., show a Toast)
                            Toast.makeText(SignupActivity.this, "User already exists with this contact number", Toast.LENGTH_SHORT).show();
                        } else {
                            // Continue with user creation and saving to Firebase
                            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Increment sUserId with each user entry
                                    long nextUserId = dataSnapshot.getChildrenCount() + 1;

                                    User user = new User(
                                            "User_" + nextUserId, // Concatenate "User_" with the incremented value
                                            name.getText().toString(),
                                            email.getText().toString(),
                                            userContact,
                                            password.getText().toString(),
                                            sGender,
                                            sCity,
                                            dob.getText().toString()
                                    );

                                    // Use the contact number as the key and set the value
                                    DatabaseReference newUserRef = usersRef.child(userContact); // Create a reference for the new user
                                    newUserRef.setValue(user); // Push the "user" object containing user data to the database

                                    Toast.makeText(SignupActivity.this, "Created Account Successfully", Toast.LENGTH_SHORT).show();

                                    // ...
                                    onBackPressed();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled event if needed
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });
            }
        });


    }
}
