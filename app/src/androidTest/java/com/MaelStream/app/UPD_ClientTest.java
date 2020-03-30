package com.MaelStream.app;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UPD_ClientTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Desc:
     *  Simulates user inputs when testing the app
     *
     * Bugs:
     *  None atm
     */
    @Test
    public void streamWorking() throws Exception {
        // Type in IP address and port number in use
        onView(withId(R.id.address)).perform(typeText("192.168.1.74"), closeSoftKeyboard());
        onView(withId(R.id.address)).perform(typeText("10080"), closeSoftKeyboard());

        // Connect to the video
        onView(withId(R.id.connect)).perform(click());
    }
}
