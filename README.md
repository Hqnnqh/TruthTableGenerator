# TruthTableGenerator
Logical truthtable generator and logical expression simplifier

## Algorithms
### Boolean Expression Parser & Evaluator
Uses a stack of values and one of operators to evaluate expression according to the right precedence.

### Quine-McCluskey Minimization
Used to minimize the boolean expression down to it's prime implicants.
[Wikipedia](https://en.wikipedia.org/wiki/Quine%E2%80%93McCluskey_algorithm)

### Petrick's Method
Used to optimize the prime implicants of the expression down to the essential prime implicants.
[Wikipedia](https://en.wikipedia.org/wiki/Petrick%27s_method)

## Requirements
- JDK
- JUnit