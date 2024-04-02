package intol.bftmap;

import java.io.Serializable;
import java.util.List;

import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class DTIMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DTIRequests type;
	private Coin coin;
	private NFT nft;
	private int key;
	private double value;
	private String name;
	private String uri;
	private String errorMessage;
    private List<Coin> coinList;
    private List<NFT> nftList;
    private List<Integer> idList;
	
	public DTIMessage() {}
	
	public static byte[] toBytes(DTIMessage message) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);

        objOut.flush();
        byteOut.flush();

        return byteOut.toByteArray();
    } 
    
    public static DTIMessage fromBytes(byte[] rep) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (DTIMessage) objIn.readObject();
    }
    
    public void setType(DTIRequests type) {
        this.type = type;
    }
    
    public DTIRequests getType() {
        return type;
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

    public List<Coin> getCoinList() {
        return coinList;
    }

    public void setCoinList(List<Coin> coinList) {
        this.coinList = coinList;
    }

    public List<NFT> getNftList() {
        return nftList;
    }

    public void setNftList(List<NFT> nftList) {
        this.nftList = nftList;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

	public Coin getCoin() {
		return coin;
	}

	public void setCoin(Coin coin) {
		this.coin = coin;
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public NFT getNft() {
		return nft;
	}

	public void setNft(NFT nft) {
		this.nft = nft;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
