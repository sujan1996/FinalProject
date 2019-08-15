package com.example.projectfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    Button buttonApos, buttonAneg, buttonBpos, buttonBneg, buttonABpos, buttonABneg, buttonOpos, buttonOneg;
    TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        buttonApos = findViewById(R.id.buttonApos);
        buttonAneg = findViewById(R.id.buttonAneg);
        buttonBpos = findViewById(R.id.buttonBpos);
        buttonBneg = findViewById(R.id.buttonBneg);
        buttonABpos = findViewById(R.id.buttonABpos);
        buttonABneg = findViewById(R.id.buttonABneg);
        buttonOpos = findViewById(R.id.buttonOpos);
        buttonOneg = findViewById(R.id.buttonOneg);


        buttonApos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, AposActivity.class);
                startActivity(i);
            }
        });
        buttonAneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, AnegActivity.class);
                startActivity(i);
            }
        });
        buttonBpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, BposActivity.class);
                startActivity(i);
            }
        });
        buttonBneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, BnegActivity.class);
                startActivity(i);
            }
        });
        buttonABpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, ABposActivity.class);
                startActivity(i);
            }
        });
        buttonABneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, ABnegActivity.class);
                startActivity(i);
            }
        });buttonOpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, OposActivity.class);
                startActivity(i);
            }
        });buttonOneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FirstActivity.this, OnegActivity.class);
                startActivity(i);
            }
        });

//        tvSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(FirstActivity.this,LoginActivity.class);
//                startActivity(i);
//            }
//        });

    }
}
