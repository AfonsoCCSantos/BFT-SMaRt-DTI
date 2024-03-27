package intol.bftmap;

import java.io.Serializable;
import java.util.HashSet;

import intol.bftmap.models.Coin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class DTIMessage<K,V> implements Serializable {
	private DTIRequests type;
	private V value;
	private K key;
	private String name;
	private String uri;
    private HashSet<Coin> coinSet;
	
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

    public HashSet<Coin> getCoinSet() {
        return coinSet;
    }

    public void setCoinSet(HashSet<Coin> coinSet) {
        this.coinSet = coinSet;
    }

}
