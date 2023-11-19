package com.hqnnqh.turthtable.generator;

import com.hqnnqh.turthtable.generator.parser.Parser;

public class TruthTableGenerator {

	private final String expression;

	private int[] variables; // ASCII codes of variable-chars
	private int rows;

	private final Parser parser;

	private boolean[] outputs;

	public TruthTableGenerator(String expression) {

		this.expression = expression;
		this.variables = expression.chars().filter(character -> Character.isLetter(((char) character))).distinct()
				.toArray();
		this.rows = (int) Math.pow(2, variables.length);
		this.parser = new Parser();
		this.outputs = new boolean[rows];

	}

	public void generateTable(char[] variables) {
		this.variables = new int[variables.length];
		for (int i = 0; i < variables.length; i++)
			this.variables[i] = variables[i];

		this.rows = (int) Math.pow(2, variables.length);
		this.outputs = new boolean[rows];

		generateTable();

	}

	public void generateTable() {
		for (int i = 0; i < variables.length; i++)
			System.out.print(((char) variables[i]) + ((i == variables.length - 1) ? "|" : "|\t"));
		System.out.println(expression + "|");
		for (int i = 0; i < rows; i++) {

			String currentExpression = expression;
			String inputs = Integer.toBinaryString(i);
			// add zeros at beginning
			while (inputs.length() < variables.length)
				inputs = "0" + inputs;

			for (int j = 0; j < variables.length; j++) {
				currentExpression = currentExpression.replaceAll(String.valueOf(((char) variables[j])),
						String.valueOf(inputs.charAt(j)));
				System.out.print(inputs.charAt(j) + "|\t");
			}

			int result = ((outputs[i] = parser.evaluate(currentExpression)) ? 1 : 0);
			System.out.println(result + "\t|");
		}
		System.out.println();
	}

	public boolean[] getOutputs() {
		return this.outputs;
	}

}
