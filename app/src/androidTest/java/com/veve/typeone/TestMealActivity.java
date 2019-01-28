package com.veve.typeone;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.veve.typeone.activities.MealActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMealActivity {

    @Rule
    public ActivityTestRule<MealActivity> rule = new ActivityTestRule<>(MealActivity.class);

    @Before
    public void Before() {}

    @Test
    public void Test() {
        onView(withId(R.id.addIngredient)).perform(click());
    }

}
