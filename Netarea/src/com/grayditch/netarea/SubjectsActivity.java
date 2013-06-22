package com.grayditch.netarea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.grayditch.entity.FunctionForMarksSingleton;
import com.grayditch.entity.Marks;
import com.grayditch.entity.Subject;
import com.grayditch.entity.SubjectList;
import com.grayditch.program.FunctionForMarks;

public class SubjectsActivity extends ExpandableListActivity {
	private final static int ID_DIALOG_FETCHING = 0;

	// DEFINIMOS VARIABLES
	private final String NAME = "name";
	private FunctionForMarks functions;

	private SubjectList subjects;
	private Message m = null;
	private String isLoged = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// INICIALIZAMOS VARIBLES

		subjects = SubjectList.getInstance();
		functions = FunctionForMarksSingleton.getInstance();

		final LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// DEFINIMOS UN HEADER DATA (NUMERO DESPLEGABLES LISTADO)
		final ArrayList<HashMap<String, String>> headerData = new ArrayList<HashMap<String, String>>();

		ArrayList<ArrayList<HashMap<String, Object>>> childData = new ArrayList<ArrayList<HashMap<String, Object>>>();

		ListIterator<Subject> subIterator = subjects.getIterator();
		while (subIterator.hasNext()) {
			Subject sub = subIterator.next();

			final HashMap<String, String> subjectBloc = new HashMap<String, String>();
			subjectBloc.put(NAME, sub.getName());
			headerData.add(subjectBloc);

			final ArrayList<HashMap<String, Object>> subjectData = new ArrayList<HashMap<String, Object>>();
			childData.add(subjectData);

			ListIterator<Marks> marksIterator = sub.getMarks().listIterator();

			while (marksIterator.hasNext()) {
				Marks marks = marksIterator.next();

				final HashMap<String, Object> markData = new HashMap<String, Object>();
				markData.put(NAME, marks.getNewMark() + "__" + marks.getType()
						+ "__" + marks.getPercentage() + "__" + marks.getMark()
						+ "__" + marks.getStatistics());
				subjectData.add(markData);
			}

		}

		setListAdapter(new SimpleExpandableListAdapter(this, headerData,
				R.layout.expandablemain, new String[] { NAME },
				new int[] { R.id.TextViewGrupo }, childData, 0, null,
				new int[] {}) {
			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				final View v = super.getChildView(groupPosition, childPosition,
						isLastChild, convertView, parent);

				String[] pieces = ((String) ((Map<String, Object>) getChild(
						groupPosition, childPosition)).get(NAME)).split("__");

				if (pieces[0].equals("true")) {
					((ImageView) v.findViewById(R.id.flag))
							.setImageResource(R.drawable.bandera);
				} else {
					((ImageView) v.findViewById(R.id.flag)).setImageResource(0);
				}
				if (pieces[2].equals(" ")) {
					((TextView) v.findViewById(R.id.type))
							.setText(getString(R.string.no_marks));
				} else {
					((TextView) v.findViewById(R.id.type)).setText(pieces[1]);
				}
				((TextView) v.findViewById(R.id.percent)).setText(pieces[2]);

				((TextView) v.findViewById(R.id.mark)).setText(pieces[3]);

				return v;
			}

			@Override
			public View newChildView(boolean isLastChild, ViewGroup parent) {
				return layoutInflater.inflate(R.layout.row, null, false);
			}
		});
		layoutInflater.inflate(R.layout.mainexp, null, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.subjects_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh:
			showDialog(ID_DIALOG_FETCHING);
			new Thread(new Runnable() {
				public void run() {
					getInfo();
				}

			}).start();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub

		parent.getAdapter();
		ExpandableListAdapter mAdapter = parent.getExpandableListAdapter();
		String child = mAdapter.getChild(groupPosition, childPosition)
				.toString();
		System.out.println(child);

		final String url = child.split("__")[4].substring(0,
				child.split("__")[4].length() - 1);
		final String title = child.split("__")[1];

		if (!url.equals(" ")) {
			final Intent intent = new Intent(this, StatisticsActivity.class);
			showDialog(ID_DIALOG_FETCHING);
			new Thread(new Runnable() {
				public void run() {
					try {

						final String source;
						source = functions.getStatistics(url);
						if (!source.equals("null")) {
							intent.putExtra("TITLE", title);
							intent.putExtra("SOURCE", source);
							startActivity(intent);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					dismissDialog(ID_DIALOG_FETCHING);
				}
			}).start();
		}
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	private void getInfo() {
		Intent intent = new Intent(this, SubjectsActivity.class);

		// FunctionForMarks marksFunction = new FunctionForMarks();
		try {
			Map<String, ArrayList<Subject>> map = new HashMap<String, ArrayList<Subject>>();
			map = functions.getMarks();

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
			this.finish();
		} else {
			m = new Message();
			m.what = LoginActivity.LOGIN_ERROR;
			SubjectsActivity.this.mLoginHandler.sendMessage(m);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ID_DIALOG_FETCHING) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(getString(R.string.log_in));
			dialog.setMessage(getString(R.string.fetching_statistics));
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
				Toast.makeText(SubjectsActivity.this,
						getString(R.string.login_error), Toast.LENGTH_LONG)
						.show();
				break;
			}
			super.handleMessage(msg);
		}
	};

}
