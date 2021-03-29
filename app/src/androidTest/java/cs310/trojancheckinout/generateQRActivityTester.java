package cs310.trojancheckinout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.WriterException;

import static org.junit.Assert.*;
import org.mockito.Mock;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Assert;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(AndroidJUnit4.class)
//@RunWith(MockitoJUnitRunner.class)
public class generateQRActivityTester {

    public static final Bitmap testbitmap = null;

//    @Mock
//    Context context;

    @Rule
    public ActivityTestRule<generateQRActivity> activityRule = new ActivityTestRule<>(generateQRActivity.class );

    private generateQRActivity qrActivity;

    @Before
    public void setUp() throws Exception {
        qrActivity = activityRule.getActivity();
    }

    @Test
    public void checkNotNull() throws WriterException {

        assertThat(qrActivity, notNullValue());

    }

    @Test
    public void checkBitmap() throws WriterException {
        Bitmap test = qrActivity.encodeAsBitmap("SAL");
        assertNotEquals(null, test);

    }

    @After
    public void tearDown() throws Exception {
        activityRule = null;
        qrActivity = null;
    }
}