import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ScoreBoard {
	public void startProcess() {
		Scanner in = new Scanner(System.in);
		String input = "", query = "";
		String user_id = LoginManager.getInstance().getMember().getUser_id();
		ResultSet result = null;

		DBManager.getInstance().connect();
		try {
			query = "SELECT a.name, IF(ars.score IS NULL, 0, ars.score) as score FROM Artist as a "
					+ "LEFT OUTER JOIN Artist_Score as ars on ars.artist_num=a.artist_num AND ars.user_id='" + user_id
					+ "' ORDER BY ars.score DESC";
			result = DBManager.getInstance().query(query);
			System.out.println("******Artist Score Board******");

			int i = 0;
			while (result.next()) {
				i++;
				System.out.format("%3d. %-20s %s\n", i, result.getString("name"), result.getString("score"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBManager.getInstance().close();

		DBManager.getInstance().connect();
		try {
			query = "SELECT g.name, IF(gs.score IS NULL, 0, gs.score) as score FROM Genre as g "
					+ "LEFT OUTER JOIN Genre_Score as gs on gs.genre_num=g.genre_num AND gs.user_id='" + user_id
					+ "' ORDER BY gs.score DESC";

			result = DBManager.getInstance().query(query);
			System.out.println("******Genre Score Board******");

			int i = 0;
			while (result.next()) {
				i++;
				System.out.format("%3d. %-20s %s\n", i, result.getString("name"), result.getString("score"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBManager.getInstance().close();
		
		DBManager.getInstance().connect();
		try {
			query = "SELECT t.name, IF(ts.score IS NULL, 0, ts.score) as score FROM Tempo as t "
					+ "LEFT OUTER JOIN Tempo_Score as ts on ts.tempo_num=t.tempo_num AND ts.user_id='" + user_id
					+ "' ORDER BY ts.score DESC";
			result = DBManager.getInstance().query(query);
			System.out.println("******Tempo Score Board******");

			int i = 0;
			while (result.next()) {
				i++;
				System.out.format("%3d. %-20s %s\n", i, result.getString("name"), result.getString("score"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBManager.getInstance().close();
	}
}
