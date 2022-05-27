package lon;

import java.util.ArrayList;

public class LONList implements LONObject {
	private final ArrayList<LONObject> list;

	public LONList() {
		list = new ArrayList<>();
	}

	public int size() {
		return list.size();
	}

	public LONObject get(int index) {
		return list.get(index);
	}

	public void add(LONObject object) {
		list.add(object);
	}

	public void remove(LONObject object) {
		list.remove(object);
	}

	@Override
	public String toString() {
		String string = "(";

		for (LONObject object : list) {
			string += object + " ";
		}
		return string.substring(0, string.length() - 1) + ")";
	}
}