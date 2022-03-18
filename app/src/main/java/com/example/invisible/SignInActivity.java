package com.example.invisible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import NotActivityPackage.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    public static User CURRENT_USER_DATA;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }
    // [END on_start_check_user]

    public void signIn(View view) {
        // [START sign_in_with_email]
        EditText Email = findViewById(R.id.si_editTextEmail);
        EditText Password = findViewById(R.id.si_editTextPassword);
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (email.equals("")||password.equals("")) {
            Toast.makeText(getApplicationContext(),"Missing requirements.",Toast.LENGTH_SHORT).show();
        }
        else {

            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                progressBar.setVisibility(View.INVISIBLE);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser firebaseUser) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Users").orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    CURRENT_USER_DATA = snapshot.getValue(User.class);
                    startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
        startActivity(intent);
    }

}