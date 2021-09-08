package olr.sudoku.util;

import java.util.Iterator;
import java.util.List;

import olr.sudoku.model.Board;
import olr.sudoku.model.Case;
import olr.sudoku.model.Square;

public class SudokuPrinter {

	public SudokuPrinter() {
		super();
	}
	
	public void print(Board b) {
		System.out.println("Beginning of Board ..");
		for (int l = 1; l < 10; l++) {
			List<Case> list = b.getAllCasesFromLine(l);
			Iterator<Case> it = list.iterator();
			while (it.hasNext()) System.out.print(it.next());
			System.out.println("");
		}
		System.out.println("- - - - - - - - -");
	}

	public void printAllPossibilities(Board b) {
		List<Square> list = b.getAllSquaresUnresolved();
		Iterator<Square> it = list.iterator();
		while (it.hasNext()) {
			Square s = it.next();
			System.out.println(s);
			Iterator<Case> itc = s.getAllCases().iterator();
			while (itc.hasNext()) {
				Case c = itc.next();
				if (c.isModificationAuthorized()) {
					System.out.print(c.getLineNumber() + " / " + c.getColumnNumber() + " : ");
					System.out.println(c.getAllPossibleValues());
				}
			}
			System.out.println("------------");
		}
	}
}
