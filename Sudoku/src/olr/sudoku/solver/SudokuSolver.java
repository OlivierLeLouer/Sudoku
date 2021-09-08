package olr.sudoku.solver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import olr.sudoku.model.Board;
import olr.sudoku.model.Case;
import olr.sudoku.model.Square;
import olr.sudoku.util.SudokuLoader;
import olr.sudoku.util.SudokuPrinter;

public class SudokuSolver {
	
	private static String CurrentSudokuFacile    = "421058973\r008017042\r007200510\r003026000\r050009406\r600735809\r006803100\r000000000\r000941200\r";
	private static String CurrentSudokuMoyen     = "003007006\r040305000\r006001700\r072000104\r000076890\r001200600\r259000007\r060000015\r180000300\r";
	private static String CurrentSudokuDifficile = "003958061\r018700345\r000000000\r600170800\r004009000\r000320600\r200000090\r070006000\r030000007\r";
	private static String CurrentSudokuExpert    = "045002000\r020890400\r071065920\r004000000\r050004060\r000000080\r080100000\r600000070\r010008600\r";
	private static String CurrentSudokuExpert2   = "400901000\r020005070\r000000300\r008000400\r002007900\r007520080\r600040102\r000610000\r209000000\r";
	
	public static void main(String[] args) {
		LoadAndSolve(CurrentSudokuExpert2);
	}
	
	public static boolean LoadAndSolve(String SudokuToSolve) {
		SudokuLoader sl = new SudokuLoader();
		Board b = sl.loadFrom(SudokuToSolve);
		SudokuPrinter sp = new SudokuPrinter();
		sp.print(b);
		System.out.println("Valeurs possibles");
		sp.printAllPossibilities(b);
		SudokuSolver sv = new SudokuSolver(b);
		boolean bool = sv.solve();
		if (!bool) {
			sp.print(sv.board);
			System.out.println("Valeurs possibles");
			sp.printAllPossibilities(b);
			return false;
		}
		return true;
	}

	private Board board;
	
	public SudokuSolver(Board boardToSolve) {
		super();
		board = boardToSolve;
	}

	private boolean modificationInProgress;
	
	public boolean solve() {
		modificationInProgress = true;
		while (modificationInProgress) {
			modificationInProgress = false;
			solveEachSquare();
			solveEachLine();
			solveEachColumn();
			if (modificationInProgress) {
				determineValuesForAllCases();
				board.determineAllSquaresUnresolved();
			}
			SudokuPrinter sp = new SudokuPrinter();
			sp.print(board);
			if (!checkAll()) return false;
			if (!modificationInProgress) {
				//S'il n'y a pas de modif en cours, alors on tente le coup de provoquer une modif pour voir ce que ca fait
				boolean result = false;
				while (!result) {
					System.out.println("Valeurs possibles");
					sp.printAllPossibilities(board);
					Case c = board.searchFirstCaseToTry();
					if (c == null) return true;
					String newBoardToSolve = board.toStringToSave(c);
					result = LoadAndSolve(newBoardToSolve);	
				}
			}
		}
		return true;
	}
	
	private void solveNineCases(List<Case> list) {
		//On commence par séparer les cases deja remplies des autres
		List<Case> alreadyFilledCases = new ArrayList<>();
		Iterator<Case> it = list.iterator();
		while (it.hasNext()) {
			Case currentCase = it.next();
			if (currentCase.getCurrentValue() != 0) {
				alreadyFilledCases.add(currentCase);
			}
		}
		list.removeAll(alreadyFilledCases);
		List<Integer> alreadyUsedInteger = new ArrayList<>();
		it = alreadyFilledCases.iterator();
		while (it.hasNext()) {
			alreadyUsedInteger.add(it.next().getCurrentValue());
		}
		//On supprime des valeurs permises d'une case celles qui sont déjà utilisées par ailleurs.
		Iterator<Integer> allInts = alreadyUsedInteger.iterator();
		while (allInts.hasNext()) {
			int currentInt = allInts.next().intValue();
			it = list.iterator();
			while (it.hasNext()) {
				boolean actionDone = it.next().removePossibleValue(currentInt);
				if (!modificationInProgress && actionDone) modificationInProgress = true;
			}
		}
		//On cherche les cases qui n'ont qu'une valeur en attente
		it = list.iterator();
		while (it.hasNext()) {
			Case currentCase = it.next();
			List<Integer> possibleValues = currentCase.getAllPossibleValues();
			Iterator<Integer> iti = possibleValues.iterator();
			while (iti.hasNext()) {
				int number = iti.next();
				Iterator<Case> it2 = list.iterator();
				boolean numberPossible = true;
				while (it2.hasNext()) {
					Case anotherCase = it2.next();
					if (currentCase != anotherCase) {
						if (anotherCase.isValuePossible(number)) {
							numberPossible = false;
							break;
						}
					}
				}
				if (numberPossible) {
					currentCase.setFinalValue(number);
					modificationInProgress = true;
				}
			}
		}
		if (modificationInProgress) return;
		//On cherche les paires qui excluent des chiffres d'autres cases 
		//(1 paire présente 2 fois dans un carré exclue ses chiffres des autres cases du carré)
		it = list.iterator();
		List<Case> doNotTouchCases = new ArrayList<Case>();
		List<Integer> possibleValuesToDelete = null;
		while (it.hasNext() && doNotTouchCases.isEmpty()) {
			Case currentCase = it.next();
			List<Integer> possibleValues = currentCase.getAllPossibleValues();
			if (possibleValues.size() == 2) {
				Iterator<Case> it2 = list.iterator();
				while (it2.hasNext() && doNotTouchCases.isEmpty()) {
					Case anotherCase = it2.next();
					if (anotherCase == currentCase) continue;
					possibleValuesToDelete = anotherCase.getAllPossibleValues();
					if (possibleValuesToDelete.size() == 2 && !possibleValues.retainAll(possibleValuesToDelete) && possibleValues.size() == 2) {
						//Les 2 listes avaient les memes values, on doit les vider des autres cases...
						doNotTouchCases.add(currentCase);
						doNotTouchCases.add(anotherCase);
					}
				}
			}
		}
		if (!doNotTouchCases.isEmpty()) {
			//On supprime des valeurs permises d'une case celles qui sont déjà utilisées par ailleurs dans les paires
			allInts = possibleValuesToDelete.iterator();
			while (allInts.hasNext()) {
				int currentInt = allInts.next().intValue();
				it = list.iterator();
				while (it.hasNext()) {
					Case c = it.next();
					if (!doNotTouchCases.contains(c)) {
						boolean actionDone = c.removePossibleValue(currentInt);
						if (!modificationInProgress && actionDone) 
							modificationInProgress = true;
					}
				}
			}
		}
	}
	
	private boolean checkAll() {
		for (int l = 1; l < 4 ; l++) {
			for (int c = 1; c < 4; c++) {
				Square square = board.getContent()[l][c];
				try {
					square.check();
				} catch (Exception ex) {
					return false;
				}
				List<Case> list = square.getAllCases();
				Iterator<Case> it = list.iterator();
				while (it.hasNext()) {
					try {
						it.next().check();
					} catch (Exception ex) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void determineValuesForAllCases() {
		for (int l = 1; l < 4 ; l++) {
			for (int c = 1; c < 4; c++) {
				List<Case> list = board.getContent()[l][c].getAllCases();
				Iterator<Case> it = list.iterator();
				while (it.hasNext()) {
					boolean actionDone = it.next().determineFinalValue();
					if (!modificationInProgress && actionDone) modificationInProgress = true;
				}
			}
		}
	}
	
	private void solveEachSquare() {
		for (int l = 1; l < 4 ; l++) {
			for (int c = 1; c < 4; c++) {
				solveNineCases(board.getContent()[l][c].getAllCases());
			}
		}
	}
	
	private void solveEachLine() {
		for (int lineNumber = 1; lineNumber < 10 ; lineNumber++) {
			solveNineCases(board.getAllCasesFromLine(lineNumber));
		}
	}
	
	private void solveEachColumn() {
		for (int columnNumber = 1; columnNumber < 10 ; columnNumber++) {
			solveNineCases(board.getAllCasesFromColumn(columnNumber));
		}
	}
}
