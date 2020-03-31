package com.MaelStream.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UPD_ClientTest {

    // Incorrect Inputs for Address and Port Test
    private static final String emptyAddress = "";
    private static final String emptyPort = "";

    private static final String incorrectAddress = "192.168.1.70";
    private static final String incorrectPort = "4424";

    private static final String formatAddress = "192168174";
    private static final String formatPort = "wasde";

    // Acceptable Inputs for Address and Port Test
    private static final String correctAddress = "192.168.1.74";
    private static final String correctPort = "4618";

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Desc:
     * Acceptance Testing to the ensure the app is working as intended
     *
     * Bugs:
     *  None atm
     */
    @Test
    public void streamWorking() throws Exception {
        // Test for validity of incorrect inputs
        onView(withId(R.id.address)).perform(typeText(emptyAddress), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(typeText(emptyPort), closeSoftKeyboard());
        onView(withId(R.id.connect)).perform(click());

        onView(withId(R.id.address)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.address)).perform(typeText(formatAddress), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(typeText(formatPort), closeSoftKeyboard());
        onView(withId(R.id.connect)).perform(click());

        onView(withId(R.id.address)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.address)).perform(typeText(incorrectAddress), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(typeText(incorrectPort), closeSoftKeyboard());
        onView(withId(R.id.connect)).perform(click());

        //Close screen
        onView(withId(R.id.btnReturn)).perform(click());

        onView(withId(R.id.address)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(clearText(), closeSoftKeyboard());

        // Type in valid IP address and port number
        onView(withId(R.id.address)).perform(typeText(correctAddress), closeSoftKeyboard());
        onView(withId(R.id.port)).perform(typeText(correctPort), closeSoftKeyboard());

        // Connect to the video
        onView(withId(R.id.connect)).perform(click());

        // Toggle the grey button
        onView(withId(R.id.toggleButton)).perform(click());
        onView(withId(R.id.toggleButton)).perform(click());
        onView(withId(R.id.toggleButton)).perform(click());

        //Close screen
        onView(withId(R.id.btnReturn)).perform(click());
    }
}
