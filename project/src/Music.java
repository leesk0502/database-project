
public class Music {
	private int music_num, playlist_num;
	public int getPlaylist_num() {
		return playlist_num;
	}

	public void setPlaylist_num(int playlist_num) {
		this.playlist_num = playlist_num;
	}

	private String title, aname, gname, tname;
	
	public Music(){}

	public int getMusic_num() {
		return music_num;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public void setMusic_num(int music_num) {
		this.music_num = music_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
