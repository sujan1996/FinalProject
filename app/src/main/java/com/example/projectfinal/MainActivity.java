package com.example.projectfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFireBaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireBaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(MainActivity.this, UserMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        emailId = findViewById(R.id.regEmail);
        password = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.regButton);
        tvSignIn = findViewById(R.id.tvsign);




        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()){
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                if (pwd.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                        mFireBaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"SignUp Unsuccessful, Try Again!!!",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    String userId = mFireBaseAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                    currentUserDb.setValue(true);
                                    startActivity(new Intent(MainActivity.this, UserMapActivity.class));

                                }
                            }
                        });


                    }

            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mFireBaseAuth.addAuthStateListener((firebaseAuthListener));
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mFireBaseAuth.removeAuthStateListener(firebaseAuthListener);
//    }
}
