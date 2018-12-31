package SudokuSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LinkedGrid {
	
	Scanner input = new Scanner(System.in);
	private Cell first;
	
	
	private int dimension;
	
	public LinkedGrid(int dimension)
	{
		this.dimension = dimension;
		
		if(dimension == 1) //If the input dimension is one, there will be only one Cell
			first = new Cell();
		else if(dimension <= 0) //If it is less than one, than there will be no Cell
			first = null;
		else //Creating the linked grid
		{
			first = new Cell();
			Cell columnMarker = first;
			Cell rowMarker = first;
			Cell temp;
			
			
			for(int x = 0; x < dimension-1; x++)
			{
				temp  = new Cell();
				columnMarker.setRight(temp);
				temp.setLeft(columnMarker);
				
				columnMarker = temp;
			}
			
			for(int y = 0; y < dimension - 1; y++)
			{
				//Making the first Cell in the new row
				temp = new Cell();
				rowMarker.setDown(temp);
				temp.setUp(rowMarker);
				rowMarker = temp;
				
				//Making the rest of the row
				columnMarker = rowMarker;
				
				for(int x = 0; x < dimension-1; x++)
				{
					temp = new Cell();
					temp.setLeft(columnMarker);
					columnMarker.setRight(temp);
					temp.setUp(temp.getLeft().getUp().getRight());
					temp.getLeft().getUp().getRight().setDown(temp);
					columnMarker = temp;
				}
			}
			
			
			rowMarker = first;
			columnMarker = first;
			
			//Sets up the square coordinates for each Cell
			for(int y = 0; y < 9; y+=3) //Go to all three rows of squares
			{
				for(int x = 0; x < 3; x++)//Go to all three rows of that square
				{
					for(int a = 1; a <= 3; a++)//Go to all three columns of squares in that row
					{
						for(int b = 0; b < 3; b++)//Go horizontally to all three squares of the row in that square
						{
							columnMarker.setSqCoord(a+y);
							columnMarker = columnMarker.getRight();
						}
					}
					rowMarker = rowMarker.getDown();
					columnMarker = rowMarker;
				}
			}
		}
	}
	
	public void display() //Display the linked grid number value.
	{
		Cell temp = first;
		Cell rowMarker = first;
		
		while (temp != null) 
		{
			while (temp != null)
			{
				System.out.print(temp.getNumber() + " ");
				temp = temp.getRight();
			}
			System.out.println();
			rowMarker = rowMarker.getDown();
			temp = rowMarker;
		}
	}
	
	public void populateGridFromFile() throws FileNotFoundException{ //Take data from a .txt file and implement it into the linked grid.
		
		File infile = new File("C:\\Users\\Albert Frost\\eclipse-workspace\\ComputerScience30\\src\\SudokuSolver\\data.txt");
		Scanner input = new Scanner(infile);

		Cell columnMarker = first;
		Cell rowMarker = first;
		
		int counter = 0;
		
		while(true)
		{	
			for(int y = 0; y < dimension; y++) //taking data from .txt file and putting into the linked grid.
			{
				columnMarker.setNumber(input.nextInt());
				columnMarker = columnMarker.getRight();
			}
			if (input.hasNextLine())//checks to see if there is a next line
			{
				input.nextLine();
				rowMarker = rowMarker.getDown();
				columnMarker = rowMarker;
			}
			else //breaks the while loop when there are no more lines.
				break;
		}
	}
	

	public Cell getFirst() {
		return first;
	}

	public void setFirst(Cell first) {
		this.first = first;
	}
	
	public boolean isGridCompleted()
	{
		boolean isCompleted = true;
		
		Cell rowMarker = first;
		Cell columnMarker = rowMarker;
		
		while(rowMarker != null)
		{
			while(columnMarker != null)
			{
				if(columnMarker.getNumber() == 0)
					isCompleted = false;
				columnMarker = columnMarker.getRight();
			}
			rowMarker = rowMarker.getDown();
			columnMarker = rowMarker;
		}
		
		return isCompleted;
	}
	
}
