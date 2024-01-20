package com.hqnnqh.turthtable.generator.simplifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simplifier {

	private int numberOfVariables;
	private char[] variables;
	private List<String> minterms;
	private String dontcares;

	/**
	 * 
	 * Simplifier for boolean expressions - using the QuineMcCluskey and Petrick's
	 * Algorithm
	 * 
	 * @param variables - all distinct variables used in the expression
	 * @param minterms  - all minterms in the expression (format: e.g.: 00-1)
	 */
	public Simplifier(char[] variables, List<String> minterms) {

		this.variables = variables;
		this.numberOfVariables = variables.length;
		this.minterms = minterms;
		this.dontcares = "-".repeat(numberOfVariables);
	}

// Quine-McCluskey: 
//	Minimizes down to the prime implicants

	/**
	 * 
	 * Minimize given expression.
	 * 
	 * @return one of the solutions for the simplified expression
	 */
	public String minimize() {

		List<String> newMinterms = minterms;
		List<String> prevMinterms;

		// reduce the expression as long as it changes the minterms

		do {
			prevMinterms = newMinterms;
			newMinterms = reduce(newMinterms);

		} while (!listsEqual(prevMinterms, newMinterms));

		List<String> primeImplicants = newMinterms;
		Optimizer optimizer = new Optimizer(primeImplicants, minterms);

		List<String> essentialPrimeImplicants = new ArrayList<>(optimizer.solve());

		StringBuilder value = new StringBuilder();

		if (essentialPrimeImplicants.isEmpty())
			return "0";

		for (int i = 0; i < essentialPrimeImplicants.size(); i++) {
			String implicant = getValue(essentialPrimeImplicants.get(i));
			for (int j = 0; j < implicant.length(); j++) {
				char currentChar = implicant.charAt(j);
				value.append(currentChar);

				if (j < implicant.length() - 1 && currentChar != '!') {
					// Add "&" only if the current character is not "!" and it's not the last
					// character in the implicant
					value.append("&");
				}
			}
			if (i < essentialPrimeImplicants.size() - 1) {
				value.append("|");
			}
		}
		return value.toString();
	}
	
	

	/**
	 * Pad a binary representation with zeros so that is has the right length
	 * (number of variables)
	 * 
	 * @param bin - binary string
	 * @return padded binary representation
	 */

	private String pad(String bin) {

		while (bin.length() < numberOfVariables)
			bin = "0" + bin;

		return bin;
	}

	/**
	 * 
	 * Whether two binary strings match or not
	 * 
	 * Example1: bin1="1111", bin2="1-01" -> returns false Example2: bin1="1100",
	 * bin2="1110" -> returns true
	 * 
	 * @param bin1
	 * @param bin2
	 * @return if the two binaries only differ by one bit
	 */
	private boolean match(String bin1, String bin2) {

		bin1 = pad(bin1);
		bin2 = pad(bin2);

		int flag = 0;

		for (int i = 0; i < bin1.length(); i++)
			if (bin1.charAt(i) != bin2.charAt(i))
				flag++;

		return (flag == 1);
	}

	/**
	 * 
	 * Replaces the differing bit of matching binary strings with dontcares(-).
	 * 
	 * Example: bin1="1100", bin2="1110" -> returns "11-0"
	 * 
	 * @param bin1
	 * @param bin2
	 * @return the replaced binary string
	 */
	private String replaceComplements(String bin1, String bin2) {

		bin1 = pad(bin1);
		bin2 = pad(bin2);

		String temp = "";

		for (int i = 0; i < bin1.length(); i++) {
			if (bin1.charAt(i) != bin2.charAt(i))
				temp += "-";
			else
				temp += bin1.charAt(i);
		}

		return temp;

	}

	/**
	 * Reduces a list of minterms.
	 * 
	 * @param minterms - list of minterms in binary represention
	 * @return reduced list
	 */

	private List<String> reduce(List<String> minterms) {

		// alternative: first sort and then only compare minterm(n) with minterm(n+1)

		List<String> newMinterms = new ArrayList<>();
		int max = minterms.size(); // maximum number of minterms
		int[] matched = new int[max]; // minterms that have been matched

		for (int i = 0; i < max - 1; i++) { // last one does not have to be compared

			// compare each minterm with the ones following
			for (int j = i + 1; j < max; j++) {

				// If matching pair is found, replace the differing bits with dont-cares.
				if (match(minterms.get(i), minterms.get(j))) {
					matched[i] = 1; // 1 indicates that the minterm has been matched -> is not a prime implicant
					matched[j] = 1;

					// If the exact same minterm has not been added, then it gets added now.
					if (!newMinterms.contains(replaceComplements(minterms.get(i), minterms.get(j))))
						newMinterms.add(replaceComplements(minterms.get(i), minterms.get(j)));
				}

			}

		}

		// adding all minterms that weren't able to be matched -> are prime
		// implicants
		for (int i = 0; i < max; i++)
			if (matched[i] != 1 && !newMinterms.contains(minterms.get(i)))
				newMinterms.add(minterms.get(i));

		return newMinterms;

	}

	/**
	 * Checks if two lists contain the same values
	 * 
	 * @param list1
	 * @param list2
	 * @return if they are equal/contain the same values
	 */

	private boolean listsEqual(List<String> list1, List<String> list2) {

		if (list1.size() != list2.size())
			return false;

		Collections.sort(list1);
		Collections.sort(list2);

		for (int i = 0; i < list1.size(); i++) {
			if (!list1.get(i).equals(list2.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Formats a binary string to base-logical syntax.
	 * 
	 * @param bin - given binary string
	 * @return the formatted string
	 */

	private String getValue(String bin) {

		if (bin.equals(dontcares)) { // always return true
			return "1";
		}
		String temp = "";

		bin = pad(bin);

		for (int i = 0; i < numberOfVariables; i++) {
			if (bin.charAt(i) != '-') {
				if (bin.charAt(i) == '0')
					temp += ("!" + variables[i]);
				else
					temp += variables[i];
			}
		}
		return temp;

	}

}
