package com.example.projectfinal.LoginPage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectfinal.MainActivity;
import com.example.projectfinal.OposActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.UserMapActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OpLogAct extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp ;
    FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_log);
        mFireBaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.regEmail);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.regButton);
        tvSignUp = findViewById(R.id.tvsignup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUser = mFireBaseAuth.getCurrentUser();

                if (mFireBaseUser !=null){
                    Intent i = new Intent(OpLogAct.this, UserMapActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(OpLogAct.this,"Please Log In",Toast.LENGTH_SHORT).show();
                }

            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
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
                    Toast.makeText(OpLogAct.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    mFireBaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(OpLogAct.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(OpLogAct.this,"Login error",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(OpLogAct.this, UserMapActivity.class);
                                intent.putExtra("bloodType","O+");
                                startActivity(intent);

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(OpLogAct.this,"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(OpLogAct.this, OposActivity.class);
                startActivity(intSignUp);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFireBaseAuth.addAuthStateListener(mAuthStateListener);
    }
}