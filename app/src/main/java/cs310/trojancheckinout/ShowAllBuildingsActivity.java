package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

import cs310.trojancheckinout.R;
import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.User;

public class ShowAllBuildingsActivity extends AppCompatActivity {

    private RecyclerView mFireStoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    List<Building> allBuildingNames = new ArrayList<Building>();

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
                buildingViewHolder.building_name.setText(building.getBuildingName());
                buildingViewHolder.curr_capacity_value.setText(building.getCurrCapacity());
                buildingViewHolder.max_capacity_value.setTextColor(Color.rgb(227, 106, 106));
//                int curr_capacity = Integer.valueOf(building.getCurrCapacity());
//                int max_capacity = Integer.valueOf(building.getMaxCapacity());
//                int remaining = max_capacity - curr_capacity;
                buildingViewHolder.max_capacity_value.setText(building.getMaxCapacity());
                buildingViewHolder.curr_capacity_value.setTextColor(Color.rgb(77, 152, 161));
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

        public BuildingViewHolder(@NonNull View itemView) {
            super(itemView);
            building_name = itemView.findViewById(R.id.building_name);
            curr_capacity_value = itemView.findViewById(R.id.curr_capacity_value);
            max_capacity_value = itemView.findViewById(R.id.max_capacity_value);
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

    public void refreshButton(View view) {
        Intent intent = new Intent(ShowAllBuildingsActivity.this, ShowAllBuildingsActivity.class);
        startActivity(intent);
    }



}
