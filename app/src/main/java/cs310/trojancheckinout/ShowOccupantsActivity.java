package cs310.trojancheckinout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowOccupantsActivity extends AppCompatActivity {

    private static final String TAG = "ShowOccupantsActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OccupantsAdapter occupantsAdapter;
    private OccupantsAdapter.RecyclerViewClickListener listener;
    private ArrayList<String> emails;
    private ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_occupants);

        Bundle bundle = getIntent().getExtras();
        emails = bundle.getStringArrayList("occEmails");
        String email;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(emails == null){
            Log.d("debugging onclick", "n is null");
            emails.add("None at this time");
        }

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("tag", "in the on complete");
                        if (task.isSuccessful()) {
                            Log.d("tag", "task was successful");
                            for(int y=0;y<emails.size();y++){
                                Log.d("tag", "in the first for loop");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("tag", "in the document for loop");
                                    if(document.getId().compareTo(emails.get(y)) == 0) {
                                        Log.d("tag", "found matching document to email");
                                        String l_f_name = document.getString("lastName")+", "+document.getString("firstName");
                                        Log.d("tag", l_f_name);
                                        names.add(l_f_name);
                                        Log.d("tag","name added");
                                    }
                                    Log.d("tag", document.getId());
                                }

                            }
                            setAdapter();
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void setAdapter() {
        setOnClickListener();
        recyclerView = (RecyclerView)findViewById(R.id.OccupantsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        occupantsAdapter = new OccupantsAdapter(names, this, listener);
        recyclerView.setAdapter(occupantsAdapter);
    }

    private void setOnClickListener() {
        listener = new OccupantsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("Source", "Occupants");
                intent.putExtra("email", emails.get(position));
                Log.d("DEBUG", "occupant's email: " + emails.get(position));
                // put extra?
                startActivity(intent);

            }
        };
    }

}