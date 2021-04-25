package cs310.trojancheckinout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Timer;
import java.util.TimerTask;

import cs310.trojancheckinout.models.Building;

public class ShowOccupantsActivity extends AppCompatActivity {

    private static final String TAG = "ShowOccupantsActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OccupantsAdapter occupantsAdapter;
    private OccupantsAdapter.RecyclerViewClickListener listener;
    private ArrayList<String> occ = new ArrayList<String>();
    private ArrayList<String> occEmails = new ArrayList<String>();
    private ArrayList<String> occNames = new ArrayList<String>();
    public Building b;
    public String bName;
    public Handler mHandler;
    //public Runnable refresh;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_occupants);




        Log.d("debugging onclick", "beginning on onCreate");

        //got building name
        Bundle bundle = getIntent().getExtras();
        bName = bundle.getString("building name");
        Log.d("debugging onclick", "bName: " + bName);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("buildings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("debugging onclick", "beginning of onComplete");
                        if (task.isSuccessful()) {
                            //going through documents in building collection
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("debugging onclick", "inside for loop " + document.getId() + " => " + document.getData());
                                //if building name matches name of onclick building
                                String buildName = (String) document.get("buildingName");
                                Log.d("debugging onclick", "buildName" + buildName);
                                if (buildName.compareTo(bName) == 0) {
                                    Log.d("debugging onclick", "building found");
                                    //b is assigned to that building object
                                    b = document.toObject(Building.class);
                                    break;
                                }
                            }
//
                        } else {
                            Log.d("debugging onclick", "identifyBuilding: couldn't get building documents: ", task.getException());
                        }
                        //grabbing occupants list for that building
                        occ = (ArrayList<String>) b.getOccupants();
                        Log.d("debugging onclick", "getOccupantNames: after calling getOccupants");

                        //store emails of occupants currently checked in
                        Log.d("debugging onclick", "getOccupantNames: before getting occEmails");
                        for(int j = 0; j < occ.size(); j++){
                            String everyEmail = occ.get(j);
                            if(everyEmail.compareTo("0") < 0 || everyEmail.compareTo("0") > 0){
                                Log.d("debugging onclick", "looking at email# " + j + " which is " + occ.get(j));
                                Log.d("debugging onclick", "adding " + j + " " + occ.get(j) + " to occEmails" );
                                occEmails.add(occ.get(j));
                            }
                        }

                        Log.d("debugging onclick", "getOccupantNames: after getting occEmails");

                        Log.d("debugging onclick", "before sending intent");
//                        occEmails.removeAll(occEmails);

                        db.collection("users")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Log.d("tag", "in the on complete");
                                        if (task.isSuccessful()) {
                                            Log.d("tag", "task was successful");
                                            for(int y=0;y<occEmails.size();y++){
                                                Log.d("tag", "in the first for loop");
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("tag", "in the document for loop");
                                                    if(document.getId().compareTo(occEmails.get(y)) == 0) {
                                                        Log.d("tag", "found matching document to email");
                                                        String l_f_name = document.getString("lastName")+", "+document.getString("firstName");
                                                        Log.d("tag", l_f_name);
                                                        occNames.add(l_f_name);
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
                });
        //new
        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,5000);

    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            ShowOccupantsActivity.this.mHandler.postDelayed(m_Runnable, 15000);
            Intent intent = new Intent(ShowOccupantsActivity.this, ShowOccupantsActivity.class);
            intent.putExtra("building name", bName);
            startActivity(intent);
        }

    };//runnable

    private void setAdapter() {
        setOnClickListener();
        recyclerView = (RecyclerView)findViewById(R.id.OccupantsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        occupantsAdapter = new OccupantsAdapter(occNames, this, listener);
        recyclerView.setAdapter(occupantsAdapter);
    }


    private void setOnClickListener() {
        listener = new OccupantsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("Source", "Occupants");
                intent.putExtra("email", occEmails.get(position));
                Log.d("DEBUG", "occupant's email: " + occEmails.get(position));
                // put extra?
                startActivity(intent);

            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }


}