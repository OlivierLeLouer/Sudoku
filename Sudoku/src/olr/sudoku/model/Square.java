package olr.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Square {

	Case[][] content;
	Board board;
	int lineNumber;
	int columnNumber;
	
	public Square(int l, int c, Board p) {
		super();
		initialize(l, c, p);
	}
	
	private void initialize(int l, int c, Board p) {
		content = new Case[4][4];
		board = p;
		lineNumber = l;
		columnNumber = c;
		for (int i = 1; i < 4 ; i++) {
			for (int j = 1; j < 4; j++) {
				Case newCase = new Case(i, j, this);
				content[i][j] = newCase;
			}
		}		
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Case[][] getContent() {
		return content;
	}
	
	public List<Case> getAllCases() {
		List<Case> result = new ArrayList<Case>();
		for (int i = 1; i < 4 ; i++) {
			for (int j = 1; j < 4; j++) {
				result.add(content[i][j]);
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int l = 1; l < 4 ; l++) {
			for (int c = 1; c < 4; c++) {
				sb.append(content[l][c].toString());
			}
			if (l < 3) sb.append("\r\n");
		}
		return sb.toString();
	}

	public boolean isUnresolved() {
		for (int l = 1; l < 4 ; l++) {
			for (int c = 1; c < 4; c++) {
				if (content[l][c].isModificationAuthorized())
					return true;
			}
		}
		return false;
	}

	public boolean checkNineCases(List<Case> list)  throws Exception {
		List<Integer> allValues = new ArrayList<Integer>();
		for (Case cas : list) {
			if (!cas.isModificationAuthorized()) {
				if (allValues.contains(cas.getCurrentValue()))
					throw new Exception ("Valeur déjà présente dans le carré, la ligne ou la colonne.");
				allValues.add(cas.getCurrentValue());
			}		
		}
		return true;
	}

	public boolean check() throws Exception {
		for (int l = 1; l < 4 ; l++) {
			for (int c = 1; c < 4; c++) {
				checkNineCases(board.getContent()[l][c].getAllCases());
			}
		}
		for (int lineNumber = 1; lineNumber < 10 ; lineNumber++) {
			checkNineCases(board.getAllCasesFromLine(lineNumber));
		}
		for (int columnNumber = 1; columnNumber < 10 ; columnNumber++) {
			checkNineCases(board.getAllCasesFromColumn(columnNumber));
		}
		return true;
	}
}
