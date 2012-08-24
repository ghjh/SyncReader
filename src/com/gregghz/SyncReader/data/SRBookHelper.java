package com.gregghz.SyncReader.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SRBookHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;

	public SRBookHelper(Context context) {
		super(context, "srbooks", null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE books" +
				"(id INTEGER NOT NULL," +
				"title VARCHAR(255)," +
				"path VARCHAR(255)," +
				"PRIMARY KEY (id)," +
				"UNIQUE (path))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				
	}
	
	public Error addBook(SRBook book) {
		ContentValues values = new ContentValues(2);
		values.put("title", book.getTitle());
		values.put("path", book.path());
		
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert("books", null, values);
		if (id == -1) {
			return new Error();
		}
		return null;
	}
	
	public List<SRBook> getBooks() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query("books", new String[]{"id", "path", "title"},
				null, null, null, null, "title ASC");
		List<SRBook> books = new ArrayList<SRBook>();
		
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			books.add(new SRBook(c.getString(1)));
			c.moveToNext();
		}
		
		c.close();
		return books;
	}

	public class Error {
		public Error() {
			
		}
	}

}
