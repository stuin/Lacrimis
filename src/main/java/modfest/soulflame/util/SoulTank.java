package modfest.soulflame.util;

import java.util.ArrayList;
import java.util.function.Predicate;

public class SoulTank {
	private final ArrayList<Runnable> listeners = new ArrayList<>();
	private final int capacity;
	private boolean canExtract = true;
	private int tears = 0;

	public SoulTank(int capacity) {
		this.capacity = capacity;
	}
	
	public void disableExtract() {
		canExtract = false;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getTears() {
		return tears;
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
	
	public static boolean SOURCE(Object value) {
		if(value instanceof SoulTank) {
			SoulTank tank = ((SoulTank) value);
			return tank.canExtract && tank.getTears() > 0;
		}
		return false;
	}

	public static Predicate<Object> source(int min_power) {
		return (Object value) -> SoulTank.SOURCE(value) && ((SoulTank) value).getTears() >= min_power;
	}
}
