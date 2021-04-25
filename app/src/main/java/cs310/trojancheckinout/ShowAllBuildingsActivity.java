package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import cs310.trojancheckinout.R;
import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.User;

public class ShowAllBuildingsActivity extends AppCompatActivity {

    private RecyclerView mFireStoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_buildings);

        Button makeQRButton = (Button) findViewById(R.id.building_make_QR);

        makeQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MakeQRIntent = new Intent(ShowAllBuildingsActivity.this, generateQRActivity.class);
                startActivity(MakeQRIntent);
            }
        });


        mFireStoreList = findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("buildings");

        FirestoreRecyclerOptions<Building> buildings = new FirestoreRecyclerOptions.Builder<Building>().setQuery(query, Building.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Building, BuildingViewHolder>(buildings) {
            @NonNull
            @Override
            public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new BuildingViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BuildingViewHolder buildingViewHolder, int i, @NonNull Building building) {
                String email;

                buildingViewHolder.building_name.setText(building.getBuildingName());
                buildingViewHolder.curr_capacity_value.setText(building.getCurrCapacity());
                buildingViewHolder.max_capacity_value.setTextColor(Color.rgb(227, 106, 106));
                buildingViewHolder.max_capacity_value.setText(building.getMaxCapacity());
                buildingViewHolder.curr_capacity_value.setTextColor(Color.rgb(77, 152, 161));

                buildingViewHolder.updateCapacity_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildingData.setBuildingName(building.getBuildingName());
                        buildingData.setBuildingCode(building.getQRcode());
                        buildingData.setCurrCapacity(building.getCurrCapacity());
                        Intent intent = new Intent(ShowAllBuildingsActivity.this, EditCapacity.class);
                        startActivityForResult(intent, 0);
                    }
                });


            }
        };

        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList.setAdapter(adapter);

    }

    private class BuildingViewHolder extends RecyclerView.ViewHolder {
        private TextView building_name;
        private TextView curr_capacity_value;
        private TextView max_capacity_value;
        private Button updateCapacity_button;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> occupantNames;
        ArrayList<String> occ = new ArrayList<String>();
        public Building b;
        ArrayList<String> occEmails = new ArrayList<String>();
        ArrayList<String> occNames = new ArrayList<String>();
        String email;


        public BuildingViewHolder(@NonNull View itemView) {
            super(itemView);

            building_name = itemView.findViewById(R.id.building_name);
            curr_capacity_value = itemView.findViewById(R.id.curr_capacity_value);
            max_capacity_value = itemView.findViewById(R.id.max_capacity_value);
            updateCapacity_button = itemView.findViewById(R.id.updateCapacity_button);

            itemView.findViewById(R.id.occupants_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bName = building_name.getText().toString();
                    Log.d("debugging onclick", "building name:" + bName);
                    Log.d("debugging onclick", "onclick: before database call");

                    Intent intent = new Intent(ShowAllBuildingsActivity.this, ShowOccupantsActivity.class);
                    Log.d("debugging onclick", "after intent declaration");
                    intent.putExtra("building name", bName);
                    Log.d("debugging onclick", "after putting extra");
                    startActivity(intent);
                }

            });

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }



}