package com.hqnnqh.turthtable.generator.main;

import java.util.Arrays;

import com.hqnnqh.turthtable.generator.TruthTableGenerator;
import com.hqnnqh.turthtable.generator.simplifier.MinTermConverter;
import com.hqnnqh.turthtable.generator.simplifier.Simplifier;

/**
 * 
 * TODO: better description
 * 
 * Java Implementation of Logical Stack Calculations, Quine-McKluskey Algorithm
 * and Petrick's Method
 * 
 * @author Hqnnqh
 *
 */

public class Main {

	public static void main(String[] args) {

//		(A == (!C->!B)) && (B == (A&&C)) && (C==!A)
//		(S->(B!=T)) && (G==(S&&(T||B))) && (T==S||(S&&G)) && (!(S||G) ==B) && (B->(G!=T))
//		(s==(r->(!v||!t))) && (v==(u&&!t)) && (t==s&&u&&v) && (u == !r)
//		(a==(h->(!b&&!c))) && (b==(d&&!c)) && (c==a&&b&&d) && (d == !h)
//		(p->q) -> (q->r->(p->r))
//		!A&&!B&&!C&&!D || !A&&!B&&!C&&D || !A&&!B&&C&&D || !A&&B&&C&&D || A&&!B&&!C&&!D || A&&!B&&!C&&D || A&&!B&&C&&D || A&&B&&C&&D
//		TODO: is this the right output? !A&&!B&&!C || !A&&!B&&C || !A&&B&&!C || A&&!B&&C||A&&B&&!C || A&&B&&C
//		(a->b)->((!a->b)->b)
//		TruthTableGenerator generator = new TruthTableGenerator(formular);
//		generator.generateTable();
		String formular = "(a>b=c-d) = ((a>b=c)-d)";
		TruthTableGenerator generator = new TruthTableGenerator(formular);

		generator.generateTable();

		Simplifier simplifier = new Simplifier(MinTermConverter.getVariables(formular),
				MinTermConverter.convertToStringList(formular));
		String minimizedFormular = simplifier.minimize();
		TruthTableGenerator generatorMinimized = new TruthTableGenerator(minimizedFormular);
		generatorMinimized.generateTable(MinTermConverter.getVariables(formular));

		System.out.println("initial and minimized outputs are equal: "
				+ Arrays.equals(generator.getOutputs(), generatorMinimized.getOutputs()));
	}
}
