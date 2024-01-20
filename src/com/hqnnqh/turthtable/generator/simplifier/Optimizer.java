package com.hqnnqh.turthtable.generator.simplifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Optimizer {

	private List<List<String>> pos; // Product of Sum representation for prime implicants

	private List<Set<String>> sop; // Sum of Product representation for simplified expressions, X*X=X

	private Set<String> sSop; // Set representation of Sum of Product (SOP) as strings, X+X=X

	/**
	 * Implementation of petrick's method to optimize boolean expressions down to
	 * essential prime implicants.
	 * 
	 * @param primeImplicants - prime implicants of a given expression
	 * @param minterms        - minterms of a given expression
	 */
	public Optimizer(List<String> primeImplicants, List<String> minterms) {
		pos = new ArrayList<>();
		sop = new ArrayList<>();
		sSop = new HashSet<>();

		// Create Product of Sum (POS) representation
		for (int i = 0; i < minterms.size(); i++) {

			List<String> sum = new ArrayList<>();

			for (int j = 0; j < primeImplicants.size(); j++) {
				if (covers(primeImplicants.get(j), minterms.get(i))) {
					sum.add(primeImplicants.get(j));
				}
			}

			pos.add(sum);
		}

	}

	/**
	 * Optimizes given prime implicants to essential ones. If there are multiple
	 * solutions, it returns the first one.
	 * 
	 * @return Set of essential prime implicants
	 */
	public Set<String> solve() {
		convertPOSToSOP();
		Collections.sort(sop, (a, b) -> Integer.compare(a.size(), b.size()));
		return sop.get(0);
	}

	public List<Set<String>> solveToShortest() {

		int shortestSolution = solve().size();

		return sop.stream().filter(set -> set.size() == shortestSolution).collect(Collectors.toList());
	}

	/**
	 * Returns whether a specific prime implicant covers a minterm
	 * 
	 * @param pi      - prime implicant
	 * @param minterm
	 * @return whether the pi covers the minterm
	 */
	private boolean covers(String pi, String minterm) {
		for (int i = 0; i < minterm.length(); i++) {
			if (pi.charAt(i) != '-' && pi.charAt(i) != minterm.charAt(i))
				return false;
		}
		return true;

	}

	/**
	 * Converts Product of Sum (POS) to Sum of Product (SOP)
	 */
	private void convertPOSToSOP() {
		Set<String> product = new HashSet<>();
		expand(product, 0, pos.size());

	}

	/**
	 * Recursive method to expand the product terms to obtain SOP
	 * 
	 * @param product
	 * @param posIndex
	 * @param size
	 */
	private void expand(Set<String> product, int posIndex, int size) {

		if (posIndex == size) {
			// combination complete

			String stringRep = convertToString(product);
			if (!sSop.contains(stringRep)) {
				sSop.add(stringRep);
				sop.add(new HashSet<>(product));
			}
			return;
		}
		// Iterate through sums at current index
		int posSize = pos.get(posIndex).size();
		for (int i = 0; i < posSize; i++) {
			if (!product.contains(pos.get(posIndex).get(i))) {
				product.add(pos.get(posIndex).get(i));
				expand(product, posIndex + 1, size);
				// removed again to explore other possibilities
				product.remove(pos.get(posIndex).get(i));
			}
			// current sum has already been computed
			else
				expand(product, posIndex + 1, size);
		}
	}

	/**
	 * Converts set of prime implicants to string representation
	 * 
	 * @param product - Set of prime implicants
	 * @return String representation of the prime implicants
	 */
	private String convertToString(Set<String> product) {
		StringBuilder result = new StringBuilder();
		for (String s : product)
			result.append(s);
		return result.toString();
	}

}
