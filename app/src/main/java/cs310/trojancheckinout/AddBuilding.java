package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Bitmap;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;

import cs310.trojancheckinout.models.User;
import cs310.trojancheckinout.models.Building;
public class AddBuilding extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String buildingName;
    String acronym;
    String capacity;
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    public final static int WIDTH = 500;
    public final static String STR = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        Button addBuildingButton = (Button) findViewById(R.id.addButton);
        EditText buildingNameEdit = (EditText) findViewById(R.id.buildingName_edit);
        EditText acronymEdit = (EditText) findViewById(R.id.buildingAcronym_edit);
        EditText maxCapacityEdit = (EditText) findViewById(R.id.capacity_edit);

        buildingNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                buildingName = buildingNameEdit.getText().toString();
                if(buildingName.length() <= 1){
                    buildingNameEdit.setError("Enter building name");
                }
                return true;
            }
        });

        acronymEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                acronym = acronymEdit.getText().toString();
                if(acronym.length() <= 1){
                    acronymEdit.setError("Enter building acronym");
                }
                return true;
            }
        });

        maxCapacityEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                capacity = maxCapacityEdit.getText().toString();
                if(capacity.length() <= 1){
                    maxCapacityEdit.setError("Enter building capacity");
                }
                return true;
            }
        });

        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buildingName = buildingNameEdit.getText().toString();
                acronym = acronymEdit.getText().toString();
                capacity = maxCapacityEdit.getText().toString();

                if(buildingName.length() <= 1){
                    buildingNameEdit.setError("Enter building name");
                }
                if(acronym.length() <= 1){
                    acronymEdit.setError("Enter building acronym");
                }
                if(capacity.length() <= 1){
                    maxCapacityEdit.setError("Enter building capacity");
                }


                if(buildingName.length() > 1 && capacity.length() > 1  && acronym.length() > 1) {

                    //add to database
                    List<String> occupants = new ArrayList<String>();
                    Building building = new Building(buildingName, "0", capacity, occupants, acronym);
                    db.collection("buildings").document(acronym).set(building);

                    //navigate back to show all buildings page
                    Intent intent = new Intent(AddBuilding.this, ShowAllBuildingsActivity.class);
                    startActivityForResult(intent, 0);
                }

            }
        });

    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }
}