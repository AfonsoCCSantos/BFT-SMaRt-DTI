package intol.bftmap.models;

public class NFT {
	
	private int id;
	private int ownerId;
	private String name;
	private String uri;
	private double value;
	
	public NFT(int id, int ownerId, String name, String uri, double value) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.name = name;
		this.uri = uri;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
