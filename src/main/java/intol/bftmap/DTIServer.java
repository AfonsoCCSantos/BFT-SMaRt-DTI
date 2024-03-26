package intol.bftmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;

public class DTIServer<K, V> extends DefaultSingleRecoverable {
	
	private final Logger logger = LoggerFactory.getLogger("bftsmart");
	private TreeMap<Integer, Coin> replicaMapCoins;
	private TreeMap<Integer, NFT> replicaMapNfts;
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
                	Coin coin = new Coin(counterCoins, msgCtx.getSender(), (int) response.getValue());
                	Coin value = replicaMapCoins.put(counterCoins, coin);

                    response.setValue(value.getId());
                    counterCoins++;
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
		// TODO Auto-generated method stub
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
