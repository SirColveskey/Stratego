import java.awt.Graphics;

import java.util.Arrays;
import java.util.ArrayList;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.event.MouseMotionListener;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box.Filler;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.lang.Math;

public class Board implements MouseListener, MouseMotionListener{
	public String Winner = "";
	public Piece[][] Squares = new Piece[10][10];
	public String Turn;
	public String Dialogue = "";
	public int[] RedPieces;
	public int[] BluePieces;
	public int[] Pools = new int[22];
	public JFrame Window;
	public GamePanel Canvas;
	public boolean MouseClicked = false;
	public int X1 = -1;
	public int Y1 = -1;
	public int X2 = -1;
	public int Y2 = -1;
	public int posx;
	public int posy;
	public String State;
	public ArrayList<Piece> activePieces;
	public int index = 0;
	public ArrayList<Piece> secondaryPieces;

	
	public Board()
	{
		Window = new JFrame("Stratego");
		Canvas = new GamePanel();
		Canvas.setVisible(true);
		Window.add( Canvas );
		Window.setResizable(false);
		Window.pack();
		Window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		Window.setVisible( true );	
		Turn = "Red";
		Canvas.playArea.addMouseListener(this);
		Canvas.playArea.addMouseMotionListener(this);
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
			{
				Squares[i][j] = new Piece("blank");
				if((j == 2 && i == 4) || (j == 3 && i == 4) || (j == 2 && i == 5) || (j == 3 && i == 5) 
						|| (j == 6 && i == 4) || (j == 7 && i == 4) || (j == 6 && i == 5) || (j == 7 && i == 5))
					Squares[i][j] = new Piece("lake");
					
			}
		}	
		Flip();
	}
	
	public void SetupMatch()
	{
		String[] pieces = {
	            "B","B","B","B","B","B", /*Bombs*/
	            "1","2","3","3",         /*Marshal, General, Colonels*/
	            "4","4","4",             /*Majors*/
	            "5","5","5","5",         /*Captians*/
	            "6","6","6","6",         /*Lieutenants*/
	            "7","7","7","7",         /*Sergant*/
	            "8","8","8","8","8",     /*Miner*/
	            "9","9","9","9","9","9","9","9", /*Scouts*/
	            "S", "F"};               /*Spy, Flag*/
		
		ArrayList<Piece> p1Pieces = new ArrayList<Piece>();
		ArrayList<Piece> p2Pieces = new ArrayList<Piece>();
		
		for (int i=0; i<40; ++i)
		{ //Populate lists of pieces
		 	p1Pieces.add(new Piece("R" + pieces[i]));
		 	p2Pieces.add(new Piece("B" + pieces[i]));
		}
		secondaryPieces = p2Pieces;
		activePieces = p1Pieces;
		index = 0;
		State = "Red Placement";
		
	} 
	
	public void Flip()
	{
		boolean result;
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
			{
				result = (Squares[i][j].Color == Turn);
				Canvas.grid[i][j].NewPiece(Squares[i][j],result);
			}
		}
		Canvas.repaint();
	}
	
	public void ChangeTurns()
	{
		if(Dialogue == "")
		{
			Dialogue = "The current player has completed their turn. Next player, you're up!";
		}
		JPanel hold = new JPanel();
		
		JDialog turnChange = new JDialog(Window, Dialogue, true);
		turnChange.setLocationRelativeTo(Canvas.playArea);
		JOptionPane.showMessageDialog(turnChange, Dialogue);
		turnChange.getContentPane().add(hold);
		Dialogue = "";
		//turnChange.pack();
		//turnChange.setVisible(true);
	}
	
	public void CheckGameover()
	{
		
	}
	
	public void GetMove()
	{
		if(Squares[Y1][X1].Color != Turn) //Only move own color
			return;
		System.out.println("Passed Stage 1");
		if(Squares[Y2][X2].Color == Turn) //Don't move on own color
			return;
		System.out.println("Passed Stage 2");
		if(Squares[Y2][X2].Color == "lake") //Lakes!
			return;
		System.out.println("Passed Stage 3");
		if((X1 != X2 && Y1 != Y2) || (X1 == X2 && Y1 == Y2)) //Move in a straight line
			return;
		System.out.println("Passed Stage 4");
		System.out.println(X1 + " " + X2 + " " + Y1 + " " + Y2);
		if(Squares[Y1][X1].Range < Math.abs((X1 - X2) + (Y1 - Y2))) //Move in piece range
			return;
		System.out.println("Passed Stage 5");
		if (Math.abs((X1 - X2) + (Y1 - Y2)) > 1) //if movement > 1
		{
			
			if(X1 != X2) //If the movement is on X
			{
				int start=X1, stop=X2;
				if (X1 > X2)
				{
					start = X2;
					stop = X1;
				}
				for (int i=start+1; i<stop-1; i++) //Check intermediate spaces for obstacles 
				{
					if(Squares[Y1][i].Color != "")
						return;
				}
			}
			else //If the movement is on Y
			{
				int start=Y1, stop=Y2;
				if (Y1 > Y2)
				{
					start = Y2;
					stop = Y1;
				}
				for (int i=start+1; i<stop-1; i++)
				{
					if(Squares[i][X1].Color != "")
						return;
				}		
			}
		}
		System.out.println("Passed Stage 6 -- Successful Move");
		//move processing:
		int result = -2;
		if(Squares[Y2][X2].Color == "")
		{
			Squares[Y2][X2] = Squares[Y1][X1];
			Squares[Y1][X1] = new Piece("blank");
		}
		else
		{
			result = Squares[Y1][X1].Attack(Squares[Y2][X2]);
		}
		if(result == 1)
		{
			String DefRank;
			String AtkRank;
			if(Squares[Y1][X1].Rank == 10)
				AtkRank = "Spy";
			else if(Squares[Y1][X1].Rank == 0)
				AtkRank = "Bomb";
			else
				AtkRank = Squares[Y1][X1].Rank + "";
			if(Squares[Y2][X2].Rank == 10)
				DefRank = "Spy";
			else if(Squares[Y2][X2].Rank == 0)
				DefRank = "Bomb";
			else
				DefRank = Squares[Y2][X2].Rank + "";
			Dialogue = Squares[Y1][X1].Color + " Has defeated " + Squares[Y2][X2].Color + "'s piece: " + DefRank + " with a " + AtkRank;
			Squares[Y2][X2] = Squares[Y1][X1];
			Squares[Y1][X1] = new Piece("blank");
		}
		if(result == -1)
		{
			String DefRank;
			String AtkRank;
			if(Squares[Y1][X1].Rank == 10)
				AtkRank = "Spy";
			else if(Squares[Y1][X1].Rank == 0)
				AtkRank = "Bomb";
			else
				AtkRank = Squares[Y1][X1].Rank + "";
			if(Squares[Y2][X2].Rank == 10)
				DefRank = "Spy";
			else if(Squares[Y2][X2].Rank == 0)
				DefRank = "Bomb";
			else
				DefRank = Squares[Y2][X2].Rank + "";
			Dialogue = Squares[Y1][X1].Color + " Has been defeated by " + Squares[Y2][X2].Color + "'s piece: " + DefRank + " with a " + AtkRank;
			Squares[Y1][X1] = new Piece("blank");
		}
		if(result == 0)
		{
			String DefRank;
			String AtkRank;
			if(Squares[Y1][X1].Rank == 10)
				AtkRank = "Spy";
			else if(Squares[Y1][X1].Rank == 0)
				AtkRank = "Bomb";
			else
				AtkRank = Squares[Y1][X1].Rank + "";
			if(Squares[Y2][X2].Rank == 10)
				DefRank = "Spy";
			else if(Squares[Y2][X2].Rank == 0)
				DefRank = "Bomb";
			else
				DefRank = Squares[Y2][X2].Rank + "";
			Dialogue = Squares[Y1][X1].Color + " Has tied " + Squares[Y2][X2].Color + "'s piece: " + DefRank + " with a " + AtkRank + ". Both have been defeated.";
			Squares[Y1][X1] = new Piece("blank");
			Squares[Y2][X2] = new Piece("blank");
		}
		if(result == 2)
		{
			FlagCapture(Turn);
			Muster();
			Flip();
			return;
		}
		Turn = "";
		Canvas.CursorPiece = new Piece("blank");
		Muster();
		Flip();
		ChangeTurns();
		if(State == "Red Turn")
		{
			Turn = "Blue";
		}
		if(State == "Blue Turn")
		{
			Turn = "Red";
		}
		if(Turn == "Blue")
		{
			State = "Blue Turn";
		}
		else
		{
			State = "Red Turn";
		}
	}
	
	public void Muster()
	{
		for(int i=0; i<22; i++)
		{
			Pools[i] = 0;
		}
		int increment;
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
			{
				if(Squares[i][j].Rank == -1)
					continue;
				if(Squares[i][j].Color == "Red")
				{
					increment = 0;
				}
				else if(Squares[i][j].Color == "Blue")
				{
					increment = 11;
				}
				else
					continue;
				Pools[increment + Squares[i][j].Rank] += 1;
			}
		}
		Canvas.UpdatePools(Pools);
	}
	
	public void FlagCapture(String Color)
	{
		
	}
	
	public void LoadMatch(String name)
	{
		
	}
	
	public void SaveMatch(String name)
	{
		
	}
	
	public void Blank()
	{
		//Show blank screen with text "Hit Space to Continue" or something of the like
		//For turn swaps
	}
	
	public void drawPiece(Graphics g){
		g.drawImage(activePieces.get(0).insignia,posx,posy,null);
		Flip();
	}
	
	public void PlacePiece()
	{
		if (State == "Red Placement")
		{
			if (Y1 > 3)
				return;
			if (Squares[Y1][X1].Color != "")
				return;
		}
		if (State == "Blue Placement")
		{
			if (Y1 < 6)
				return;
			if (Squares[Y1][X1].Color != "")
				return;
		}
		Squares[Y1][X1] = activePieces.get(0);
		activePieces.remove(0);
		if(activePieces.isEmpty())
		{
			String CurrentState = State;
			activePieces = secondaryPieces;
			Turn = "";
			Muster();
			Flip();
			X1 = Y1 = -1;
			if (CurrentState == "Red Placement")
			{
				Canvas.CursorPiece = new Piece("blank");
				Flip();
				ChangeTurns();
				Turn = "Blue";
				State = "Blue Placement";
			}
			if (CurrentState == "Blue Placement")
			{
				Turn = "";
				Canvas.CursorPiece = new Piece("blank");
				Flip();
				ChangeTurns();
				Turn = "Red";
				State = "Red Turn";
			}
		}
		Muster();
		Flip();
	}
	
	
	public void mousePressed(MouseEvent e){
		X1 = e.getX()/75;
		Y1 = e.getY()/75;
		if(State == "Red Placement")
		{
			PlacePiece();
		}
		if(State == "Blue Placement")
		{
			PlacePiece();
		}
		if(State == "Red Turn")
		{
			if(Y1 != -1 && X1 != -1 && Squares[Y1][X1].Color == "Red")
				Canvas.CursorPiece = Squares[Y1][X1];
			else
				Canvas.CursorPiece = new Piece("blank");
				
		}
		if(State == "Blue Turn")
		{
			if(Y1 != -1 && X1 != -1 && Squares[Y1][X1].Color == "Blue")
				Canvas.CursorPiece = Squares[Y1][X1];
			else
				Canvas.CursorPiece = new Piece("blank");
				
		}
		
		//System.out.println("The mouse was clicked down at "+X+" "+Y);
	}
	public void mouseReleased(MouseEvent e){
		X2 = e.getX()/75;
		Y2 = e.getY()/75;
		
		if(State == "Red Turn")
		{
			GetMove();
			Canvas.CursorPiece = new Piece("blank");
		}
		if(State == "Blue Turn")
		{
			GetMove();
			Canvas.CursorPiece = new Piece("blank");
		}
		
		//System.out.println("The mouse was clicked up");	
	}
	public void mouseEntered(MouseEvent e){
		if (State == "Red Placement" || State == "Blue Placement")
			Canvas.CursorPiece = activePieces.get(0);
		Flip();
		//System.out.println("The mouse has entered the building");		
	}
	public void mouseExited(MouseEvent e){
		X1 = Y1 = X2 = Y2 = -1;
		Canvas.CursorPiece = new Piece("blank");
		Flip();
		//System.out.println("The mouse has left the building");
	}
	public void mouseClicked(MouseEvent e){
		//Doesn't Matter
	}
	public void mouseDragged(MouseEvent e){
		Canvas.X = e.getX();
		Canvas.Y = e.getY();
		if (State == "Red Placement" || State == "Blue Placement")
			Canvas.CursorPiece = activePieces.get(0);
		Flip();
	}
	public void mouseMoved(MouseEvent e){
		Canvas.X = e.getX();
		Canvas.Y = e.getY();
		if (State == "Red Placement" || State == "Blue Placement")
			Canvas.CursorPiece = activePieces.get(0);
		Flip();
	}
	
}

class GamePanel extends JPanel {
	public int[] Selection1 = {-1,-1};
	public int[] Selection2 = {-1,-1};
	public Piece BLANK_PIECE;
	public BufferedImage bgImage;
	public PiecePanel[][] grid = new PiecePanel[10][10];
	public JPanel[] nums = new JPanel[22];
	public JLabel[] lab = new JLabel[22];
	public Box playArea;
	public Piece CursorPiece;
	public int X,Y;
	public GamePanel(){
		BLANK_PIECE = new Piece("blank");
		Box[] Base = new Box[3];
		Box[] Center = new Box[3];
		Box[] Collumn = new Box[11];
		int[] CollumnHs = {210,37,63,37,37,144,37,63,37,37,25};
		int[] CollumnWs = {400,0,400,0,0,400,0,400,0,0,400};
		Box[] Logs = new Box[10];
		try {
			bgImage = ImageIO.read(new File("src/imgs/Board.png"));
		} catch (IOException ex) {
			System.out.println("Error with file loading.");
		}
		for(int i=0; i<22; i++)
		{
			nums[i] = new JPanel();
			nums[i].setOpaque(false);
			lab[i] = new JLabel();
			lab[i].setText("0");
			nums[i].add(lab[i]);
		}
		for(int i=0; i < 10; i++)
		{
			Logs[i] = Box.createHorizontalBox();
			//Logs[i].add(Box.createRigidArea(new Dimension(750,0)));
			for(int j=0; j<10; j++)
			{
				grid[i][j] = new PiecePanel(BLANK_PIECE,false);
				Logs[i].add(grid[i][j]);
			}
		}
		for(int i=0; i < 11; i++)
		{
			Collumn[i] = Box.createHorizontalBox();
			Collumn[i].add(Box.createRigidArea(new Dimension(CollumnWs[i],CollumnHs[i])));
		}
		//Left Collumn Pool Additions 
		Collumn[1].add(nums[0]);
		Collumn[1].add(nums[9]);
		Collumn[1].add(nums[8]);
		Collumn[1].add(nums[7]);
		Collumn[1].add(nums[6]);
		Collumn[3].add(nums[5]);
		Collumn[3].add(nums[4]);
		Collumn[3].add(nums[3]);
		Collumn[3].add(nums[2]);
		Collumn[3].add(nums[1]);
		Collumn[4].add(nums[10]);
		Collumn[6].add(nums[11]);
		Collumn[6].add(nums[20]);
		Collumn[6].add(nums[19]);
		Collumn[6].add(nums[18]);
		Collumn[6].add(nums[17]);
		Collumn[8].add(nums[16]);
		Collumn[8].add(nums[15]);
		Collumn[8].add(nums[14]);
		Collumn[8].add(nums[13]);
		Collumn[8].add(nums[12]);
		Collumn[9].add(nums[21]);
		//Center Panel
		Center[0] = Box.createVerticalBox();
		Center[0].add(Box.createRigidArea(new Dimension(405,0)));
		Center[1] = Box.createVerticalBox();
		playArea = Center[1];
		//Center[1].addMouseListener(this);
		//Center[1].add(new JButton("Button Y"));
		Center[1].add(Box.createRigidArea(new Dimension(750,0)));
		//Center[1].add(new JButton("Button Y"));
		Center[2] = Box.createVerticalBox();
		//Center[2].add(new JButton("Button Z"));
		Center[2].add(Box.createRigidArea(new Dimension(45,0)));
		//Center[2].add(new JButton("Button Z"));
		Base[0] = Box.createHorizontalBox();
		Base[0].add(Box.createRigidArea(new Dimension(0,75)));
		Base[0].add(new JButton("Button 1"));
		Base[0].add(new JButton("Button 1"));
		Base[1] = Box.createHorizontalBox();
		Base[1].add(Box.createRigidArea(new Dimension(0,750)));
		for(Box x : Collumn)
		{
			x.setBorder(BorderFactory.createLineBorder(Color.black));
			Center[0].add(x);
		}
		for(Box x : Logs)
		{
			Center[1].add(x);
		}
		for(Box x : Center)
		{
			x.setBorder(BorderFactory.createLineBorder(Color.black));
			Base[1].add(x);
		}
		Base[2] = Box.createHorizontalBox();
		Base[2].add(Box.createRigidArea(new Dimension(0,75)));
		Base[2].add(new JButton("Button 3"));
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);
		for(Box x : Base)
		{
			x.setBorder(BorderFactory.createLineBorder(Color.black));
			add(x);
		}
	}
	
	public void UpdatePools(int[] Pools)
	{
		for(int i=0; i<22; i++)
		{
			lab[i].setText(Integer.toString(Pools[i]));
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(bgImage, 0, 0, null);
		if(CursorPiece != null){
			if(CursorPiece.Color != ""){
				g.drawImage(CursorPiece.insignia, X + 360, Y + 40, null);
			}}
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(1200,900);
	}
}
/*
class MousePanel extends JPanel{
	public BufferedImage Castle;
	public BufferedImage Rank;
	public int X;
	public int Y;
	public MousePanel(Piece piece, int x, int y){
		X = x;
		Y = y;
		Castle = piece.piece;
		Rank = piece.insignia;
		setOpaque(false);
	}
	public void UpdatePosition(int x, int y){
		X = x;
		Y = y;
	}
	public void NewPiece(Piece piece){
		Castle = piece.piece;
		Rank = piece.insignia;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(Castle, X, Y, null);
		g.drawImage(Rank, X, Y, null);
	}
	public Dimension getPreferredSize()
	{
		return new Dimension(75,75);
	}
}*/
class PiecePanel extends JPanel{
	public BufferedImage Castle;
	public BufferedImage Rank;
	public boolean YourTurn;
	public PiecePanel(Piece piece, boolean Turn)
	{
		YourTurn = Turn;
		Castle = piece.piece;
		Rank = piece.insignia;
		setOpaque(false);
	}
	
	public void NewPiece(Piece piece, boolean Turn)
	{
		YourTurn = Turn;
		Castle = piece.piece;
		Rank = piece.insignia;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(Castle != null)
			g.drawImage(Castle, 0, 0, null);
		if(YourTurn)
		{
			if(Rank != null)
				g.drawImage(Rank, 0, 0, null);
		}
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(75,75);
	}
}
