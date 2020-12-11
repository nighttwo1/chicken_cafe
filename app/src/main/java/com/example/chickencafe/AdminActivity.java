package com.example.chickencafe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView rooms;
    private Button neworder_btn;
    private ImageView add_chat;

    private ArrayList<String> mRoomList;
    private ArrayAdapter<String> adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRoomList = new ArrayList<>();
        rooms = (ListView)findViewById(R.id.rooms);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mRoomList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        rooms.setAdapter(adapter);

        add_chat = (ImageView)findViewById(R.id.add_chat);
        add_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this, R.style.AlertDialogTheme);
                builder.setTitle("새로운 대화창을 만든다.");
                builder.setMessage("방 번호를 입력하여 새로운 대화창을 만드세요.");
                builder.setView(R.layout.dialog);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog f = (Dialog) dialog;
                                EditText input = (EditText) f.findViewById(R.id.addboxdialog);
                                String value = input.getText().toString();

                                if(!value.isEmpty() && isNumeric(value)) {
                                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                                    CreateRoom(value);
                                }else{
                                    Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 방 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
        /*
        neworder_btn = (Button)findViewById(R.id.neworder_btn);
        neworder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(getApplicationContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("새로운 대화창을 만든다.");
                builder.setMessage("방 번호를 입력하여 새로운 대화창을 만드세요.");
                builder.setView(edittext);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(!edittext.getText().toString().isEmpty() && isNumeric(edittext.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), edittext.getText().toString(), Toast.LENGTH_SHORT).show();
                                    CreateRoom(edittext.getText().toString());
                                }else{
                                    Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 방 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
         */


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        DatabaseReference databaseReference1 = firebaseDatabase.getReference("message");
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    String room = snapshot.getKey();
                    Log.d("AdminActivity_roomList", room);

                    mRoomList.add(room);
                    adapter.notifyDataSetChanged();
                }catch (DatabaseException e){
                    recreate();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AdminMessageActivity.class);
                intent.putExtra("room_number", mRoomList.get(position));
                startActivity(intent);
            }
        });
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void CreateRoom(String room_number){
        //databaseReference.child(android_id+"/message").push().setValue(new Message("0", msg));
        databaseReference.child("message/"+room_number).setValue("");
    }
}
