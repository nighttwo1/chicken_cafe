package com.example.chickencafe;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMessageActivity extends AppCompatActivity {

    private TextView mConnectionStatus;
    private EditText mInputEditText;
    private TextView mDeviceText;
    private ListView mMessageListview;

    private Adapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private static final String TAG = "FirebaseClient";

    private String room_number;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message);

        Intent intent = getIntent();
        room_number = intent.getStringExtra("room_number");
        Log.d("roomnum_check", room_number);

        Button sendButton = (Button)findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String sendMessage = mInputEditText.getText().toString();
                if ( sendMessage.length() > 0 ) {
                    //sendMessage(sendMessage);
                    sendMessage(sendMessage);
                }
            }
        });
        mConnectionStatus = (TextView)findViewById(R.id.connection_status_textview);
        mInputEditText = (EditText)findViewById(R.id.input_string_edittext);
        mDeviceText = (TextView)findViewById(R.id.deviceText);
        mMessageListview = (ListView) findViewById(R.id.message_listview);

        mInputEditText.setTextIsSelectable(true);

        adapter = new Adapter();
        mMessageListview.setAdapter(adapter);

        mMessageListview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        // When message is added, it makes listview to scroll last message
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(adapter.getCount()!=0)
                    mMessageListview.setSelection(adapter.getCount()-1);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //TODO: 앱 처음 실행시, 이전의 대화내용 불러오기.
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("message/"+room_number+"/admin");
//        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    Message msg2 = snapshot.getValue(Message.class);
//                    adapter.addItem(msg2);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue(Message.class)!= null) {
//                    Message temp = snapshot.getValue(Message.class);
//                    Log.d("tempt2",snapshot.getValue().toString());
//                    Log.d("tempt", temp.getType()+" "+temp.getMsg());
//                    adapter.addItem(temp);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Message temp = snapshot.getValue(Message.class);
                    Log.d("tempt2",snapshot.getValue().toString());
                    Log.d("tempt", temp.getType()+" "+temp.getMsg());
                    adapter.addItem(temp);
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


    }

    void sendMessage(String msg){
        if(isNumeric(msg)) {
            //TODO: firebase 서버에 msg write
            WriteMessage(msg);
        }else{
            Toast.makeText(getApplicationContext(), "방번호(숫자만)를 입력해주세요", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void WriteMessage(String msg){
        //databaseReference.child(android_id+"/message").push().setValue(new Message("0", msg));
        databaseReference.child("message/"+room_number+"/admin").push().setValue(new Message("0", msg));
    }
}