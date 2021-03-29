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
public class sharedDataTest {
    @Test
    public void checkSetEmail() {
        String email = "shaniaja@usc.edu";
        sharedData.setCurr_email(email);
        assertEquals(email, sharedData.getCurr_email());
    }
}
