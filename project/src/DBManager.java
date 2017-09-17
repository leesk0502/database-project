
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	public final static String DBName = "teamC";
	public final static String DBURL = "jdbc:mysql://db.huy.kr:3306/" + DBName + "?autoReconnect=true";
	public final static String DBID = "team";
	public final static String DBPW = "team2016";

	public final static String TABLE_CATEGORY = "category";
	public final static String TABLE_PURCHASE = "purchase";
	public final static String TABLE_BOOK = "book";

	private Connection mConn;
	private Statement mStatement;

	private DBManager() {};

	private static class SingleHolder {
		public static DBManager single = new DBManager();
	}

	public static DBManager getInstance() {
		return SingleHolder.single;
	}

	/**
	 *
	 * @param query
	 *            String The query to be executed
	 * @return a ResultSet object containing the results or null if not
	 *         available
	 * @throws SQLException
	 */
	public ResultSet query(String query) throws SQLException {
		mStatement = mConn.createStatement();
		ResultSet res = mStatement.executeQuery(query);
		return res;
	}

	/**
	 * @desc Method to insert, update, delete data to a table
	 * @param insertQuery
	 *            String The Insert query
	 * @return boolean
	 * @throws SQLException
	 */
	public int update(String insertQuery) throws SQLException {
		mStatement = mConn.createStatement();
		int result = mStatement.executeUpdate(insertQuery);
		return result;
	}
	
	public void connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			mConn = DriverManager.getConnection(DBURL, DBID, DBPW);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			mStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}