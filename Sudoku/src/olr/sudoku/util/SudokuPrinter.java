package olr.sudoku.util;

import java.util.Iterator;
import java.util.List;

import olr.sudoku.model.Board;
import olr.sudoku.model.Case;

public class SudokuPrinter {

	public SudokuPrinter() {
		super();
	}
	
	public void print(Board b) {
		for (int l = 1; l < 10; l++) {
			List<Case> list = b.getAllCasesFromLine(l);
			Iterator<Case> it = list.iterator();
			while (it.hasNext()) System.out.print(it.next());
			System.out.println("");
		}
	}
}
