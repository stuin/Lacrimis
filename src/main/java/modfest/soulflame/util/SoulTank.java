package modfest.soulflame.util;


public class SoulTank {
	private final int capacity;
	private int tears = 0;
	private Runnable listener = null;

	public SoulTank(int capacity) {
		this.capacity = capacity;
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

		if(listener != null)
			listener.run();
	}

	public boolean transfer(SoulTank value, int amount) {
		int in = value.removeTears(amount);
		int out = addTears(in);
		if(out < in)
			value.addTears(in - out);
		return out > 0;
	}

	public int removeTears(int goal) {
		if(tears >= goal) {
			tears -= goal;
			return goal;
		}
		goal = tears;
		tears = 0;

		if(listener != null)
			listener.run();

		return goal;
	}
	
	public int addTears(int goal) {
		if(tears + goal > capacity) {
			goal = capacity - tears;
			tears = capacity;
			return goal;
		}
		tears += goal;

		if(listener != null)
			listener.run();

		return goal;
	}
	
	public void setListener(Runnable listener) {
		this.listener = listener;
	}
}
