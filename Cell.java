package SudokuSolver;

public class Cell {
	public int number, sqCoord;
	Cell up, down, left, right;
	boolean [] potentialNumber = new boolean [10];
	/*
	* address meanings: 
	* [0] - has the cell been solved?
	* [1-9] - does it have the potential to be this number
	* 
	*/
	public Cell()
	{
		up = null;
		down = null;
		left = null;
		right = null;
		number = 0;
		sqCoord = 0;
		for(int x= 1; x< 10; x++)
			potentialNumber[x] = true;
		potentialNumber[0] = false;
	}
	
	public void copyCell (Cell copyCell)
	{
		this.number = copyCell.getNumber();
		this.sqCoord = copyCell.getSqCoord();
		for(int x= 0; x< 10; x++)
			this.potentialNumber[x] = copyCell.getPotentialNumber(x);
		
	}
	
	public boolean getPotentialNumber(int position) {
		return potentialNumber[position];
	}
	public void setPotentialNumber(int position, Boolean state) {
		potentialNumber[position] = state;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Cell getUp() {
		return up;
	}
	public void setUp(Cell up) {
		this.up = up;
	}
	public Cell getDown() {
		return down;
	}
	public void setDown(Cell down) {
		this.down = down;
	}
	public Cell getLeft() {
		return left;
	}
	public void setLeft(Cell left) {
		this.left = left;
	}
	public Cell getRight() {
		return right;
	}
	public void setRight(Cell right) {
		this.right = right;
	}
	public int getSqCoord() {
		return sqCoord;
	}
	public void setSqCoord(int sqCoord) {
		this.sqCoord = sqCoord;
	}
}
