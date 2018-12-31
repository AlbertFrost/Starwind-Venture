package SudokuSolver;

public class SolvingMethods {

	Cell first;
	Cell columnMarker;
	Cell rowMarker;
	
	LinkedGrid Grid;
	
	boolean change; //Variable used to check if the solving methods did solve any of the cells
	
	public SolvingMethods(LinkedGrid Grid)
	{
		this.Grid = Grid;
		first = Grid.getFirst();
		change = true;
	}
	
	/*
	 * The solve() method is separated from the constructor unlike in the guesser class to avoid the over use of fullInitialCheck() method
	 */
	public void solve()
	{
		while(change == true)
		{
			change = false;
			
			singlePotentialCheckMethod();
			onlyPotentialInSectionCheckMethod();
			samePotentialCountCheckMethod(); //Currently does not work
		}
		
		Guesser guesser = new Guesser(Grid);
		Grid = guesser.run();
		
	}
	
	private void singlePotentialCheckMethod()//Does a complete check of each Cell to see if it has only one potential it can be
	{
		System.out.println("Starting Method 1...");
		
		rowMarker = first;
		columnMarker = first;
		
		while(rowMarker != null)//Vertically move the marker until the end
		{
			while(columnMarker != null)//Horizontally move the marker until the end
			{
				if(columnMarker.getPotentialNumber(0) == false);
					singlePotential(columnMarker);
					
				columnMarker = columnMarker.getRight();//Move the marker to the right
			}
			rowMarker = rowMarker.getDown();//Move the marker down
			columnMarker = rowMarker;
		}
	}
	
	public void onlyPotentialInSectionCheckMethod()//Method to see if there is one Cell that has one potential that no other Cell in that square, row or column
	{
		System.out.println("Starting Method 2...");
		
		columnMarker = first;
		rowMarker = first;
		Cell cellColumnMarker;
		Cell cellRowMarker;
		
		int []potentialCount = new int [10];//The array is used to count for many potentials of a number is in the section
		
		for(int i = 1; i < 10;  i++)
			potentialCount[i] = 0;
		
		/*
		 * This part of the method does the square section check
		 * The columnMarker goes to each square at the top-right cell, then the cellColumnMarker and cellRowMarker are set to the columnMarker
		 * The cellColumnMarker moves to the right to check each cell, and the cellRowMarker helps the cellColumnMarker move down the rows
		 * Every time the cellColumnMarker goes onto an unsolved cell, it checks the potentials and counts them all into an array
		 * The array counts the potentials of that section
 		 */
		for(int y = 0; y < 2; y++)//for loop to move down the row in the grid of squares
		{
			for(int x = 0; x < 2; x++)//for loop to move right in the grid of squares
			{
				cellColumnMarker = columnMarker;//set markers in the square
				cellRowMarker = cellColumnMarker;
				
				for(int a = 0; a <= 2; a++)//for loop to move down the row in the square of Cells
				{
					for(int b = 0; b <= 2; b++)//for loop to move down the column in the square of Cells
					{
						for(int i = 1; i < 10; i++)
							if(cellColumnMarker.getPotentialNumber(i) == true)//find what potentials the Cell contains
								potentialCount[i]++;
						
						cellColumnMarker = cellColumnMarker.getRight();
					}
					cellRowMarker = cellRowMarker.getDown();
					cellColumnMarker = cellRowMarker;
				}

				
				int potentialNumber = checkForSinglePotentialWithArray(potentialCount);/*Obtains the number value that is only a potential once in the section, 
				if the return value is 0, then there is no number value seen only once as a potential in the section*/
				if(potentialNumber > 0)
					{
						cellColumnMarker = columnMarker;//set markers in the square
						cellRowMarker = cellColumnMarker;
						
						for(int a = 0; a <= 2; a++)//for loop to move down the row in the square of Cells
						{
							for(int b = 0; b <= 2; b++)//for loop to move down the column in the square of Cells
							{
								if(cellColumnMarker.getPotentialNumber(potentialNumber) == true)
									changeCell(cellColumnMarker, potentialNumber);
								
								cellColumnMarker = cellColumnMarker.getRight();//move right the square horizontal marker
							}
							cellRowMarker = cellRowMarker.getDown();//move down the square vertical marker
							cellColumnMarker = cellRowMarker;
						}
					}
				
				for(int i = 1; i < 10;  i++)//reset array
					potentialCount[i] = 0;
				
				columnMarker = columnMarker.getRight().getRight().getRight();//move to next right square in the grid
			}
			rowMarker = rowMarker.getDown().getDown().getDown();//move to the next down square in the grid
			columnMarker = rowMarker;
		}
		
		
		//This part of the code does the row check
		rowMarker = first;
		columnMarker = rowMarker;
		
		for(int i = 1; i < 10;  i++)//reset array
			potentialCount[i] = 0;
		
		while(rowMarker != null)
		{
			while(columnMarker != null)
			{
				for(int i = 1; i < 10; i++)
					if(columnMarker.getPotentialNumber(i) == true)//find what potentials the Cell contains
						potentialCount[i]++;
				
				columnMarker = columnMarker.getRight();
			}

			int potentialNumber = checkForSinglePotentialWithArray(potentialCount);/*Obtains the number value that is only a potential once in the section, 
			if the return value is 0, then there is no number value seen only once as a potential in the section*/
			if(potentialNumber > 0)
			{
				columnMarker = rowMarker;//Sets columnMarker back to the start to find cell with the one potential
				
				while(columnMarker.getPotentialNumber(potentialNumber) == false)//Find the cell that has the one potential
					columnMarker = columnMarker.getRight();
				
				changeCell(columnMarker, potentialNumber);
			}
			
			for(int i = 1; i < 10;  i++)//reset array
				potentialCount[i] = 0;
			
			rowMarker = rowMarker.getDown();//Moves row marker down and sets columnMarker to row marker
			columnMarker = rowMarker;
		}
		
		//This part of the code does the column check
		rowMarker = first;
		columnMarker = rowMarker;
		
		for(int i = 1; i < 10;  i++)//reset array
			potentialCount[i] = 0;
		
		while(columnMarker != null)
		{
			while(rowMarker != null)
			{
				for(int i = 1; i < 10; i++)
					if(rowMarker.getPotentialNumber(i) == true)//find what potentials the Cell contains
						potentialCount[i]++;
				
				rowMarker = rowMarker.getDown();
			}

			int potentialNumber = checkForSinglePotentialWithArray(potentialCount);/*Obtains the number value that is only a potential once in the section, 
			if the return value is 0, then there is no number value seen only once as a potential in the section*/
			if(potentialNumber > 0)
			{
				rowMarker = columnMarker;
				
				while(rowMarker.getPotentialNumber(potentialNumber) == false)//Find the cell that has the one potential
					rowMarker = rowMarker.getDown();
				
				changeCell(rowMarker, potentialNumber);
			}
			
			for(int i = 1; i < 10;  i++)//reset array
				potentialCount[i] = 0;
			
			columnMarker = columnMarker.getRight();
			rowMarker = columnMarker;
		}
	}
	
	private void samePotentialCountCheckMethod()
	{
		Cell analyzeMarker;//Marker used to check the other cells of the section to see if they match with the cell being tested ont = 0;
		
		int matchCells = 0, potentialMatchs = 0;
		
		
		//Row Check
		columnMarker = first;
		rowMarker = first;
		
		/*
		 * This part of the function checks rows, it goes to every cell of that row, then compares it to the other cells
		 * First we check if both have the same potential count, then checks to see if the potentials of the two cells match
		 * This must happen the same amount of times as the potential count of the cell we are checking with
		 * 
		 * Example: a cell has the potential of 2, 3, 5
		 * Then we must find two other cells with the exact same potentials before we can change the potentials of the rest of
		 * The cells in the section
		 */
		while(rowMarker != null)//Loop to move the marker vertically
		{
			while(columnMarker != null)//Loop to move the marker horizontally
			{
				matchCells = 0;
				analyzeMarker = rowMarker;
				
				while(analyzeMarker != null)
				{
					potentialMatchs = 0;
					
					if(analyzeMarker.getPotentialNumber(0) == false)
						for(int i = 1; i < 10; i++)
						{
							if(analyzeMarker.getPotentialNumber(i) == columnMarker.getPotentialNumber(i))
								potentialMatchs++;
						}
					
					if(potentialMatchs == 9)
						matchCells++;
					
					analyzeMarker = analyzeMarker.getRight();
				}
				
				if(matchCells == countPotential(columnMarker))
					for(int i = 1; i < 10; i++)
						if(columnMarker.getPotentialNumber(i) == true)
							rowPotentialChange(rowMarker, columnMarker, i);
				
				columnMarker = columnMarker.getRight();
			}
			rowMarker = rowMarker.getDown();
			columnMarker = rowMarker;
		}
		
		
		//Column check
		columnMarker = first;
		rowMarker = first;
		
		/*
		 * This part of the function checks columns, it goes to every cell of that column, then compares it to the other cells
		 * First we check if both cells have the same potential count, then checks to see if the potentials of the two cells match
		 * This must happen the same amount of times as the potential count of the cell we are checking with
		 * 
		 * Example: a cell has the potential of 2, 3, 5
		 * Then we must find two other cells with the exact same potentials before we can change the potentials of the rest of
		 * The cells in the section
		 */
		while(columnMarker != null)//Loop to move the marker vertically
		{
			while(rowMarker != null)//Loop to move the marker horizontally
			{
				matchCells = 0;
				analyzeMarker = columnMarker;
				
				while(analyzeMarker != null)
				{
					potentialMatchs = 0;
					
					if(analyzeMarker.getPotentialNumber(0) == false)
						for(int i = 1; i < 10; i++)
						{
							if(analyzeMarker.getPotentialNumber(i) == rowMarker.getPotentialNumber(i))
								potentialMatchs++;
						}
					
					if(potentialMatchs == 9)
						matchCells++;
					
					analyzeMarker = analyzeMarker.getDown();
				}
				
				if(matchCells == countPotential(rowMarker))
					for(int i = 1; i < 10; i++)
						if(rowMarker.getPotentialNumber(i) == true)
							columnPotentialChange(columnMarker, rowMarker, i);
				
				rowMarker = rowMarker.getDown();
			}
			columnMarker = columnMarker.getRight();
			rowMarker = columnMarker;
		}
		
		
		//Square Check
		columnMarker = first;
		rowMarker = first;
		Cell columnCellMarker = null;
		Cell rowCellMarker = null;

		Cell columnAnalyzeMarker = null;
		Cell rowAnalyzeMarker = null;
		/*
		 * This part of the function checks squares, it goes to every cell of that square, then compares it to the other cells
		 * First we check if both cells have the same potential count, then checks to see if the potentials of the two cells match
		 * This must happen the same amount of times as the potential count of the cell we are checking with
		 * 
		 * Example: a cell has the potential of 2, 3, 5
		 * Then we must find two other cells with the exact same potentials before we can change the potentials of the rest of
		 * The cells in the section
		 */
		
		for(int YSquareCount = 0; YSquareCount < 2; YSquareCount++)//Loop to move the rowMarker vertically to each square
		{
			for(int XSquareCount = 0; XSquareCount < 2; XSquareCount++)//Loop to move the columnMarker horizontally to each square
			{
				columnCellMarker = columnMarker;
				rowCellMarker = columnMarker;
				
				for(int rowCellCount = 0; rowCellCount < 2 && rowCellMarker != null; rowCellCount++)//Loop to move the rowCellMarker inside the square down vertically
				{
					for(int columnCellCount = 0; columnCellCount < 2 && columnCellMarker != null; columnCellCount++)//Loop to move the columnCellMarker horizontally to the right
					{//columnCellMarker is know as the 'original' cell
						
						columnAnalyzeMarker = columnMarker;
						rowAnalyzeMarker = columnMarker;
						
						matchCells = 0;
						
						for(int rowAnalyzeCount = 0; rowAnalyzeCount < 2 && rowAnalyzeMarker != null; rowAnalyzeCount++)//Loop to move the rowAnalyzeMarker vertically down 
						{
							for(int columnAnalyzeCount = 0; columnAnalyzeCount < 2 && columnAnalyzeMarker != null; columnAnalyzeCount++)//Loop to move the columnAnalyzeMarker horizontally to the right
							{
								potentialMatchs = 0;
								
								if(columnAnalyzeMarker.getPotentialNumber(0) == false)//Checks if there is no number already on that cell
									for(int i = 1; i < 10; i++)//Loops to see how many potentials match between the cell being analyze and the one being worked with originally
									{
										if(columnCellMarker.getPotentialNumber(i) == columnAnalyzeMarker.getPotentialNumber(i))
											potentialMatchs++;
									}
								
								if(potentialMatchs == 9)//If the potentials of the two cells match, then say one other cell matches the original
									matchCells++;

								columnAnalyzeMarker = columnAnalyzeMarker.getRight();
							}
							
							rowAnalyzeMarker = rowAnalyzeMarker.getDown();
							columnAnalyzeMarker = rowAnalyzeMarker;
						}
						
						if(matchCells == countPotential(columnCellMarker))//If there are amount of cells that match the original including the original, then change the potential of the other cells in the square
						{
							for(int i = 1; i < 10; i++)
							{
								if(columnCellMarker.getPotentialNumber(i) == true)
									squarePotentialChange(columnMarker, columnCellMarker, i);
							}
						}
						
						columnCellMarker = columnCellMarker.getRight();
					}
					
					rowCellMarker = rowCellMarker.getDown();
					columnCellMarker = rowCellMarker;
				}
				
				columnMarker = columnMarker.getRight().getRight().getRight();
			}
			rowMarker = rowMarker.getDown().getDown().getDown();
			columnMarker = rowMarker;
		}
	}
		
	private int checkForSinglePotentialWithArray(int []potentialCount)//Function that accepts an array and checks if there is a potential that only appeared once
	{
		int singlePotentialValue = 0;
		
		for(int i = 1; i < 10; i++)
			if(potentialCount[i] == 1)
				singlePotentialValue = i;
		
		return singlePotentialValue;
	}
	
	public void fullInitialCheck() //Do this method after uploading the data from the .txt file to establish the possibilities for all Cells
	{
		columnMarker = first;
		rowMarker = first;
		
		while(rowMarker != null)//Vertically move the marker until the end
		{
			while(columnMarker != null)//Horizontally move the marker until the end
			{
				if(columnMarker.getNumber() != 0)
				{
					columnMarker.setPotentialNumber(0, true);
					for(int i = 1; i < 10; i++)
						if(i != columnMarker.getNumber())
							columnMarker.setPotentialNumber(i, false);

					changeCell(columnMarker, columnMarker.getNumber());
				}
				columnMarker = columnMarker.getRight();//Move the marker to the right
			}
			rowMarker = rowMarker.getDown();//Move the marker down
			columnMarker = rowMarker;
		}
		System.out.println("Finished initial check");
	}
	
	private void singlePotential(Cell check)//Checks to see if a Cell has only one potential
	{
		int counter = 0, position = 0;
		
		for(int i = 1; i < 10; i++)//Count for many potentials there are in the Cell
			if(check.getPotentialNumber(i) == true)
			{
				counter++;
				position = i;
			}
		
		if(counter == 1)//If there is only one potential, than fill in that Cell
		{
			changeCell(check, position);
		}
	}
	
	private void rowChange(Cell marker, int number)//Change the potentials of an entire row of cells due to one of the cells having a number value
	{
		Cell marker2 = marker;//Make second marker to go the other way
		
		while(marker != null)//Change potentials of all the cells right of the changed cell
		{
			marker.setPotentialNumber(number, false);
			marker = marker.getRight();
		}
		
		while(marker2 != null)//Change potentials of all the cells left of the changed cell
		{
			marker2.setPotentialNumber(number, false);
			marker2 = marker2.getLeft();
		}
	}
	
	/*
	 * Input: Cell, number of changed potential
	 * Second marker is created to go the opposite direction of the column and change those cells' potential
	 * Each marker goes through the column and change each cell's potential to false of the given number
	 */
	private void columnChange(Cell marker, int number)
	{
		Cell marker2 = marker;//Make second marker to go the other direction
		
		while(marker != null)//Change potentials of all the cells above of the changed cell
		{
			marker.setPotentialNumber(number, false);
			marker = marker.getUp();
		}
		
		while(marker2 != null)//Change potentials of all the cells down of the changed cell
		{
			marker2.setPotentialNumber(number, false);
			marker2 = marker2.getDown();
		}
	}
	
	/*
	 * Input: Cell, number to change potential
	 * First the square where the cell is located is identified
	 * Then all cells in the square have their potential of the given number changed to false
	 */
	private void squareChange(Cell marker, int number)
	{
		Cell columnMarkerSquareChange = first;//setMarkers
		Cell rowMarkerSquareChange = first;
		
		//First place the marker at the square which the cell is at
		while(rowMarkerSquareChange != null && columnMarkerSquareChange.getSqCoord() != marker.getSqCoord())//Loop to move the marker vertically
		{
			while(columnMarkerSquareChange != null && columnMarkerSquareChange.getSqCoord() != marker.getSqCoord())//Loop to move the marker horizontally
				columnMarkerSquareChange = columnMarkerSquareChange.getRight().getRight().getRight();//Move the marker to the next column of squares
			
			if(columnMarkerSquareChange != null && columnMarkerSquareChange.getSqCoord() == marker.getSqCoord())//Stops the marker from moving anymore when it has reached the target square
				break;
			
			rowMarkerSquareChange = rowMarkerSquareChange.getDown().getDown().getDown();//Move the marker to the new row of squares
			columnMarkerSquareChange = rowMarkerSquareChange;
		}
		
		rowMarkerSquareChange = columnMarkerSquareChange;//Set marker
		
		//Changes the potentials of the cells in the square
		for(int squareChangeY = 0; squareChangeY <= 2; squareChangeY++)//Loop to move the marker vertically
		{
			for(int squareChangeX = 0; squareChangeX <= 2; squareChangeX++)//Loop to move the marker horizontally
			{
				columnMarkerSquareChange.setPotentialNumber(number, false);//Change potential of the cell
				columnMarkerSquareChange = columnMarkerSquareChange.getRight();//Move marker to the right
			}
			rowMarkerSquareChange = rowMarkerSquareChange.getDown();//move the marker down
			columnMarkerSquareChange = rowMarkerSquareChange;//adjust the marker to the new row
		}
	}
	
	private void rowPotentialChange(Cell columnMarker, Cell originalMarker, int number)//Used to change a potential in a row of cells, only used by Method4()
	{
		
		while(columnMarker != null)
		{
			if(columnMarker.getPotentialNumber(0) == false)
				for(int i = 1; i < 10; i++)
					if(originalMarker.getPotentialNumber(i) != columnMarker.getPotentialNumber(i))
					{
						columnMarker.setPotentialNumber(number, false);
						break;
					}
					
			columnMarker = columnMarker.getRight();
		}
	}
	
	private void columnPotentialChange(Cell rowMarker, Cell originalMarker, int number)//Used to change a potential in a row of cells, only used by Method4()
	{
		while(rowMarker != null)
		{
			if(rowMarker.getPotentialNumber(0) == false)
				for(int i = 1; i < 10; i++)
					if(originalMarker.getPotentialNumber(i) != rowMarker.getPotentialNumber(i))
					{
						rowMarker.setPotentialNumber(number, false);
						break;
					}
			rowMarker = rowMarker.getDown();
		}
	}
	
	private void squarePotentialChange(Cell squareMarker, Cell cellMarker, int number)
	{
		Cell columnCellMarker = squareMarker, 
				rowCellMarker = squareMarker;//Set Markers
		
		for(int YCellPotentialCounter = 0; YCellPotentialCounter < 2; YCellPotentialCounter++)//Loop to move the marker vertically
		{
			for(int XCellPotentialCounter = 0; XCellPotentialCounter < 2; XCellPotentialCounter++)//Loop to move the marker horizontally
			{
				if(columnCellMarker.getPotentialNumber(0) == false)
					for(int i = 1; i < 10; i++)//Loop to check if the cell is not one of the cells that match the original cell
					{
						if(cellMarker.getPotentialNumber(i) != columnCellMarker.getPotentialNumber(i))//As soon as one potential does not match with the original cell, this cell's potential is changed
						{
							columnCellMarker.setPotentialNumber(number, false);
							break;
						}
					}
				
				columnCellMarker = columnCellMarker.getRight();
			}
			rowCellMarker = rowCellMarker.getDown();
			columnCellMarker = rowCellMarker;
		}
	}
	
	/*
	 * Input: Cell, number it has been assigned
	 * Change the potential array of the cell to false except position 0 which is turned to true
	 * Sets the number of the cell to the assigned number
	 * Changes the potential of the row, column and square of the cell to have false potential for the assigned number
	 */
	private void changeCell(Cell temp, int numberChange)
	{
		change = true;
		
		temp.setPotentialNumber(0, true);//Change the first array value to notify that the cell has a number value
		
		for(int i = 1; i < 10; i++)//Changes all the potentials of the cell to false
			temp.setPotentialNumber(i, false);
		
		temp.setNumber(numberChange);
		
		rowChange(temp, numberChange);//Change potentials of the row the changed cell is on
		columnChange(temp, numberChange);//Change potentials of the column the changed cell is on
		squareChange(temp, numberChange);//Change potentials of the square the changed cell is on

//		Grid.display();
//		System.out.println();
		
	}

	public void checkPotentialOfCell(int x, int y)//Check what potentials a cell has for debugging purposes
	{
		Cell check = first;
		
		for(int a = 1; a < x; a++)//Move the marker to the correct column
			check = check.getRight();
			
		for(int b = 1; b < y; b++)//Move the marker to the correct row
			check = check.getDown();
		
		System.out.println("Cell in position x:"+(x)+" y:"+(y));
		System.out.println("The number of the cell is: "+check.getNumber());
		for(int i = 0; i < 10; i++)
			System.out.println("The potenial of "+i+" is: "+check.getPotentialNumber(i));
	}

	/*
	 * Input: Cell
	 * To count the number of potentials the cell current has
	 * Output: Number of potentials of cell
	 */
	private int countPotential(Cell cell)
	{
		int counter = 0;
		for(int countPotential = 1; countPotential < 10; countPotential++)
			if(cell.getPotentialNumber(countPotential) == true)
				counter++;
		
		return counter;
	}
}
