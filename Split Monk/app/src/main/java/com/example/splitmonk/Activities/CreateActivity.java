package com.example.splitmonk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.splitmonk.R;
import com.example.splitmonk.Utils.progressBarUtils;
import com.example.splitmonk.models.Group;
import com.example.splitmonk.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.paperdb.Paper;

public class CreateActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currUser;
    EditText group_name, group_desc;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Paper.init(this);
        InitViews();
        InitData();

        currUser = mAuth.getCurrentUser();
        if(currUser == null){
            Paper.book().write("isLoggedIn", false);
            Toast.makeText(this, "Pleaes Log-in to continue", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(CreateActivity.this, OnboardingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group_name_text = group_name.getText().toString().trim();
                String group_desc_text = group_desc.getText().toString().trim();
                if(group_name_text.equals("") || group_desc_text.equals("")){
                    Toast.makeText(CreateActivity.this, "Please Enter all Details", Toast.LENGTH_LONG).show();
                }else if(group_desc_text.length() < 10){
                    Toast.makeText(CreateActivity.this, "Group descriptions should of minimum 10 characters", Toast.LENGTH_SHORT).show();
                }else if(group_name_text.length() < 4){
                    Toast.makeText(CreateActivity.this, "Group name should of minimum 4 characters", Toast.LENGTH_SHORT).show();
                }else{
                    progressBarUtils.showProgress(CreateActivity.this, CreateActivity.this);
                    String group_id_txt = group_name_text.subSequence(0, 4) + String.valueOf(group_desc_text.length() + group_name_text.length());
                    check_Group(group_name_text, group_desc_text, group_id_txt);
                }
            }
        });

    }

    private void check_Group(String group_name_text, String group_desc_text, String group_id_txt) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-groups").child(group_id_txt);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    progressBarUtils.hideProgress();
                    Toast.makeText(CreateActivity.this, "Please pick some other group name", Toast.LENGTH_LONG).show();
                }else{
                    Group curr = new Group(group_name_text, group_id_txt, group_desc_text, new ArrayList<>(), new ArrayList<>());
                    Toast.makeText(CreateActivity.this, "Group Name approved!", Toast.LENGTH_SHORT).show();
                    createGroup(curr);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBarUtils.hideProgress();
                Toast.makeText(CreateActivity.this, "Some Error occured, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createGroup(Group curr) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-groups");
        ArrayList<String> users = curr.getUsers();
        users.add(currUser.getUid());
        curr.setUsers(users);
        ref.child(curr.getGroup_id()).setValue(curr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(CreateActivity.this, "Group Created and user added to group!", Toast.LENGTH_SHORT).show();
                    create_UpdateUserDB(curr);
                }else{
                    progressBarUtils.hideProgress();
                    Toast.makeText(CreateActivity.this, "Some Error occured, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void create_UpdateUserDB(Group curr) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-users").child(currUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User current_user = snapshot.getValue(User.class);
                ArrayList<String> groups = current_user.getGroups();
                groups.add(curr.getGroup_id());
                assert current_user != null;
                current_user.setGroups(groups);

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("split-monk-users");
                ref.child(current_user.getUid()).setValue(current_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(CreateActivity.this, "User data updated!", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }else{
                            progressBarUtils.hideProgress();
                            Toast.makeText(CreateActivity.this, "Some Error occured, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBarUtils.hideProgress();
                Toast.makeText(CreateActivity.this, "Some Error occured, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        progressBarUtils.hideProgress();
        startActivity(new Intent(CreateActivity.this, DashboardActivity.class));
        finish();
    }

    private void searchGroup(String group_id_txt) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-groups").child(group_id_txt);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null){
                    progressBarUtils.hideProgress();
                    Toast.makeText(CreateActivity.this, "This Group doesn't exist. Please Enter a valid ID.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CreateActivity.this, "This Group exists", Toast.LENGTH_SHORT).show();
                    Group curr = snapshot.getValue(Group.class);
                    assert curr != null;
                    search_AddUserToGroup(group_id_txt, curr);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBarUtils.hideProgress();
                Toast.makeText(CreateActivity.this, "Some Error occured, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void search_AddUserToGroup(String group_id_txt, Group curr) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-groups");
        ArrayList<String> users = curr.getUsers();
        if(users.contains(currUser.getUid())){
            progressBarUtils.hideProgress();
            Toast.makeText(this, "User already part of that group!", Toast.LENGTH_SHORT).show();
            return;
        }
        users.add(currUser.getUid());
        curr.setUsers(users);
        ref.child(curr.getGroup_id()).setValue(curr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(CreateActivity.this, "User added to Group!", Toast.LENGTH_SHORT).show();
                    create_UpdateUserDB(curr);
                }else{
                    progressBarUtils.hideProgress();
                    Toast.makeText(CreateActivity.this, "Some Error occured, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InitData() {

    }

    private void InitViews() {
        mAuth = FirebaseAuth.getInstance();
        group_desc = findViewById(R.id.et_group_desc);
        group_name = findViewById(R.id.et_group_name);
        create = findViewById(R.id.btn_create_group);
    }

}