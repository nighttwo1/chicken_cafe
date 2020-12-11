package com.example.chickencafe;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerActivity extends AppCompatActivity {
    private TextView deviceText;
    private ListView mMessageListview;

    private Adapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private static final String TAG = "FirebaseClient";

    private String room_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Intent intent = getIntent();
        room_number = intent.getStringExtra("room_number");

        deviceText = (TextView)findViewById(R.id.deviceText);
        deviceText.setText(deviceText.getText().toString()+"/"+room_number+"호실");

        mMessageListview = (ListView) findViewById(R.id.message_listview);
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
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("message/"+room_number+"/customer");
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
}
