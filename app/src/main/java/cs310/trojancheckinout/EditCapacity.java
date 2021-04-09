package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EditCapacity extends AppCompatActivity {
    Button updateButton;
    EditText capacity_edit;
    TextView buildingNameTextView;

    private String capacity;
    private int currCapValue = Integer.parseInt(buildingData.getCurrCapacity());
    private String buildingCode = buildingData.getBuildingCode();
    private String buildingName = buildingData.getBuildingName();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_capacity);
        updateButton = (Button) findViewById(R.id.updateButton);
        capacity_edit = (EditText) findViewById(R.id.capacity_edit);
        buildingNameTextView = (TextView) findViewById(R.id.buildingName);
        buildingNameTextView.setText(buildingName + "(" + buildingCode + ")");

        capacity_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                capacity = capacity_edit.getText().toString();
                int newMaxCapValue = Integer.parseInt(capacity);
                if(capacity.length() <= 1){
                    capacity_edit.setError("Enter new capacity");
                }
                else if(newMaxCapValue <= currCapValue){
                    capacity_edit.setError("New max Capacity must be greater than the current capacity of this building.");
                }
                return true;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                capacity = capacity_edit.getText().toString();
                int newMaxCapValue = Integer.parseInt(capacity);

                Log.d("edit", "curr cap is " + currCapValue);
                Log.d("edit", "new max cap is " + capacity);
                Log.d("edit", "new max cap integer version is " + newMaxCapValue);

                if((capacity.length() >= 1) && (newMaxCapValue > currCapValue)) {
                    Log.d("edit", "about to update capacity ");
                    //update capacity in database
                    DocumentReference buildingRef = db.collection("buildings").document(buildingCode);
                    buildingRef
                            .update("maxCapacity", capacity)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("updating capacity", "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error updating capacity", "Error updating document", e);
                                }
                            });
//
                }
                else if(newMaxCapValue <= currCapValue){
                    capacity_edit.setError("New max Capacity must be greater than the current capacity of this building.");
                }
                else if(capacity.length() <= 1){
                    capacity_edit.setError("Enter new capacity");
                }
            }
        });

    }
}