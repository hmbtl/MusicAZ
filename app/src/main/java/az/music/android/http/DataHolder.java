package az.music.android.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.music.android.model.Category;
import az.music.android.model.Track;

public class DataHolder {
	public static final int SEARCH_CAT = -1 ;
	public static final int TOP_CAT = -2 ;
	public static final int NEW_CAT = -3 ;
	public static final int MYLIST_CAT = -4 ;
	public static final int HIST_CAT = -5 ;
	
	private static DataHolder holder ;

	static {
		holder = new DataHolder() ;
	}

	private DataHolder() {
	}

	public static DataHolder getInstance() {
		return holder ;
	}	

	private Map<Integer, Category> catMap = new HashMap<Integer, Category>()  ;
	private List<Category> catList = new ArrayList<Category>() ;

	public List<Category> getCats() throws Exception {
		if (catList.size() == 0) {
			DataHTTPAdapter adapter = new DataHTTPAdapter() ;
			List<String[]> cats = adapter.getCategories() ;
			for (int i = 0; i < cats.size(); i++) {
				Category c = new Category(Integer.parseInt(cats.get(i)[0]), cats.get(i)[1]) ;
				catMap.put(c.getId(), c) ;
				catList.add(c) ;
				Bitmap mIcon11 = null;
				String urldisplay = "http://www.music.az/resources/site/img/icons/categories/" + c.getId() + ".png";
		        try {
		            InputStream in = new java.net.URL(urldisplay).openStream();
		            mIcon11 = BitmapFactory.decodeStream(in);
		        } catch (Exception e) {
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		        }
			}
			Category topCat = new Category(TOP_CAT, "Sevilən mahnilar") ;
			catMap.put(topCat.getId(), topCat) ;
			catList.add(0, topCat) ;
			
			Category newCat = new Category(NEW_CAT, "Son əlavələr") ;
			catMap.put(newCat.getId(), newCat) ;
			catList.add(0, newCat) ;
			
			Category myCat = new Category(MYLIST_CAT, "Bəyəndiklərim") ;
			catMap.put(myCat.getId(), myCat) ;
			catList.add(myCat) ;
			
			Category histCat = new Category(HIST_CAT, "Yaddaş") ;
			catMap.put(histCat.getId(), histCat) ;
			catList.add(histCat) ;
		}
		
		return catList ;
	}
	
	public List<Track> getSongs(int categoryId) throws Exception {
		if (catMap.get(categoryId).getTrackList().size() == 0) {
			DataHTTPAdapter adapter = new DataHTTPAdapter() ;
			List<String[]> songs = adapter.getSongs(categoryId) ;
			for (int i = 0; i < songs.size(); i++) {
				Track track = new Track(categoryId, Integer.parseInt(songs.get(i)[0]), songs.get(i)[1], catMap.get(categoryId).getTrackList().size()) ;
				catMap.get(categoryId).addSong(track) ;
			}
		}
		return catMap.get(categoryId).getTrackList() ;
	}
	
	public List<Track> getMoreSongs(int categoryId) throws Exception {
		if (catMap.get(categoryId).getTrackList().size() > 0) {
			int page = 1 ;
			int current = 0 ;
			if (catMap.containsKey(categoryId)) {
				current = catMap.get(categoryId).getTrackList().size() ;
				page = current / 50 ;
			}
			DataHTTPAdapter adapter = new DataHTTPAdapter() ;
			List<String[]> songs = adapter.getSongs(categoryId, page + 1) ;
			for (int i = 0; i < songs.size(); i++) {
				Track track = new Track(categoryId, Integer.parseInt(songs.get(i)[0]), songs.get(i)[1], catMap.get(categoryId).getTrackList().size()) ;
				catMap.get(categoryId).addSong(track) ;
			}
		}
		return catMap.get(categoryId).getTrackList() ;
	}

	public void searchSongs(String query, String catId) throws Exception {
		DataHTTPAdapter adapter = new DataHTTPAdapter() ;
		List<String[]> songs = adapter.searchSongs(query, catId) ;
		Category c = new Category(SEARCH_CAT, "Search") ;
		for (int i = 0; i < songs.size(); i++) {
			Track track = new Track(SEARCH_CAT, Integer.parseInt(songs.get(i)[0]), songs.get(i)[1], c.getTrackList().size()) ;
			c.addSong(track) ;
		}
		catMap.put(SEARCH_CAT, c) ;
	}
	
	public void searchNextSongs(String query, String queryCatId) throws Exception {
		int page = 1 ;
		Category c = new Category(SEARCH_CAT, "Search") ;
		if (catMap.containsKey(0)) {
			c = catMap.get(0) ;
			int current = catMap.get(SEARCH_CAT).getTrackList().size() ;
			page = current / 50 ;
		}
		
		DataHTTPAdapter adapter = new DataHTTPAdapter() ;
		List<String[]> songs = adapter.searchSongs(query, queryCatId, page) ;
		for (int i = 0; i < songs.size(); i++) {
			Track track = new Track(SEARCH_CAT, Integer.parseInt(songs.get(i)[0]), songs.get(i)[1], c.getTrackList().size()) ;
			c.addSong(track) ;
		}
		catMap.put(SEARCH_CAT, c) ;
	}
	
	public Map<Integer, Category> getCatMap() {
		return catMap;
	}
	
	public boolean isLoaded() {
		return catMap.size() > 0 ;
	}
	
	public boolean isLoaded(int categoryId) {
		if (categoryId == HIST_CAT && catMap.get(categoryId).getTrackList().size() == 0) {
			return true ;
		}
		return catMap.containsKey(categoryId) && catMap.get(categoryId).getTrackList().size() > 0 ;
	}
	
	public boolean addSong(int songId) throws Exception {
		DataHTTPAdapter adapter = new DataHTTPAdapter() ;
		return adapter.addSong(songId) ;
	}
	
	public boolean removeSong(int songId) throws Exception {
		DataHTTPAdapter adapter = new DataHTTPAdapter() ;
		return adapter.removeSong(songId) ;
	}
	
	public boolean clearSongs() throws Exception {
		DataHTTPAdapter adapter = new DataHTTPAdapter() ;
		return adapter.clearSongs() ;
	}
	
	public boolean clearHistory() throws Exception {
		DataHTTPAdapter adapter = new DataHTTPAdapter() ;
		return adapter.clearHistory() ;
	}
	
}
