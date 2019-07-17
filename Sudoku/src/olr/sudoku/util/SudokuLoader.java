package olr.sudoku.util;

import olr.sudoku.model.Board;
import olr.sudoku.model.Case;
import olr.sudoku.model.Square;

public class SudokuLoader {

	public Board loadFrom(String content) {
		Board board = new Board();
		int index = 0;
		int lineNumber = 0;
		int lineNumberInSquare = 0;
		int squareNumberL = 0;
		int squareNumberC = 0;
		while (content.indexOf("\r", index) != -1) {
			lineNumber++;
			if (lineNumber <= 3) {
				squareNumberL = 1;
				lineNumberInSquare = lineNumber;
			} else if (lineNumber <= 6) {
				squareNumberL = 2;
				lineNumberInSquare = lineNumber - 3;
			} else {
				squareNumberL = 3;
				lineNumberInSquare = lineNumber - 6;
			}

			String line = content.substring(index, content.indexOf("\r", index));
			System.out.println(line);
			int columnNumber = 0;
			for (int k = 0; k < 9 ; k++) {
				if (k < 3) {
					columnNumber = k+1;
					squareNumberC = 1;
				} else if (k < 6) {
					columnNumber = k + 1 - 3;
					squareNumberC = 2;
				} else {
					columnNumber = k + 1 - 6;
					squareNumberC = 3;
				}
				char car = line.charAt(k);
				if(car != '0') {
					Square currentSquare = board.getContent()[squareNumberL][squareNumberC];
					Case currentCase = currentSquare.getContent()[lineNumberInSquare][columnNumber];
					currentCase.setInitialValue(Integer.valueOf(String.valueOf(car)));
				}
			}
			index = content.indexOf("\r", index) + 1;
		}
		return board;
	}

}
