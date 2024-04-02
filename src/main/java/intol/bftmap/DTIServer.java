package intol.bftmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;

public class DTIServer<K, V> extends DefaultSingleRecoverable {
	
	private final int AUTHORIZED_CLIENT_TO_MINT = 4;
	private final Logger logger = LoggerFactory.getLogger("bftsmart");
	private TreeMap<Integer, Coin> replicaMapCoins;
	private TreeMap<Integer, NFT> replicaMapNFTs;
	private Integer counterCoins;
	private Integer counterNFTs;
	
	public DTIServer(int id) {
		replicaMapCoins = new TreeMap<>();
		replicaMapNFTs = new TreeMap<>();
		counterCoins = 1;
		counterNFTs = 1;
		
		new ServiceReplica(id, this, this);
	}
	
	public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java DTIServer <server id>");
            System.exit(-1);
        }
        new DTIServer<Integer, String>(Integer.parseInt(args[0]));
    }

	@Override
	public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
		try {
            DTIMessage response = new DTIMessage();
            DTIMessage request = DTIMessage.fromBytes(command);
            DTIRequests cmd = request.getType();
            
            logger.info("Ordered execution of a {} request from {}", cmd, msgCtx.getSender());

            switch (cmd) {
                case MINT:
                	double coinValue = (double) request.getValue();
                	if (msgCtx.getSender() != AUTHORIZED_CLIENT_TO_MINT || coinValue <= 0) {
                		response.setValue(-1);
                		response.setErrorMessage(coinValue <= 0 ? "Value needs to be greater than zero." : 
                															 "Operation not authorized."  );
                		return DTIMessage.toBytes(response);
                	}
                	Coin coin = new Coin(counterCoins, msgCtx.getSender() , coinValue);
                	replicaMapCoins.put(counterCoins, coin);
                    response.setValue(counterCoins);
                    counterCoins++;
                    return DTIMessage.toBytes(response);
				
				case SPEND:
                	List<Integer> coinIds = request.getIdList();
					int receiver = (int) request.getKey();
					double value = (double) request.getValue();
					double valueOfUserCoins = 0;

					for (int coinId : coinIds) {
						Coin c = replicaMapCoins.get(coinId); 
						if (c == null || c.getOwnerId() != msgCtx.getSender()) {
							response.setValue(-1);
	                		return DTIMessage.toBytes(response);
						}
						if(c != null) valueOfUserCoins += c.getValue();
					}

                	if (valueOfUserCoins < value) {
                		response.setValue(-1);
                		return DTIMessage.toBytes(response);
                	}

					for (int coinId : coinIds) {
						replicaMapCoins.remove(coinId); 
					}

                	Coin coinToReceiver = new Coin(counterCoins, receiver, value);
					replicaMapCoins.put(counterCoins, coinToReceiver);
                    counterCoins++;

					if (valueOfUserCoins == value) {
						response.setValue(0);
                		return DTIMessage.toBytes(response);
					}

					Coin remainingValueCoin = new Coin(counterCoins, msgCtx.getSender(), valueOfUserCoins - value);
					replicaMapCoins.put(counterCoins, remainingValueCoin);
					response.setValue(counterCoins);
                    counterCoins++;
                    return DTIMessage.toBytes(response);
                    
                case MINT_NFT:
                	double nftValue = (double) request.getValue();
                	if (nftValue <= 0) {
                		response.setValue(-1);
                		response.setErrorMessage("Value needs to be greater than zero.");
                		return DTIMessage.toBytes(response);
                	}
                	
                	NFT nft = new NFT(counterNFTs, msgCtx.getSender() , request.getName() , request.getUri(), nftValue);
                	replicaMapNFTs.put(counterNFTs, nft);
                	response.setValue(counterNFTs);
                	counterNFTs++;
                	return DTIMessage.toBytes(response);
				case SET_NFT_PRICE:
					int nftId = (int) request.getKey();
                	nftValue = (double) request.getValue();
					nft = replicaMapNFTs.get(nftId);

                	if (nft == null || msgCtx.getSender() != nft.getOwnerId() || nftValue <= 0) {
                		response.setValue(-1);
                		return DTIMessage.toBytes(response);
                	}

					nft.setValue(nftValue);
                	replicaMapNFTs.put(nftId, nft);

                	response.setValue(nft.getId());
                	return DTIMessage.toBytes(response);
				case BUY_NFT:
                	coinIds = request.getIdList();
					NFT nftToBuy = replicaMapNFTs.get((int) request.getValue());  
					valueOfUserCoins = 0;

					for (int coinId : coinIds) {
						Coin c = replicaMapCoins.get(coinId); 
						if (c == null || c.getOwnerId() != msgCtx.getSender()) {
							response.setValue(-1);
	                		return DTIMessage.toBytes(response);
						}
						if(c != null) valueOfUserCoins += c.getValue();
					}

                	if (valueOfUserCoins < nftToBuy.getValue()) {
                		response.setValue(-1);
                		return DTIMessage.toBytes(response);
                	}

					for (int coinId : coinIds) {
						replicaMapCoins.remove(coinId); 
					}

                	Coin coinToNFTOwner = new Coin(counterCoins, nftToBuy.getOwnerId(), nftToBuy.getValue());
					replicaMapCoins.put(counterCoins, coinToNFTOwner);
                    counterCoins++;

					nftToBuy.setOwnerId(msgCtx.getSender());

					if (valueOfUserCoins == nftToBuy.getValue()) {
						response.setValue(0);
                		return DTIMessage.toBytes(response);
					}

					remainingValueCoin = new Coin(counterCoins, msgCtx.getSender(), valueOfUserCoins - nftToBuy.getValue());
					replicaMapCoins.put(counterCoins, remainingValueCoin);
					response.setValue(counterCoins);
                    counterCoins++;
                    return DTIMessage.toBytes(response);
				case MY_COINS:
					List<Coin> userCoins = new ArrayList<>();

					for (Coin c : replicaMapCoins.values()) {
						if (c.getOwnerId() == msgCtx.getSender()) 
							userCoins.add(c); 
					}

                    if (userCoins != null) {
                        response.setCoinList(userCoins);
                    }
                    return DTIMessage.toBytes(response);
				case MY_NFTS:
					List<NFT> userNFTs = new ArrayList<>();

					for (NFT nft2 : replicaMapNFTs.values()) {
						if (nft2.getOwnerId() == msgCtx.getSender()) 
							userNFTs.add(nft2); 
					}
					
                    if (userNFTs != null) {
                        response.setNftList(userNFTs);
                    }                              
                    return DTIMessage.toBytes(response);
            }

            return null;
        }catch (IOException | ClassNotFoundException ex) {
            logger.error("Failed to process ordered request", ex);
            return new byte[0];
        }
	}

	@Override
	public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
		try {
            DTIMessage response = new DTIMessage();
            DTIMessage request = DTIMessage.fromBytes(command);
            DTIRequests cmd = request.getType();

            logger.info("Unordered execution of a {} request from {}", cmd, msgCtx.getSender());

            switch (cmd) {
                case MY_COINS:
					List<Coin> userCoins = new ArrayList<>();

					for (Coin c : replicaMapCoins.values()) {
						if (c.getOwnerId() == msgCtx.getSender()) 
							userCoins.add(c); 
					}
					
					for (Coin c : userCoins) {
						System.out.println("Coin id: " + c.getId() + " Coin owner: " + c.getOwnerId());
					}

                    if (userCoins != null) {
                        response.setCoinList(userCoins);
                    }
                    return DTIMessage.toBytes(response);

				case SEARCH_NFT:
					List<NFT> resultNFTs = new ArrayList<>();
					String textToSearch = request.getName().toLowerCase();

					for (NFT nft : replicaMapNFTs.values()) {
						if (nft.getName().toLowerCase().contains(textToSearch)) 
							resultNFTs.add(nft); 
					}

                    if (resultNFTs != null) {
                        response.setNftList(resultNFTs);
                    }
                    return DTIMessage.toBytes(response);

				case MY_NFTS:
					List<NFT> userNFTs = new ArrayList<>();

					for (NFT nft : replicaMapNFTs.values()) {
						if (nft.getOwnerId() == msgCtx.getSender()) 
							userNFTs.add(nft); 
					}
					
                    if (userNFTs != null) {
                        response.setNftList(userNFTs);
                    }                
                    return DTIMessage.toBytes(response);
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.error("Failed to process unordered request", ex);
            return new byte[0];
        }
        return null;
	}
	

	@Override
	public byte[] getSnapshot() {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(replicaMapCoins);
            out.writeObject(replicaMapNFTs);
            out.writeObject(counterCoins);
            out.writeObject(counterNFTs);
            out.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(); //debug instruction
            return new byte[0];
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void installSnapshot(byte[] state) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(state); ObjectInput in = new ObjectInputStream(bis)) {
			replicaMapCoins = (TreeMap<Integer, Coin>) in.readObject();
			replicaMapNFTs = (TreeMap<Integer, 	NFT>) in.readObject();
			counterCoins = (Integer) in.readObject();
			counterNFTs = (Integer) in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace(); //debug instruction
        }
	}
	
}
