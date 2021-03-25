package cs310.trojancheckinout;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateSAccountPostLogInDirect {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createSAccountPostLogInDirect() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.createAccountButton), withText("Create Account"),
                        childAtPosition(
                                allOf(withId(R.id.loginfields),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.studentButton), withText("Student"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.firstname_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Gauri"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.lastname_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Madhok"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.student_id_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("123456789"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.email_address_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("gauritest@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.password_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        4),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("gauri"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.major_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        5),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("Business"), closeSoftKeyboard());


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.submitButton), withText("Sign Up As Student"),
                        childAtPosition(
                                allOf(withId(R.id.buttons),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                6),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.email_address_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.loginfields),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("gauritest@usc.edu"), closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.password_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.loginfields),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("gauri"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.loginButton), withText("Log In"),
                        childAtPosition(
                                allOf(withId(R.id.loginfields),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());

//        ViewInteraction button = onView(
//                allOf(withId(R.id.button), withText("CHECK IN OR CHECK OUT"),
//                        withParent(withParent(withId(R.id.nav_host_fragment))),
//                        isDisplayed()));
//        button.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.icon),
                        withParent(allOf(withId(R.id.navigation_dashboard), withContentDescription("Profile"),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
