package com.veve.typeone.activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.veve.typeone.AppDatabase;
import com.veve.typeone.DaoAccess;
import com.veve.typeone.model.ThreeColumnRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestDatabase {

    private DaoAccess daoAccess;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        daoAccess = db.daoAccess();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        ThreeColumnRecord record = new ThreeColumnRecord();
        record.setTime(System.currentTimeMillis());
        daoAccess.insertThreeColumnRecord(record);
        Cursor cursor = daoAccess.fetchRawThreeColRecords();
        assert(cursor.getCount() > 0);
    }

}