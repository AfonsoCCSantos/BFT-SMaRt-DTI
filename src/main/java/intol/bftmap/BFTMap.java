/**
 * BFT Map implementation (client side).
 *
 */
package intol.bftmap;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bftsmart.tom.ServiceProxy;

public class BFTMap<K, V> implements Map<K, V> {
    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    private final ServiceProxy serviceProxy;

    public BFTMap(int id) {
        serviceProxy = new ServiceProxy(id);
    }
    
    /**
     *
     * @param key The key associated to the value
     * @return value The value previously added to the map
     */
    @Override
    public V get(Object key) {
        byte[] rep;
        try {
            BFTMapMessage<K,V> request = new BFTMapMessage<>();
            request.setType(BFTMapRequestType.GET);
            request.setKey(key);
            
            //invokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(BFTMapMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send GET request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }
        try {
            BFTMapMessage<K,V> response = BFTMapMessage.fromBytes(rep);
            return response.getValue();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of GET request");
            return null;
        }
    }

    /**
     *
     * @param key The key associated to the value
     * @param value Value to be added to the map
     */
    @Override
    public V put(K key, V value) {
        byte[] rep;
        try {
            DTIMessage<K,V> request = new DTIMessage<>();
            request.setType(DTIRequests.MINT);
            request.setValue(value);

            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send PUT request");
            return null;
        }
        if (rep.length == 0) {
            return null;
        }

        try {
            BFTMapMessage<K,V> response = BFTMapMessage.fromBytes(rep);
            return response.getValue();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of PUT request");
            return null;
        }
    }

    public List<Coin> getMyCoins() {
        byte[] rep;
        try {
            DTIMessage<K,V> request = new DTIMessage<>();
            request.setType(DTIRequests.MY_COINS);

            //invokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(DTIMessage.toBytes(request));
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("Failed to send MY_COINS request");
            return null;
        }
        if (rep == null || rep.length == 0) {
        	logger.error("Failed to send MY_COINS request");
            return null;
        }

        try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (List<Coin>) response.getCoinList();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MY_COINS request");
            return null;
        }
    }

    public List<NFT> getMyNFTs() {
        byte[] rep;
        try {
            DTIMessage<K,V> request = new DTIMessage<>();
            request.setType(DTIRequests.MY_NFTS);

            //inWvokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MY_NFTs request");
            return null;
        }
        if (rep.length == 0) {
            return null;
        }

        try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (List<NFT>) response.getNftList();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MY_NFTs request");
            return null;
        }
    }

    public List<NFT> searchNFT(String text) {
        byte[] rep;
        try {
            DTIMessage<K,V> request = new DTIMessage<>();
            request.setType(DTIRequests.SEARCH_NFT);
            request.setName(text);

            //invokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(DTIMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send SEARCH_NFT request");
            return null;
        }
        if (rep.length == 0) {
            return null;
        }

        try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (List<NFT>) response.getNftList();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SEARCH_NFT request");
            return null;
        }
    }
    
    /**
     * Method created to be able to mint a new coin.
     * 
     * @param coinValue
     * @return
     */
    public int mintCoin(double coinValue) {
    	byte[] rep;
    	try {
    		DTIMessage<K,V> request = new DTIMessage<>();
    		request.setType(DTIRequests.MINT);
    		request.setValue(coinValue);
    		
    		rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
    	} catch (IOException e) {
            logger.error("Failed to send MINT request");
            return -1;
        }
    	
    	if (rep.length == 0) {
            return -1;
        }
    	
    	try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (int) response.getValue();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MINT request");
            return -1;
        }
    }

    public int mintNFT(String name, String uri, double nftValue) {
    	byte[] rep;
    	try {
    		DTIMessage<K,V> request = new DTIMessage<>();
    		request.setType(DTIRequests.MINT_NFT);
    		request.setValue(nftValue);
    		request.setName(name);
    		request.setUri(uri);
    		
    		rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
    	} catch (IOException e) {
            logger.error("Failed to send MINT_NFT request");
            return -1;
        }
    	
    	if (rep.length == 0) {
            return -1;
        }
    	
    	try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (int) response.getValue();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MINT request");
            return -1;
        }
    }

    public int setNFTPrice(int nftId, double newNFTValue) {
    	byte[] rep;
    	try {
    		DTIMessage<K,V> request = new DTIMessage<>();
    		request.setType(DTIRequests.SET_NFT_PRICE);
            request.setKey(nftId);
    		request.setValue(newNFTValue);
    		
    		rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
    	} catch (IOException e) {
            logger.error("Failed to send SET_NFT_PRICE request");
            return -1;
        }
    	
    	if (rep.length == 0) {
            return -1;
        }
    	
    	try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (int) response.getValue();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SET_NFT_PRICE request");
            return -1;
        }
    }

    public int spendCoins(List<Integer> coinIds, int receiverId, double value) {
    	byte[] rep;
    	try {
    		DTIMessage<K,V> request = new DTIMessage<>();
    		request.setType(DTIRequests.SPEND);
    		request.setValue(value);
    		request.setKey(receiverId);
            request.setIdList(coinIds);
    		
    		rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
    	} catch (IOException e) {
            logger.error("Failed to send SPEND request");
            return -1;
        }
    	
    	if (rep.length == 0) {
            return -1;
        }
    	
    	try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (int) response.getValue();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SPEND request");
            return -1;
        }
    }

    public int buyNFT(List<Integer> coinIds, int nftId) {
    	byte[] rep;
    	try {
    		DTIMessage<K,V> request = new DTIMessage<>();
    		request.setType(DTIRequests.BUY_NFT);
    		request.setValue(nftId);
            request.setIdList(coinIds);
    		
    		rep = serviceProxy.invokeOrdered(DTIMessage.toBytes(request));
    	} catch (IOException e) {
            logger.error("Failed to send BUY_NFT request");
            return -1;
        }
    	
    	if (rep.length == 0) {
            return -1;
        }
    	
    	try {
    		DTIMessage<K,V> response = DTIMessage.fromBytes(rep);
    		return (int) response.getValue();
    	} catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of BUY_NFT request");
            return -1;
        }
    }

    @Override
    public int size() {
        byte[] rep;
        try {
            BFTMapMessage<K,V> request = new BFTMapMessage<>();
            request.setType(BFTMapRequestType.SIZE);

            //invokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(BFTMapMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send SIZE request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }
        try {
            BFTMapMessage<K,V> response = BFTMapMessage.fromBytes(rep);
            return response.getSize();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SIZE request");
            return -1;
        }
    }
    
    

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("You are supposed to implement this method :)");
    }
}
