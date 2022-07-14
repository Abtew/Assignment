package Java_assignment.Java_assignment.csvReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class ParenthesisChecker {
	public static void main(String args[]) throws IOException {
		// Take input from the keyboard
		System.out.print("Enter value: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String stBuf = br.readLine();

		// validate the input
		ParenthesisChecker par = new ParenthesisChecker();
		System.out.println(par.validatePar(stBuf));
	}

	private boolean validatePar(String str) {
		// Stack - to pop out LIFO
		Stack<Character> stk = new Stack<>();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			/*
			 * check with closing parenthesis. If stack is empty or does not pops out the
			 * proper opening parenthesis, the closing parenthesis does not have a proper
			 * opening paranthesis.
			 */
			if (ch == '[' || ch == '(' || ch == '{') {
				stk.push(ch);
			} else if (ch == ']') {
				if (stk.isEmpty() || stk.pop() != '[') {
					return false;
				}
			} else if (ch == '}') {
				if (stk.isEmpty() || stk.pop() != '{') {
					return false;
				}
			} else if (ch == ')') {
				if (stk.isEmpty() || stk.pop() != '(') {
					return false;
				}
			}
		}
		// At this point, if stack is empty, parenthesis are properly closed
		return stk.isEmpty();
	}
}
