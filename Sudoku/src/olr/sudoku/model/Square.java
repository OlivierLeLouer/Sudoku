package olr.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Square {

	Case[][] content;
	Board board;
	List<Case> allCaseUnresolved;
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
		allCaseUnresolved = new ArrayList<Case>();
		for (int i = 1; i < 4 ; i++) {
			for (int j = 1; j < 4; j++) {
				Case newCase = new Case(i, j, this);
				content[i][j] = newCase;
				allCaseUnresolved.add(newCase);
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
			sb.append("\r\n");
		}
		return sb.toString();
	}
}
