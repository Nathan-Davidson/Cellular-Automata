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

	public void flipCell(int row, int col) {
		currBoard[row][col] = !currBoard[row][col];
	}

	public void reset() {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				currBoard[row][col] = false;
			}
		}
	}
}
