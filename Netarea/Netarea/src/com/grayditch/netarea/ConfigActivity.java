package com.grayditch.netarea;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ConfigActivity extends Activity {
	private final static int EMPTY = -1;

	private int scheduleAt;
	private Boolean isActive = false;
	private Spinner spinner;
	private ToggleButton toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_configurator);

		isActive = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getBoolean("runService", false);

		Boolean isCheckedToSaveCredentialsActive = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext())
				.getBoolean("rememberCredentials", false);

		toggle = (ToggleButton) findViewById(R.id.togglebutton);
		toggle.setChecked(isActive);

		if (!isCheckedToSaveCredentialsActive) {
			toggle.setEnabled(isCheckedToSaveCredentialsActive);
			Button save = (Button)findViewById(R.id.saveButton);
			save.setEnabled(isCheckedToSaveCredentialsActive);
			TextView infoToSave = (TextView) findViewById(R.id.infoActivateToSave);
			infoToSave.setTextColor(Color.YELLOW);
			infoToSave.setTypeface(null,Typeface.BOLD);
		}
		else{
			TextView infoToSave = (TextView) findViewById(R.id.infoActivateToSave);
			infoToSave.setVisibility(4);
		}

		spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.animales, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

		spinner.setEnabled(isActive);

		if (isActive) {

			scheduleAt = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getInt("scheduleAt", EMPTY);

			// Si hay información, la ponemos en los textviews
			if (scheduleAt != EMPTY) {
				int pos;
				switch (scheduleAt) {
				case 30:
					pos = 0;
					break;

				case 60:
					pos = 1;
					break;
				case 90:
					pos = 2;
					break;
				case 120:
					pos = 3;
					break;
				default:
					pos = 0;
					break;
				}
				spinner.setSelection(pos);
			}
		}

	}

	public void onToggleClicked(View v) {
		// Perform action on clicks
		if (((ToggleButton) v).isChecked()) {
			isActive = true;
			spinner.setEnabled(isActive);
		} else {
			isActive = false;
			spinner.setEnabled(isActive);
		}
	}

	public void saveAndStart(View v) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).edit();
		editor.putBoolean("runService", isActive);
		editor.putInt("scheduleAt", scheduleAt);
		editor.commit();

		if (isActive) {
			stopService(new Intent(this, NetareaService.class));
			startService(new Intent(this, NetareaService.class));
		} else {
			stopService(new Intent(this, NetareaService.class));
		}
		finish();
	}

	public void cancel(View v) {
		finish();
	}

	public class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {

			switch (pos) {
			case 0:
				scheduleAt = 30;
				break;

			case 1:
				scheduleAt = 60;
				break;
			case 2:
				scheduleAt = 90;
				break;
			case 3:
				scheduleAt = 120;
				break;
			default:
				break;
			}
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

}
