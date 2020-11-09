package com.SamsungProject.SamsungProject.ui.chats;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.SamsungProject.SamsungProject.Database;
import com.SamsungProject.SamsungProject.PlayerParameters;
import com.SamsungProject.SamsungProject.R;
import com.SamsungProject.SamsungProject.TournamentParameters;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatFragment extends Fragment implements View.OnClickListener {
    EditText editText;
    TextView textView;

    private String user_name,room_name;

    DatabaseReference reference;
    String temp_key;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        Button button = view.findViewById(R.id.button2);
        button.setOnClickListener(this);
        super.onCreate(savedInstanceState);
        editText = view.findViewById(R.id.editText2);
        textView = view.findViewById(R.id.textView);
        if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
        {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user_name = ChatRoomFragment.dataHashMap.get("user_name").toString();
        room_name = ChatRoomFragment.dataHashMap.get("room_name").toString();
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        reference = FirebaseDatabase.getInstance(app).getReference().child(room_name);
        getActivity().setTitle(" Room - "+room_name);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return view;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            getActivity().finish();

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:

                send(v);

                break;
        }
    }
    public void send(View v)
    {
        Map<String,Object> map = new HashMap<String,Object>();
        temp_key = reference.push().getKey();
        reference.updateChildren(map);

        DatabaseReference child_ref = reference.child(temp_key);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name",user_name);
        map2.put("msg", editText.getText().toString());
        child_ref.updateChildren(map2).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        editText.setText("");




    }
    public void append_chat(DataSnapshot ss)
    {
        String chat_msg,chat_username;
        Iterator i = ss.getChildren().iterator();
        while(i.hasNext())
        {
            chat_msg = ((DataSnapshot)i.next()).getValue().toString();
            chat_username = ((DataSnapshot)i.next()).getValue().toString();
            textView.append(chat_username + ": " +chat_msg + " \n");
        }
    }




}
