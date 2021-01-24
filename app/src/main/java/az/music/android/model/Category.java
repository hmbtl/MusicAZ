package az.music.android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
	private int id ;
	private String name ;
	
	private List<Track> trackList = new ArrayList<>() ;
	private Map<Integer, Track> songMap = new HashMap<>() ;
	
	public Category(int id, String name) {
		this.id = id ;
		this.name = name ;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void addSong(Track s) {
		if (!songMap.containsKey(s.getId())) {
			trackList.add(s) ;
			songMap.put(s.getId(), s) ;
		}
	}
	
	public void addSongFirst(Track s) {
		Track track = songMap.remove(s.getId()) ;
		if (track != null) {
			trackList.remove(track) ;
		}
		trackList.add(0, s) ;
		songMap.put(s.getId(), s) ;
	}


	public List<Track> getTrackList() {
		return trackList;
	}
	
	public void clear() {
		trackList = new ArrayList<>() ;
		songMap = new HashMap<>() ;
	}
	
	public void removeSong(Track s) {
		Track track = songMap.remove(s.getId()) ;
		trackList.remove(track) ;
	}
	
}
