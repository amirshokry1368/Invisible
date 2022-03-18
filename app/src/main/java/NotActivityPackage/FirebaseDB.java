package NotActivityPackage;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.invisible.HomeActivity;
import com.example.invisible.SignInActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class FirebaseDB {
    private DatabaseReference db;
    private FirebaseDatabase database;
    private final Activity activity;
    public User user;


    public FirebaseDB(Activity activity) {
        database = FirebaseDatabase.getInstance();
        db = database.getReference();
        this.activity = activity;
    }
    public FirebaseDB(Activity activity ,String path) {
        database = FirebaseDatabase.getInstance(path);
        db = database.getReference();
        this.activity = activity;
    }
    public void create(String TableName, Object object) {
        db.child(TableName).push().setValue(object);
    }

    public void find(String tableName, String email) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child(tableName).orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
