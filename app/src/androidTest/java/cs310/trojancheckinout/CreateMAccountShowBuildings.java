package cs310.trojancheckinout;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class CreateMAccountShowBuildings {
    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void listGoesOverTheFold() {
        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.managerButton)).perform(click());
        Espresso.onView(withId(R.id.firstname_edit)).perform(typeText("Nenad"));
        Espresso.onView(withId(R.id.lastname_edit)).perform(typeText("Medovicic"));
        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("nenadm@usc.edu"));
        Espresso.onView(withId(R.id.password_edit)).perform(typeText("123"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.submitButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("nenadm@usc.edu"));
        Espresso.onView(withId(R.id.password_edit)).perform(typeText("123"));
        Espresso.onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withText(endsWith("SHOW BUILDINGS"))).check(matches(isDisplayed()));

    }
}

