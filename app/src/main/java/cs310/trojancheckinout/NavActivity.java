package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Button profileButton = (Button) findViewById(R.id.profile_button);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedData.setData("SAL");
                Intent profileActivityIntent = new Intent(NavActivity.this, ProfileActivity.class);
                startActivityForResult(profileActivityIntent, 0);
                //profileActivityIntent.putExtra("email", currEmail);
                //startActivity(profileActivityIntent);
                //((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        Button showBuildingsButton = (Button) findViewById(R.id.button);

        showBuildingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buildingActivityIntent = new Intent(NavActivity.this, ShowAllBuildingsActivity.class);
                startActivityForResult(buildingActivityIntent, 0);
                //((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        Button checkInButton = (Button) findViewById(R.id.check_button);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkActivityIntent = new Intent(NavActivity.this, CheckIn.class);
                startActivityForResult(checkActivityIntent, 0);
                //((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
    }

}