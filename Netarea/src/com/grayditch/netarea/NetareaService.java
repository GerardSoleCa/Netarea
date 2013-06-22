package com.grayditch.netarea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.grayditch.database.DatabaseFunctions;
import com.grayditch.entity.FunctionForMarksSingleton;
import com.grayditch.entity.Marks;
import com.grayditch.entity.MarksPerSubject;
import com.grayditch.entity.Subject;
import com.grayditch.entity.SubjectList;
import com.grayditch.program.FunctionForMarks;

public class NetareaService extends Service {
	private DatabaseFunctions dbFunc;
	private final static String EMPTY = "empty";
	private final static int NON_PERIOD = -1;
	private FunctionForMarks functions;
	private String user = "";
	private String pass = "";
	private String isLoged = "0";
	private SubjectList subjects;

	private int scheduleAt;
	private Boolean isActive = false;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {

	}

	private void startListener() {
		// Comença al cap de 1 Minut
		int delay = 60 * 1000;

		// Cada 5 minuts
		// int period = 5 * 60 * 1000;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			public void run() {
				dbFunc = new DatabaseFunctions(getApplicationContext());
				try {

					if (isInternetConnection()) {
						Map<String, ArrayList<Subject>> map = new HashMap<String, ArrayList<Subject>>();
						map = functions.login(user, pass);

						isLoged = map.keySet().iterator().next();

						if (isLoged.equals("1")) {

							subjects.addSubjects(map.get(isLoged));

							// Llista de la base de dades (Assignatura|notes)
							List<MarksPerSubject> list = dbFunc.getListofInt();

							if (list.size() == 0) {
								ListIterator<Subject> subIterator = subjects
										.getIterator();
								while (subIterator.hasNext()) {
									Subject sub = subIterator.next();
									ListIterator<Marks> markIterator = sub
											.getMarks().listIterator();
									int count = 0;
									while (markIterator.hasNext()) {
										Marks mark = markIterator.next();

										if (!mark.getPercentage().equals(" ")) {
											count++;
										}
									}

									dbFunc.putMark(sub.getName(), count);
								}

							} else {
								ListIterator<MarksPerSubject> listMarksXsubject = list
										.listIterator();

								Boolean isBreaked = false;

								// Iterador per la Classe Subject rebuda de
								// netarea
								ListIterator<Subject> subIterator = subjects
										.getIterator();

								while ((subIterator.hasNext()) && !isBreaked) {
									Subject sub = subIterator.next();
									MarksPerSubject mXs = listMarksXsubject
											.next();

									if (sub.getName().equals(mXs.getName())) {
										ListIterator<Marks> markIterator = sub
												.getMarks().listIterator();
										int count = 0;
										while (markIterator.hasNext()) {
											Marks mark = markIterator.next();

											if (!mark.getPercentage().equals(
													" ")) {
												count++;
											}
										}

										if (count == mXs.getNumMarks()) {
											// No fer Res
										} else {
											// Introduir a la DB
											dbFunc.putMark(sub.getName(), count);
											String ns = Context.NOTIFICATION_SERVICE;
											NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
											showNotification(mNotificationManager);
										}
									} else {
										dbFunc.createDB();
										isBreaked = true;
									}

								}

							}
						}
					}
				}

				catch (IOException e) {
					e.printStackTrace();
				}
				dbFunc.disconnectDB();
			}

		}, delay, (scheduleAt * 60 * 1000));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		subjects = SubjectList.getInstance();
		functions = FunctionForMarksSingleton.getInstance();

		isActive = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getBoolean("runService", false);
		if (!isActive) {
			this.stopSelf();
		}

		else {

			scheduleAt = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getInt("scheduleAt", NON_PERIOD);

			Boolean checked = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext()).getBoolean("rememberCredentials",
					false);
			// en caso que sí, llenaremos los textviews con valores
			if (checked) {
				String nombre = PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()).getString("usuari", EMPTY);
				String passw = PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext())
						.getString("contrasenya", EMPTY);

				// Si hay información, la ponemos en los textviews
				if (!nombre.equals(EMPTY) && !passw.equals(EMPTY)) {
					user = nombre;
					pass = passw;
					startListener();
				} else {
					stopService(intent);
				}
			}
		}
		return START_STICKY;

	}

	private void showNotification(NotificationManager mNM) {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence detail = getText(R.string.not_detail);
		CharSequence title = getText(R.string.not_title);
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_stat_not,
				title, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SubjectsTestActivity.class),
				PendingIntent.FLAG_CANCEL_CURRENT);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, title, detail, contentIntent);

		notification.defaults |= Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNM.notify(101, notification);
	}

	private boolean isInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
