package cs310.trojancheckinout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import cs310.trojancheckinout.models.History;
import cs310.trojancheckinout.models.TempUser;
import cs310.trojancheckinout.models.User;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter adapter;
    private ArrayList<History> histories = new ArrayList<History>();
    private ArrayList<String> historyList;
    private FirebaseFirestore db;
    private RecyclerView list;
    private Bundle bundle;
    private String currEmail;
    private DocumentSnapshot userDoc;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        list = findViewById(R.id.recycler_view_histories);
        db = FirebaseFirestore.getInstance();
        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(histories);
        list.setAdapter(adapter);

        //get user email
        bundle = getIntent().getExtras();
        currEmail = bundle.getString("email");

        //start realtime update
        Button ok_id = (Button) findViewById(R.id.okButtonHistory);
        //Button pop_up_profile = findViewById(R.id.pop_up_kickProfile);
        LinearLayout pop_up_profile = (LinearLayout) findViewById(R.id.pop_up_kickHistory);
        final DocumentReference docRef = db.collection("users").document(sharedData.getCurr_email());
        docRef.addSnapshotListener((snapshot, e) -> {
            Log.d("Doc", "inside listener");
            if (e != null) {
                Log.d("Doc", "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d("Doc", "Current data: " + snapshot.getData());
                TempUser tempUser = snapshot.toObject(TempUser.class);
                if (tempUser.isKicked_out()){
                    Log.d("kick", "inside temp user kicked out");
                    pop_up_profile.setVisibility(View.VISIBLE);
                    ok_id.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("kick", "kick out Clicked notification");
                            pop_up_profile.setVisibility(View.INVISIBLE);
                            //set kicked out to false
                            DocumentReference checkOutRef = db.collection("users").document(sharedData.getCurr_email());
                            checkOutRef
                                    .update("kicked_out", false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("updating kicked out", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("error updating time out date", "Error updating document", e);
                                        }
                                    });
                        }
                    });
                }else{
                    pop_up_profile.setVisibility(View.INVISIBLE);
                }
            } else {
                Log.d("Doc", "Current data: null");
            }
        }); //end kick out


        DocumentReference docIdRef = db.collection("users").document(currEmail);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    db.collection("history").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String entry = document.getId();
                                    String entry_temp = entry.substring(1);
                                    Log.d("document", entry_temp);
                                    if (currEmail.equals(entry_temp)){
                                        historyList.add(entry);
                                    }
                                }

                                getPersonData(list, db, historyList, histories);
                                adapter.notifyDataSetChanged();
                                System.out.println("HISTORY: " + histories.toString());
                                Log.d("size after inserting all", "SIZE: " + histories.size());
                                Log.d("document", "history list: " + historyList.toString());

                            } else {
                                Log.d("document", "Failed with: ", task.getException());
                            }
                        }
                    });
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
    }

    void getPersonData(RecyclerView list, FirebaseFirestore db, ArrayList<String> historyList, final ArrayList<History> histories){

        for (int i = 0; i < historyList.size(); i++){
            DocumentReference docIdRef = db.collection("history").document(historyList.get(i));
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        String buildingName = document.getString("buildingName");
                        double time_elapsed = document.getDouble("totalTime");
                        String last_timeIn = document.getString("timeInTime");
                        String last_timeOut = document.getString("timeOutTime");
                        String last_DateIn = document.getString("timeInDate");
                        String last_DateOut = document.getString("timeOutDate");
                        Log.d("time in", "last_timeIn is " + last_timeIn);
                        Log.d("time out", "last_timeOut is " + last_timeOut);
                        Log.d("time in", "last_timeIn is " + last_DateIn);
                        Log.d("time out", "last_timeOut is " + last_DateOut);

                        History new_history = new History(last_DateIn, last_timeIn, last_DateOut, last_timeOut, time_elapsed, buildingName);
                        histories.add(new_history);
                        Log.d("size", "SIZE: " + histories.size());
                        adapter.notifyDataSetChanged();


                        if (!document.exists()) {
                            Log.d("document", "Document does not exist!");
                        } else {
                            Log.d("Document", "Document exists!");
                        }
                    } else {
                        Log.d("document", "Failed with: ", task.getException());
                    }
                }
            });
        }//end
        adapter.notifyDataSetChanged();
    }
} //end class