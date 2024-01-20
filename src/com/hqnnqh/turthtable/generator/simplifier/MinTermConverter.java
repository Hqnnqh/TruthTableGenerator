package com.hqnnqh.turthtable.generator.simplifier;

import java.util.ArrayList;
import java.util.List;

import com.hqnnqh.turthtable.generator.parser.Parser;

public class MinTermConverter {

	public static char[] getVariables(String formular) {
		int[] variablesAsInts = formular.chars().filter(character -> Character.isLetter(((char) character))).distinct()
				.toArray();

		char[] variables = new char[variablesAsInts.length];

		for (int i = 0; i < variables.length; i++)
			variables[i] = (char) variablesAsInts[i];
		return variables;
	}

	public static List<String> fromFormular(String formular) {
		List<String> minterms = new ArrayList<>();

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
				minterms.add(currExpression);
			}
		}
		return minterms;
	}

	public static List<String> fromOutputs(boolean[] outputs) {
		if (!((outputs.length & (outputs.length - 1)) == 0 && outputs.length != 0) || outputs.length == 1) {
			throw new RuntimeException("Invalid number of outputs! Required to be a power of 2 and bigger than 1!");
		}
		List<String> minterms = new ArrayList<>();

		int numberOfVariables = (int) (Math.log(outputs.length) / Math.log(2));
		int numberOfRows = (int) Math.pow(2, numberOfVariables);

		for (int i = 0; i < numberOfRows; i++) {
			if (outputs[i] == true) {
				String inputs = Integer.toBinaryString(i);
				// add zeros at beginning
				while (inputs.length() < numberOfVariables)
					inputs = "0" + inputs;

				minterms.add(inputs);
			}
		}
		return minterms;

	}

}
