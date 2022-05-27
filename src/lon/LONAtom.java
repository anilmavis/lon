package lon;

public class LONAtom implements LONObject {
	private final String value;

	public LONAtom(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}