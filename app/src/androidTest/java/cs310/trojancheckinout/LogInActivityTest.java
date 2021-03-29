package cs310.trojancheckinout;


import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs310.trojancheckinout.models.Building;
import cs310.trojancheckinout.models.User;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;

import android.content.Context;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class LogInActivityTest {

    private String email = "ashna33@usc.edu";
    private String password = "tester";
    private FirebaseFirestore db;

    @Test
    public void checkLogInFunction() {
        db = FirebaseFirestore.getInstance();
        //checks to see that if a user is checked out, then the time out and time elapsed fields are filled with data
        DocumentReference docIdRef = db.collection("users").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assertEquals(password, document.getString("password"));
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });

    }
}