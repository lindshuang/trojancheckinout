package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import cs310.trojancheckinout.models.Building;

public class DeleteBuilding extends AppCompatActivity {

    String acronym = "";
    double current_cap;
    String cap="";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_building);

        Button deleteBuilding = (Button) findViewById(R.id.deleteButton);
        EditText acroEdit = (EditText) findViewById(R.id.buildingAcro_edit);
        acroEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                acronym = acroEdit.getText().toString();
                if(acronym.length() <= 1){
                    acroEdit.setError("Enter building acronym");
                }
                return true;
            }
        });

        deleteBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acronym = acroEdit.getText().toString();
                if(acronym.length() <= 1){
                    acroEdit.setError("Enter building acronym");
                }

                Log.d("delete_building", acronym);
                if(acronym.length() > 1) {
                    //check to see if capacity is greater than 0
                    DocumentReference docIdRef = db.collection("buildings").document(acronym);
                    Log.d("test", "test1");
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.d("test", "test2");
                            if (task.isSuccessful()) {
                                Log.d("test", "test3");
                                DocumentSnapshot document = task.getResult();
                                cap = document.getString("currCapacity");
                                Log.d("cap", "cap is " + cap);
                                if (!document.exists()) {
                                    Log.d("document", "Document does not exist! in delete");
                                    acroEdit.setError("Building does not exist");
                                }
                                else {
                                    Log.d("Document", "Document exists! delete building curr");
                                    Log.d("test", "test4");
                                    current_cap = Double.parseDouble(cap);
                                    Log.d("double", "double version of curr cap is " + current_cap);
                                    if(current_cap == 0.0 || current_cap == 0) {
                                        //delete from database
                                        Log.d("test", "test5");
                                        db.collection("buildings").document(acronym)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("delete_building", "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("delete_building", "Error deleting document", e);
                                                    }
                                                });

                                        //navigate back to show all buildings page
                                         Intent intent = new Intent(DeleteBuilding.this, ShowAllBuildingsActivity.class);
                                         startActivityForResult(intent, 0);
                                    }
                                    else {
                                        acroEdit.setError("Building currently has occupants.");
                                    }
                                }
                                Log.d("in check out", "cap+ "+current_cap);

                            } else {
                                Log.d("document", "Failed with: ", task.getException());
                            }
                        }
                    });
                   // current_cap = "1.0";
                    Log.d("current cap", "cap is "+ current_cap);

                }

            }
        });



    }
}