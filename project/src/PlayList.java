import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayList {
	public void startProcess() {
		ResultSet result = null;
		String query = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();
		ArrayList<Music> musicList = new ArrayList<>();

		System.out.println("*****Play List*****");

		DBManager.getInstance().connect();
		try {
			String title, artist;
			int music_num, i = 0;
			query = "SELECT * FROM (SELECT pl.playlist_num, m.*, a.name as Aname FROM Play_List as pl, Music as m, Artist as a "
					+ "WHERE user_id = '" + user_id
					+ "' and pl.music_num = m.music_num and m.artist_num = a.artist_num) p ;";

			result = DBManager.getInstance().query(query);

			while (result.next()) {
				i++;

				Music music = new Music();
				music.setPlaylist_num(result.getInt("playlist_num"));
				music.setMusic_num(result.getInt("music_num"));
				music.setTitle(result.getString("title"));
				music.setAname(result.getString("Aname"));
				musicList.add(music);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBManager.getInstance().close();
		startAdditionalProcess(musicList);
	}

	public void startAdditionalProcess(ArrayList<Music> musicList) {
		Scanner in = new Scanner(System.in);
		String menu = "";
		String query = "";
		String music_num = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();

		do {
			System.out.format("%-20s%-20s%-20s\n","Music num","Title","Artist");
			int i = 0;
			for (Music m : musicList) {
				System.out.format("%-20d%-20s%-20s\n", ++i, m.getTitle(), m.getAname());
			}
			
			System.out.println();
			System.out.println();
			
			System.out.println("********************");
			System.out.println("1. Play music");
			System.out.println("2. Remove music");
			System.out.println("3. Back");
			System.out.println("********************");
			System.out.print("Enter the number of menu: ");
			
			menu = in.nextLine();
			
			DBManager.getInstance().connect();
			switch (menu) {
			case "1":
				try {
					System.out.printf("Enter the music num to play : ");
					music_num = in.nextLine();
					try {
						music_num = String.valueOf(musicList.get(Integer.valueOf(music_num) - 1).getMusic_num());
					} catch (Exception e) {
						System.out.println("Check music number again.");
						break;
					}
					query = "UPDATE Play_Count SET play_count = play_count + 1 WHERE user_id = '" + user_id + "' AND music_num='"
							+ music_num + "';";
					DBManager.getInstance().update(query);
					System.out.println("Play music successfully");
					System.out.println();
				} catch (SQLException e) {
					System.err.println("Download Exception: " + e.getMessage());
				}
				break;
			case "2":
				try {
					System.out.printf("Enter the music num to delete from play list : ");
					String key = in.nextLine();
					String playlist_num = "";
					try {
						playlist_num = String.valueOf(musicList.get(Integer.valueOf(key) - 1).getPlaylist_num());
					} catch (Exception e) {
						System.out.println("Check music number again.");
						break;
					}
					query = "DELETE FROM Play_List WHERE playlist_num='" + playlist_num + "';";

					DBManager.getInstance().update(query);
					musicList.remove(Integer.parseInt(key));
					System.out.println("Delete music successfully.");
					System.out.println();
				} catch (Exception e) {
					System.err.println("Remove Exception: " + e.getMessage());
				}
				break;
			}
			DBManager.getInstance().close();
		} while (!menu.equals("3"));
	}
}