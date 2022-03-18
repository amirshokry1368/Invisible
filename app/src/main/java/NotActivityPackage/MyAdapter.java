package NotActivityPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invisible.HomeActivity;
import com.example.invisible.R;
import com.example.invisible.SignInActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Message> list;

    public MyAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position) {
        Message message = list.get(position);
        holder.message.setText(message.getMessage());
        holder.message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                databaseReference.orderByChild("email").equalTo(SignInActivity.CURRENT_USER_DATA.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            Post post = d.getValue(Post.class);
                            if (post.getMessage().equals(holder.message.getText().toString())) {
                                databaseReference.child(d.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        notifyItemRemoved(position);
                                        list.remove(position);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView message;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.home_MessageTextView);

        }
    }

}
