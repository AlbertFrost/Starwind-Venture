package SudokuSolver;

import SudokuSolverV2.solvingMethods;

public class Guesser {
	
	Cell first;
	LinkedGrid Grid;
	LinkedGrid oldGrid;
	
	public Guesser(LinkedGrid oldGrid)
	{
		this.oldGrid = oldGrid;
		Grid = new LinkedGrid(9);
		first = Grid.getFirst();
		
		Cell rowMarkerNewGrid = first;
		Cell columnMarkerNewGrid = rowMarkerNewGrid;
		
		Cell rowMarkerOldGrid = oldGrid.getFirst();
		Cell columnMarkerOldGrid = rowMarkerOldGrid;
		
		while(rowMarkerOldGrid != null)
		{
			while(columnMarkerOldGrid != null)
			{
				columnMarkerNewGrid.copyCell(columnMarkerOldGrid);
				
				columnMarkerOldGrid = columnMarkerOldGrid.getRight();
				columnMarkerNewGrid = columnMarkerNewGrid.getRight();
			}
			rowMarkerOldGrid = rowMarkerOldGrid.getDown();
			columnMarkerOldGrid = rowMarkerOldGrid;

			rowMarkerNewGrid = rowMarkerNewGrid.getDown();
			columnMarkerNewGrid = rowMarkerNewGrid;
		}
	}
	
	public LinkedGrid run()
	{
		System.out.println("Guessing...");
		LinkedGrid returnGrid;
		
		if(!Grid.isGridCompleted())
			guess();
		
		if(Grid.isGridCompleted())
		{
			System.out.println("Returning NEW grid");
			returnGrid = Grid;
		}
		else
		{
			System.out.println("Returning OLD grid");
//			oldGrid.display();
//			System.out.println("NEW GRID WAS:");
//			Grid.display();
			returnGrid = oldGrid;
		}
		
		return returnGrid;
	}
	
	private void guess()
	{
		Cell rowMarker;
		Cell columnMarker;
		
		for(int numberOfPotential = 2; numberOfPotential < 9; numberOfPotential++)//For loop to find a cell first with two potentials, but keeps moving up if there are none available
		{
			rowMarker = first;//setting markers
			columnMarker = first;
			while(rowMarker != null)
			{
				while(columnMarker != null)
				{
					if(columnMarker.getPotentialNumber(0) == false)
					{
						if(countPotential(columnMarker) == numberOfPotential)
						{
							for(int findPotential = 1; findPotential < 10; findPotential++)
							{
								if(columnMarker.getPotentialNumber(findPotential) == true)
								{
									changeCell(columnMarker, findPotential);
									SolvingMethods solver = new SolvingMethods(Grid);
									solver.solve();
								}
							}
						}
					}
					columnMarker = columnMarker.getRight();
				}
				rowMarker = rowMarker.getDown();
				columnMarker = rowMarker;
			}
		}
	}
	
	private void changeCell(Cell temp, int number)//Change the number of a cell and set all potentials of that cell correctly
	{
		temp.setPotentialNumber(0, true);//Change the first array value to notify that the cell has a number value
		
		for(int i = 1; i < 10; i++)//Changes all the potentials of the cell to false
			temp.setPotentialNumber(i, false);
		
		temp.setNumber(number);
		
		rowChange(temp, number);//Change potentials of the row the changed cell is on
		columnChange(temp, number);//Change potentials of the column the changed cell is on
		squareChange(temp, number);
	}
	
	private int countPotential(Cell cell)
	{
		int counter = 0;
		for(int countPotential = 1; countPotential < 10; countPotential++)
			if(cell.getPotentialNumber(countPotential) == true)
				counter++;
		
		return counter;
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
	
	private void columnChange(Cell marker, int number)//Change the potentials of an entire column of cells due to one of the cells having a number value
	{
		Cell marker2 = marker;//Make second marker to go the other way
		
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
	
	private void squareChange(Cell marker, int number)//Changes the potential of a square where the cell has been given a number
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
			columnMarkerSquareChange = rowMarkerSquareChange;//readjust the marker to the new row
		}
	}
}
