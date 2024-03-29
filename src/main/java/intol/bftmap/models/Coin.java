package intol.bftmap.models;

import java.io.Serializable;

public class Coin implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int ownerId;
	private double value;
	
	public Coin(int id, int ownerId, double value) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.value = value;
	}
	
	public Coin(double value) {
		super();
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
