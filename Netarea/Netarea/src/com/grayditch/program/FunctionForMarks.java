package com.grayditch.program;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.grayditch.entity.Marks;
import com.grayditch.entity.Subject;
import com.grayditch.entity.SubjectList;

public class FunctionForMarks {
	private Map<String, String> cookies;
	private static String server = "SIA";
	// private static String server = "PROVES2";
	private String user = "";
	private String pass = "";
	private SubjectList subjectList;

	public FunctionForMarks() {
		subjectList = SubjectList.getInstance();
	}

	public Map<String, ArrayList<Subject>> login(String user, String pass)
			throws MalformedURLException, IOException {

		this.user = user;
		this.pass = pass;

		return getMarks();
	}

	public Map<String, ArrayList<Subject>> getMarks()
			throws MalformedURLException, IOException {
		Elements table = null;

		ArrayList<Subject> subjects = new ArrayList<Subject>();
		Response response = Jsoup
				.connect(
						"https://mitra.upc.es/" + server
								+ "/ACCES_PERFIL.AUTENTIFICAR2")
				.data("v_procediment", "NETAREA.INICI", "v_username", user,
						"v_password", pass).ignoreHttpErrors(true)
				.method(Method.POST).timeout(100000).execute();

		cookies = response.cookies();

		if (!cookies.toString().equals("{}")) {

			Document document = response.parse(); // If necessary.

			Connection connection = Jsoup.connect("https://mitra.upc.es/"
					+ server + "/NETAREA.INICI");

			for (Entry<String, String> cookie : cookies.entrySet()) {
				connection.cookie(cookie.getKey(), cookie.getValue());
			}

			document = connection.get();

			Elements tBody = document.select("h4");

			for (Element src : tBody) {
				try {
					if (src.select("b")
							.first()
							.text()
							.equals("Qualificacions de l'avaluació continuada:")) {
						table = src.select("table");
						break;
					} else {
					}
				} catch (NullPointerException e) {

				}

			}
			for (Element assignatura : table) {

				ArrayList<Marks> marks = new ArrayList<Marks>();
				String subj = assignatura.getElementsByClass("titolTaula")
						.text();

				Elements tr = assignatura.select("tr");

				for (Element row : tr) {
					if (!row.text().equals(subj)) {
						Boolean newMark;

						Elements td = row.select("td");

						if (td.size() == 1) {
						} else {
							if (td.eq(0).select("img").size() == 1) {
								newMark = true;
							} else
								newMark = false;

							if (td.size() == 2) {

								marks.add(new Marks(newMark, td.eq(1).text(),
										" ", " ", " "));

							} else {

								String statistics = " ";
								if (td.eq(4).select("a").size() == 0) {

								} else {
									statistics = "https://mitra.upc.es/"
											+ server
											+ "/"
											+ td.eq(4).select("a").first()
													.attr("href");
								}

								marks.add(new Marks(newMark, td.eq(1).text(),
										td.eq(2).text(), td.eq(3).text(),
										statistics));
							}
						}
					}
				}
				subjects.add(new Subject(subj, marks));
			}

			subjectList.setPicture(getImage());
			subjectList.setStudent(document.select("h1").text());

			Map<String, ArrayList<Subject>> map = new HashMap<String, ArrayList<Subject>>();
			map.put("1", subjects);
			return map;
		}

		else {
			Map<String, ArrayList<Subject>> map = new HashMap<String, ArrayList<Subject>>();
			map.put("0", subjects);
			return map;
		}
	}

	public String getStatistics(String url) throws IOException {
		String source;

		Connection connection = Jsoup.connect(url);

		for (Entry<String, String> cookie : cookies.entrySet()) {
			connection.cookie(cookie.getKey(), cookie.getValue());
		}

		Document document = connection.get();

		source = document.select("h4").select("table").html();

		return source;
	}

	public Bitmap getImage() throws IOException {
		// InputStream content = null;
		// try {
		String uri = "https://mitra.upc.es/" + server
				+ "/netarea_expedients.foto_estud";
		//
		// HttpGet httpGet = new HttpGet(url);
		// HttpClient httpclient = new DefaultHttpClient();
		// String cookieString = "";
		// for (Entry<String, String> cookie : cookies.entrySet()) {
		// cookieString += cookie.getKey() + "=" + cookie.getValue() + ";";
		// }
		// // System.out.println(cookieString);
		// httpGet.setHeader("Cookie", cookieString);
		// // Execute HTTP Get Request
		// HttpResponse response = httpclient.execute(httpGet);
		// subjectList.setPicture(response.getEntity().getContent());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return subjectList.getPicture();
		URL url = new URL(uri);

		String cookieString = "";
		for (Entry<String, String> cookie : cookies.entrySet()) {
			cookieString += cookie.getKey() + "=" + cookie.getValue() + ";";
		}
		HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();
		conn1.addRequestProperty("Cookie", cookieString);
		// subjectList.setPicture(conn1.getInputStream());
		// System.out.println(conn1.getInputStream());
		conn1.connect();
		InputStream is = conn1.getInputStream();
		return BitmapFactory.decodeStream(is);
		// System.out.println(is);

		// BufferedReader br = new BufferedReader(new InputStreamReader(
		// conn1.getInputStream()));
		//
		// String input;
		//
		// while ((input = br.readLine()) != null) {
		// System.out.println(input);
		// }
		// br.close();
		// return null;
	}

}