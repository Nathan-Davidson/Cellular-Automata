public class GameOfLifeBoard {
	private int height;
	private int width;
  private boolean[][] currBoard;
  private boolean[][] nextBoard;

  public GameOfLifeBoard(int height, int width) {
		this.height = height;
		this.width = width;
    this.currBoard = new boolean[height][width];
    this.nextBoard = new boolean[height][width];
  }

	/**
	 * Advances the board by one tick from the current state. Per the rules of
	 * the Game of Life, live cells with less than two or more than three
	 * neighbors become dead and dead cells with three neighbors become live.
	 */
  public void tick() {
		for(int row = 0; row < this.height; row++) {
			for(int col = 0; col < this.width; col++) {
				int neighbors = getNumNeighbors(row, col);
				if(currBoard[row][col] && (neighbors < 2 || neighbors > 3)) {
						nextBoard[row][col] = false;
				} else if(!currBoard[row][col] && neighbors == 3) {
						nextBoard[row][col] = true;
				} else {
					nextBoard[row][col] = currBoard[row][col];
				}
			}
		}

		boolean[][] temp = currBoard;
		currBoard = nextBoard;
		nextBoard = temp;
	}

	/**
	 * Determines the number of live neighbors of a cell.
	 *
	 * @param row the row of the cell to compute neighbor values for
	 * @param col the column of the cell to compute neighbor values for
	 * @return the number of neighbors the cell has
	 */
	public int getNumNeighbors(int row, int col) {
		int numNeighbors = 0;

		if (isLive(row + 1, col + 1)) {
			numNeighbors++;
		}
		if (isLive(row + 1, col)) {
			numNeighbors++;
		}
		if (isLive(row + 1, col - 1)) {
			numNeighbors++;
		}
		if (isLive(row, col + 1)) {
			numNeighbors++;
		}
		if (isLive(row, col - 1)) {
			numNeighbors++;
		}
		if (isLive(row - 1, col + 1)) {
			numNeighbors++;
		}
		if (isLive(row - 1, col)) {
			numNeighbors++;
		}
		if (isLive(row - 1, col - 1)) {
			numNeighbors++;
		}

		return numNeighbors;
	}

	/**
	 * Determines if cell is live, wrapping edges.
	 *
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @return true if the cell is live, false if it is not
	 */
	public boolean isLive(int row, int col) {
		if(row < 0)
			row = this.height - 1;
		if(col < 0)
			col = this.width - 1;
		if(row == this.height)
			row = 0;
		if(col == this.width)
			col = 0;
		return currBoard[row][col];
	}

	/**
	 * Flip a cell from alive to dead or vice versa.
	 *
	 * @param row the row of the cell
	 * @param col the column of the cell
	 */
	public void flipCell(int row, int col) {
		currBoard[row][col] = !currBoard[row][col];
	}

	/**
	 * Resets the board, making all cells dead
	 */
	public void reset() {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				currBoard[row][col] = false;
			}
		}
	}
}
