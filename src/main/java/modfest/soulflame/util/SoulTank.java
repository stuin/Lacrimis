package modfest.soulflame.util;


public class SoulTank {
	private final int capacity;
	private int tears = 0;

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
	}
	
	public int removeTears(int goal) {
		if(tears >= goal) {
			tears -= goal;
			return goal;
		}
		goal = tears;
		tears = 0;
		return goal;
	}
	
	public int addTears(int goal) {
		if(tears + goal > capacity) {
			goal = capacity - tears;
			tears = capacity;
			return goal;
		}
		tears += goal;
		return goal;
	}
}
