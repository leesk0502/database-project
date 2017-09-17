import java.util.Scanner;

public class Main {
		
	public void startProcess(){
		Scanner in = new Scanner(System.in);

		String menu;
		do {
			prinMainMenu();
			System.out.print("Select menu : ");
			menu = in.nextLine();

			switch (menu) {
			case "1":
				MusicSearch musicSearch = new MusicSearch();
				musicSearch.startProcess();
				break;
			case "2":
				PlayList playList = new PlayList();
				playList.startProcess();
				break;
			case "3":
				Recommend recommend = new Recommend();
				recommend.startProcess();
				break;
			case "4":
				ScoreBoard scoreBoard = new ScoreBoard();
				scoreBoard.startProcess();
				break;
			}
		} while (true);
	}
	
	public void prinMainMenu(){
		
		System.out.println();
		System.out.println();
		
		System.out.println("********* Naver Music *********");
		System.out.println("1. Music Search");
		System.out.println("2. Play List");
		System.out.println("3. Recommend Music");
		System.out.println("4. Show score board");
		System.out.println("*******************************");
		
		System.out.println();
		System.out.println();
	}
}
