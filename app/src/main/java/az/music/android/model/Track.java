package az.music.android.model;

public class Track {
	private int id ;
	private String artist ;
	private String name ;
	
	private int fragmentId ;
	private int categoryId ;
	private int index ;
	private String title ;
	
	public Track(int categoryId, int id, String name, int index) {
		this.id = id ;
		this.name = name ;
		this.categoryId = categoryId ;
		this.index = index ;
	}
	
	public Track(int id, String name, String artist) {
		this.id = id ;
		this.name = name ;
		this.artist = artist ;
	}

	public int getId() {
		return id;
	}

	public String getArtist() {
		return artist;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public int getIndex() {
		return index;
	}

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}
	
}
