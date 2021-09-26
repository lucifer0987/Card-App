package com.example.splitmonk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.splitmonk.R;
import com.example.splitmonk.Utils.progressBarUtils;
import com.example.splitmonk.models.User;
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

public class EnterUpiActivity extends AppCompatActivity {

    Button submit;
    EditText upi_et;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_upi);

        Paper.init(this);
        InitViews();
        InitData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upi_et.getText().toString().equals("")){
                    Toast.makeText(EnterUpiActivity.this, "Please Enter a valid UPI ID", Toast.LENGTH_LONG).show();
                }else{
                    UpdateDB(fbUser);
                }
            }
        });

    }

    private void UpdateDB(FirebaseUser user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for(DataSnapshot curr: snapshot.getChildren()){
                    User currUser = curr.getValue(User.class);
                    userList.add(currUser);
                }

                for(int i = 0; i < userList.size(); i++){
                    Log.e("login", "old user");
                    if(userList.get(i).getUid().equals(user.getUid())){
                        if(userList.get(i).getUpdated_profile().equals("0")){
                            showDialogAndUpdate(userList.get(i));
                        }
                    }
                }

                User curr = new User(user.getEmail(), user.getDisplayName(), user.getUid(), "0", "");
                Paper.book().write("isLoggedIn", true);
                showDialogAndUpdate(curr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showDialogAndUpdate(User curr_user) {
        progressBarUtils.hideProgress();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-users");

        curr_user.setUpi(upi_et.getText().toString());
        curr_user.setUpdated_profile("1");
        ref.child(curr_user.getUid()).setValue(curr_user);
        updateUI(mAuth.getCurrentUser());
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(EnterUpiActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void InitData() {

    }

    private void InitViews() {
        submit = findViewById(R.id.btn_submit_upi);
        upi_et = findViewById(R.id.et_upi_id);
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
    }
}