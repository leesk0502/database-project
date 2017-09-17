import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {

	public Login() {
	}

	public void startProcess(){
		Scanner in = new Scanner(System.in);

		String menu;
		do {
			printLoginMenu();
			System.out.print("Select menu : ");
			menu = in.nextLine();

			switch (menu) {
			case "1":
				boolean isLogin = doLoginProcess();
				if ( isLogin) {
					Main main = new Main();
					main.startProcess();
				} else {
					System.out.println("Fail - Check your ID and Password.");
				}
				break;
			case "2":
				boolean isRegister = doRegisterProcess();
				if( isRegister ){
					System.out.println("Success - Please login with your information.");
				} else {
					System.out.println("Fail - Duplicate User ID. Please try again.");
				}
				break;
			}
		} while (true);
	}
	
	public void printLoginMenu() {
		System.out.println("********* Naver Music *********");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("*******************************");
	}

	public boolean doLoginProcess() {
		boolean isLogin = false;
		Scanner in = new Scanner(System.in);
		String id = "", password = "";
		ResultSet result = null;

		System.out.print("Enter ID : ");
		id = in.nextLine();
		System.out.print("Enter Password : ");
		password = in.nextLine();

		DBManager.getInstance().connect();
		String query = "SELECT * FROM User where user_id='" + id + "' and password='" + password + "' LIMIT 1";
		try {
			result = DBManager.getInstance().query(query);
			if (result.next()) {
				isLogin = true;
				Member member = new Member(result.getString("user_id"), result.getString("name"), result.getString("password"));
				LoginManager.getInstance().setMember(member);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		DBManager.getInstance().close();
		return isLogin;
	}
	
	public boolean doRegisterProcess(){
		boolean isRegister = false;
		Scanner in = new Scanner(System.in);
		String id="", password="", name="";		
		
		System.out.print("Enter ID : ");
		id = in.nextLine();
		System.out.print("Enter Name : ");
		name = in.nextLine();
		System.out.print("Enter Password : ");
		password = in.nextLine();

		DBManager.getInstance().connect();
		String query = "INSERT INTO User (user_id, name, password) VALUES ('"+id+"', '"+name+"', '"+password+"')";
		try {
			int result = DBManager.getInstance().update(query);
			if (result > 0) {
				isRegister = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		DBManager.getInstance().close();
		
		return isRegister;
	}
}
