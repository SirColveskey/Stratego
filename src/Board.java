import java.awt.Graphics;

import java.util.Arrays;
import java.util.ArrayList;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Box.Filler;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

public class Board {
	public String Winner = "";
	public Piece[][] Squares = new Piece[10][10];
	public String Turn;
	public int[] RedPieces;
	public int[] BluePieces;
	public int[] Pools = new int[22];
	public JFrame Window;
	public GamePanel Canvas;

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
		Turn = "Blue";
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
			{
				Squares[i][j] = new Piece("blank");
			}
		}
		Squares[0][0] = new Piece("R1");
		Squares[3][4] = new Piece("B7");
		Squares[3][6] = new Piece("R7");
		Muster();
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
		
		for (String p : pieces)
		{ //Populate lists of pieces
		 	p1Pieces.add(new Piece("R" + p));
		 	p2Pieces.add(new Piece("B" + p));
		}
		for (Piece p : p1Pieces)
		{ //test that lists populated correctly. Prints in console properly
			System.out.print(p.Rank);
			Squares[1][1] = p; //doesn't work either
		}
		for (int i=0; i<10; ++i)
		{ //place pieces in order in starting positions. Doesn't work???
			for (int j=0; j<4; ++j)
			{
				Squares[i][j] = p1Pieces.get(0);
				p1Pieces.remove(0);
			}
			
		}
		
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
	}
	
	public void CheckGameover()
	{
		
	}
	
	public void GetMove()
	{
		
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
}

class GamePanel extends JPanel {
	public Piece BLANK_PIECE;
	public BufferedImage bgImage;
	public PiecePanel[][] grid = new PiecePanel[10][10];
	public JPanel[] nums = new JPanel[22];
	public JLabel[] lab = new JLabel[22];
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
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(1200,900);
	}
	
}

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
