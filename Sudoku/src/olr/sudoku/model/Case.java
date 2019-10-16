package olr.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Case {

	int initialValue = 0;
	int currentValue;
	boolean isOriginal = false;
	boolean alterable;
	boolean[] possibleValues;
	Square square;
	int lineNumber;
	int columnNumber;
	
	/**
	 * @return the columnNumber
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	public Case(int l, int c, Square s) {
		super();
		initialize(l, c, s);
	}
	
	private void initialize(int l, int c, Square s) {
		square = s;
		lineNumber = l;
		columnNumber = c;
		currentValue = 0;
		alterable = true;
		possibleValues = new boolean[10];
		for (int i = 0; i < 10; i++) {
			possibleValues[i] = true;
		}
	}
	
	public void setFinalValue(int finalValue) {
		if (isOriginalValue()) {
			System.out.println("Impossible to modify a case with an initalValue");
			return;
		}
		currentValue = finalValue;
		alterable = false;
		for (int i = 0; i < 10; i++) {
			possibleValues[i] = false;
		}
		possibleValues[finalValue] = true;
	}
	
	public void setInitialValue(int iValue) {
		initialValue = iValue;
		setFinalValue(initialValue);
		isOriginal = true;
	}
	
	public boolean isValuePossible(int value) {
		return possibleValues[value];
	}
	
	public List<Integer> getAllPossibleValues() {
		List<Integer> result = new ArrayList<Integer>();
		for (int i= 1; i< 10; i++) {
			if (possibleValues[i]) {
				result.add(i);
			}
		}
		return result;
	}
	
	public boolean removePossibleValue(int valueToRemove) {
		if (possibleValues[valueToRemove]) {
			possibleValues[valueToRemove] = false;
			return true;
		}
		return false;
	}
	
	public boolean determineFinalValue() {
		if (!alterable) {
			return false;
		}
		int result = -1;
		for (int i = 1; i< 10; i++) {
			if (possibleValues[i]) {
				if (result == -1) 
					result = i;
				else
					return false;
			}
		}
		if (result != -1) {
			setFinalValue(result);
			return true;
		}
		return false;
	}
	
 	public boolean isOriginalValue() {
		return isOriginal;
	}
	
	public Square getSquare() {
		return square;
	}
	
	public int getInitialValue() {
		return initialValue;
	}
	
	public int getCurrentValue() {
		return currentValue;
	}

	@Override
	public String toString() {
		if (currentValue > 0)
			return String.valueOf(currentValue) + " ";
		return "  ";
	}
}
