package com.kefi.aziz.androidds;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateToDo extends DialogFragment {

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    final Calendar myCalendar = Calendar.getInstance();

    private String key = "";
    private String task = " ";
    private String date= "";
    private String description = "";
    String aaa ="";
    public UpdateToDo() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(mUser.getUid());


    }


    public static UpdateToDo newInstance(String key, String task, String description, String dates) {
        UpdateToDo frag = new UpdateToDo();
        Bundle args = new Bundle();

        args.putString("date",dates);
        args.putString("key",key);
        args.putString("task",task);
        args.putString("description",description);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_data, container);
    }
    EditText mDate;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText mTask =(EditText) view.findViewById(R.id.mEditedTask);
        EditText mDescription =(EditText) view.findViewById(R.id.mEditedDescription);
         mDate =(EditText) view.findViewById(R.id.mEditedDate);
        DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {

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
        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), dates, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        key= getArguments().getString("key");


        task = getArguments().getString("task");
        mTask.setText(task.toString());
        mTask.setSelection(task.length());

        description = getArguments().getString("description");

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        date =  getArguments().getString("date");
        mDate.setText(date);
        mDate.setSelection(date.length());

        Button deleteBtn = view.findViewById(R.id.btnDelete);
        Button updateBtn = view.findViewById(R.id.btnUpdate);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mTask.getText().toString().trim();
                description = mDescription.getText().toString().trim();
                date = mDate.getText().toString().trim();

//                String date = DateFormat.getDateInstance().format(new Date());

                Model model = new Model(task, description, key, date);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Task has been updated successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(view.getContext(), "Update failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Task has been deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(view.getContext(), "Delete failed" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dismiss();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        mDate.setText(sdf.format(myCalendar.getTime()));
    }
}
