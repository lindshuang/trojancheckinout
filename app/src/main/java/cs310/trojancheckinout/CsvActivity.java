package cs310.trojancheckinout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv);

        Button csvButton = findViewById(R.id.csv_button);
        csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                mediaIntent.setType("*/*"); // Set MIME type as per requirement
                startActivityForResult(mediaIntent,GET_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Doc", "browse file");

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri csvUri = data.getData();
            Log.d("Doc", "CSV URI= " + csvUri);

            try {
                //File csvFile = new File(csvUri.getPath());
                Log.d("Doc", "CSV path= " + csvUri.getPath());
                InputStream input = getContentResolver().openInputStream(csvUri);
                CSVReader reader = new CSVReader(new InputStreamReader(input));

                //Parse CSV
                List<List<String>> records = new ArrayList<List<String>>();
                String[] values = null;
                while ((values = reader.readNext()) != null) {
                    Log.d("Doc", "record: " + Arrays.asList(values));
                    records.add(Arrays.asList(values));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Log.d("Doc", "BAD ERROR");
        }
    }

}