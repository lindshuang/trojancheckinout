package cs310.trojancheckinout;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

public class StudentSignUpMajor {
    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void listGoesOverTheFold() {

        Espresso.onView(withId(R.id.createAccountButton)).perform(click());
        Espresso.onView(withId(R.id.title)).check(matches(withText("Choose occupation")));
        Espresso.onView(withId(R.id.studentButton)).perform(click());
        Espresso.onView(withId(R.id.major)).check(matches(withText("Major: ")));
    }
}
