package cs310.trojancheckinout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private String currEmail = "";
    private boolean isManager = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Class<? extends Activity> activityClass;
        Bundle bundle = getIntent().getExtras();
        // currEmail = bundle.getString("email");
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if(bundle == null) {
            activityClass = LogInActivity.class;
            Intent newActivity = new Intent(MainActivity.this, activityClass);
            startActivityForResult(newActivity, 0);
        }
        else{
            //currEmail = bundle.getString("email");
            currEmail = sharedData.getCurr_email();
            if(currEmail.equals("")){
                activityClass = LogInActivity.class;
                Intent newActivity = new Intent(MainActivity.this, activityClass);
                startActivityForResult(newActivity, 0);
            }
            callDatabase(navView);
        }

        Log.d("Manager", "IsManager: " + isManager);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    void callDatabase(final BottomNavigationView navView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("users").document(currEmail);

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    //User(String firstName, String lastName, String email, String password, boolean checked_in, String occupation, String studentID, String profilePicture
                    DocumentSnapshot document = task.getResult();
                    String occupation = document.getString("occupation");
                    if (occupation.equals("Manager")) {
                        isManager = true;
                        hideMenu(navView, isManager);
                    }else{
                        hideMenu(navView, isManager);
                    }
                }
                Log.d("Manager", "IsManager2: " + isManager);
            }
        });
    }

    void hideMenu(BottomNavigationView navView, boolean isManagerInput){
        Log.d("Manager", "IsManager3: " + isManagerInput);
        if (!isManagerInput) {
            Menu nav_Menu = navView.getMenu();
            nav_Menu.findItem(R.id.navigation_notifications).setVisible(false);
        }else{
            Menu nav_Menu = navView.getMenu();
            nav_Menu.findItem(R.id.navigation_notifications).setVisible(true);
        }
    }

    private boolean userIsLoggedOn(){
        return true;
    }
}