package example;

import java.lang.reflect.InvocationTargetException;

import lon.LONException;
import lon.Parser;
import lon.Serialiser;

public class Main {
	public static void main(String[] args)
			throws LONException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		Person anil = new Person("anil", 0, 1);
		System.out.println(Serialiser.toLONString(anil));
		Person gok = (Person) Parser.parse("(gök 2 3)", Person.class);
		System.out.println(Serialiser.toLONString(gok));
	}
}