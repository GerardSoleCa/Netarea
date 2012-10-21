package com.grayditch.netarea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.grayditch.entity.FunctionForMarksSingleton;
import com.grayditch.entity.Subject;
import com.grayditch.entity.SubjectList;
import com.grayditch.program.FunctionForMarks;

public class LoginActivity extends Activity {
	private final static int ID_DIALOG_FETCHING = 0;
	protected static final int LOGIN_ERROR = 0;
	// Instancias de objetos a usar
	private final static String EMPTY = "empty";
	private SubjectList subjects;
	private FunctionForMarks functions;
	private CheckBox cb_remember;
	private EditText nif, pass;
	private Boolean checked = false;
	private Message m = null;
	private String isLoged = "0";

	// ////////////////////////////////////////////////////
	// ////////////////////ON CREATE//////////////////////
	// ///////////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		subjects = SubjectList.getInstance();
		functions = FunctionForMarksSingleton.getInstance();

		// INICIALIZACIÓN VARIBLES
		nif = (EditText) findViewById(R.id.etNif);
		pass = (EditText) findViewById(R.id.etPassword);
		cb_remember = (CheckBox) findViewById(R.id.cb_remember);

		// LISTENER de si queremos recordar o no los credenciales
		cb_remember.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Editor editor = PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()).edit();
				editor.putBoolean("rememberCredentials", isChecked);
				editor.commit();

				if (!isChecked) {
					stopService(new Intent(getApplicationContext(),
							NetareaService.class));
					editor = PreferenceManager.getDefaultSharedPreferences(
							getApplicationContext()).edit();
					editor.putBoolean("runService", isChecked);
					editor.commit();
				}
			}
		});
		// FIN LISTENER

		// Obtenemos el valor de checked, de si queriamos guardar los
		// credenciales o no
		checked = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getBoolean("rememberCredentials",
				false);

		// en caso que sí, llenaremos los textviews con valores
		if (checked) {
			String nombre = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getString("usuari", EMPTY);
			String passw = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getString("contrasenya", EMPTY);

			// Si hay información, la ponemos en los textviews
			if (!nombre.equals(EMPTY) && !passw.equals(EMPTY)) {
				nif.setText(nombre);
				pass.setText(passw);
				cb_remember.setChecked(true);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.contact:

			intent = new Intent(this, ContactActivity.class);
			startActivity(intent);
			return true;
		case R.id.help:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// //////////////////////////////////////////////////////////
	// //////////////////CREDENTIALS CONTROL/////////////////////
	// /////////////////////////////////////////////////////////

	private void addLoginDetails() {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).edit();
		editor.putString("usuari", nif.getText().toString());
		editor.putString("contrasenya", pass.getText().toString());
		editor.commit();
	}

	private void removeLoginDetails() {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).edit();
		editor.remove("usuari");
		editor.remove("contrasenya");
		editor.commit();
	}

	// ///////////////////////////////////////////////////////////
	// /////////////////FIRE EVENT ON CLICKBUTTON////////////////
	// //////////////////////////////////////////////////////////

	public void LoginClicked(View v) {

		if ((nif.getText().toString().equals("")) || (pass.getText().toString().equals(""))) {
			Toast.makeText(LoginActivity.this,
					getString(R.string.blank), Toast.LENGTH_LONG)
					.show();
		} else {
			// Guardar los datos si se ha marcado remember
			if (cb_remember.isChecked()) {
				addLoginDetails();
			} else {
				removeLoginDetails();
			}

			if (isInternetConnection()) {
				showDialog(ID_DIALOG_FETCHING);
				new Thread(new Runnable() {
					public void run() {
						getInfo();
					}
				}).start();
			}
		}
	}

	private void getInfo() {
		// cerramos el teclado al clicar en login
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
		Intent intent = new Intent(this, SubjectsTestActivity.class);
		final String user = nif.getText().toString();
		final String password = pass.getText().toString();

		// FunctionForMarks marksFunction = new FunctionForMarks();
		try {
			Map<String, ArrayList<Subject>> map = new HashMap<String, ArrayList<Subject>>();
			map = functions.login(user, password);

			isLoged = map.keySet().iterator().next();

			if (isLoged.equals("1")) {
				subjects.addSubjects(map.get(isLoged));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		dismissDialog(ID_DIALOG_FETCHING);
		if (isLoged.equals("1")) {
			startActivity(intent);
			// if (checked) {
			// System.out.println("Parant el servei");
			// stopService(new Intent(this, NetareaService.class));
			// System.out.println("Engegant el servei");
			// startService(new Intent(this, NetareaService.class));
			// }
		} else {
			m = new Message();
			m.what = LoginActivity.LOGIN_ERROR;
			LoginActivity.this.mLoginHandler.sendMessage(m);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ID_DIALOG_FETCHING) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(getString(R.string.log_in));
			dialog.setMessage(getString(R.string.fetching));
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		}
		return super.onCreateDialog(id);
	}

	Handler mLoginHandler = new Handler() {
		/** Gets called on every message that is received */
		// @Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LoginActivity.LOGIN_ERROR:
				Toast.makeText(LoginActivity.this,
						getString(R.string.login_error), Toast.LENGTH_LONG)
						.show();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private boolean isInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			Toast.makeText(LoginActivity.this, getString(R.string.no_internet),
					Toast.LENGTH_LONG).show();
			return false;
		}
	}
}