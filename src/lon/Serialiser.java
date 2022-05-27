package lon;

import java.lang.reflect.Field;

public class Serialiser {
	public static String toLONString(Object o) throws IllegalArgumentException, IllegalAccessException {
		LONList list = new LONList();
		Class<?> c = o.getClass();

		for (Field field : c.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(LONSerialisable.class)) {
				list.add(new LONAtom(String.valueOf(field.getName())));
				list.add(new LONAtom(String.valueOf(field.get(o))));
			}
		}
		return list.toString();
	}
}
