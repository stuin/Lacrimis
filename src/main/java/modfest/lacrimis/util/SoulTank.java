package modfest.lacrimis.util;

import java.util.ArrayList;

public class SoulTank {
	private final ArrayList<Runnable> listeners = new ArrayList<>();
	private final int max_capacity;
	private int capacity;
	private int tears = 0;

	public SoulTank(int capacity) {
		this.max_capacity = capacity;
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getTears() {
		return tears;
	}

	public int getSpace() {
		return capacity - tears;
	}

	public void setLimit(int capacity) {
		if(capacity <= max_capacity && capacity >= 0)
			this.capacity = Math.max(capacity, tears);
	}

	public void setTears(int tears) {
		if(tears >= 0 && tears <= capacity)
			this.tears = tears;
		else if(tears > capacity)
			this.tears = capacity;

		for(Runnable r : listeners)
			r.run();
	}

	public boolean transfer(SoulTank value, int amount) {
		//Move tears between tanks
		int in = value.removeTears(amount);
		int out = addTears(in);
		if(out < in)
			value.addTears(in - out);
		return out > 0;
	}

	public int removeTears(int goal) {
		if(tears >= goal)
			tears -= goal;
		else {
			goal = tears;
			tears = 0;
		}

		for(Runnable r : listeners)
			r.run();
		return goal;
	}

	public int addTears(int goal) {
		if(tears + goal > capacity) {
			goal = capacity - tears;
			tears = capacity;
		} else
			tears += goal;

		for(Runnable r : listeners)
			r.run();
		return goal;
	}

	public void addListener(Runnable listener) {
		listeners.add(listener);
	}
}
