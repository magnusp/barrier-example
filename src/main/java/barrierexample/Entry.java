package barrierexample;

public class Entry {
	int id;
	String data;
	private int barrier;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getBarrier() {
		return this.barrier;
	}

	public void setBarrier(int barrier) {
		this.barrier = barrier;
	}
}
