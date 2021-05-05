package com.kefi.aziz.androidds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RegistrationActivity extends AppCompatActivity {

    private EditText email, password,firstname,lastname,password2,dateOfBirth;
    private Button btnsignup;
    private TextView text;
    private FirebaseAuth mAuth;
    final Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        dateOfBirth = (EditText) findViewById(R.id.dateofBirth);
        email =(EditText) findViewById(R.id.RegistrationEmail);
        password =(EditText) findViewById(R.id.password);
        password2 =(EditText) findViewById(R.id.password2);
        btnsignup =(Button) findViewById(R.id.RegistrationButton);
        text = (TextView) findViewById(R.id.RegistrationPageQuestion);

        dateOfBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = RegistrationActivity.this.email.getText().toString().trim();
                String password = RegistrationActivity.this.password.getText().toString().trim();
                String password2 = RegistrationActivity.this.password2.getText().toString().trim();
                String firstname = RegistrationActivity.this.firstname.getText().toString().trim();
                String lastname = RegistrationActivity.this.lastname.getText().toString().trim();
                String dateofbirth = RegistrationActivity.this.dateOfBirth.getText().toString().trim();



                if (TextUtils.isEmpty(firstname)){
                    RegistrationActivity.this.firstname.setError("First name is required");
                    return;
                }

                if (TextUtils.isEmpty(lastname)){
                    RegistrationActivity.this.lastname.setError("last name is required");
                    return;
                }

                if (TextUtils.isEmpty(dateofbirth)){
                    RegistrationActivity.this.dateOfBirth.setError("Date of birth is required");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    RegistrationActivity.this.email.setError("A valid email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    RegistrationActivity.this.password.setError("A valid password is required");
                    return;

                }
                if (TextUtils.isEmpty(password2)) {
                    RegistrationActivity.this.password2.setError("repeat your password");
                    return;
                }
                if (!password.equals(password2)) {
                    RegistrationActivity.this.password2.setError("Password Not matching");
                    return;
                }else{
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();


                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                User user= new User(firstname,lastname,dateofbirth,email);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = database.getReference("User");
                                databaseReference.child(mAuth.getUid()).setValue(user);
                                        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();

                            } else{
                                String error = task.getException().toString();
                                Toast.makeText(RegistrationActivity.this, "Registration failed" + error, Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
                }

            }
        });
    }
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
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        dateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
