package com.kefi.aziz.androidds;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

public class AddToDo extends DialogFragment {

    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private FirebaseUser mUser;
    public AddToDo() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(mUser.getUid());

    }


    public static AddToDo newInstance(String title) {
        AddToDo frag = new AddToDo();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_file, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        final EditText task = view.findViewById(R.id.task);
        final EditText description = view.findViewById(R.id.description);
        final EditText date = view.findViewById(R.id.date);

        Button save = view.findViewById(R.id.saveBtn);
        Button cancel = view.findViewById(R.id.CancelBtn);


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field

        //if the user clicks cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //if the user clicks save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTask = task.getText().toString().trim();
                String mDescription = description.getText().toString().trim();
                String mdate = date.getText().toString().trim();
                String id = reference.push().getKey();//get the key for each data set

//                String date = DateFormat.getDateInstance().format(new Date());

                if (TextUtils.isEmpty(mTask)) {
                    task.setError("Task Required");
                    return;
                }
                if (TextUtils.isEmpty(mDescription)) {
                    description.setError("Description Required");
                    return;
                } else {


                    //use the Model class to pack up the data
                    Model model = new Model(mTask, mDescription, id, mdate);
                    //update the data to Firebase
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(getContext(), "Failed: " + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

    }
    }
