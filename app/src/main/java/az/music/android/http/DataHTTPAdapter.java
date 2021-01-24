package az.music.android.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataHTTPAdapter {
	private JSONParser parser = new JSONParser() ;
	
	public static final String baseUrl = "http://music.az" ;
	public static final String baseApiUrl = baseUrl + "/api" ;
	
	public List<String[]> getCategories() throws Exception {
		List<String[]> cats = new ArrayList<String[]>() ;
		try {
			JSONObject json = parser.getJSONFromUrl(baseApiUrl + "/categories?sid=" + SID.SID) ;
			JSONArray arr = json.getJSONArray("categories") ;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i) ;
				String id = jsonObj.getString("id") ;
				String name = jsonObj.getString("name") ;
				cats.add(new String[] {id, name}) ;
			}
		} catch (Exception e) {
			e.printStackTrace() ;
			throw e ;
		}
		return cats ;
	}
	
	public List<String[]> getSongs(int categoryId) throws Exception {
		List<String[]> songs = new ArrayList<String[]>() ;
		JSONObject json = null ;
		try {	
			if (categoryId == DataHolder.TOP_CAT) {
				json = parser.getJSONFromUrl(baseApiUrl + "/top?sid=" + SID.SID) ;
			} else if (categoryId == DataHolder.NEW_CAT) {
				json = parser.getJSONFromUrl(baseApiUrl + "/new?sid=" + SID.SID) ;
			} else if (categoryId == DataHolder.MYLIST_CAT) {
				json = parser.getJSONFromUrl(baseApiUrl + "/playlist/get?sid=" + SID.SID) ;
			} else if (categoryId == DataHolder.HIST_CAT) {
				json = parser.getJSONFromUrl(baseApiUrl + "/history/?type=music&method=play&sid=" + SID.SID) ;
			} else {
				json = parser.getJSONFromUrl(baseApiUrl + "/categories/" + categoryId + "?sid=" + SID.SID) ;
			}
			json = json.getJSONObject("categories") ;
			JSONArray arr = json.getJSONArray("items") ;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i) ;
				String id = jsonObj.getString("id") ;
				String name = jsonObj.getString("name") ;
				songs.add(new String[] {id, name}) ;
			}
		} catch (Exception e) {
			e.printStackTrace() ;
			throw e ;
		}
		return songs ;
	}
	
	public List<String[]> getSongs(int categoryId, int page) throws Exception {
		List<String[]> songs = new ArrayList<String[]>() ;
		try {
			JSONObject json = parser.getJSONFromUrl(baseApiUrl + "/categories/" + categoryId + "?page=" + page + "&sid=" + SID.SID) ;
			json = json.getJSONObject("categories") ;
			JSONArray arr = json.getJSONArray("items") ;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i) ;
				String id = jsonObj.getString("id") ;
				String name = jsonObj.getString("name") ;
				songs.add(new String[] {id, name}) ;
			}
		} catch (JSONException e) {
			e.printStackTrace() ;
			throw e ;
		}
		return songs ;
	}
	
	public List<String[]> searchSongs(String query, String catId) throws Exception {
		List<String[]> songs = new ArrayList<String[]>() ;
		try {
			JSONObject json = null ;
			if (catId == null) {
				json = parser.getJSONFromUrl(baseApiUrl + "/search?query=" + query + "&sid=" + SID.SID) ;
			} else {
				json = parser.getJSONFromUrl(baseApiUrl + "/search?query=" + query + "&category="+ catId +"&sid=" + SID.SID) ;
			}

			json = json.getJSONObject("categories") ;
			JSONArray arr = json.getJSONArray("items") ;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i) ;
				String id = jsonObj.getString("id") ;
				String name = jsonObj.getString("name") ;
				songs.add(new String[] {id, name}) ;
			}
		} catch (Exception e) {
			e.printStackTrace() ;
			throw e ;
		}
		return songs ;
	}
	
	public List<String[]> searchSongs(String query, String catId, int page) throws Exception {
		List<String[]> songs = new ArrayList<String[]>() ;
		try {
			JSONObject json = null ;
			if (catId == null) {
				json = parser.getJSONFromUrl(baseApiUrl + "/search?query=" + query + "&page=" + page + "&sid=" + SID.SID) ;
			} else {
				json = parser.getJSONFromUrl(baseApiUrl + "/search?query=" + query + "&page=" + page + "&category=" + catId + "&sid=" + SID.SID) ;
			}

			json = json.getJSONObject("categories") ;
			JSONArray arr = json.getJSONArray("items") ;
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObj = arr.getJSONObject(i) ;
				String id = jsonObj.getString("id") ;
				String name = jsonObj.getString("name") ;
				songs.add(new String[] {id, name}) ;
			}
		} catch (Exception e) {
			e.printStackTrace() ;
			throw e ;
		}
		return songs ;
	}
	
	public String[] getStreamURL(int songId) throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrl(baseApiUrl + "/play/" + songId + "?sid=" + SID.SID) ;
			json = json.getJSONObject("categories") ;
			String name = json.getString("name") ;
			String link = json.getString("link").replaceAll("\\\\", "") ;
			return new String[] {name, link} ;
		} catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}
	}
	
	public String[] register(String msisdn) throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrlPost(baseApiUrl + "/register", new String[]{"number"}, new String[]{msisdn}) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return new String[] {success} ;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e ;
		}
	}
	
	public String[] newpin(String msisdn) throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrlPost(baseApiUrl + "/lost", new String[]{"number"}, new String[]{msisdn}) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return new String[] {success} ;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e ;
		}
	}
	
	public String[] login(String msisdn, String password) throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrlPost(baseApiUrl + "/login", new String[]{"number","password"}, new String[]{msisdn, password}) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			try {
				String sid = json.getString("sid") ;
				SID.SID = sid ;
				return new String[] {success, sid} ;
			} catch (Exception e) {}
			return new String[] {success} ;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e ;
		}
	}


	public boolean logout() throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrl(baseApiUrl + "/logout?sid=" + SID.SID) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return success.equalsIgnoreCase("true") ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}


	
	public boolean addSong(int songId) throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrlPost(baseApiUrl + "/playlist/add/" + songId, new String[]{"sid"}, new String[]{SID.SID}) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return success.equalsIgnoreCase("true") ;
		} catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}
	}
	
	public boolean removeSong(int songId) throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrlPost(baseApiUrl + "/playlist/delete/" + songId, new String[]{"sid"}, new String[]{SID.SID}) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return success.equalsIgnoreCase("true") ;
		} catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}
	}
	
	
	public boolean clearSongs() throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrlPost(baseApiUrl + "/playlist/clear", new String[]{"sid"}, new String[]{SID.SID}) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return success.equalsIgnoreCase("true") ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}
	
	public boolean clearHistory() throws Exception {
		try {
			JSONObject json = parser.getJSONFromUrl(baseApiUrl + "/history?method=clear&sid=" + SID.SID) ;
			json = json.getJSONObject("categories") ;
			String success = json.getString("success") ;
			return success.equalsIgnoreCase("true") ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}
}
