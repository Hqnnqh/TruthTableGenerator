package com.hqnnqh.turthtable.generator.simplifier;

import java.util.ArrayList;
import java.util.List;

import com.hqnnqh.turthtable.generator.parser.Parser;

public class MinTermConverter {

	// simpleTerm = term with only &&, ||, !

	public static char[] getVariables(String formular) {
		int[] variablesAsInts = formular.chars().filter(character -> Character.isLetter(((char) character))).distinct()
				.toArray();

		char[] variables = new char[variablesAsInts.length];

		for (int i = 0; i < variables.length; i++)
			variables[i] = (char) variablesAsInts[i];
		return variables;
	}

	public static List<String> convertToStringList(String formular) {
		List<String> minTerms = new ArrayList<>();

		Parser parser = new Parser();
		int[] variables = formular.chars().filter(character -> Character.isLetter(((char) character))).distinct()
				.toArray();
		int numberOfVariables = variables.length;
		int numberOfRows = (int) Math.pow(2, numberOfVariables);

		for (int i = 0; i < numberOfRows; i++) {

			String currentExpression = formular;
			String inputs = Integer.toBinaryString(i);
			// add zeros at beginning
			while (inputs.length() < numberOfVariables)
				inputs = "0" + inputs;

			boolean[] statesOfRow = new boolean[numberOfVariables];
			for (int j = 0; j < numberOfVariables; j++) {
				currentExpression = currentExpression.replaceAll(String.valueOf(((char) variables[j])),
						String.valueOf(inputs.charAt(j)));
				statesOfRow[j] = inputs.charAt(j) == '1';
			}

			if (parser.evaluate(currentExpression)) {
				String currExpression = "";
				
				for (int j = 0; j < numberOfVariables; j++) {
					currExpression = currExpression + ((statesOfRow[j] == true) ? '1' : '0');

				}
				minTerms.add(currExpression);
			}
		}
		return minTerms;
	}

	public static List<String> convertOutputsToMinterms(boolean[][] validInputRows) {

		if (validInputRows == null || validInputRows.length < 1)
			throw new RuntimeException("ERROR when generating minterm-list from inputs");

		List<String> minterms = new ArrayList<String>();

		for (int i = 0; i < validInputRows.length; i++) {

			StringBuilder builder = new StringBuilder();

			for (int j = 0; j < validInputRows[i].length; j++)
				builder.append(((validInputRows[i][j]) ? "1" : "0"));
			minterms.add(builder.toString());

		}
		return minterms;

	}

}
