package SudokuSolver;

import java.io.FileNotFoundException;

public class Program {

	public static void main(String[] args) throws FileNotFoundException {
		
		LinkedGrid Grid = new LinkedGrid(9);
		Grid.populateGridFromFile();
		Grid.display();
		SolvingMethods solver = new SolvingMethods(Grid);
		solver.fullInitialCheck();
		solver.solve();
		System.out.println("Displaying Grid");
		Grid.display();
	}
}
