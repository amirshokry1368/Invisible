package com.example.invisible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import NotActivityPackage.FirebaseDB;
import NotActivityPackage.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Button signUpBtn = findViewById(R.id.su_signUpBtn);
        signUpBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }

    public void toLogin(View view) {
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);
    }

    public void toHome(View view) {
        EditText Name = findViewById(R.id.su_editTextName);
        EditText Email = findViewById(R.id.su_editTextEmail);
        EditText Password = findViewById(R.id.su_editTextPassword);

        String name = Name.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(name.equals("")||email.equals("")||password.equals("")) {
            Toast.makeText(getApplicationContext(),"Missing requirements.",Toast.LENGTH_SHORT).show();
        }
        else {
            ProgressBar progressBar = findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.VISIBLE);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                progressBar.setVisibility(View.INVISIBLE);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
    private void updateUI(FirebaseUser firebaseUser) {
        EditText Name = findViewById(R.id.su_editTextName);
        EditText Email = findViewById(R.id.su_editTextEmail);
        FirebaseDB firebaseDB = new FirebaseDB(MainActivity.this);
        User user = new User(Name.getText().toString(), Email.getText().toString());
        firebaseDB.create("Users", user);
        //
        SignInActivity.CURRENT_USER_DATA = user;
        //
        /*DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Users").orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    SignInActivity.CURRENT_USER_DATA = snapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });*/
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}