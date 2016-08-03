import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Piece {
	public String Color;
	public int Rank;
	public int Range;
	BufferedImage piece;
	BufferedImage insignia;
	public Piece(String ID)
	{
		if(ID == "blank")
		{
			piece = null;
			insignia = null;
			Range = 0;
			Rank = -3;
			Color = "";
			return;
		}
		String firstChar = ID.substring(0,1);
		System.out.println(firstChar);
		String lastChar = ID.substring(1,2);
		System.out.println(lastChar);
		if(firstChar.equals("R"))
		{
			Color = "Red";
			try {
				piece = ImageIO.read(new File("src/imgs/RED.png"));
			} catch (IOException ex) {
				System.out.println("Error with file loading.");
				System.out.println("src/imgs/RED.png");}
		}
		else if(firstChar.equals("B"))
		{
			Color = "Blue";
			try {
				piece = ImageIO.read(new File("src/imgs/BLUE.png"));
			} catch (IOException ex) {
				System.out.println("Error with file loading.");}
		}
		if(lastChar.equals("S"))
		{
			Rank = 10;
		}
		else if(lastChar.equals("B"))
		{
			Rank = 0;
		}
		else if(lastChar.equals("F"))
		{
			Rank = -1;
		}
		else
		{
			Rank = Integer.parseInt(lastChar);
		}
		try {
			insignia = ImageIO.read(new File("src/imgs/"+Color+lastChar+".png"));
		} catch (IOException ex) {
			System.out.println("Error with file loading.");
			System.out.println("src/imgs/"+Color+lastChar+".png");}
	}
	
	public int Attack(Piece other)
	{
		if(other.Rank == -1)
			return 2;
		else if(other.Rank == 0)
		{
			if(Rank != 8)
				return -1;
			else
				return 1;
		}
		else if(Rank == 10 && other.Rank == 1)
			return 1;
		else if(Rank < other.Rank)
			return 1;
		else if(Rank > other.Rank)
			return -1;
		else
			return 0;
	}
}
