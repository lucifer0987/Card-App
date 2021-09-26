package com.example.splitmonk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitmonk.R;
import com.example.splitmonk.Utils.progressBarUtils;
import com.example.splitmonk.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_sign_in;
    TextView btn_sign_up;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Paper.init(this);
        initViews();

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = et_email.getText().toString().trim();
                String password_txt = et_password.getText().toString().trim();

                if(email_txt.equals("") || password_txt.equals("")){
                    Toast.makeText(LogInActivity.this, "Please Enter all details", Toast.LENGTH_SHORT).show();
                }else{
                    progressBarUtils.showProgress(LogInActivity.this, LogInActivity.this);
                    signIn(email_txt, password_txt);
                }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

    }

    private void signIn(String email_txt, String password_txt) {
        mAuth.signInWithEmailAndPassword(email_txt, password_txt)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        progressBarUtils.hideProgress();
                        Paper.book().write("isLoggedIn", true);
                        updateUI(user);
                    } else {
                        progressBarUtils.hideProgress();
                        Log.e("Login", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LogInActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(LogInActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void initViews() {
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }
}