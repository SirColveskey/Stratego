import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
public class Driver {
	public static void main(String[] args)
	{
		Board board = new Board();
		board.Flip();
		board.SetupMatch();
		board.Flip();
		/*board.CheckGameover();
		while(true)
		{
			board.GetMove();
			board.Muster();
			board.CheckGameover();
			if(board.winner != "")
				break;
			if(board.turn == "Red")
				board.turn = "Blue";
			if(board.turn == "Blue")
				board.turn == "Red";
			board.Blank();*/
	//	}
		//System.out.println("The winner is: "+board.winner);
	}
}
