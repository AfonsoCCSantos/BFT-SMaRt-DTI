package intol.bftmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.TreeMap;

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
	private int counterCoins;
	private int counterNFTs;
	
	
	public DTIServer(int id) {
		replicaMapCoins = new TreeMap<>();
		replicaMapCoins = new TreeMap<>();
		counterCoins = 0;
		counterNFTs = 0;
		
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
            DTIMessage<K,V> response = new DTIMessage<>();
            DTIMessage<K,V> request = DTIMessage.fromBytes(command);
            DTIRequests cmd = request.getType();
            
            logger.info("Ordered execution of a {} request from {}", cmd, msgCtx.getSender());

            switch (cmd) {
                case MINT:
                	double coinValue = (double) request.getValue();
                	if (!(msgCtx.getSender() != AUTHORIZED_CLIENT_TO_MINT) || coinValue <= 0) {
                		response.setValue(-1);
                		return DTIMessage.toBytes(response);
                	}
                	Coin coin = new Coin(counterCoins, msgCtx.getSender() , coinValue);
                	Coin newCoinValue = replicaMapCoins.put(counterCoins, coin);
                    response.setValue(newCoinValue.getId());
                    counterCoins++;
                    return DTIMessage.toBytes(response);
                    
                case MINT_NFT:
                	double nftValue = (double) request.getValue();
                	if (!(msgCtx.getSender() != AUTHORIZED_CLIENT_TO_MINT || nftValue <= 0)) {
                		response.setValue(-1);
                		return DTIMessage.toBytes(response);
                	}
                	
                	NFT nft = new NFT(counterNFTs, msgCtx.getSender() , request.getName() , request.getUri() , nftValue);
                	NFT newNFTValue = replicaMapNFTs.put(counterNFTs, nft);
                	response.setValue(newNFTValue.getId());
                	counterNFTs++;
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
            DTIMessage<K,V> response = new DTIMessage<>();
            DTIMessage<K,V> request = DTIMessage.fromBytes(command);
            DTIRequests cmd = request.getType();

            logger.info("Unordered execution of a {} request from {}", cmd, msgCtx.getSender());

            switch (cmd) {
                case MY_COINS:
					HashSet<Coin> userCoins = new HashSet<>();

					for(Coin c : replicaMapCoins.values()) {
						if(c.getOwnerId() == msgCtx.getSender()) userCoins.add(c); 
					}

                    if (userCoins != null) {
                        response.setCoinSet(userCoins);
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
            //out.writeObject(replicaMap);
            out.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(); //debug instruction
            return new byte[0];
        }
	}
	
	@Override
	public void installSnapshot(byte[] state) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(state); ObjectInput in = new ObjectInputStream(bis)) {
			//replicaMap = (TreeMap<K, V>) in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace(); //debug instruction
        }
	}
	
}
