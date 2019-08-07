package com.veve.typeone.activities;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.net.Uri;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


public class CursorTest {

    @Test
    public void testUnits() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        //Uri dbUri = Uri.parse("content://user_dictionary/words");
        Uri dbUri = Uri.parse("content://db1/SugarLevel");
        Cursor cursor = context.getContentResolver().query(dbUri, null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.getColumnCount() > 0);


//                .query(
//                        UserDictionary.Words.CONTENT_URI,   // The content URI of the words table
//                        mProjection,                        // The columns to return for each row
//                        mSelectionClause                    // Selection criteria
//                        mSelectionArgs,                     // Selection criteria
//                        mSortOrder);                        // The sort order for the returned rows
    }

    //getApplicationContext().getContentResolver().query("content://user_dictionary/words", )
}

