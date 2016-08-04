import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Piece implements Cloneable, Serializable{
	public String Color;
	public int Rank;
	public int Range;
	transient public BufferedImage piece;
	transient public BufferedImage insignia;
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
		if(ID == "lake")
		{
			piece = null;
			insignia = null;
			Range = 0;
			Rank = -3;
			Color = "lake";
			return;
		}
		String firstChar = ID.substring(0,1);
		//System.out.println(firstChar);
		String lastChar = ID.substring(1,2);
		//System.out.println(lastChar);
		if(firstChar.equals("R"))
		{
			Color = "Red";
			try {
				piece = ImageIO.read(Piece.class.getResourceAsStream("/resources/RED.png"));
			} catch (IOException ex) {
				System.out.println("Error with file loading.");
				System.out.println("src/resources/RED.png");}
		}
		else if(firstChar.equals("B"))
		{
			Color = "Blue";
			try {
				piece = ImageIO.read(Piece.class.getResourceAsStream("/resources/BLUE.png"));
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
			insignia = ImageIO.read(Piece.class.getResourceAsStream("/resources/"+Color+lastChar+".png"));
		} catch (IOException ex) {
			System.out.println("Error with file loading.");
			System.out.println("/resources/"+Color+lastChar+".png");}
		if(Rank == 9)
			Range = 10;
		else if(Rank == 0 || Rank == -1)
			Range = 0;
		else
			Range = 1;
	}
	
	public void Reimage()
	{
		String lastChar;
		if(Color.equals("Red")){
			try {
				piece = ImageIO.read(Piece.class.getResourceAsStream("/resources/RED.png"));
			} catch (IOException ex) {
				System.out.println("Error with file loading.");
				System.out.println("/resources/RED.png");}
		}
		if(Color.equals("Blue")){
			try {
				piece = ImageIO.read(Piece.class.getResourceAsStream("/resources/BLUE.png"));
			} catch (IOException ex) {
				System.out.println("Error with file loading.");}}
		if(Rank == -1)
		{
			lastChar = "F";
		}
		else if(Rank == 0)
		{
			lastChar = "B";
		}
		else if(Rank == 10)
		{
			lastChar = "S";
		}
		else if(Rank == -3)
		{
			return;
		}
		else
		{
			lastChar = Rank+"";
		}
		try {
			insignia = ImageIO.read(Piece.class.getResourceAsStream("/resources/"+Color+lastChar+".png"));
		} catch (IOException ex) {
			System.out.println("Error with file loading.");
			System.out.println("/resources/"+Color+lastChar+".png");}
		
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
	
	@Override
	public Piece clone()
	{
		String color = "R";
		String rank = Rank + "";
		if(Color == "Blue")
			color = "B";
		if(Rank == 10)
			rank = "S";
		if(Rank == 0)
			rank = "B";
		if(Rank == -1)
			rank = "F";
		//System.out.println(color+rank);
		return new Piece(color+rank);
	}
}
