//package cs310.trojancheckinout;
//
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.ViewInteraction;
//import androidx.test.filters.LargeTest;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.rule.GrantPermissionRule;
//import androidx.test.runner.AndroidJUnit4;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.hamcrest.core.IsInstanceOf;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.replaceText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withParent;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//import static org.hamcrest.Matchers.is;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class LogInCheckInValidQR2 {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Rule
//    public GrantPermissionRule mGrantPermissionRule =
//            GrantPermissionRule.grant(
//                    "android.permission.CAMERA");
//
//    @Test
//    public void logInCheckInValidQR2() {
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.email_address_edit),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.loginfields),
//                                        0),
//                                1),
//                        isDisplayed()));
//        appCompatEditText.perform(replaceText("gauritest@usc.edu"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.password_edit),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.loginfields),
//                                        1),
//                                1),
//                        isDisplayed()));
//        appCompatEditText2.perform(replaceText("gauri"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.loginButton), withText("Log In"),
//                        childAtPosition(
//                                allOf(withId(R.id.loginfields),
//                                        childAtPosition(
//                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                                                1)),
//                                2),
//                        isDisplayed()));
//        appCompatButton.perform(click());
//
//        ViewInteraction checkButton = onView(allOf(withId(R.id.check_button), withText("Check In or Check Out")));
//        checkButton.perform(click());
//        //ActivityTestRule<NavActivity> mActivityTestRule = new ActivityTestRule<>(NavActivity.class);
//        //Espresso.onView(withText("Check In or Check Out")).perform(click());
//
////        ViewInteraction appCompatButton2 = onView(
////                allOf(withText("Check In or Check Out"),
////                        childAtPosition(
////                                childAtPosition(
////                                        withId(R.id.nav_activity),
////                                        0),
////                                0),
////                        isDisplayed()));
////        appCompatButton2.perform(click());
//
//
//        ViewInteraction appCompatButton3 = onView(
//                allOf(withId(R.id.checkIn_button), withText("SCAN QR"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                                        1),
//                                5),
//                        isDisplayed()));
//        appCompatButton3.perform(click());
//
//        ViewInteraction appCompatButton4 = onView(
//                allOf(withId(R.id.confirmCheckButton), withText("Confirm"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.LinearLayout")),
//                                        1),
//                                0),
//                        isDisplayed()));
//        appCompatButton4.perform(click());
//
//        ViewInteraction appCompatButton5 = onView(
//                allOf(withId(R.id.refresh_button), withText("Refresh"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatButton5.perform(click());
//
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.status), withText("You are checked in."),
//                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class))),
//                        isDisplayed()));
//        textView.check(matches(withText("You are checked in.")));
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }
//}
