package com.example.realtimedatabase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    //    private EditText ed;
    private EditText ed_id,ed_name;
    private TextView tvget;
    private Button bt, btget, btdelete, btUpdate;

    private RecyclerView recyclerView;
    private UserAAdapter adapter;

    private List<User1> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, Boolean> map = new HashMap<>();
        map.put("key1", true);
        map.put("key2", false);


//        ed = findViewById(R.id.ed_data);
        bt = findViewById(R.id.bt_pushdata);

        ed_id =findViewById(R.id.ed_id);
        ed_name = findViewById(R.id.ed_name);
        recyclerView = findViewById(R.id.recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mList = new ArrayList<>();
        adapter = new UserAAdapter(mList, new UserAAdapter.Inclick() {
            @Override
            public void onClickUpdateItem(User1 user) {
                OpenDialogUpdateItem(user);
            }

            @Override
            public void onClickDeleteItem(User1 user) {
                onClickDeleteDataItem(user);
            }
        });

        recyclerView.setAdapter(adapter);
        getListUser("4");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = Integer.parseInt(ed_id.getText().toString().trim());
//                String name = ed_name.getText().toString();
//                onClickAddUser1(new User1(id,name));


                ClickAddAllUser();
            }
        });
        
//        tvget = findViewById(R.id.tv_getdata);
//        btget = findViewById(R.id.bt_getdata);
//        btdelete = findViewById(R.id.bt_deletedata);
//        btUpdate = findViewById(R.id.bt_updatedata);


//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickPushData();
//            }
//        });
//
//        btget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                readDatabase();
//            }
//        });
//        btdelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickDeleteData();
//            }
//        });
//
//        btUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickUpdateData();
//            }
//        });
    }

    private void onClickDeleteDataItem(User1 user) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Ban co chac chan muon xoa")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("list_user");

                        myRef.child(String.valueOf(user.getId())).removeValue(new DatabaseReference.CompletionListener() {
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

    private void OpenDialogUpdateItem(User1 user) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layot_custom);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText ed_update = dialog.findViewById(R.id.ed_item);
        Button btCancel = dialog.findViewById(R.id.bt_item_cancel);
        Button btUpdate = dialog.findViewById(R.id.bt_update_item);


        ed_update.setText(user.getName());

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("list_user");

                user.setName(ed_name.getText().toString().trim());

                myRef.child(String.valueOf(user.getId())).updateChildren(user.toMap());
            }
        });
        dialog.show();
    }

    private void getListUser(String key){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_user");




//        C1
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(mList != null) mList.clear();
//
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                    User1 user1 = dataSnapshot.getValue(User1.class);
//                    mList.add(user1);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //Sort theo 1 truong tang dan
        Query query = myRef.orderByChild("rate");

        //Sort theo key
        Query query1 = myRef.orderByKey();

        //sort theo value trong 1 list String
        Query query3 = myRef.orderByValue();

        //filter

        //lay 2 thang dau cua ds
        Query query4 = myRef.limitToFirst(2);
        //lay 2 thang cuoi cua ds
        Query query5 = myRef.limitToLast(2);


        //tra ve ds >= dieu kien nao do
        Query query6 = myRef.orderByChild("rate").startAt(4);

        //tra ve ds > dieu kien nao do
        Query query7 = myRef.orderByChild("rate").startAfter(4);

        //tra ve ds <= dieu kien nao do
        Query query8 = myRef.orderByChild("rate").endAt(4);

        //tra ve ds < dieu kien nao do
        Query query9 = myRef.orderByChild("rate").endBefore(4);

        //tra ve ds = dieu kien nao do
        Query query10 = myRef.orderByChild("rate").equalTo(4);




        //get
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User1 user = snapshot.getValue(User1.class);
                if(user!=null){
                    if(user.getName().contains(key)){
                        mList.add(user);
                    }
                    //Sort theo 1 truong giam dan
//                    mList.add(0,user);

//                    mList.add(user);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User1 user = snapshot.getValue(User1.class);
                if(user ==  null || mList == null || mList.isEmpty()) return;
                for(int i=0;i<mList.size();i++){
                    if(user.getId() == mList.get(i).getId()){
                        mList.set(i,user);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User1 user = snapshot.getValue(User1.class);
                if(user ==  null || mList == null || mList.isEmpty()) return;
                for(int i=0;i<mList.size();i++){
                    if(user.getId() == mList.get(i).getId()){
                        mList.remove(mList.get(i));
                        break;
                    }
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void onClickAddUser1(User1 user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_user");

        String path = String.valueOf(user.getId());
        myRef.child(path).setValue(user,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(), "Push data succes", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ClickAddAllUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_user");

        List<User1> list = new ArrayList<>();
        list.add(new User1(1,"name1"));
        list.add(new User1(2,"name2"));
        list.add(new User1(3,"name3"));

        myRef.setValue(list);

    }


//    private void onClickPushData() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("my_map");
//
//        Map<String,Boolean> map = new HashMap<>();
//        map.put("1", true);
//        map.put("2", false);
//        map.put("3", true);
//        map.put("4", false);
//
//        myRef.setValue(map, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(getApplicationContext(), "Push data succes", Toast.LENGTH_LONG).show();
//            }
//        });
//



        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("user_infor");
//
//        User user = new User(1, "hung nguyen", new Job(1, "job1"));
//        myRef.setValue(user, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(getApplicationContext(), "Push data succes", Toast.LENGTH_LONG).show();
//            }
//        });


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
//    }

//    private void readDatabase() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("user_infor");
//
////        DatabaseReference myRef = database.getReference("message");
//
//
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                //String
////                String value = dataSnapshot.getValue(String.class);
////                Log.d(TAG, "Value is: " + value);
////
////                tvget.setText(value);
//
//                //Object
////                User user = dataSnapshot.getValue(User.class);
////                tvget.setText(user.toString());
//
//                //Map
//                Map<String,Boolean> mapResult = new HashMap<>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    String key = dataSnapshot1.getKey();
//                    Boolean value = dataSnapshot1.getValue(Boolean.class);
//                    mapResult.put(key, value);
//                }
//                tvget.setText(mapResult.toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//    }

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

//    private void onClickUpdateData() {
////        C1: update ca  1 object
////        // Write a message to the database
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("user_infor");
////
////        User user = new User(2,"Test",new Job(2,"job2"));
////        myRef.setValue(user, new DatabaseReference.CompletionListener() {
////            @Override
////            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
////                Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
////            }
////        });
//
////        C2: update 1 thuoc tinh
////            FirebaseDatabase database = FirebaseDatabase.getInstance();
////            DatabaseReference myRef = database.getReference("user_infor/name");
////            myRef.setValue("Test2", new DatabaseReference.CompletionListener() {
////                @Override
////                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
////                    Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
////                }
////            });
////            myRef.child("job").child("name").setValue("hello");
//
//        // Cach 3:
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("user_infor/name");
////        Map<String, Object> map = new HashMap<>();
////
////        map.put("id", 1);
////        map.put("name", "abc");
////        map.put("job/name","job 3");
////        myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
////                @Override
////                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
////                    Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
////                }
////            });
//
//
//        //Update Map
//        //C1: ghi de map
//
////        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("my_map");
////
////        Map<String,Boolean> map = new HashMap<>();
////        map.put("1", false);
////        map.put("2", false);
////        map.put("3", true);
////        map.put("4", false);
////        myRef.setValue(map, new DatabaseReference.CompletionListener() {
////            @Override
////            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
////                Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
////            }
////        });
//
//        //c2: update 1 item trong map
//
////            FirebaseDatabase database = FirebaseDatabase.getInstance();
////            DatabaseReference myRef = database.getReference("my_map/1");
////            myRef.setValue(true, new DatabaseReference.CompletionListener() {
////                @Override
////                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
////                    Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
////                }
////            });
//
//            //c3:
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("my_app");
//            Map<String, Object> map = new HashMap<>();
//
//            map.put("1",false);
//            map.put("2",true);
//
//            myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                        Toast.makeText(getApplicationContext(), "Update data succes", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//    }
}