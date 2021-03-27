package cs310.trojancheckinout;

    import androidx.test.espresso.Espresso;
    import androidx.test.espresso.matcher.ViewMatchers;
    import androidx.test.ext.junit.rules.ActivityScenarioRule;

    import org.junit.Rule;
    import org.junit.Test;

    import java.util.Map;

    import static androidx.test.espresso.action.ViewActions.click;
    import static androidx.test.espresso.action.ViewActions.typeText;
    import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
    import static androidx.test.espresso.assertion.ViewAssertions.matches;
    import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
    import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
    import static androidx.test.espresso.matcher.ViewMatchers.withId;
    import static androidx.test.espresso.matcher.ViewMatchers.withText;
    import androidx.test.espresso.contrib.RecyclerViewActions;
    import static org.hamcrest.Matchers.allOf;
    import static org.hamcrest.Matchers.endsWith;
    import static org.hamcrest.Matchers.hasEntry;
    import static org.hamcrest.Matchers.instanceOf;
    import static org.hamcrest.Matchers.is;
    import static org.hamcrest.Matchers.not;

public class StudentSignInNoShowBuildings {
    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void listGoesOverTheFold() {
        Espresso.onView(withId(R.id.email_address_edit)).perform(typeText("gauritest@usc.edu"));
        Espresso.onView(withId(R.id.password_edit)).perform(typeText("gauri"));
        Espresso.onView(withId(R.id.loginButton)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withText(endsWith("SHOW BUILDINGS"))).check(matches(not(isDisplayed())));

    }
}

