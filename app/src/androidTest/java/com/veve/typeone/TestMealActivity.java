package com.veve.typeone;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.veve.typeone.activities.MealActivity;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMealActivity {

    @Rule
    public ActivityTestRule<MealActivity> rule = new ActivityTestRule<>(MealActivity.class);

    @Before
    public void Before() {
    }

    @Test
    public void TestNew() {
        onView(withId(R.id.addIngredient)).perform(click());
        onView(withId(R.id.weightInput)).check(matches(withText("0")));
        //onData(allOf(withParent(withId(R.id.productSelection)), isSelected(), isDisplayed())).onChildView(isSelected()).check(matches(withText("Sugar")));
        //onData(withId(R.id.productSelection)).onChildView(isSelected()).check(matches(withText("Sugar")));
        //onData(withId(R.id.productSelection)).onChildView(withText("Sugar")).check(matches(withText("Sugar")));
        //onData(withId(R.id.productSelection)).atPosition(0).check(matches(withText("Sugar")));
        onData(allOf(withParent(withId(R.id.productSelection)), isSelected(), isDisplayed())).atPosition(0).check(matches(withText("Sugar")));
    }

    static class SelectedItemMatcher extends BaseMatcher {

        @Override
        public boolean matches(Object item) {
            return false;
        }

        @Override
        public void describeTo(Description description) {

        }

    }

}