package com.veve.typeone.activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;

import com.veve.typeone.AppDatabase;
import com.veve.typeone.DaoAccess;
import com.veve.typeone.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DiaryActivityTest {

    protected static final String DATABASE_NAME = "db";
    private DaoAccess daoAccess;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        boolean databasePresent = (dbFile != null && dbFile.exists());
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        daoAccess = appDatabase.daoAccess();
        daoAccess.deleteDiaryRecords();
        Cursor cursor = daoAccess.fetchRawThreeColRecords();
        Log.d("DB1", "returned cursor of size " + cursor.getCount());
    }

    @After
    public void closeDb() throws IOException {
        appDatabase.close();
    }


    @Rule
    public ActivityTestRule<DiaryActivity> mActivityTestRule = new ActivityTestRule<>(DiaryActivity.class);

    @Test
    public void diaryActivityTest() {
        onView(allOf(withId(R.id.diaryRecords), hasMinimumChildCount(1)))
                .check(doesNotExist());
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.save)).perform(click());
        onView(allOf(withId(R.id.diaryRecords), hasMinimumChildCount(1))).check(matches(isDisplayed()));
    }

}