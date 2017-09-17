import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MusicSearch {

	public void startProcess() {
		Scanner in = new Scanner(System.in);
		String keyword = "";
		ResultSet result = null;
		ArrayList<Music> musicList = new ArrayList<>();

		System.out.println("***** Music Search *****");
		System.out.print("Enter the keyword of music(Music title or Artist name): ");

		keyword = in.nextLine();
		
		System.out.println();
		
		DBManager.getInstance().connect();
		try {
			String query = "select * from "
					+ "(select m.music_num, m.title, a.name as Aname, t.name as Tname, g.name as Gname "
					+ "from Music as m , Artist as a, Tempo as t, Genre as g where m.artist_num=a.artist_num "
					+ "and m.tempo_num=t.tempo_num and g.genre_num=m.genre_num) p " + "where title like '%" + keyword
					+ "%' or Aname like '%" + keyword + "%' order by music_num asc;";

			result = DBManager.getInstance().query(query);
			int music_num, i = 0;
			String title, artist, genre, tempo;

			System.out.format("%-20s%-20s%-20s%-20s%-20s\n","Music num","Title","Artist","Genre","Tempo");
			while (result.next()) {
				i++;
				music_num = result.getInt("music_num");
				title = result.getString("title");
				artist = result.getString("Aname");
				genre = result.getString("Gname");
				tempo = result.getString("Tname");

				System.out.format("%-20d%-20s%-20s%-20s%-20s\n", i, title, artist, genre, tempo);

				Music music = new Music();
				music.setMusic_num(music_num);
				music.setTitle(title);
				musicList.add(music);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBManager.getInstance().close();

		startAdditionalPorcess(musicList);
	}

	public void startAdditionalPorcess(ArrayList<Music> musicList) {
		Scanner in = new Scanner(System.in);
		String menu = "";
		String query = "";
		String music_num = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();

		do {
			System.out.println();
			System.out.println();
			
			System.out.println("******* menu *******");
			System.out.println("1.add music to play list");
			System.out.println("2.download music");
			System.out.println("3.back");
			System.out.println("********************");
			System.out.print("Enter the number of menu: ");

			menu = in.nextLine();
			
			DBManager.getInstance().connect();
			switch (menu) {

			case "1":// add play list
				System.out.print("Please enter the music number to add playlists: ");
				music_num = in.nextLine();
				try {
					music_num = String.valueOf(musicList.get(Integer.valueOf(music_num) - 1).getMusic_num());
				} catch (Exception e) {
					System.out.println("Check music number again.");
					break;
				}

				query = "INSERT INTO Play_List (user_id, music_num) VALUES ('" + user_id + "', " + music_num + ");";
				try {
					int result = DBManager.getInstance().update(query);
					if (result > 0) {
						System.out.println("Music was added to playlist.");
					} else {
						System.out.println("Check music number again.");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case "2":// download music
				System.out.print("Please enter the music number to download: ");
				music_num = in.nextLine();
				try {
					music_num = String.valueOf(musicList.get(Integer.valueOf(music_num) - 1).getMusic_num());
				} catch (Exception e) {
					System.out.println("Check music number again.");
					break;
				}
				query = "INSERT INTO Download (music_num, user_id) VALUES ('" + music_num + "', '" + user_id + "');";

				try {
					int result = DBManager.getInstance().update(query);
					if (result > 0) {
						System.out.println("Music was downloaded.");
					} else {
						System.out.println("Check music number again.");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
			DBManager.getInstance().close();
		} while (!menu.equals("3"));
	}
}
