package simulator.factories;


import org.json.JSONObject;
import Exceptions.InvalidArgumentException;

public interface Factory<T> {
	public T createInstance(JSONObject info) throws InvalidArgumentException;
}
