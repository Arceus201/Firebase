package com.example.realtimedatabase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //    private EditText ed;
    private TextView tvget;
    private Button bt, btget, btdelete, btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, Boolean> map = new HashMap<>();
        map.put("key1", true);
        map.put("key2", false);


//        ed = findViewById(R.id.ed_data);
        bt = findViewById(R.id.bt_pushdata);
        tvget = findViewById(R.id.tv_getdata);
        btget = findViewById(R.id.bt_getdata);
        btdelete = findViewById(R.id.bt_deletedata);
        btUpdate = findViewById(R.id.bt_updatedata);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
            }
        });

        btget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readDatabase();
            }
        });
        btdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteData();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateData();
            }
        });
    }


    private void onClickPushData() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");

        User user = new User(1, "hung nguyen", new Job(1, "job1"));
        myRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(), "Push data succes", Toast.LENGTH_LONG).show();
            }
        });


//        DatabaseReference myRef = database.getReference("message");

//        myRef.setValue(ed.getText().toString().trim(), new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(getApplicationContext(), "Push data succes", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        DatabaseReference myRef2 = database.getReference("test");
//        myRef2.setValue("Hung nguyen");
//
//        DatabaseReference myRef3 = database.getReference("check");
//        myRef2.setValue(true);
//
//        DatabaseReference myRef4 = database.getReference("project1/test");
//        myRef2.setValue("test project 1");
    }

    private void readDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");

//        DatabaseReference myRef = database.getReference("message");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//
//                tvget.setText(value);

                User user = dataSnapshot.getValue(User.class);
                tvget.setText(user.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void onClickDeleteData() {

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("test");
//        myRef.removeValue(new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(getApplicationContext(), "Delete data succes", Toast.LENGTH_LONG).show();
//            }
//        });

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Ban co chac chan muon xoa")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("user_infor/name");

                        myRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getApplicationContext(), "Delete data succes", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void onClickUpdateData() {
//        C1: update ca  1 object
//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("user_infor");
//
//        User user = new User(2,"Test",new Job(2,"job2"));
//        myRef.setValue(user, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
//            }
//        });

//        C2: update 1 thuoc tinh
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("user_infor/name");
//            myRef.setValue("Test2", new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                    Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
//                }
//            });
//            myRef.child("job").child("name").setValue("hello");

        // Cach 3:
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("user_infor/name");
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("id", 1);
//        map.put("name", "abc");
//        map.put("job/name","job 3");
//        myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                    Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
//                }
//            });

    }
}