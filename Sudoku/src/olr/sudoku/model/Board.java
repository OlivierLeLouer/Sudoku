package olr.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

	Square[][] content;
	List<Square> allSquaresUnresolved;
	
	public Board() {
		super();
		initialize();
	}
	
	private void initialize() {
		content = new Square[4][4];
		allSquaresUnresolved = new ArrayList<Square>(9);
		for (int i = 1; i < 4 ; i++) {
			for (int j = 1; j < 4; j++) {
				Square newSquare = new Square(i, j, this);
				content[i][j] = newSquare;
				allSquaresUnresolved.add(newSquare);
			}
		}
	}
	
	public Square[][] getContent() {
		return content;
	}
	
	public List<Case> getAllCasesFromLine(int lineNumber) {
		List<Case> result = new ArrayList<Case>();
		int l = 3;
		int lineInSquare = 0;
		if (lineNumber < 4) {
			l = 1;
			lineInSquare = lineNumber;
		} else if (lineNumber < 7) {
			l = 2;
			lineInSquare = lineNumber - 3;
		} else {
			lineInSquare = lineNumber - 6;
		}
		for (int sq = 1; sq < 4 ; sq++) {
			Square currentSquare = getContent()[l][sq];
			for (int c = 1; c < 4 ; c++) {
				result.add(currentSquare.getContent()[lineInSquare][c]);
			}
		}
		return result;
	}

	public List<Case> getAllCasesFromColumn(int columnNumber) {
		List<Case> result = new ArrayList<Case>();
		int c = 3;
		int columnInSquare = 0;
		if (columnNumber < 4) {
			c = 1;
			columnInSquare = columnNumber;
		} else if (columnNumber < 7) {
			c = 2;
			columnInSquare = columnNumber - 3;
		} else {
			columnInSquare = columnNumber - 6;
		}
		for (int sq = 1; sq < 4 ; sq++) {
			Square currentSquare = getContent()[sq][c];
			for (int l = 1; l < 4 ; l++) {
				result.add(currentSquare.getContent()[l][columnInSquare]);
			}
		}
		return result;
	}

}
