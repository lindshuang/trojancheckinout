package cs310.trojancheckinout;

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
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class CheckInTest {

    private FirebaseFirestore db;

    @Before
    public void setUpDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    @Test
    public void timeDataCorrectIfCheckedOut() {
        //checks to see that if a user is checked out, then the time out and time elapsed fields are filled with data
        DocumentReference docIdRef = db.collection("users").document("nutakki@usc.edu");
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean status = document.getBoolean("checked_in");
                    List<String> histories = (List<String>) document.get("histories");
                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");
                    }
                    else {
                        Log.d("Document", "Document exists! Status is " + status);
                        if(status == false && (histories != null && histories.size() > 0)) {
                            String history = histories.get(histories.size()-1);
                            DocumentReference infoIdRef = db.collection("history").document(history);
                            infoIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        String timeOutDate = document.getString("timeOutDate");
                                        String timeOutTime = document.getString("timeOutTime");
                                        String totalTime = document.getString("totalTime");
                                        if (!document.exists()) {
                                            Log.d("document", "Document does not exist!");
                                        }
                                        else {
                                            assertNotEquals(timeOutDate, "");
                                            assertNotEquals(timeOutTime, "");
                                            assertNotEquals(totalTime, "");
                                        }
                                    } else {
                                        Log.d("document", "Failed with: ", task.getException());
                                    }
                                }
                            });

                        }
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
    }

    @Test
    public void timeDataCorrectIfCheckedIn() {
        //checks to see that if a user is checked in, then the time out and time elapsed fields are empty
        String email = "shaniaja@usc.edu";
        DocumentReference docIdRef = db.collection("users").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean status = document.getBoolean("checked_in");
                    List<String> histories = (List<String>) document.get("histories");
                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");
                    }
                    else {
                        Log.d("Document", "Document exists! Status is " + status);
                        if(status == true && (histories != null && histories.size() > 0)) {
                            String history = histories.get(histories.size()-1);
                            DocumentReference infoIdRef = db.collection("history").document(history);
                            infoIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        String timeInDate = document.getString("timeInDate");
                                        String timeInTime = document.getString("timeInTime");
                                        String timeOutDate = document.getString("timeOutDate");
                                        String timeOutTime = document.getString("timeOutTime");
                                        String totalTime = document.getString("totalTime");
                                        if (!document.exists()) {
                                            Log.d("document", "Document does not exist!");
                                        }
                                        else {
                                            assertNotEquals(timeInDate, "");
                                            assertNotEquals(timeInTime, "");
                                            assertEquals(timeOutDate, "");
                                            assertEquals(timeOutTime, "");
                                            assertEquals(totalTime, "");
                                        }
                                    } else {
                                        Log.d("document", "Failed with: ", task.getException());
                                    }
                                }
                            });

                        }
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
    }


    @Test
    public void checkCurrentCapacityIsAccurate() {
        //checks to see if the capacity of a building is equal to the number of current occupants
        String buildingKey = "SAL";
        DocumentReference docIdRef = db.collection("buildings").document(buildingKey);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String capacity = document.getString("currCapacity");
                    List<String> occ = (List<String>) document.get("occupants");

                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");
                    }
                    else {
                        Log.d("Document", "Document exists!");
                        double current_capacity = Double.parseDouble(capacity);
                        //count number of occupants but need to remove the instances where index is set to 0 when user is removed
                        int num_occupants = 0;
                        for(int i = 0; i < occ.size(); i++) {
                            if(occ.get(i).compareTo("0") == 0) {
                                num_occupants++;
                            }
                        }
                        assertEquals(num_occupants, current_capacity, 0.01);
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });

    }

    @Test
    public void checkCurrentIsLessThanMaxCapacity() {
        //makes sure that current capacity is less than max
        String buildingKey = "SAL";
        DocumentReference docIdRef = db.collection("buildings").document(buildingKey);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String currentCapacity = document.getString("currCapacity");
                    String maxCapacity = document.getString("maxCapacity");

                    if (!document.exists()) {
                        Log.d("document", "Document does not exist! in check in function");

                    }
                    else {
                        Log.d("Document", "Document exists! in check in function");
                        double current_capacity = Double.parseDouble(currentCapacity);
                        double max_capacity = Double.parseDouble(maxCapacity);

                        assertTrue(current_capacity <= max_capacity);
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
    }


    @Test
    public void checkHistoryIfCheckedIn() {
        //student should have at least one history if they are checked in
        String email = "shaniaja@usc.edu";
        DocumentReference docIdRef = db.collection("users").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean status = document.getBoolean("checked_in");
                    List<String> histories = (List<String>) document.get("histories");
                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");
                    } else {
                        Log.d("Document", "Document exists! Status is " + status);
                        if (status == true) {
                            assertTrue(histories != null && histories.size() > 0);
                        }
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
    }


    @Test
    public void checkCurrentQRCodeIfCheckedOut() {
        //if user is not checked in then current QR Code should be an empty string
        String email = "shaniaja@usc.edu";
        DocumentReference docIdRef = db.collection("users").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean status = document.getBoolean("checked_in");
                    String currentQR = document.getString("current_qr");
                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");
                    } else {
                        Log.d("Document", "Document exists! Status is " + status);
                        if (status == false) {
                            assertEquals("", currentQR);
                        }
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });

    }

    @Test
    public void checkCurrentQRCodeIfCheckedIn() {
        //if user is checked in then current QR Code should contain building key
        String email = "shaniaja@usc.edu";
        DocumentReference docIdRef = db.collection("users").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean status = document.getBoolean("checked_in");
                    String currentQR = document.getString("current_qr");
                    if (!document.exists()) {
                        Log.d("document", "Document does not exist!");
                    } else {
                        Log.d("Document", "Document exists! Status is " + status);
                        if (status == true) {
                            assertNotEquals("", currentQR);
                        }
                    }
                } else {
                    Log.d("document", "Failed with: ", task.getException());
                }
            }
        });
    }

}
