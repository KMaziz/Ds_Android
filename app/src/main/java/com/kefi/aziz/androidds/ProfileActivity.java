package com.kefi.aziz.androidds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private EditText firstname,lastname,email,dateofbirth;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;
    final Calendar myCalendar = Calendar.getInstance();

        Button update,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firstname = (EditText) findViewById(R.id.firstnameedit);
                lastname= (EditText) findViewById(R.id.lastnameedit);
                email= (EditText) findViewById(R.id.emailedit);
                dateofbirth= (EditText) findViewById(R.id.dateofBirthedit);
                logout =(Button) findViewById(R.id.logout);
                update =(Button) findViewById(R.id.updatebutton);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateofbirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("User").child(onlineUserID);

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this,"error",Toast.LENGTH_LONG).show();
                }
                else {
                    User user = task.getResult().getValue(User.class);

                    firstname.setText(user.firstname);
                    lastname.setText(user.lastname);
                    email.setText(user.email);
                    dateofbirth.setText(user.dateofbirth);
                }
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = ProfileActivity.this.email.getText().toString().trim();
                String firstname = ProfileActivity.this.firstname.getText().toString().trim();
                String lastname = ProfileActivity.this.lastname.getText().toString().trim();
                String dateofbirth = ProfileActivity.this.dateofbirth.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



                if (TextUtils.isEmpty(firstname)){
                    ProfileActivity.this.firstname.setError("First name is required");
                    return;
                }

                if (TextUtils.isEmpty(lastname)){
                    ProfileActivity.this.lastname.setError("last name is required");
                    return;
                }

                if (TextUtils.isEmpty(dateofbirth)){
                    ProfileActivity.this.dateofbirth.setError("Date of birth is required");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    ProfileActivity.this.email.setError("A valid email is required");
                    return;
                }
                if (!email.matches(emailPattern))
                {
                    ProfileActivity.this.email.setError("Invalid email address");
                    return;
                }





                FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();

                users.updateEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });


                User user = new User(firstname.toString().trim(),lastname.toString().trim(),dateofbirth.toString().trim(),email.toString().trim());
                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ProfileActivity.this, "user has been updated successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(ProfileActivity.this, "Update failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    });
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        dateofbirth.setText(sdf.format(myCalendar.getTime()));
    }
}