package cs310.trojancheckinout;

import android.view.KeyEvent;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DuplicateEmail {

    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void checkEmailValid() {
        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.studentButton)).perform(click());
        Espresso.onView(withId(R.id.firstname_edit)).perform(typeText("Kira"));
        Espresso.onView(withId(R.id.lastname_edit)).perform(typeText("Pat"));
        Espresso.onView(withId(R.id.student_id_edit)).perform(typeText("1234567890"));
        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("kira@gmail.com"));
        Espresso.onView(withId(R.id.email_address_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.email_address_edit)).check(matches(hasErrorText("Enter USC Email Address")));

    }
    @Test
    public void checkID() {
        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.studentButton)).perform(click());
        Espresso.onView(withId(R.id.firstname_edit)).perform(typeText("Kira"));
        Espresso.onView(withId(R.id.lastname_edit)).perform(typeText("Pat"));
        Espresso.onView(withId(R.id.student_id_edit)).perform(typeText(" "));
        Espresso.onView(withId(R.id.student_id_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.student_id_edit)).check(matches(hasErrorText("Enter StudentID")));

    }

    @Test
    public void checkFirstName() {
        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.studentButton)).perform(click());
        Espresso.onView(withId(R.id.firstname_edit)).perform(typeText(" "));
        Espresso.onView(withId(R.id.firstname_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.firstname_edit)).check(matches(hasErrorText("Enter FirstName")));

    }

    @Test
    public void checkLastName() {
        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.studentButton)).perform(click());
        Espresso.onView(withId(R.id.firstname_edit)).perform(typeText("Kira"));
        Espresso.onView(withId(R.id.lastname_edit)).perform(typeText(" "));
        Espresso.onView(withId(R.id.lastname_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.lastname_edit)).check(matches(hasErrorText("Enter LastName")));

    }

    @Test
    public void checkPassword() {
        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.studentButton)).perform(click());
        Espresso.onView(withId(R.id.firstname_edit)).perform(typeText("Kira"));
        Espresso.onView(withId(R.id.lastname_edit)).perform(typeText("Pat"));
        Espresso.onView(withId(R.id.student_id_edit)).perform(typeText("1234567890"));
        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("kira@gmail.com"));
        Espresso.onView(withId(R.id.password_edit)).perform(typeText(" "));
        Espresso.onView(withId(R.id.password_edit)).perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Espresso.onView(withId(R.id.password_edit)).check(matches(hasErrorText("Enter Password")));

    }




}