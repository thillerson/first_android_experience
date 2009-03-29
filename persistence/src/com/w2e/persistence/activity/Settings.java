package com.w2e.persistence.activity;

import com.w2e.persistence.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

	private static final String PREFS_KEY = "com.w2e.prefs";
	private static final String FIRST_NAME_KEY = "first_name";
	private static final String LAST_NAME_KEY = "last_name";
	
	private Button saveButton;
	private EditText lastNameText;
	private EditText firstNameText;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persistence);
        setUpViews();
        SharedPreferences prefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        String firstName = prefs.getString(FIRST_NAME_KEY, null);
        String lastName = prefs.getString(LAST_NAME_KEY, null);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
    }

	protected void save() {
		String firstName = firstNameText.getText().toString();
		String lastName = lastNameText.getText().toString();
		SharedPreferences prefs = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(FIRST_NAME_KEY, firstName);
		editor.putString(LAST_NAME_KEY, lastName);
		editor.commit();
		Toast.makeText(this, String.format("set name to %s %s", firstName, lastName), Toast.LENGTH_LONG).show();
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

}