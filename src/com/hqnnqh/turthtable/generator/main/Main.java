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

//		(A = (!C>!B)) & (B = (A&C)) & (C=!A)
//		(S>(B-T)) & (G=(S&(T-B))) & (T=S|(S&G)) & (!(S|G) =B) & (B>(G-T))
//		(s=(r>(!v|!t))) & (v=(u&!t)) & (t=s&u&v) & (u = !r)
//		(a=(h>(!b&!c))) & (b=(d&!c)) & (c=a&b&d) & (d = !h)
//		(p>q) > (q>r>(p>r))
//		!A&!B&!C&!D | !A&!B&!C&D | !A&!B&C&D | !A&B&C&D | A&!B&!C&!D | A&!B&!C&D | A&!B&C&D | A&B&C&D
//		TODO: is this the right outpu? !A&!B&!C | !A&!B&C | !A&B&!C | A&!B&C|A&B&!C | A&B&C
//		(a>b)>((!a>b)>b)
//		"!A&!B&!C&!D | !A&!B&!C&D | !A&!B&C&D | !A&B&C&D | A&!B&!C&!D | A&!B&!C&D | A&!B&C&D | A&B&C&D"
//		p&q&r | p&q&!r | p&!q&r | p&!q&!r | !p&q&r | !p&q&!r | !p&!q&r
//		!p & !q & !r | !p & !q & r | !p & q & !r | !p & q & r | p & !q & !r | p & q & r NOT IN BASE JUNCTION! TOOD
//		(S>(B-T)) & (G=(S&(T-B))) & (T=S|(S&G)) & (!(S|G) =B) & (B>(G-T))
//		!(b&c) | !a & !c & b
//		a & b & !c | a & !b & !c | a & !b & c | !a & b & !c
//		!a & !b & !c | !a &!b&c | !a &b&c | a & !b &c
//		(a&b&c | a&b&!c | !a&!b&!c | !a & !b & c | !a & b & !c | !a & b & c) = !(a & !b & !c | a & !b & c)
//		"A & !(B&A&!C&D) | !A & B & !C & !D 
// !A & B & C & !D | A & !B & !C & !D | A & !B & !C & D | A & !B & C & !D | A & !B & C & D | A & B & !C& !D | A & B & !C & D | A & B & C& !D
//		!A & B & !C & !D | A & !B & !C & !D | A & !B & C & !D | A & !B & C & !D | A & B & !C & D | A & B & !C& !D | A & B & C & !D | A & B & C& D
//		!a&!b&!c&!d | !a&!b&!c&d | !a & !b & c & !d | a & !b & !c & !d | !a & b & !c & d | !a & b & c & !d | a & !b & !c & d | a & !b & c & !d | !a & b & c & d | a & b& c& !d https://ocw.nthu.edu.tw/ocw/upload/230/news/資工系王俊堯教授數位邏輯設計Unit%206_Quine-McClusky%20Method.pdf
		long startingTime = System.currentTimeMillis();
		String formular = "!A&!B&!C | !A&!B&C | !A&B&!C | A&!B&C|A&B&!C| A&B&C";
		TruthTableGenerator generator = new TruthTableGenerator(formular);

		generator.generateTable();

		Simplifier simplifier = new Simplifier(MinTermConverter.getVariables(formular),
				MinTermConverter.convertToStringList(formular));

		String minimizedFormular = simplifier.minimize();

		TruthTableGenerator generatorMinimized = new TruthTableGenerator(minimizedFormular);
		generatorMinimized.generateTable(MinTermConverter.getVariables(formular));

		System.out.println("initial and minimized outputs are equal: "
				+ Arrays.equals(generator.getOutputs(), generatorMinimized.getOutputs()));
		System.out.println("Time in millisecond: " + (System.currentTimeMillis() - startingTime));

	}
}
