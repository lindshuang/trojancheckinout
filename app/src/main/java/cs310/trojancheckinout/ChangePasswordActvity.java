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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActvity extends AppCompatActivity {
    private String password2;
    private EditText passwordChange;
    private Button submitButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_actvity);
        passwordChange = (EditText) findViewById(R.id.password_change);
        submitButton = (Button) findViewById(R.id.changePasswordButton);

        db = FirebaseFirestore.getInstance();

        passwordChange.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                password2 = passwordChange.getText().toString();
                Log.d("password is","passwordblach " + password2);
                if(password2.length() <= 3){
                    passwordChange.setError("Enter Password");
                }
                return true;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password2 = passwordChange.getText().toString();
                db.collection("users").document(sharedData.getCurr_email())
                        .update("password", password2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Doc", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Doc", "Error writing document", e);
                            }
                        });
                Intent intent = new Intent(ChangePasswordActvity.this, NavActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


}