package com.sourabhkarkal.quandoo;

import android.app.Application;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.sourabhkarkal.quandoo.activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * {@link Rule @Rule} to launch your activity under test.
 *  This is a replacement for {@link ActivityInstrumentationTestCase2}.
 *  setUp is done in {@link Before @Before} method
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void searchCustomer_MainActivity() {
        onView(withId(R.id.edtSearchCustomer))
                .perform(typeText("Gandhi"), closeSoftKeyboard());
    }

    @Test
    public void clickCustomer_MainActivity() {

        //first on recyclerview for customer
        onView(withId(R.id.rvCustomerList))
                .perform(click());

        // on click on customer table
        onView(withId(R.id.rvCustomerTableList))
                .perform(click());

        //confirm table
        onView(withId(R.id.btnConfirm ))
                .perform(click());

    }


    protected Fragment waitForFragment(int timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() <= endTime) {

            onView(withId(R.id.rvCustomerTableList))
                    .perform(click());
        }
        return null;
    }

}