package com.hqnnqh.turthtable.generator.main;

import java.util.Arrays;
import java.util.List;

import com.hqnnqh.turthtable.generator.TruthTableGenerator;
import com.hqnnqh.turthtable.generator.simplifier.MinTermConverter;
import com.hqnnqh.turthtable.generator.simplifier.Simplifier;

/**
 * 
 * 
 * Java Implementation of Logical Stack Calculations, Quine-McKluskey Algorithm
 * and Petrick's Method
 * 
 * @author Hannah Fluch git: Hqnnqh
 *
 */

public class Main {
	public static void main(String[] args) {
		long startingTime = System.currentTimeMillis();
		String formular = "1 & 0";
		TruthTableGenerator generator = new TruthTableGenerator(formular);

		generator.generateTable();

		Simplifier simplifier = new Simplifier(MinTermConverter.getVariables(formular),
				MinTermConverter.convertToStringList(formular));
		List<String> solutions = simplifier.getMinimizedSolutions();
		
		System.out.println("Shortest possible solutions: " + solutions);

		String minimizedFormular = solutions.get(0);
		TruthTableGenerator generatorMinimized = new TruthTableGenerator(minimizedFormular);
		generatorMinimized.generateTable(MinTermConverter.getVariables(formular));

		System.out.println("initial and minimized outputs are equal: "
				+ Arrays.equals(generator.getOutputs(), generatorMinimized.getOutputs()));
		System.out.println("Time in millisecond: " + (System.currentTimeMillis() - startingTime));

	}
}
