package az.music.android.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import az.music.android.SessionExpiredException;

public class JSONParser {
	public static int GET = 1;
	public static int POST = 2;

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrlPost(String url, String[] names,
			String[] values) {
		try {
			HttpEntity httpEntity = null;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(names.length);
			for (int i = 0; i < names.length; i++) {
				nameValuePairs.add(new BasicNameValuePair(names[i], values[i]));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();

			is = httpEntity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getJSON();
	}

	public JSONObject getJSONFromUrl(String url) throws Exception {
		Log.e("URL", url) ;
		// Making HTTP request
		HttpEntity httpEntity = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);

		int code = httpResponse.getStatusLine().getStatusCode() ;

		if (code == 403) {
			throw new SessionExpiredException() ;
		}

		httpEntity = httpResponse.getEntity();
		is = httpEntity.getContent();

		return getJSON();

	}

	private JSONObject getJSON() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			Log.e("TEST", sb.toString()) ;
			
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		Log.e("JSON Parser1", json);
		json = "{ \"categories\": " + json + "}";
		Log.e("JSON Parser2", json);
		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jObj;
	}
}
