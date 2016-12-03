package core.utilities;

@FunctionalInterface
public interface ValueSupplier<T> {

	public T getValue();
	
}
