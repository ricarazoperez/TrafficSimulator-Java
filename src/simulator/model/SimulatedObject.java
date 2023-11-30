package simulator.model;

import org.json.JSONObject;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public abstract class SimulatedObject {

	protected String _id;

	SimulatedObject(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	@Override
	public String toString() {
		return _id;
	}

	abstract void advance(int time) throws InvalidArgumentException, ExecutionException;

	abstract public JSONObject report();
}
