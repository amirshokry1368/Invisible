package com.example.invisible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.Cache;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Stack;

import NotActivityPackage.FirebaseDB;
import NotActivityPackage.Message;
import NotActivityPackage.MyAdapter;
import NotActivityPackage.Post;

public class HomeActivity extends AppCompatActivity {

    public static int SIZE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
        super.onResumeFragments();
        HomeMainMethod();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.homeFragment) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.sendFragment) {
                selectedFragment = new SendFragment();
            } else if (item.getItemId() == R.id.settingsFragment) {
                selectedFragment = new SettingsFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
            if (item.getItemId() == R.id.homeFragment) {
                //Home Fragment

                super.onResumeFragments();
                HomeMainMethod();

                //End of home fragment
            }
            return true;
        });

    }

    public void HomeMainMethod() {
        RecyclerView recyclerView = findViewById(R.id.home_messagesList);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Posts");
        ArrayList<Message> list = new ArrayList<>();
        Stack<Message> stk = new Stack<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                SIZE = 0;
                stk.clear();
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (post.getEmail().equals(SignInActivity.CURRENT_USER_DATA.getEmail())) {
                        Message message = new Message(post.getMessage());
                        stk.push(message);
                        SIZE+=1;
                    }
                }
                for (int i = 0; i < SIZE; i++) {
                    list.add(stk.pop());
                }
                if (list.isEmpty())
                {
                    list.add(new Message("\"You Don't have any messages.\""));
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void SendAMessage(View view) {
        EditText Email = findViewById(R.id.send_EmailEditText);
        EditText Message = findViewById(R.id.send_MessageEditText);

        String email = Email.getText().toString().trim();
        String message = Message.getText().toString();

        if (email.equals("")||message.equals("")) {
            Toast.makeText(getApplicationContext(),"Missing requirements.",Toast.LENGTH_SHORT).show();
        }
        else {

            FirebaseDB firebaseDB = new FirebaseDB(HomeActivity.this);

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    boolean[] isExist = new boolean[1];
                    isExist[0] = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        isExist[0] = true;
                        Post post = new Post(email, message);
                        firebaseDB.create("Posts", post);
                        Toast.makeText(getApplicationContext(), "Sent successfully", Toast.LENGTH_SHORT).show();
                        Email.setText("");
                        Message.setText("");
                        break;
                    }
                    if (!isExist[0]) {
                        Toast.makeText(getApplicationContext(), "User not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }

    public void logOut (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this,SignInActivity.class));
        SignInActivity.CURRENT_USER_DATA = null;
        finish();
    }

}