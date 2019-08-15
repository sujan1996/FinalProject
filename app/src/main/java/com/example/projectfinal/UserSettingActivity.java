package com.example.projectfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserSettingActivity extends AppCompatActivity {

    private EditText mName, mPhone;
    private Button mBack, mConfirm;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId;
    private String name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        mName = (EditText) findViewById(R.id.userName);
        mPhone = (EditText) findViewById(R.id.userPhone);

        mBack = (Button) findViewById(R.id.back);
        mConfirm = (Button) findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        final String userClass = intent.getStringExtra("userClass");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userClass).child(userId);

        getUserInfo();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUsersInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });



    }
    private void getUserInfo(){
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name")!=null){
                        name  = map.get("name").toString();
                        mName.setText(name);
                    }
                    if (map.get("phone")!=null){
                        name  = map.get("phone").toString();
                        mPhone.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    };


    private void saveUsersInformation() {

        name = mName.getText().toString();
        phone = mPhone.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);

        mUserDatabase.updateChildren(userInfo);

        finish();
    }
}
