package com.hqnnqh.turthtable.generator.parser;

import java.util.Stack;

public class FastParser {

	/**
	 * 
	 * @author Hannah Fluch Faster version of the parser with character operators
	 * 
	 */

	// TODO: check for right precedence
	private int precedenceOrder(char operator) {
		if (operator == '!')
			return 6;
		if (operator == '&')
			return 5;
		if (operator == '|')
			return 4;
		if (operator == '>')
			return 3;
		if (operator == '=')
			return 2;
		if (operator == '-')
			return 1;
		return 0;
	}

	/**
	 * 
	 * Performs the current logical operatoion on the stack
	 * 
	 * @param value1   - first value (swapped)
	 * @param value2   - second value (swapped)
	 * @param operator - logical operator to determine operation
	 * @return the result of the operation
	 */
	private boolean performOperation(boolean value1, boolean value2, char operator) {
//		System.out.println("v1: " + value1 + " v2: " + value2 + " op: " + operator);
		// value1 the right part of the operation and value2 the left part
		switch (operator) {
		case '!':
			return !value1; // in this case value2 is just false
		case '&':
			return value1 && value2; // &
		case '|':
			return value1 || value2; // |
		case '=':
			return value1 == value2; // =
		case '-':
			return value1 != value2; // -
		case '>':
			return !value2 || value1; // DeMorgan Deviation
		}

		throw new ParserException("ERROR when perfoming opertation! Unknown operator: " + operator);
	}

	/**
	 * 
	 * Evaluates a logical expression
	 * 
	 * @param expression - expression to be evaluated
	 * @return the result of the evaluated expression
	 */

	public boolean evaluate(String expression) {
		if (expression == null || expression.isBlank())
			throw new ParserException("Invalid expression!");

		boolean expectValue = true; // checks for the appropriate syntax

		Stack<Boolean> values = new Stack<>(); // all values (true:1/false:0)
		Stack<Character> operators = new Stack<>(); // all operators (&&, ||, ==, ...)

		for (int i = 0; i < expression.length(); i++) {

			char current = expression.charAt(i);

			if (Character.toString(current).matches("[\\s\\u00A0]+")) // skip whitespaces
				continue;

			else if (current == '(') {
				if (!expectValue)
					throw new ParserException("ERROR when parsing! Boolean-Operator expected!");

				operators.push(current);
				expectValue = true;
			}

			else if (current == '0' || current == '1') {
				if (!expectValue)
					throw new ParserException("ERROR when parsing! Boolean-Operator expected!");

				boolean value = current == '1';

				values.push(value);
				expectValue = false;
			}

			else if (current == ')') {
				if (expectValue)
					throw new ParserException("ERROR when parsing! Value expected!");

				// calculate the parentheses one operation at a time and then push the result
				// back into the stack

				if (!operators.contains('('))
					throw new ParserException("ERROR when parsing! Unexpected ')'!");

				while (!operators.isEmpty() && operators.peek() != '(') {
					char operator = operators.peek(); // get current top operator of stack and pop it
					operators.pop();

					boolean value1 = values.peek(); // get current top value of stack and pop it
					values.pop();

					boolean value2 = false;

					if (operator != '!') { // if the operator is an !, then we just need one value and the other
											// can be
						// false
						value2 = values.peek(); // get current top value of stack and pop it
						values.pop();
					}

					values.push(performOperation(value1, value2, operator));
				}

				// pop the '('
				if (!operators.isEmpty())
					operators.pop();

				expectValue = false;
			} else {

				if (expectValue && current != '!')
					throw new ParserException("ERROR when parsing! Value expected !");

				if (i == expression.length() - 1)
					throw new ParserException("ERROR when parsing! Value was expected!");

				// calculate the operations with higher precedence before adding another
				// operator
				while (!operators.isEmpty() && precedenceOrder(operators.peek()) >= precedenceOrder(current)) {

					char operator = operators.peek(); // get current top operator of stack and pop it
					operators.pop();

					boolean value1 = values.peek(); // get current top value of stack and pop it
					values.pop();

					boolean value2 = false;

					if (operator != '!') { // if the operator is an !, then we just need one value and the
											// other
											// can be
						value2 = values.peek(); // get current top value of stack and pop it
						values.pop();
					}

					values.push(performOperation(value1, value2, operator));

				}

				operators.push(current);
				expectValue = true;

			}
		}
		// do the rest of the operations
		while (!operators.isEmpty())

		{
			char operator = operators.peek(); // get current top operator of stack and pop it
			operators.pop();

			boolean value1 = values.peek(); // get current top value of stack and pop it
			values.pop();

			boolean value2 = false;

			if (operator != '!') { // if the operator is an !, then we just need one value and the other can be
				// false
				value2 = values.peek(); // get current top value of stack and pop it
				values.pop();
			}

			values.push(performOperation(value1, value2, operator));
		}
		return values.peek();
	}
}
