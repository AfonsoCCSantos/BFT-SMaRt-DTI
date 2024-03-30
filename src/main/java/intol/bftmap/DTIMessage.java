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

public class DTIMessage<K,V> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DTIRequests type;
	private V value;
	private K key;
	private String name;
	private String uri;
    private List<Coin> coinList;
    private List<NFT> nftList;
    private List<Integer> idList;
	
	public DTIMessage() {}
	
	public static <K,V> byte[] toBytes(DTIMessage<K,V> message) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);

        objOut.flush();
        byteOut.flush();

        return byteOut.toByteArray();
    } 
    
    @SuppressWarnings("unchecked")
    public static <K,V> DTIMessage<K,V> fromBytes(byte[] rep) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (DTIMessage<K,V>) objIn.readObject();
    }
    
    public V getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (V)value;
    }
    
    public void setType(DTIRequests type) {
        this.type = type;
    }
    
    public DTIRequests getType() {
        return type;
    }
    
    public K getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    public void setKey(Object key) {
        this.key = (K)key;
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
}
