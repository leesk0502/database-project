import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Recommend {
	public void startProcess() {
		Scanner in = new Scanner(System.in);
		String keyword = "", query = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();
		ResultSet result = null;

		DBManager.getInstance().connect();

		try {
			query = "SELECT * FROM Preference_Genre as pg WHERE pg.user_id='" + user_id + "'";

			result = DBManager.getInstance().query(query);
			if (result.next()) {
				DBManager.getInstance().close();
				startRecommendProcess();
			} else {
				DBManager.getInstance().close();
				startPreferenceProcess(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void startRecommendProcess() {
		Scanner in = new Scanner(System.in);
		String input = "", query = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();
		ResultSet result = null;

		do {
			System.out.println();
			System.out.println();
			
			System.out.println("****** Recommend Page *******");
			System.out.println("1. Show Recommend Music");
			System.out.println("2. Modify preferences");
			System.out.println("3. Back");
			System.out.println("*****************************");
			
			System.out.println();
			System.out.println();
			
			System.out.print("Enter the number of menu: ");
			input = in.nextLine();
			switch (input) {
			case "1":
				startRecommendMusicProcess();
				break;
			case "2":
				startPreferenceProcess(false);
				break;
			}

		} while (!input.equals("3"));

	}
	
	public void startRecommendMusicProcess(){
		Scanner in = new Scanner(System.in);
		String input = "", query = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();
		ResultSet result = null;
		ArrayList<Music> musicList = new ArrayList<>();
		
		DBManager.getInstance().connect();
		try {
			query = "SELECT p.* FROM "
					+ "(SELECT m.music_num, m.title, a.name as Aname, t.name as Tname, g.name as Gname "
					+ "FROM Music as m , Artist as a, Tempo as t, Genre as g where m.artist_num=a.artist_num "
					+ "AND m.tempo_num=t.tempo_num AND g.genre_num=m.genre_num) p "
					+ "ORDER BY RAND() LIMIT 10;";
			
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
		}catch(SQLException e){
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

				query = "INSERT INTO Play_List (user_id, music_num) VALUES ('" + user_id + "', "
						+ music_num + ");";
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
					music_num = String.valueOf(musicList.get(Integer.valueOf(music_num)).getMusic_num());
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

	public void startPreferenceProcess(boolean isFirst) {
		Scanner in = new Scanner(System.in);
		String input = "", query = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();
		ResultSet result = null;
		ArrayList<Genre> genreList = new ArrayList<>();
		ArrayList<Tempo> tempoList = new ArrayList<>();

		if (isFirst)
			System.out.println("* Welcome to Recommend page (First time) *");
		else
			System.out.println("* Modify your Preferences *");

		DBManager.getInstance().connect();
		try {
			query = "SELECT * FROM Genre";
			result = DBManager.getInstance().query(query);
			int i = 0;
			while (result.next()) {
				i++;
				Genre genre = new Genre();
				genre.setGenre_num(result.getInt("genre_num"));
				genre.setName(result.getString("name"));

				genreList.add(genre);

				System.out.println(i + ". " + result.getString("name"));
			}
		} catch (SQLException e) {

		}
		DBManager.getInstance().close();

		// Show previous selections of Genre
		if (!isFirst) {
			DBManager.getInstance().connect();
			try {
				query = "SELECT * FROM Preference_Genre as pg WHERE pg.user_id='" + user_id + "'";
				result = DBManager.getInstance().query(query);
				ArrayList<String> list = new ArrayList<>();
				while (result.next()) {
					list.add(result.getString("genre_num"));
				}
				System.out.println("Your previous selection is " + String.join(", ", list));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBManager.getInstance().close();
		}

		boolean success = true;
		do {
			success = true;
			System.out.print("Enter select your preferences Genre(ex. 1,3,5) : ");
			input = in.nextLine();

			// Delete preferences before insert
			DBManager.getInstance().connect();
			try {
				query = "START TRANSACTION;";
				DBManager.getInstance().query(query);

				query = "DELETE FROM Preference_Genre WHERE user_id='" + user_id + "'";
				DBManager.getInstance().update(query);
			} catch (SQLException e) {

			}
			for (String idx : input.split(",")) {
				idx = idx.trim();

				try {
					int genre = genreList.get(Integer.parseInt(idx) - 1).getGenre_num();
					query = "INSERT INTO Preference_Genre (genre_num, user_id) VALUES ( '" + genre + "', '" + user_id
							+ "' )";

					DBManager.getInstance().update(query);
				} catch (Exception e) {
					// If any exception in insert Rollback
					query = "ROLLBACK";
					try {
						DBManager.getInstance().query(query);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					success = false;
					System.out.println("Please check your number.");
					break;
				}
			}
			if (success) {
				query = "COMMIT";
				try {
					DBManager.getInstance().query(query);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			DBManager.getInstance().close();
		} while (!success);

		DBManager.getInstance().connect();
		try {
			query = "SELECT * FROM Tempo";
			result = DBManager.getInstance().query(query);
			int i = 0;
			while (result.next()) {
				i++;
				Tempo tempo = new Tempo();
				tempo.setTempo_num(result.getInt("tempo_num"));
				tempo.setName(result.getString("name"));

				tempoList.add(tempo);

				System.out.println(i + ". " + result.getString("name"));
			}
		} catch (SQLException e) {
		}
		DBManager.getInstance().close();

		// Show previous selections of Tempo
		if (!isFirst) {
			DBManager.getInstance().connect();
			try {
				query = "SELECT * FROM Preference_Tempo as pt WHERE pt.user_id='" + user_id + "'";
				result = DBManager.getInstance().query(query);
				ArrayList<String> list = new ArrayList<>();
				while (result.next()) {
					list.add(result.getString("tempo_num"));
				}
				System.out.println("Your previous selection is " + String.join(", ", list));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBManager.getInstance().close();
		}

		success = true;
		do {
			success = true;
			System.out.print("Enter select your preferences Tempo(ex. 1,3,5) : ");
			input = in.nextLine();

			DBManager.getInstance().connect();
			// Delete preferences before insert
			try {
				query = "START TRANSACTION;";
				DBManager.getInstance().query(query);

				query = "DELETE FROM Preference_Tempo WHERE user_id='" + user_id + "'";
				DBManager.getInstance().update(query);
			} catch (SQLException e) {

			}
			for (String idx : input.split(",")) {
				idx = idx.trim();
				try {
					int tempo = tempoList.get(Integer.parseInt(idx) - 1).getTempo_num();
					query = "INSERT INTO Preference_Tempo (tempo_num, user_id) VALUES ( '" + tempo + "', '" + user_id
							+ "' )";

					DBManager.getInstance().update(query);
				} catch (Exception e) {
					// If any exception in insert Rollback
					query = "ROLLBACK";
					try {
						DBManager.getInstance().query(query);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					success = false;
					System.out.println("Please check your number.");
					break;
				}
			}
			if (success) {
				query = "COMMIT";
				try {
					DBManager.getInstance().query(query);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			DBManager.getInstance().close();
		} while (!success);
	}

}
