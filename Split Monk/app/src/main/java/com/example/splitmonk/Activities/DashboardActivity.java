package com.example.splitmonk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitmonk.adapter.GroupAdapter;
import com.example.splitmonk.R;
import com.example.splitmonk.Utils.progressBarUtils;
import com.example.splitmonk.models.Group;
import com.example.splitmonk.models.User;
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

public class DashboardActivity extends AppCompatActivity {

    Button join_a_group, add_a_group;
    ImageView profile_pic, logout;
    TextView profile_name, profile_upi, profile_monk_points, profile_completed, profile_groups;
    TextView no_group_txt;
    GroupAdapter adapter;
    RecyclerView group_rec;
    FirebaseAuth mAuth;
    FirebaseUser currUser;
    User current_user;
    ArrayList<Group> current_user_groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Paper.init(this);
        InitViews();
        boolean logged = Paper.book().read("isLoggedIn", false);
        if(!logged){
            Intent i = new Intent(DashboardActivity.this, OnboardingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else{
            currUser = mAuth.getCurrentUser();
            if(currUser == null){
                Paper.book().write("isLoggedIn", false);
                Toast.makeText(this, "Pleaes Log-in to continue", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardActivity.this, OnboardingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }else{
                progressBarUtils.showProgress(DashboardActivity.this, DashboardActivity.this);
                hideGroup();
                adapter = new GroupAdapter(this, DashboardActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                group_rec.setLayoutManager(layoutManager);
                group_rec.setAdapter(adapter);
                Toast.makeText(DashboardActivity.this, "Long click on a group to open its page", Toast.LENGTH_LONG).show();

                loadUserData();
            }
        }

        add_a_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CreateActivity.class));
            }
        });

        join_a_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, JoinActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("isLoggedIn", false);
                Paper.book().destroy();
                Toast.makeText(DashboardActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardActivity.this, OnboardingActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

    }

    private void loadUserData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("split-monk-users").child(currUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                current_user = snapshot.getValue(User.class);
                InitData();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void loadGroupData() {
        ArrayList<String> groups = current_user.getGroups();
        ArrayList<Task<DataSnapshot>> tasks = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            Log.e("this", "1");
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("split-monk-groups").child(groups.get(i));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    current_user_groups.add(snapshot.getValue(Group.class));
                    adapter.setData(current_user_groups);
                    showGroup();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        progressBarUtils.hideProgress();
    }

    private void hideGroup(){
        group_rec.setVisibility(View.GONE);
        no_group_txt.setVisibility(View.VISIBLE);
    }

    private void showGroup(){
        group_rec.setVisibility(View.VISIBLE);
        no_group_txt.setVisibility(View.GONE);
    }

    private void InitData() {
        setProfilePic();
        profile_name.setText(current_user.getName());
        profile_upi.setText(current_user.getUpi());
        profile_monk_points.setText(String.valueOf(current_user.getMonk_points()));
        profile_groups.setText(String.valueOf(current_user.getGroups().size()));
        loadGroupData();
    }

    private void setProfilePic() {
        int x = current_user.getName().length() % 3;
        if(x == 0){
            profile_pic.setImageResource(R.drawable.avtar_1);
        }else if(x == 1){
            profile_pic.setImageResource(R.drawable.avtar_2);
        }else if(x == 2){
            profile_pic.setImageResource(R.drawable.avtart_3);
        }
    }

    private void InitViews() {
        mAuth = FirebaseAuth.getInstance();
        add_a_group = findViewById(R.id.add_a_group);
        join_a_group = findViewById(R.id.join_a_group);
        no_group_txt = findViewById(R.id.no_group_txt);
        group_rec = findViewById(R.id. group_rec);
        profile_pic = findViewById(R.id.profile_pic);
        profile_name = findViewById(R.id.profile_name);
        profile_upi = findViewById(R.id.profile_upi);
        profile_monk_points = findViewById(R.id.profile_monk_points);
        profile_completed = findViewById(R.id.profile_completed);
        profile_groups = findViewById(R.id.profile_groups);
        logout = findViewById(R.id.btn_logout);
    }
}