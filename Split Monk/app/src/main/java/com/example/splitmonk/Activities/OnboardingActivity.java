package com.example.splitmonk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.splitmonk.R;
import com.example.splitmonk.Utils.progressBarUtils;
import com.example.splitmonk.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class OnboardingActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    Button email, google;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Paper.init(this);
        InitViews();
        InitData();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(OnboardingActivity.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnboardingActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e("google login", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBarUtils.showProgress(OnboardingActivity.this, OnboardingActivity.this);
                            UpdateDB(user);
                        } else {
                            Log.e("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(OnboardingActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

                int cond = 0;
                for(int i = 0; i < userList.size(); i++){
                    if(userList.get(i).getUid().equals(user.getUid())){
                        if(Integer.parseInt(userList.get(i).getUpdated_profile()) == 1){
                            Paper.book().write("isLoggedIn", true);
                            cond = 1;
                            break;
                        }else{
                            break;
                        }
                    }
                }

                progressBarUtils.hideProgress();
                if(cond == 1){
                    updateUI(user);
                }else{
                    Intent in = new Intent(OnboardingActivity.this, EnterUpiActivity.class);
                    startActivity(in);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(OnboardingActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void InitData() {

    }

    private void InitViews() {
        google = findViewById(R.id.google_ob);
        email = findViewById(R.id.email_ob);
        mAuth = FirebaseAuth.getInstance();
    }
}