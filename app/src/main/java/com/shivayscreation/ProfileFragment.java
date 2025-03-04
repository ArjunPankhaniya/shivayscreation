package com.shivayscreation;

//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class ProfileFragment extends Fragment {
//
//    EditText nameEditText, emailEditText, contactEditText, dobEditText;
//    RadioButton maleRadioButton, femaleRadioButton;
//    RadioGroup genderRadioGroup;
//    Spinner citySpinner;
//    Button updateProfileButton;
//
//    DatabaseReference userRef;
//    FirebaseUser currentUser;
//    ArrayAdapter<String> cityAdapter;
//
//    public ProfileFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        nameEditText = view.findViewById(R.id.home_name);
//        emailEditText = view.findViewById(R.id.home_email);
//        contactEditText = view.findViewById(R.id.home_contact);
//        dobEditText = view.findViewById(R.id.home_dob);
//        maleRadioButton = view.findViewById(R.id.home_male);
//        femaleRadioButton = view.findViewById(R.id.home_female);
//        genderRadioGroup = view.findViewById(R.id.home_gender);
//        citySpinner = view.findViewById(R.id.home_city);
//        updateProfileButton = view.findViewById(R.id.home_edit_profile);
//
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            userRef = FirebaseDatabase.getInstance().getReference("shivayscreation").child("userContact");
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // Retrieve user data from dataSnapshot
//                        Log.d("Firebase", "DataSnapshot: " + dataSnapshot.getValue());
//                        User user = dataSnapshot.getValue(User.class);
//                        if (user != null) {
//                            nameEditText.setText(user.getName());
//                            emailEditText.setText(user.getEmail());
//                            contactEditText.setText(user.getContact());
//                            dobEditText.setText(user.getDob());
//
//                            if (user.getGender().equalsIgnoreCase("male")) {
//                                maleRadioButton.setChecked(true);
//                            } else {
//                                femaleRadioButton.setChecked(true);
//                            }
//
//                            int cityPosition = cityAdapter.getPosition(user.getCity());
//                            citySpinner.setSelection(cityPosition);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle onCancelled event if needed
//                    Log.e("Firebase", "Failed to retrieve data: " + databaseError.getMessage());
//                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getCityList());
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        citySpinner.setAdapter(cityAdapter);
//
//        updateProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Implement the logic for updating the profile
//            }
//        });
//
//        return view;
//    }
//
//    private ArrayList<String> getCityList() {
//        ArrayList<String> cities = new ArrayList<>();
//        // Add your city list here or fetch from Firebase
//        cities.add("Rajkot");
//        cities.add("Ahmedabad");
//        cities.add("Surat");
//        // Add more cities as needed
//        return cities;
//    }
//
//    public static class User {
//        private String name;
//        private String email;
//        private String contact;
//        private String dob;
//        private String gender;
//        private String city;
//
//        public User() {
//            // Default constructor required for calls to DataSnapshot.getValue(User.class)
//        }
//
//        public User(String name, String email, String contact, String dob, String gender, String city) {
//            this.name = name;
//            this.email = email;
//            this.contact = contact;
//            this.dob = dob;
//            this.gender = gender;
//            this.city = city;
//        }
//
//        // Getters and setters for each property
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public String getContact() {
//            return contact;
//        }
//
//        public void setContact(String contact) {
//            this.contact = contact;
//        }
//
//        public String getDob() {
//            return dob;
//        }
//
//        public void setDob(String dob) {
//            this.dob = dob;
//        }
//
//        public String getGender() {
//            return gender;
//        }
//
//        public void setGender(String gender) {
//            this.gender = gender;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//    }
//}



import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    SharedPreferences sp;

    Button logout, updateProfile,editProfile;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText name, email, contact, dob;

    RadioButton male,female;
    RadioGroup gender;

    Spinner city;
    ArrayList<String> arrayList;
    Calendar calendar;

    String sCity;
    String sGender;
    SQLiteDatabase db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sp = getActivity().getSharedPreferences(ConstantSp.PREF, Context.MODE_PRIVATE);

        db = getActivity().openOrCreateDatabase("Shivays_Creation", Context.MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        name = view.findViewById(R.id.home_name);
        email = view.findViewById(R.id.home_email);
        contact = view.findViewById(R.id.home_contact);
        dob = view.findViewById(R.id.home_dob);
        city = view.findViewById(R.id.home_city);
        male = view.findViewById(R.id.home_male);
        female = view.findViewById(R.id.home_female);
        editProfile = view.findViewById(R.id.home_edit_profile);
        logout = view.findViewById(R.id.home_logout);
        updateProfile = view.findViewById(R.id.home_update_profile);

        calendar = Calendar.getInstance();

        dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    showDatePickerDialog();
                    return true;
                }
                return false;
            }
        });

        arrayList = new ArrayList<>();
        arrayList.add("Select City");
        arrayList.add("Gandhinagar");
        arrayList.add("Rajkot");
        arrayList.add("Ahmedabad");
        arrayList.add("Demo");
        arrayList.add("XYZ");
        arrayList.add("Surat");
        arrayList.remove(3);
        arrayList.set(3, "Vadodara");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = arrayList.get(i);
                    new CommonMethod(getActivity(), sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender = view.findViewById(R.id.home_gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = view.findViewById(i);
                sGender = radioButton.getText().toString();
                new CommonMethod(getActivity(), sGender);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new CommonMethod(getActivity(),LoginWithOtpActivity.class);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().trim().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(getActivity(), "Please Select Gender");
                } else if (sCity.equals("")) {
                    new CommonMethod(getActivity(), "Please Select City");
                } else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please Select Date of Birth");
                } else {
                    String selectQuery = "SELECT * FROM USERS WHERE USERID='" + sp.getString(ConstantSp.ID,"") + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor.getCount() > 0) {
                        String updateQuery = "UPDATE USERS SET NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',GENDER='"+sGender+"',CITY='"+sCity+"',DOB='"+dob.getText().toString()+"' WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"' ";
                        db.execSQL(updateQuery);
                        new CommonMethod(getActivity(),"Update Successfully");

                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.CITY,sCity).commit();
                        sp.edit().putString(ConstantSp.DOB,dob.getText().toString()).commit();

                        setData(false);
                    } else {
                        new CommonMethod(getActivity(),"Invalid UserId");
                    }
                }
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);

        return view;
    }

    private void setData(boolean isEnable) {
        name.setEnabled(isEnable);
        email.setEnabled(isEnable);
        contact.setEnabled(isEnable);
        dob.setEnabled(isEnable);

        male.setEnabled(isEnable);
        female.setEnabled(isEnable);

        city.setEnabled(isEnable);

        if(isEnable){
            editProfile.setVisibility(View.GONE);
            updateProfile.setVisibility(View.VISIBLE);
        }
        else{
            editProfile.setVisibility(View.VISIBLE);
            updateProfile.setVisibility(View.GONE);
        }

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        dob.setText(sp.getString(ConstantSp.DOB,""));

        sGender = sp.getString(ConstantSp.GENDER,"");
        if(sGender.equalsIgnoreCase("Male")){
            male.setChecked(true);
        }
        else if(sGender.equalsIgnoreCase("Female")){
            female.setChecked(true);
        }

        sCity = sp.getString(ConstantSp.CITY,"");
        int iCityPosition = 0;
        for(int i=0;i<arrayList.size();i++){
            if(sCity.equalsIgnoreCase(arrayList.get(i))){
                iCityPosition = i;
            }
        }
        city.setSelection(iCityPosition);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private final DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            dob.setText(sdf.format(calendar.getTime()));
        }
    };
}
