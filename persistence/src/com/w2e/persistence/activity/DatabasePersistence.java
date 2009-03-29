package com.w2e.persistence.activity;

import com.w2e.persistence.GuestBookDBOpenHelper;
import com.w2e.persistence.R;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class DatabasePersistence extends ListActivity {

	private Button saveButton;
	private EditText lastNameText;
	private EditText firstNameText;
	private DBPersistenceCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db);
		setUpViews();
		reselect();
	}

	private void reselect() {
		GuestBookDBOpenHelper helper = new GuestBookDBOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(GuestBookDBOpenHelper.TABLE_NAME, new String[] {"_id", "firstName", "lastName"}, null, null, null, null, "firstName");
		adapter = new DBPersistenceCursorAdapter(this, cursor, true);
		setListAdapter(adapter);
	}

	private void setUpViews() {
		firstNameText = (EditText)findViewById(R.id.first_name_text);
		lastNameText = (EditText)findViewById(R.id.last_name_text);
		saveButton = (Button)findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				save();
			}
		});
	}

	protected void save() {
		String firstName = firstNameText.getText().toString();
		String lastName = lastNameText.getText().toString();
		GuestBookDBOpenHelper helper = new GuestBookDBOpenHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("firstName", firstName);
		values.put("lastName", lastName);
		
		db.insert(GuestBookDBOpenHelper.TABLE_NAME, null, values);
		
		helper.close();
		reselect();
	}

	private class DBPersistenceCursorAdapter extends CursorAdapter {

		public DBPersistenceCursorAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tv = (TextView)view.findViewById(android.R.id.text1);
			tv.setText(String.format("%s %s", cursor.getString(1), cursor.getString(2)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(android.R.layout.simple_list_item_1, null);
			return v;
		}
		
	}
}
