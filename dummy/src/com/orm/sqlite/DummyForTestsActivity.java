package com.orm.sqlite;

import com.orm.sqlite.example.ExampleDataAccess;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;

public class DummyForTestsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_for_tests);
		new ExampleDataAccess(new SQLiteOpenHelper(this, "dummy", null, 1) {

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				db.execSQL(ExampleDataAccess.DATABASE_CREATE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dummy_for_tests, menu);
		return true;
	}

}
