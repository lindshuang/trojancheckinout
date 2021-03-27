package cs310.trojancheckinout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import cs310.trojancheckinout.models.User;

public class NavActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentSnapshot userDoc;
    String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Button profileButton = (Button) findViewById(R.id.profile_button);
        Button showBuildingsButton = (Button) findViewById(R.id.button);

        DocumentReference docIdRef2 = db.collection("users").document(sharedData.getCurr_email());

        docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userDoc = task.getResult();
                if (task.isSuccessful()) {
                    role = userDoc.getString("occupation");
                    if(role.compareTo("Manager")==0) {
                        showBuildingsButton.setVisibility(View.VISIBLE);
                    } else {
                        showBuildingsButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivityIntent = new Intent(NavActivity.this, ProfileActivity.class);
                startActivityForResult(profileActivityIntent, 0);
            }
        });



        showBuildingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buildingActivityIntent = new Intent(NavActivity.this, ShowAllBuildingsActivity.class);
                startActivityForResult(buildingActivityIntent, 0);
            }
        });

        Button checkInButton = (Button) findViewById(R.id.check_button);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkActivityIntent = new Intent(NavActivity.this, CheckIn.class);
                startActivityForResult(checkActivityIntent, 0);
            }
        });
    }

}