package lon;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static enum TokenType {
		LEFT_BRACKET, RIGHT_BRACKET, ATOM, SPACE
	}

	public static class Token {
		private final TokenType type;
		private final String value;

		public Token(TokenType type, String value) {
			this.type = type;
			this.value = value;
		}

		public TokenType getType() {
			return type;
		}

		public String getValue() {
			return value;
		}
	}

	private static List<Token> analyse(String form) throws LONException {
		List<Token> tokens = new ArrayList<>();
		String value = "";
		int leftBracketCounter = 0;
		int rightBracketCounter = 0;

		for (char c : form.toCharArray()) {
			switch (c) {
			case '(': {
				leftBracketCounter++;

				if (!value.isEmpty()) {
					tokens.add(new Token(TokenType.ATOM, value));
					value = "";
				}
				tokens.add(new Token(TokenType.LEFT_BRACKET, String.valueOf(c)));
				break;
			}
			case ')': {
				rightBracketCounter++;

				if (!value.isEmpty()) {
					tokens.add(new Token(TokenType.ATOM, value));
					value = "";
				}
				tokens.add(new Token(TokenType.RIGHT_BRACKET, String.valueOf(c)));
				break;
			}
			case ' ': {
				if (!value.isEmpty()) {
					tokens.add(new Token(TokenType.ATOM, value));
					value = "";
				}
				tokens.add(new Token(TokenType.SPACE, String.valueOf(c)));
				break;
			}
			default:
				value += c;
			}
		}

		if (leftBracketCounter > rightBracketCounter || leftBracketCounter < rightBracketCounter) {
			throw new LONException("unbalanced brackets");
		}

		if (!value.isEmpty()) {
			tokens.add(new Token(TokenType.ATOM, value));
		}
		return tokens;
	}

	public static Object parse(String form, Class<?> myClass)
			throws LONException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		if (form.isBlank()) {
			throw new LONException("blank form");
		}
		List<Token> tokens = analyse(form);
		LONList root = null;
		LONList list = null;
		LONList parent = null;

		for (Token token : tokens) {
			switch (token.getType()) {
			case LEFT_BRACKET -> {
				if (root == null) {
					root = new LONList();
					list = root;
					parent = list;
				} else {
					LONList l = new LONList();
					list.add(l);
					list = l;
				}
			}
			case RIGHT_BRACKET -> list = parent;
			case ATOM -> {
				if (root == null) {
					return new LONAtom(token.getValue());
				}
				list.add(new LONAtom(token.getValue()));
			}
			case SPACE -> {
			}
			}
		}
		Constructor<?> constructor = myClass.getConstructors()[0];
		Object[] initargs = new Object[constructor.getParameterCount()];

		for (int i = 0; i < initargs.length; i++) {
			if (constructor.getParameterTypes()[i].equals(String.class)) {
				initargs[i] = root.get(i).toString();
			} else if (constructor.getParameterTypes()[i].equals(int.class)) {
				initargs[i] = Integer.parseInt(root.get(i).toString());
			} else if (constructor.getParameterTypes()[i].equals(double.class)) {
				initargs[i] = Double.parseDouble(root.get(i).toString());
			}
		}
		Object instance = constructor.newInstance(initargs);
		return instance;
	}
}