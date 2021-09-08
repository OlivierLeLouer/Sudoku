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
	
	/**
	 * @return the allSquaresUnresolved
	 */
	public List<Square> getAllSquaresUnresolved() {
		return allSquaresUnresolved;
	}

	public void determineAllSquaresUnresolved() {
		List<Square> toRemove = new ArrayList<Square>();
		for (Square square : allSquaresUnresolved) {
			if (!square.isUnresolved())
				toRemove.add(square);
		}
		allSquaresUnresolved.removeAll(toRemove);
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

	public boolean isSolved() {
		return allSquaresUnresolved.isEmpty();
	}


	public Case searchFirstCaseToTry() {
		for (int lineNumber = 1; lineNumber < 10 ; lineNumber++) {
			List<Case> allCases = this.getAllCasesFromLine(lineNumber);
			for (Case case1 : allCases) {
				if (case1.isPossibleToMakeATry()) return case1;
			}
		}
		return null;
	}

	public String toStringToSave(Case c) {
		StringBuilder result = new StringBuilder();
		for (int lineNumber = 1; lineNumber < 10 ; lineNumber++) {
			List<Case> allCases = this.getAllCasesFromLine(lineNumber);
			for (Case case1 : allCases) {
				if (case1.equals(c)) {
					result.append(String.valueOf(c.getValueToTest()));	
				} else {
					result.append(String.valueOf(case1.getCurrentValue()));
				}
			}
			result.append("\r");
		}
		return result.toString();
	}
}
