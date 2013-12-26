package com.orm.sqlite;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DummyForTestsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_for_tests);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dummy_for_tests, menu);
		return true;
	}

}
