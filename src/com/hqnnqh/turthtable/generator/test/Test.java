package com.hqnnqh.turthtable.generator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hqnnqh.turthtable.generator.simplifier.MinTermConverter;
import com.hqnnqh.turthtable.generator.simplifier.Simplifier;
/**
 * 
 * @author Hannah Fluch github: Hqnnqh
 *
 */
class Test {

	/**
	 * Sources:
	 *  https://en.wikipedia.org/wiki/Quine–McCluskey_algorithm
	 *  https://en.wikipedia.org/wiki/Petrick%27s_method
	 *  https://krex.k-state.edu/bitstream/handle/2097/22765/LD2668R41966L334.pdf;jsessionid=1B1E0AD378A9280693DDC7F7A8BB6B25?sequence=1
	 *  https://ocw.nthu.edu.tw/ocw/upload/230/news/資工系王俊堯教授數位邏輯設計Unit%206_Quine-McClusky%20Method.pdf
	 *  POS Logic Source year 2
	 */

	// Tests for base junctions

	@org.junit.jupiter.api.Test
	public void testExpression1Base() {
		// !A&!D |!(B&A | 0 & B) & !A
		assertEquals("A|!B&!C", simplify("(A | !B | !C) & (A | !B | C) & (A | B | !C)"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression2Base() {

		assertEquals("a&!c|!a&b|!b&c", simplify("a&!b | b&!c | !b&c | !a&b"));

	}

	// Tests for expressions with only one true case
	@org.junit.jupiter.api.Test
	public void testExpression3Unique() {
		assertEquals("s&r&!v&!t&!u", simplify("(s=(r>(!v|!t))) & (v=(u&!t)) & (t=s&u&v) & (u = !r)"));

	}

	@org.junit.jupiter.api.Test
	public void testExpression4Unique() {
		assertEquals("a&h&!b&!c&!d", simplify("(a=(h>(!b&!c))) & (b=(d&!c)) & (c=a&b&d) & (d = !h)"));
	}

	// Tests for tautologies and contradictions
	@org.junit.jupiter.api.Test
	public void testExpression5Tautology() {

		assertEquals("1", simplify("(p>q) > (q>r>(p>r))"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression6Tautology() {

		assertEquals("1", simplify("((A > B) & !B) > !A"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression7Tautology() {
		assertEquals("1", simplify("A > B = !B > !A"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression8Contradiction() {

		assertEquals("0", simplify("A > B = !B & A"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression9Contradiction() {

		assertEquals("0", simplify("((A > B) & (B > C)) = !(A > B) | !(B > C)"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression10Contradiction() {

		assertEquals("0", simplify("!A & A"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression11() {

		assertEquals("!B&!C|C&D", simplify(
				"!A&!B&!C&!D | !A&!B&!C&D | !A&!B&C&D | !A&B&C&D | A&!B&!C&!D | A&!B&!C&D |A&!B&C&D | A&B&C&D"));
	}

	@org.junit.jupiter.api.Test
	public void testExpression12() {
		assertEquals("!A&!C|A&B|!B&C", simplify("!A&!B&!C | !A&!B&C | !A&B&!C | A&!B&C|A&B&!C| A&B&C"));

	}

	private String simplify(String formular) {
		return new Simplifier(MinTermConverter.getVariables(formular), MinTermConverter.fromFormular(formular))
				.minimize();
	}

}
