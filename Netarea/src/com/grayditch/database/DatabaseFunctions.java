package com.grayditch.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grayditch.entity.MarksPerSubject;

public class DatabaseFunctions {
	private SQLiteDatabase db;
	private DatabaseHelper dbh;

	public DatabaseFunctions(Context context) {
		super();
		dbh = new DatabaseHelper(context, "NetareaDB", null, 1);
		db = dbh.getWritableDatabase();
	}

	public void putMark(String subject, int mark) {
		// REPLACE INTO table SET id = 42, foo = 'bar';
		db.execSQL("REPLACE INTO Netarea (subject, marks) " + "VALUES (\""
				+ subject + "\", " + mark + ")");
	}

	public List<MarksPerSubject> getListofInt() {
		Cursor c = db.rawQuery("SELECT * FROM Netarea ORDER BY subject", null);

		List<MarksPerSubject> list = new ArrayList<MarksPerSubject>();
		while (c.moveToNext()) {
			list.add(new MarksPerSubject(c.getString(0), c.getInt(1)));
		}
		return list;
	}

	public void disconnectDB() {
		db.close();
	}

	public void createDB(){
		dbh.CreateDB(db);
	}
}
