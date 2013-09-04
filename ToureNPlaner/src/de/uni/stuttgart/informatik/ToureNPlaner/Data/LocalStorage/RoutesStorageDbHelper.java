/*
 * Copyright 2013 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.TBTResult;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage.RoutesStorageContract.RoutesEntry.*;

public class RoutesStorageDbHelper extends SQLiteOpenHelper {
	private static final String TEXT_TYPE = " TEXT";
	private static final String BINARY_TYPE = " BLOB";
	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + TABLE_NAME + " (" +
					_ID + " INTEGER PRIMARY KEY," +
					COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
					COLUMN_NAME_ROUTEDATA + BINARY_TYPE + COMMA_SEP +
					COLUMN_NAME_TBTROUTEDATA + BINARY_TYPE + COMMA_SEP +
					COLUMN_NAME_TIMESTAMP + INT_TYPE +
					" )";

	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "FeedReader.db";

	public RoutesStorageDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void storeRoute(Session session) {
		// Gets the data repository in write mode
		SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		//values.put(RoutesStorageContract.RoutesEntry.COLUMN_NAME_ENTRY_ID, result.);
		byte[] serializedroute = serializeObject(session.getResult());
		if (serializedroute == null) {
			//TODO: Log something
			return;
		}

		byte[] serializedtbtroute = null;
		if (session.gettbtResult() != null) {
			serializedtbtroute = serializeObject(session.gettbtResult());
		}
		if (serializedtbtroute == null) {
			//whatever
		}

		long timestamp = System.currentTimeMillis() / 1000;
		values.put(COLUMN_NAME_ROUTEDATA, serializedroute);
		values.put(COLUMN_NAME_TIMESTAMP, timestamp);
		values.put(COLUMN_NAME_TBTROUTEDATA, serializedtbtroute);

// Insert the new row, returning the primary key value of the new row
		long newRowId;
		if (db != null) {
			newRowId = db.insert(
					TABLE_NAME,
					"null",
					values);
			String successmsg = "stored result in db at time " + new Date(timestamp*1000).toString();
			Log.d("TP", successmsg);
			Toast.makeText(ToureNPlanerApplication.getContext(), successmsg, Toast.LENGTH_LONG).show();
		}
	}

	public List<StoredRoute> LoadRoutes() {
		SQLiteDatabase db = getReadableDatabase();
		// Define a projection that specifies which columns from the database
// you will actually use after this query.
		String[] projection = {
				_ID,
				COLUMN_NAME_ROUTEDATA,
				COLUMN_NAME_TBTROUTEDATA,
				COLUMN_NAME_TIMESTAMP
		};

// How you want the results sorted in the resulting Cursor
		String sortOrder =
				COLUMN_NAME_TIMESTAMP + " DESC";

		Cursor c = db.query(
				TABLE_NAME,  // The table to query
				projection,                               // The columns to return
				null,                                // The columns for the WHERE clause
				null,                            // The values for the WHERE clause
				null,                                     // don't group the rows
				null,                                     // don't filter by row groups
				sortOrder                                 // The sort order
		);

		List<StoredRoute> list = new LinkedList<StoredRoute>();
		long timestamp;
		c.moveToFirst();
		while (!c.isAfterLast()) {
			//long itemId = c.getLong(c.getColumnIndexOrThrow(_ID));
			timestamp = c.getLong(c.getColumnIndexOrThrow(COLUMN_NAME_TIMESTAMP));
			byte[] serializedRoute = c.getBlob(c.getColumnIndexOrThrow(COLUMN_NAME_ROUTEDATA));
			byte[] serializedtbtRoute = c.getBlob(c.getColumnIndexOrThrow(COLUMN_NAME_TBTROUTEDATA));
			Result result = (Result) deserializeObject(serializedRoute);
			TBTResult tbtresult = (TBTResult) deserializeObject(serializedtbtRoute);
			if (result == null) {
				Log.d("TP", "Tried to load invalid result from database, continue");
				continue;
			}
			StoredRoute route = new StoredRoute();
			route.timestamp = timestamp;
			route.result = result;
			route.tbtresult = tbtresult;
			Log.d("TP", "Loaded route with timestamp " + route.timestamp + " and " + route.getNumnodes() + " nodes");
			list.add(route);
			c.moveToNext();
		}
		return list;
	}

	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();
			byte[] buf = bos.toByteArray();
			return buf;
		} catch(IOException e) {
			return null;
		}
	}

	public static Object deserializeObject(byte[] b) {
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
			Object object = in.readObject();
			in.close();
			return object;
		} catch(ClassNotFoundException e) {
			return null;
		} catch(IOException e) {
			return null;
		}
	}
}
