package cs310.trojancheckinout;

import android.view.KeyEvent;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.action.ViewActions.typeText;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class InvalidLoginTest {

    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void nonUSCEmail(){

        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("linds@gmail.com"));
        Espresso.onView(withId(R.id.email_address_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.email_address_edit)).check(matches(hasErrorText("Enter USC Email Address")));
        //Espresso.onView(withId(R.id.email_address_edit)).check(matches(withText("Lalala")));
    }

    @Test
    public void invalidUSCEmailValidPassword(){

        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("lindsay@usc.edu")); //does not exist
        Espresso.onView(withId(R.id.email_address_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.password_edit)).perform(typeText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.password_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
//        Espresso.onView(withId(R.id.error_text)).check(matches(withText("Email Address not found")));
        Espresso.onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.error_text)).check(matches(isDisplayed()));
    }

    @Test
    public void validUSCEmailInvalidPassword(){

        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("linds@usc.edu")); //correct
        Espresso.onView(withId(R.id.email_address_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.password_edit)).perform(typeText("321"), ViewActions.closeSoftKeyboard()); //wrong pass
        Espresso.onView(withId(R.id.password_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        //Espresso.onView(withId(R.id.error_text)).check(matches(withText("Password does not match")));
        Espresso.onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.error_text)).check(matches(isDisplayed()));
    }



}

